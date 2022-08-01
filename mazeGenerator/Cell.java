package mazeGenerator;

import java.util.ArrayList;
import java.util.UUID;


public class Cell implements java.io.Serializable{

	private UUID id;

	private ArrayList<PathOption> pathOptions = new ArrayList<>();

	private int x;
	private int y;

	private boolean drawn = false;

	private boolean goal = false;

	public Cell(int column, int row) {
		this.x = column;
		this.y = row;
		this.id = UUID.randomUUID();
	}

	public void addPathOption(Cell cell, dirs direction) {
		pathOptions.add(new PathOption(cell, direction));
	}


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean drawn(){
		return drawn;
	}

	public void setDrawn(){
		drawn = true;
	}

	public ArrayList<PathOption> getPathOptions() {
		return pathOptions;
	}

	public void setGoal(boolean goal) {
		this.goal = goal;
	}

	public boolean isGoal() {
		return goal;
	}

	public UUID getId(){
		return id;
	}

}
