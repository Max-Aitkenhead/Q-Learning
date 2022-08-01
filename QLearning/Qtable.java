package QLearning;

import java.util.ArrayList;
import java.util.Objects;
import mazeGenerator.Cell;
import mazeGenerator.dirs;
public class Qtable {

    private ArrayList<State> states;

    private State goalState;
    private Action lastAction;
    private Action lastJuncAction;
    private dirs lastDirection;
    private boolean foundGoalState;

    public Qtable() {
        states = new ArrayList<>();
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public State newState(Cell cell) {
        State state = new State(cell);
        states.add(state);
        return state;
    }

    public State getGoalState() {
        for (State state : states)
            if (state.isGoal())
                return state;
        System.err.println("Goal state not found");
        return null;
    }

    public boolean hasFoundGoal() {
        return foundGoalState;
    }

    public void setGoal(State state, boolean goalStatus) {
        if (!Objects.equals(goalState, null))
            goalState.setGoal(false);
        state.setGoal(goalStatus);
        goalState = state;
        foundGoalState = goalStatus;
    }

    public Action getLastAction() {
        return lastAction;
    }

    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }

    public dirs getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(dirs lastDirection) {
        this.lastDirection = lastDirection;
    }

    public Action getLastJuncAction() {
        return lastJuncAction;
    }

    public void setLastJuncAction() {
        this.lastJuncAction = lastAction;
    }

    public void resetStateMetaData(){
        for(State state : states){
            state.setLeavingAction(null);
            state.setArrivingAction(null);
        }
    }



}
