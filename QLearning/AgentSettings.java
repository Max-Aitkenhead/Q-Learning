package QLearning;

import java.util.ArrayList;

public class AgentSettings {

    public int episodeFailPoint = 500;

    public int moveFailPoint = 5000;

    public double learningRate = 0.9;

    public double discountRate = 0.8;

    public boolean visualLearning = false;

    public double visualLearningMultiplier = 1;

    public boolean proportionalRandomMove = false;

    public boolean staticRandomMove = true;

    public double staticRandomMoveAmount = 0.85;

    public boolean randomNextMoves = false;

    public boolean onlyLearnJunctions = false;

    public boolean backTrack = true;

    public boolean pickNewPaths = true;

    public boolean qTableDecay = false;

    public double decayFactor = 0.99995;

    public boolean printEpisodeLengths = false;

    public ArrayList<String> toCsv() {
        ArrayList<String> csvSettingsLine = new ArrayList<>();

        csvSettingsLine.add(String.valueOf(learningRate));
        csvSettingsLine.add(String.valueOf(discountRate));
        csvSettingsLine.add(String.valueOf(visualLearning));
        csvSettingsLine.add(String.valueOf(visualLearningMultiplier));
        csvSettingsLine.add(String.valueOf(qTableDecay));
        csvSettingsLine.add(String.valueOf(decayFactor));
        csvSettingsLine.add(String.valueOf(proportionalRandomMove));
        csvSettingsLine.add(String.valueOf(staticRandomMove));
        csvSettingsLine.add(String.valueOf(staticRandomMoveAmount));
        csvSettingsLine.add(String.valueOf(randomNextMoves));
        csvSettingsLine.add(String.valueOf(onlyLearnJunctions));
        csvSettingsLine.add(String.valueOf(backTrack));
        csvSettingsLine.add(String.valueOf(pickNewPaths));
        return csvSettingsLine;
    }

    public ArrayList<String> getCsvTitleLine() {
        ArrayList<String> csvTitleLine = new ArrayList<>();
        csvTitleLine.add("Repetition Number");
        csvTitleLine.add("Learning Rate");
        csvTitleLine.add("Discount Rate");
        csvTitleLine.add("Visual Learning");
        csvTitleLine.add("Visual Learning Multiplier");
        csvTitleLine.add("Q-Table Decay");
        csvTitleLine.add("Decay Factor");
        csvTitleLine.add("Proportional Random Move");
        csvTitleLine.add("Static Random Move");
        csvTitleLine.add("Static Random Move Amount");
        csvTitleLine.add("Random Next Moves");
        csvTitleLine.add("Only learn junctions");
        csvTitleLine.add("Back Tracking");
        csvTitleLine.add("Pick New Paths");
        return csvTitleLine;

    }

}
