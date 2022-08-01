package QLearning;

import java.util.*;

import mazeGenerator.*;

public class Agent {

    private AgentSettings settings;

    private Cell firstCell;
    private Qtable qTable;
    private Cell currentCell;
    private State currentState;


    public Agent(Cell firstCell) {
        settings = new AgentSettings();
        qTable = new Qtable();
        this.firstCell = firstCell;
        this.currentCell = firstCell;
    }

    /**
     * Decides how many episodes to run
     * calculates final length score
     * screen
     *
     * @return data output object
     */
    public AgentOutputData learn() {
        AgentOutputData outputData = new AgentOutputData();

        int consistantRuns = 0;
        int bestIterRunLength = 0;

        while (true) {
            if (outputData.getStepNumbersOfEpisodes().size() > settings.episodeFailPoint) {
                outputData.setFailed();
                break;
            }
            int itersPerRun = 0;
            while (true) {
                itersPerRun++;
                if (move())
                    break;
                else if (itersPerRun > settings.moveFailPoint) {
                    break;
                }
            }
            if(settings.printEpisodeLengths)
                System.out.println(itersPerRun);
            setupNewEpisode();
            outputData.addEpisodeLength(itersPerRun);
            if (outputData.getNumEpisodes() > 10) {
                int heighest = 0;
                int lowest = 0;
                for (int i = outputData.getNumEpisodes() - 10; i < outputData.getNumEpisodes(); i++) {
                    if (outputData.getStepNumbersOfEpisodes().get(i) > heighest)
                        heighest = outputData.getStepNumbersOfEpisodes().get(i);
                    if (lowest == 0 || outputData.getStepNumbersOfEpisodes().get(i) < lowest)
                        lowest = outputData.getStepNumbersOfEpisodes().get(i);
                }

//                if ((heighest - lowest) < ((heighest + lowest) / 30) && heighest < settings.moveFailPoint)
                if ((heighest < 1.2* lowest) && heighest < settings.moveFailPoint)
                    break;


            }
        }
        outputData.addLearnedPathCell(firstCell);
        settings.staticRandomMove = false;
        if (!outputData.isFailed()) {
            int noMove = 0;
            while (noMove < settings.moveFailPoint) {
                if (move())
                    break;
                else
                    outputData.addLearnedPathCell(currentCell);
                noMove++;
            }
        }
        outputData.addQtable(qTable);
        outputData.addSettings(settings);
        return outputData;
    }

    /**
     * makes agent move one spot
     * calc and update q-value
     *
     * @return true if found reward else return false
     */
    public boolean move() {

        if (settings.onlyLearnJunctions && !checkJunction()) // checks if new state is needed
            return false;

        if (Objects.equals(currentState, null)) // if new state, create it
            currentState = getNewState(currentCell);

        if (!Objects.equals(qTable.getLastAction(), null)) { // add destination for previous action
            qTable.getLastAction().setNextState(currentState);
            for (Action action : currentState.getActions())
                if (action.getDirection() == qTable.getLastDirection().getOpposite())
                    action.setNextState(qTable.getLastAction().getOriginState());
        }

        if (currentCell.isGoal()) { // found the reward! (break condition)
            qTable.setGoal(currentState, true);
            return true;
        }
        if (currentState.isGoal())
            qTable.setGoal(currentState, false);

        if (currentState.getActions().size() > 2)
            qTable.setLastJuncAction();

        Action currentBestAction = getBestAction(currentState); // find best action of current state
        State nextState = currentBestAction.getNextState(); // gets destination of best action, null if not visited yet

        double currentQval = currentBestAction.getQValue();
        double nextQval;
        double cellJumpReward;
        if (Objects.equals(nextState, null)) { // if not visited, assume 0 qval and -1 reward
            nextQval = 0;
            cellJumpReward = -1;
        } else {
            cellJumpReward = getReward(nextState); // if visited extract next best qval
            Action nextBestAction = getBestAction(nextState);
            nextQval = nextBestAction.getQValue();
        }

        double newQval = calcQValue(currentQval, cellJumpReward, nextQval); // calc new q for current action

        currentBestAction.setQValue(newQval);

        currentState.setArrivingAction(qTable.getLastAction());
        currentState.setLeavingAction(currentBestAction);

        Cell nextCell = getNextCell(currentCell, currentBestAction.getDirection());
        if (Objects.equals(nextCell, null))
            System.err.println("next CELL null error");


        qTable.setLastAction(currentBestAction);
        qTable.setLastDirection(currentBestAction.getDirection());
        currentCell = nextCell;
        currentState = nextState;

        return false;
    }

    /**
     * Finds the best action of any given state based on qtable
     *
     * @param state
     * @return the best action
     */
    private Action getBestAction(State state) {
        ArrayList<Action> possibleActions = new ArrayList<>(state.getActions());
        Collections.shuffle(possibleActions);
        if (possibleActions.size() == 1)
            return possibleActions.get(0);

        if (state.getId() == currentState.getId() && state.getId() != firstCell.getId() && !currentState.isGoal()) {
            for (Action action : state.getActions()) {
                if (settings.backTrack)
                    if (!Objects.equals(qTable.getLastAction(), null)
                            && Objects.equals(action.getNextState(), qTable.getLastAction().getOriginState()))
                        possibleActions.remove(action);
            }
            for (Action action : state.getActions()) {
                if (settings.pickNewPaths && possibleActions.size() > 1)
                    if (!Objects.equals(currentState.getArrivingAction(), null)
                            && Objects.equals(action.getNextState(), currentState.getArrivingAction().getOriginState()))
                        possibleActions.remove(action);
                if (settings.pickNewPaths && possibleActions.size() > 1)
                    if (!Objects.equals(currentState.getLeavingAction(), null)
                            && Objects.equals(action.getNextState(), currentState.getLeavingAction().getNextState()))
                        possibleActions.remove(action);
            }
        }

        if (possibleActions.size() < 1)
            System.err.println("zero available moves error");
        Action bestAction = possibleActions.get(0);

        for (Action action : possibleActions) {
//            if (settings.pickNewPaths && Objects.equals(action, action.getNextState().getActions().get()));
//                continue;
            if (action.getQValue() > bestAction.getQValue())
                bestAction = action;
        }
        if (currentState.getId() == state.getId()) {
            Random random = new Random();
            if (settings.staticRandomMove && random.nextDouble() > settings.staticRandomMoveAmount)
                return possibleActions.get(0);
        }


        return bestAction;
    }

    public State getNewState(Cell currentCell) {
        for (State state : qTable.getStates())
            if (state.getId() == currentCell.getId())
                return state;
        return qTable.newState(currentCell);
    }

    /**
     * @param currentQval
     * @param cellJumpReward
     * @param nextQval
     * @return newQvalue
     */
    private double calcQValue(double currentQval, double cellJumpReward, double nextQval) {
        double newQ = currentQval + settings.learningRate * (cellJumpReward + (settings.discountRate * nextQval) - currentQval);
//        if(newQ == currentQval)
//            System.out.println("");
        return newQ;
    }

    /**
     * Gets the cell you would be at if you took that action
     *
     * @param cell   - The cell of which you want to find the next cell of
     * @param direction - The action to take it to the next cell
     * @return
     */
    private Cell getNextCell(Cell cell, dirs direction) {
        for (PathOption path : cell.getPathOptions())
            if (direction == path.getDirection())
                return path.getNextCell();
        System.err.println("error getting next CELL");
        return null;
    }

    /**
     * Gets reward of any given cell jump
     *
     * @param state
     * @return
     */
    private double getReward(State state) {
        double reward = 0;

        if (state.isGoal()) {
            reward += 100;
            return reward;
        }
        reward -= 1;

        if (qTable.hasFoundGoal()) {
            double distanceFromNextStateToGoal = calcDistance(state, qTable.getGoalState());
            double distanceFromCurrentStateToGoal = calcDistance(currentState, qTable.getGoalState());
            if (settings.visualLearning)
                reward += (settings.visualLearningMultiplier * (distanceFromCurrentStateToGoal - distanceFromNextStateToGoal));
            if (reward == 0)
                reward = -1;
        }
        return reward;
    }

    private double calcDistance(State state1, State state2) {
        return Math.pow(Math.pow(state2.getX() - state1.getX(), 2) + Math.pow(state2.getY() - state1.getY(), 2), 0.5);
    }

    /**
     *  Move forward if not junction
     * @return true if at junction, false if not.
     */
    private boolean checkJunction() {
        if (currentCell.isGoal() || currentCell.getId() == firstCell.getId())
            return true;
        if (currentCell.getPathOptions().size() == 2) {
            PathOption pathToTake = null;
            for (PathOption path : currentCell.getPathOptions()) {
                if (!Objects.equals(path.getNextCell(), getNextCell(currentCell, qTable.getLastDirection().getOpposite())))
                    pathToTake = path;
            }

            currentCell = pathToTake.getNextCell();
            qTable.setLastDirection(pathToTake.getDirection());
            if (Objects.equals(currentCell, null))
                System.out.println("fail at 2 paths");
            return false;
        }

        if (currentCell.getPathOptions().size() == 1) {
            qTable.setLastDirection(currentCell.getPathOptions().get(0).getDirection());
            currentCell = currentCell.getPathOptions().get(0).getNextCell();
            if (Objects.equals(currentCell, null))
                System.out.println("fail at 1 path");
            return false;
        }
        return true;
    }


    /**
     * Gets Action that would lead the agent backwards
     *
     * @return type Action
     */
    private Action getBackwardAction() {
        for (Action action : currentState.getActions())
            if (Objects.equals(action.getDirection(), qTable.getLastDirection().getOpposite()))
                return action;
        return null;
    }

    public void setupNewEpisode() {
        currentCell = firstCell;
        qTable.setLastAction(null);
        qTable.setLastDirection(null);
        qTable.resetStateMetaData();
        currentState = null;
    }

    public Qtable getQtable() {
        return qTable;
    }

    public int getX() {
        return currentCell.getX();
    }

    public int getY() {
        return currentCell.getY();
    }

    public void setCell(Cell cell) {
        currentCell = cell;
    }

//    public void returnToStart() {
//        currentCell = firstCell;
//    }

    public AgentSettings settings() {
        return settings;
    }


}
