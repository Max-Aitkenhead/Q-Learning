package mazeGenerator;

import java.util.ArrayList;

public class WaterMaze extends Maze {

    public WaterMaze(int xDimention, int yDimention) {
        super(xDimention, yDimention);
        createCellBlock(xDimention, yDimention);
        setGoal(getCell(xDimention - 1, yDimention - 1));
        drawMaze();
    }

    @Override
    public void drawMaze() {
        for (Cell[] cellRow : getCells())
            for (Cell cell : cellRow) {
                ArrayList<dirs> directions = getOpenMoves(cell);
                for (dirs direction : directions) {
                    Cell nextCell = getCell(cell.getX() + direction.getTransX(), cell.getY() + direction.getTransY());
                    cell.addPathOption(nextCell, direction);
                }
            }
    }
}
