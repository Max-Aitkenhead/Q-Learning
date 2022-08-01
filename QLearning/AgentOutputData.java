package QLearning;

import java.util.ArrayList;
import java.io.*;

import mazeGenerator.Cell;
import mazeGenerator.ClassicMaze;
import mazeGenerator.Maze;
import mazeGenerator.WaterMaze;

public class AgentOutputData {

    private int repeitionNumber = 0;
    private ArrayList<Integer> stepNumbersOfEpisodes = new ArrayList<>();
    private ArrayList<Cell> learnedPath = new ArrayList<>();
    private boolean failed;
    private Maze maze;
    private Qtable qTable;
    private AgentSettings agentSettings;

    public void addEpisodeLength(int numberOfSteps) {
        stepNumbersOfEpisodes.add(numberOfSteps);
    }

    public ArrayList<Integer> getStepNumbersOfEpisodes() {
        return stepNumbersOfEpisodes;
    }

    public void addQtable(Qtable qTable) {
        this.qTable = qTable;
    }

    public Qtable getqTable() {
        return qTable;
    }

    public void addSettings(AgentSettings agentSettings) {
        this.agentSettings = agentSettings;
    }

    public AgentSettings settings() {
        return agentSettings;
    }

    public void setFailed() {
        failed = true;
    }

    public boolean isFailed() {
        return failed;
    }

    public void addMaze(Maze maze) {
        this.maze = maze;
    }

    public Maze getMaze() {
        return maze;
    }

    public void addLearnedPathCell(Cell cell) {
        learnedPath.add(cell);
    }

    public ArrayList<Cell> getLearnedPath() {
        return learnedPath;
    }

    public int getNumEpisodes() {
        return stepNumbersOfEpisodes.size();
    }

    public void setRepeitionNumber(int repeitionNumber){
        this.repeitionNumber = repeitionNumber;
    }

    public int getRepeitionNumber(){
        return repeitionNumber;
    }




}
