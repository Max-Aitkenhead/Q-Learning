package QLearning;

import mazeGenerator.dirs;

public class Action {
    private dirs direction;
    private double qValue;
    private State originState;
    private State nextState;

    public Action(State originState, dirs direction) {
        this.originState = originState;
        this.direction = direction;
    }

    public double getQValue() {
        return qValue;
    }

    public void setQValue(double qValue) {
        this.qValue = qValue;
    }

    public dirs getDirection() {
        return direction;
    }

    public void setNextState(State nextState) {
        this.nextState = nextState;
    }

    public State getNextState() {
        return nextState;
    }

    public State getOriginState() {
        return originState;
    }

    public void decayQValue(double decayFactor){
        qValue *= decayFactor;
    }
}