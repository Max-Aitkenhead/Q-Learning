package mazeGenerator;

public class TMaze extends Maze {

    private final int TmazeArmLength = 2;

    public TMaze() {
        super(5, 3);
        setXDimention((TmazeArmLength * 2) + 1);
        setYDimention(TmazeArmLength + 1);
        createCellBlock(5, 3);
        setGoal(getCell(TmazeArmLength, TmazeArmLength));
        drawMaze();
    }

    @Override
    public void drawMaze() {
        for (int i = 0; i < getXdimension(); i++) {
            if (checkValidDirection(getCell(i, 0), dirs.left))
                getCell(i, 0).addPathOption(getNextCell(getCell(i, 0), dirs.left), dirs.left);
            if (checkValidDirection(getCell(i, 0), dirs.right))
                getCell(i, 0).addPathOption(getNextCell(getCell(i, 0), dirs.right), dirs.right);
        }
        for (int i = 0; i < getYdimension(); i++) {
            if (checkValidDirection(getCell(TmazeArmLength, i), dirs.up))
                getCell(TmazeArmLength, i).addPathOption(getNextCell(getCell(TmazeArmLength, i), dirs.up), dirs.up);
            if (checkValidDirection(getCell(TmazeArmLength, i), dirs.down))
                getCell(TmazeArmLength, i).addPathOption(getNextCell(getCell(TmazeArmLength, i), dirs.down), dirs.down);
        }

    }

}
