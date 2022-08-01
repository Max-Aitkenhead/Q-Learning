package mazeGenerator;

public class PathOption implements java.io.Serializable {
	private dirs direction;
	private Cell nextCell;

	public PathOption(Cell nextCell, dirs direction) {
		this.nextCell = nextCell;
		this.direction = direction;
	}

	public dirs getDirection() {
		return direction;
	}

	public Cell getNextCell() {
		return nextCell;
	}

}

