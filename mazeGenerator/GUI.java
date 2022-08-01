package mazeGenerator;

import javax.swing.*;

import QLearning.*;
import QLearning.Action;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GUI extends JPanel implements KeyListener {

    private Maze maze;
    private JFrame frame;
    private Agent agent;
    private int highlightBorderX;
    private int highlightBorderY;
    private int highlightSize;
    public boolean printSinglePath = true;
    public boolean printAllPaths = true;
    public boolean showJunctions = false;
    public boolean showGoal = true;
    private int cellSize;
    private int qTableOffset = 800;


    public GUI(Maze maze, Agent agent) {
        this.maze = maze;
        this.agent = agent;
        if (600 / maze.getXdimension() > 700 / maze.getYdimension())
            cellSize = 700 / maze.getYdimension();
        else
            cellSize = 600 / maze.getXdimension();
        drawFrame();
    }

    public void drawFrame() {
        frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.add(this);
        frame.setVisible(true);

    }

    @Override
    public void paintComponent(Graphics g2) {
        super.paintComponent(g2);

        Graphics2D g = (Graphics2D) g2;
        g.setStroke(new BasicStroke(cellSize / 15));

        for (Cell[] row : maze.getCells())
            for (Cell cell : row)
                drawMazeCell(g, cell);

        for (Cell[] row : maze.getCells())
            for (Cell cell : row)
                drawTableCell(g, cell);

        fillGuiQtable(g);
    }

    public void fillGuiQtable(Graphics2D g) {
        for (State state : agent.getQtable().getStates()) {
            int borderX = state.getX() * cellSize + qTableOffset;
            int borderY = state.getY() * cellSize + 40;
            for (Action action : state.getActions()) {
                if (action.getQValue() == 0)
                    continue;
                if (action.getQValue() > 0)
                    g.setColor(Color.BLUE);
                else
                    g.setColor(Color.RED);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 15));

                if (action.getDirection() == dirs.up)
                    g.drawString(truncate(action.getQValue()), (borderX + cellSize / 2) - (cellSize / 10),
                            borderY + 25);
                if (action.getDirection() == dirs.down)
                    g.drawString(truncate(action.getQValue()), (borderX + cellSize / 2) - (cellSize / 10),
                            borderY + cellSize - 10);
                if (action.getDirection() == dirs.left)
                    g.drawString(truncate(action.getQValue()), borderX + 25 - (cellSize / 10),
                            borderY + (cellSize / 2));
                if (action.getDirection() == dirs.right)
                    g.drawString(truncate(action.getQValue()), (borderX + cellSize - 25) - (cellSize / 10),
                            borderY + cellSize / 2);
            }
        }
    }

    public void drawTableCell(Graphics2D g, Cell cell) {
        g.setStroke(new BasicStroke(cellSize / 40));

        int borderX = cell.getX() * cellSize + qTableOffset;
        int borderY = cell.getY() * cellSize + 40;
        g.drawLine(borderX, borderY, borderX + cellSize, borderY);
        g.drawLine(borderX, borderY + cellSize, borderX + cellSize, borderY + cellSize);
        g.drawLine(borderX, borderY, borderX, borderY + cellSize);
        g.drawLine(borderX + cellSize, borderY, borderX + cellSize, borderY + cellSize);
        g.drawLine(borderX, borderY, borderX + cellSize, borderY + cellSize);
        g.drawLine(borderX + cellSize, borderY, borderX, borderY + cellSize);

    }

    public void drawMazeCell(Graphics2D g, Cell cell) {

        highlightBorderX = cell.getX() * cellSize + 50;
        highlightBorderY = cell.getY() * cellSize + 40;
        highlightSize = cellSize / 3;

        if (showJunctions == true && cell.getPathOptions().size() > 2) {
            g.setColor(Color.BLUE);
            if (cell.getPathOptions().size() > 3) {
                g.setColor(Color.YELLOW);
            }
            g.fillRect(highlightBorderX + (cellSize / 2 - highlightSize / 2), highlightBorderY + (cellSize / 2 - (highlightSize / 2)), highlightSize,
                    highlightSize);
        }

        if (cell.isGoal() && showGoal) {
            g.setColor(Color.GREEN);
            g.fillRect(highlightBorderX + (cellSize / 2 - highlightSize / 2), highlightBorderY + (cellSize / 2 - (highlightSize / 2)), highlightSize,
                    highlightSize);
        }


        g.setColor(Color.GREEN);
        if (maze instanceof ClassicMaze && printAllPaths)
            for (ArrayList<Cell> validPath : ((ClassicMaze) maze).getPathFinder().getAllValidPaths())
                printPathAtCell(cell, validPath, g);

        g.setColor(Color.red);
        if (maze instanceof ClassicMaze && printSinglePath)
            printPathAtCell(cell, ((ClassicMaze) maze).getPathFinder().getShortestPath(), g);


        if (!Objects.equals(agent, null) && agent.getX() == cell.getX() && agent.getY() == cell.getY()) {
            g.setColor(Color.RED);
            g.fillRect(highlightBorderX + (cellSize / 2 - highlightSize / 2), highlightBorderY + (cellSize / 2 - (highlightSize / 2)), highlightSize,
                    highlightSize);
        }

        ArrayList<dirs> wallDirections = new ArrayList<>(Arrays.asList(dirs.values()));

        for (PathOption pathOption : cell.getPathOptions())
            wallDirections.remove(pathOption.getDirection());

        g.setColor(Color.BLUE);

        for (dirs direction : wallDirections) {
            if (direction == dirs.up)
                g.drawLine(highlightBorderX, highlightBorderY, highlightBorderX + cellSize, highlightBorderY);
            if (direction == dirs.down)
                g.drawLine(highlightBorderX, highlightBorderY + cellSize, highlightBorderX + cellSize, highlightBorderY + cellSize);
            if (direction == dirs.left)
                g.drawLine(highlightBorderX, highlightBorderY, highlightBorderX, highlightBorderY + cellSize);
            if (direction == dirs.right)
                g.drawLine(highlightBorderX + cellSize, highlightBorderY, highlightBorderX + cellSize, highlightBorderY + cellSize);
        }
    }

    private String truncate(double value) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(value);
    }

    private void printPathAtCell(Cell cell, ArrayList<Cell> path, Graphics2D g) {
        for (Cell shortestPathCell : path)
            if (shortestPathCell.getId() == cell.getId())
                g.fillRect(highlightBorderX + (cellSize / 2 - highlightSize / 4), highlightBorderY + (cellSize / 2 - (highlightSize / 4)), highlightSize / 2,
                        highlightSize / 2);
    }


    @Override
    public void keyPressed(KeyEvent event) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
        if(agent.move())
            agent.setupNewEpisode();
        repaint();
    }

}
