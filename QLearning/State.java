package QLearning;

import java.util.ArrayList;

import mazeGenerator.Cell;
import mazeGenerator.PathOption;

import java.util.UUID;

public class State {

    private UUID id;

    private int x;
    private int y;

    private Action arrivingAction;
    private Action leavingAction;

    private boolean isGoal;

    private ArrayList<Action> actions = new ArrayList<>();

    public State(Cell cell) {
        this.id = cell.getId();
        this.x = cell.getX();
        this.y = cell.getY();
        this.isGoal = cell.isGoal();

        for (PathOption path : cell.getPathOptions())
            actions.add(new Action(this, path.getDirection()));
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public UUID getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setGoal(boolean goal){
        this.isGoal = goal;
    }


    public boolean isGoal() {
        return isGoal;
    }

    public boolean isJunction(){
        return actions.size() > 2;
    }

    public Action getArrivingAction() {
        return arrivingAction;
    }

    public void setArrivingAction(Action arrivingAction) {
        this.arrivingAction = arrivingAction;
    }

    public Action getLeavingAction() {
        return leavingAction;
    }

    public void setLeavingAction(Action leavingAction) {
        this.leavingAction = leavingAction;
    }

}
