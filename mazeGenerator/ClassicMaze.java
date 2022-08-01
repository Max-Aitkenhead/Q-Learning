package mazeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ClassicMaze extends Maze {

    private PathFinder pathFinder;
    private boolean x = false;

    public ClassicMaze(int xDimention, int yDimention) {
        super(xDimention, yDimention);
        createCellBlock(xDimention, yDimention);
        setGoal(getCell(xDimention - 1, yDimention - 1));
        drawMaze();
        setDrawn();
        thinMaze();
        solveMaze();
    }

    @Override
    public void drawMaze() {
        findNextCell(getCell(0, 0));
    }

    private void findNextCell(Cell currentCell) {
        Random random = new Random();
        ArrayList<dirs> openMoves = getOpenMoves(currentCell);
        if (getOpenMoves(currentCell).size() == 0)
            return;

//        x = true;
        dirs dirNextCell = getOpenMoves(currentCell).get(random.nextInt(getOpenMoves(currentCell).size()));
        Cell nextCell = getNextCell(currentCell, dirNextCell);
        currentCell.addPathOption(nextCell, dirNextCell);
        nextCell.addPathOption(currentCell, dirNextCell.getOpposite());
        nextCell.setDrawn();
        if (currentCell.getPathOptions().size() > 1 && currentCell.getPathOptions().get(0).getDirection() ==
                currentCell.getPathOptions().get(1).getDirection()) // check for duplicate path options
            currentCell.getPathOptions().remove(1);
        findNextCell(nextCell);
        findNextCell(currentCell);
    }

    public void thinMaze() {
        Random random = new Random();
        int pathOptionsAdded = getXdimension() * getYdimension() / 5;
        for (Cell[] row : getCells())
            for (Cell cell : row)
                if (random.nextInt(800) == 1) {
                    ArrayList<dirs> wallDirections = new ArrayList<>(Arrays.asList(dirs.values()));
                    for (PathOption pathOption : cell.getPathOptions())
                        wallDirections.remove(pathOption.getDirection());
                    if (wallDirections.size() < 1)
                        continue;
                    dirs randomDirection = wallDirections.get(random.nextInt(wallDirections.size()));
                    if (checkValidDirection(cell, randomDirection)) {
                        Cell randomNeighbour = getNextCell(cell, randomDirection);
                        cell.addPathOption(randomNeighbour, randomDirection);
                        randomNeighbour.addPathOption(cell, randomDirection.getOpposite());
                    }
                }
    }

    public void solveMaze(){
        pathFinder = new PathFinder(this);
    }


    public PathFinder getPathFinder() {
        return pathFinder;
    }
}
