package mazeGenerator;

import java.util.Objects;
import java.util.Stack;
import java.util.ArrayList;

public class PathFinder implements java.io.Serializable {

    private Stack cellStack = new Stack();
    private ArrayList<ArrayList<Cell>> validPaths = new ArrayList<>();
    private Cell lastCell;
    private Stack backWardPathsStack = new Stack();
    private Cell[][] duplicateMaze;

    public PathFinder(Maze maze) {
        duplicateMaze = maze.getCells().clone();
        lastCell = duplicateMaze[0][0];
        move(duplicateMaze[0][0]);
    }

    private void move(Cell newCell) {
        cellStack.push(newCell);
        if (newCell.isGoal()) { // check if goal
//        if (newCell.getX() == 0 && newCell.getY() == 19) {
            ArrayList<Cell> validPath = new ArrayList<>(cellStack);
            validPaths.add(validPath);
        }
        for (
                PathOption pathOption : newCell.getPathOptions())
            if (lastCell.getId() == pathOption.getNextCell().

                    getId()) {
                backWardPathsStack.push(pathOption);
                break;
            }

        lastCell = (Cell) cellStack.peek();

        for (
                PathOption pathOption : newCell.getPathOptions()) // move
            if (!

                    checkCellInStack(pathOption.getNextCell()) && !

                    checkIfBackWardMove(pathOption))

                move(pathOption.getNextCell());

        cellStack.pop();
        if (backWardPathsStack.size() > 0)
            backWardPathsStack.pop();
        if (cellStack.size() > 0)
            lastCell = (Cell) cellStack.peek();
        else
            lastCell = duplicateMaze[0][0];
    }

    private boolean checkCellInStack(Cell cell) {
        for (Object cellIterObj : cellStack) {
            Cell cellIter = (Cell) cellIterObj;
            if (cell.getId() == cellIter.getId())
                return true;
        }
        return false;
    }

    private boolean checkIfBackWardMove(PathOption pathOption) {
        for (Object backWardPathObj : backWardPathsStack) {
            PathOption backWardPath = (PathOption) backWardPathObj;
            if (Objects.equals(pathOption, backWardPath))
                return true;
        }
        return false;
    }

    public ArrayList<Cell> getShortestPath() {
        ArrayList<Cell> shortestPath = new ArrayList<>();
        for (ArrayList<Cell> validPath : validPaths)
            if (shortestPath == null || shortestPath.size() == 0 || validPath.size() < shortestPath.size())
                shortestPath = validPath;
        return shortestPath;
    }

    public ArrayList<ArrayList<Cell>> getAllValidPaths() {
        return validPaths;
    }


}
