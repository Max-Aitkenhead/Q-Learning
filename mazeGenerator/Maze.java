package mazeGenerator;

import java.util.ArrayList;

import QLearning.Action;

public class Maze implements java.io.Serializable {

    private int xDimention;
    private int yDimention;
    private Cell goalCell;
    private Cell[][] cells;
    private boolean drawn;

    public Maze(int xDimention, int yDimention) {
        this.xDimention = xDimention;
        this.yDimention = yDimention;
    }

    public void createCellBlock(int width, int height) {
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = new Cell(x, y);
                cells[x][y] = cell;
            }
        }
    }

    public boolean checkValidDirection(Cell cell, dirs direction) {
        int neighborX = cell.getX() + direction.getTransX();
        int neighborY = cell.getY() + direction.getTransY();
        if (neighborX < 0 || neighborX >= xDimention || neighborY < 0 || neighborY >= yDimention)
            return false;
        if (neighborX == 0 && neighborY == 0)
            return false;
        if (cells[neighborX][neighborY].drawn() && !drawn)
            return false;
//
        return true;
    }

    public ArrayList<dirs> getOpenMoves(Cell cell) {
        ArrayList<dirs> openMoves = new ArrayList<>();
        for (dirs direction : dirs.values()) {
            if (checkValidDirection(cell, direction))
                openMoves.add(direction);
        }
        return openMoves;
    }

    public Cell getNextCell(Cell cell, dirs direction) {
        return cells[cell.getX() + direction.getTransX()][cell.getY() + direction.getTransY()];
    }

    public int getNoJunctions() {
        int noJunctions = 0;
        for (Cell[] row : cells)
            for (Cell cell : row)
                if (cell.getPathOptions().size() > 2)
                    noJunctions++;
        return noJunctions;
    }

    public Cell getNextCell(Cell cell, Action action) {
        return getNextCell(cell, action.getDirection());
    }

    public void drawWaterMaze(GUI gui) {
    }

    public void drawMaze() {
    }

    public void drawTmaze(GUI gui) {
    }

    public Cell getCell(int x, int y) {
                return cells[x][y];
    }

    public Cell getCell(Cell cell, dirs direction){
        return cells[cell.getX() + direction.getTransX()][cell.getY()
                + direction.getTransY()];
    }

    public int getXdimension() {
        return xDimention;
    }

    public void setXDimention(int xDimention) {
        this.xDimention = xDimention;
    }

    public int getYdimension() {
        return yDimention;
    }

    public void setYDimention(int yDimention) {
        this.yDimention = yDimention;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setGoal(Cell cell) {
        if (goalCell != null)
            goalCell.setGoal(false);
        cell.setGoal(true);
        goalCell = cell;
    }

    public Cell getGoal(){
        return goalCell;
    }

    public void setDrawn() {
        this.drawn = true;
    }
}
