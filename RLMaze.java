import QLearning.*;
import mazeGenerator.*;

import java.io.*;
import java.util.ArrayList;

public class RLMaze {

    private Maze maze;
    private Agent agent;
    private GUI gui;
    private int mazeX;
    private int mazeY;
    private String mazeType;


    public static void main(String[] args) {
       new RLMaze(args);
    }

    public RLMaze(String[] args) {

        mazeX = Integer.parseInt(args[1]);
        mazeY = Integer.parseInt(args[2]);
        if (args[0].toLowerCase().equals("c"))
            maze = getNewMaze(mazeX, mazeY);
        else if (args[0].toLowerCase().equals("w"))
            maze = new WaterMaze(mazeX, mazeY);
        else if (args[0].toLowerCase().equals("t"))
            maze = new TMaze();
        else {
            System.out.println("Invalid maze choice");
            System.out.println("c - classic maze, w - water maze, t - T-maze");
            System.out.println("Input structure: <mazeType> <mazeHeight> <mazeWidth> <function> [functionArgs]");
            System.exit(0);
        }
//
        if (args[3].equals("learnMaze")) {
            if (args[4].equals("p"))
                learnSingleMaze(true);
            else
                learnSingleMaze(false);
        } else if (args[3].equals("stepOver"))
            stepOver();

        else if (args[3].equals("moveFood"))
            if(args[4].equals("n"))
                testMoveFood(false);
            else
                testMoveFood(true);

        else if (args[3].equals("test"))
            test(args[4], Integer.parseInt(args[5]));


        else {
            System.out.println("Invalid function choice");
            System.out.println("learnMaze - Learns the maze and prints results");
            System.out.println("stepOver - Allows the user to step through the learning process");
            System.out.println("moveFood - Trains the agent then moves the goal");
            System.out.println("test - Test effects of different agent variables");
            System.out.println("Input structure: <mazeType> <mazeHeight> <mazeWidth> <function> [functionArgs]");
            System.exit(0);
        }


        gui = new GUI(maze, agent);

        //        maze = getNewMaze();
//        maze = new WaterMaze(12, 12);
//         maze = new TMaze();


//        stepOver();
//      compareBackTrack();
//        compareOnlyLearnJunctions();
//        compareLearningRate();
//        compareDiscountRate();
//        compareLearningAndDiscountRate();
//        compareVisualLearning();
//        comparePickNewPaths();
//        findDivergenceFromShortestPath();
//        moveFood();
//        moveFoodWithStaticRandomMoves();
//        compareEpsilonGreedyPolicy();
//        comparePolicyAndVisualLearning();
//        findMazeWhereAgentGetsLost();
//        getRangeOfMazes(70, 70);
//        test();
//        fourway();


    }


    public Agent getNewAgent() {
        agent = null;
        Agent newAgent = new Agent(maze.getCell(0, 0));
        return newAgent;
    }

    private Maze getNewMaze() {
        Maze newMaze;
        if(mazeType.equals("c")) {
            maze = new ClassicMaze(mazeX, mazeY);
        }
        if(mazeType.equals("w")) {
            newMaze = new WaterMaze(mazeX, mazeY);
        }
        else{
            newMaze = new TMaze();
        }
        return newMaze;
    }

    private Maze getNewMaze(int height, int width) {
        Maze newMaze = new ClassicMaze(width, height);
        return newMaze;
    }


    public void learnSingleMaze(boolean printEpisodes) {
        agent = getNewAgent();
        agent.settings().printEpisodeLengths = printEpisodes;
        AgentOutputData output = learn();
        printCoreData(output);

    }

    public void stepOver() {
        agent = getNewAgent();
    }

    public void test(String settingid, int noReps) {
        int avgTrueNoEps = 0;
        int avgFalseNoEps = 0;
        int avgTruePathLen = 0;
        int avgFalsePathLen = 0;
        for (int i = 0; i < noReps; i++) {
            maze = getNewMaze();

            agent = getNewAgent();
            adjustBooleanSetting(settingid, false);
            System.out.println(agent.settings().onlyLearnJunctions);

            AgentOutputData output = learn();
            avgFalseNoEps += output.getStepNumbersOfEpisodes().size();
            avgFalsePathLen += output.getLearnedPath().size();
            System.out.println("F- " + output.getStepNumbersOfEpisodes().size());
//            if(output.getStepNumbersOfEpisodes().size() < 12)
//                return;

            agent = getNewAgent();
            adjustBooleanSetting(settingid, true);
            System.out.println(agent.settings().onlyLearnJunctions);

            AgentOutputData output2 = learn();
            avgTrueNoEps += output2.getStepNumbersOfEpisodes().size();
            avgTruePathLen += output2.getLearnedPath().size();

            System.out.println("T- " + output2.getStepNumbersOfEpisodes().size());
//            if(output2.getStepNumbersOfEpisodes().size() == 301) {
//                System.out.println("--------------");
//                for(int x : output2.getStepNumbersOfEpisodes())
//                    System.out.println(x);
//                return;
//            }
        }
        avgFalseNoEps /= noReps;
        avgTrueNoEps /= noReps;
        avgFalsePathLen /= noReps;
        avgTruePathLen /= noReps;
        System.out.println("--------------");
        System.out.println("No Eps False - " + avgFalseNoEps);
        System.out.println("No Eps True - " + avgTrueNoEps);
        System.out.println("path len false - " + avgFalsePathLen);
        System.out.println("path len true - " + avgTruePathLen);


    }

    public void adjustBooleanSetting(String settingId, boolean newSetting){
        String typeOfDay;
        switch (settingId) {
            case "visualLearning":
                agent.settings().visualLearning = newSetting;
                break;
            case "pickNewPaths":
                agent.settings().pickNewPaths = newSetting;
                break;
            case "antibacktracking":
                agent.settings().backTrack = newSetting;
                break;
            case "onlyLearnJunctions":
                agent.settings().onlyLearnJunctions = newSetting;
                break;
            case "epsilonGreedy":
                agent.settings().staticRandomMove = newSetting;
                break;

            default:
                System.out.println("bad test input");
                System.exit(0);
                break;


        }
    }

    public void compareBackTrack() {
        agent = getNewAgent();
        agent.settings().backTrack = false;
        AgentOutputData output = learn();
        System.out.println(output.getStepNumbersOfEpisodes().size());

        agent = getNewAgent();
        agent.settings().backTrack = true;
        AgentOutputData output2 = learn();
        System.out.println(output2.getStepNumbersOfEpisodes().size());

        System.out.println("stop");
    }


    public void compareVisualLearning() {
        ArrayList<AgentOutputData> outputData = new ArrayList<>();

        int noReps = 20;
        for (float visualLearningAmount = 0; visualLearningAmount <= 20; visualLearningAmount += 1)
            for (int reps = 0; reps < noReps; reps++) {

                agent = getNewAgent();
                agent.settings().visualLearning = true;
                agent.settings().visualLearningMultiplier = visualLearningAmount;
                System.out.println("learning multiplier: " + visualLearningAmount);
                AgentOutputData outputx = learn();
                outputx.setRepeitionNumber(reps);
                printCoreData(outputx);
                outputData.add(outputx);

            }
        writeToFile(outputData, "compareVisualLearning2D_classic_delete_this5");
    }

    public void compareOnlyLearnJunctions() {
        ArrayList<AgentOutputData> outputData = new ArrayList<>();

        int noReps = 3;
        for (int nthMazeGen = 0; nthMazeGen < 15; nthMazeGen++) {
            for (int rep = 0; rep < noReps; rep++) {
                agent = getNewAgent();
                agent.settings().onlyLearnJunctions = true;
                AgentOutputData output1 = learn();
                printCoreData(output1);
                outputData.add(output1);

//            }
//            for (int rep = 0; rep < noReps; rep++) {

                agent = getNewAgent();
                agent.settings().onlyLearnJunctions = false;
                AgentOutputData output2 = learn();
                printCoreData(output2);
                outputData.add(output2);
            }
            maze = getNewMaze();
        }
        writeToFile(outputData, "compareOnlyLearnJuntions_classic20x20");
    }

    public void compareLearningAndDiscountRate() {
        ArrayList<AgentOutputData> outputData = new ArrayList<>();
        int noReps = 50;
        for (double learningRate = 0.1; learningRate <= 1; learningRate += 0.1)
            for (double discountRate = 0.1; discountRate <= 1; discountRate += 0.1)
                for (int rep = 0; rep < noReps; rep++) {
                    agent = getNewAgent();
                    agent.settings().learningRate = learningRate;
                    agent.settings().discountRate = discountRate;
                    AgentOutputData outputx = new AgentOutputData();
                    outputx = learn();
                    outputx.setRepeitionNumber(rep);
                    System.out.println(learningRate);
                    System.out.println(discountRate);
                    printCoreData(outputx);
                    if (outputx.settings() == null)
                        System.out.println("yooooooo");

                    outputData.add(outputx);
                }
        writeToFile(outputData, "learningAndDiscount3D_2_40x40_classic");
    }

    public void compareLearningRate() {
        ArrayList<AgentOutputData> outputData = new ArrayList<>();
        for (double learningRate = 0; learningRate <= 1; learningRate += 0.1) {
            for (int rep = 0; rep < 5; rep++) {
                agent = getNewAgent();
                agent.settings().learningRate = learningRate;
                AgentOutputData outputx = new AgentOutputData();
                outputx = learn();
                outputx.setRepeitionNumber(rep);
                System.out.println(learningRate);
                printCoreData(outputx);
                if (outputx.settings() == null)
                    System.out.println("yooooooo");

                outputData.add(outputx);
            }
        }
        writeToFile(outputData, "CompareLearningRate");
    }

    public void compareDiscountRate() {
        ArrayList<AgentOutputData> outputData = new ArrayList<>();

        for (double discountRate = 0; discountRate <= 1; discountRate += 0.1) {
            for (int rep = 0; rep < 5; rep++) {
                agent = getNewAgent();
                agent.settings().discountRate = discountRate;
                AgentOutputData outputx = new AgentOutputData();
                outputx = learn();
                outputx.setRepeitionNumber(rep);
                System.out.println(discountRate);
                printCoreData(outputx);
                if (outputx.settings() == null)
                    System.out.println("yooooooo");

                outputData.add(outputx);
            }
        }
        writeToFile(outputData, "CompareDiscountRate");

    }


    public void comparePickNewPaths() {
        agent = getNewAgent();
        agent.settings().pickNewPaths = false;
        AgentOutputData output = learn();
        printCoreData(output);

        agent = getNewAgent();
        agent.settings().pickNewPaths = true;
        AgentOutputData output2 = learn();
        printCoreData(output2);
    }

    public void compareEpsilonGreedyPolicy() {
        ArrayList<AgentOutputData> outputData = new ArrayList<>();
        int noReps = 20;
        for (double randomMoveAmount = 0.8; randomMoveAmount <= 1.01; randomMoveAmount += 0.01)
            for (int repetition = 0; repetition < noReps; repetition++) {
                agent = getNewAgent();
                agent.settings().staticRandomMove = true;
                agent.settings().staticRandomMoveAmount = randomMoveAmount;
                AgentOutputData outputx = new AgentOutputData();
                outputx = learn();
                outputx.setRepeitionNumber(repetition);
                System.out.println(randomMoveAmount);
                printCoreData(outputx);
                if (outputx.settings() == null)
                    System.out.println("yooooooo");

                outputData.add(outputx);
            }

        writeToFile(outputData, "policyVariations2D");
    }

    public void comparePolicyAndVisualLearning() {
        ArrayList<AgentOutputData> outputData = new ArrayList<>();
        int noReps = 10;
        for (double visualLearningAmount = 0; visualLearningAmount <= 3; visualLearningAmount += 0.1)
            for (double randomMoveAmount = 0.86; randomMoveAmount <= 1.01; randomMoveAmount += 0.02)
                for (int repetition = 0; repetition < noReps; repetition++) {
                    agent = getNewAgent();
                    agent.settings().proportionalRandomMove = false;
                    agent.settings().staticRandomMove = true;
                    agent.settings().staticRandomMoveAmount = randomMoveAmount;
                    agent.settings().visualLearningMultiplier = visualLearningAmount;
                    AgentOutputData outputx = new AgentOutputData();
                    outputx = learn();

                    outputx.setRepeitionNumber(repetition);
                    System.out.println(visualLearningAmount);
                    System.out.println(randomMoveAmount);
                    printCoreData(outputx);
//                    if(outputx.getNumEpisodes() < 15)
//                        return;
                    outputData.add(outputx);
                }

        writeToFile(outputData, "policy_visualLearning3D_classic");
    }


    public void findDivergenceFromShortestPath() {
        agent = getNewAgent();
        int noMazesTried = 0;
        AgentOutputData output;
        while (true) {
            noMazesTried++;
            System.out.println(noMazesTried);
            maze = new ClassicMaze(40, 40);
            agent = getNewAgent();
            output = learn();
            int agentShortestPath = output.getLearnedPath().size();
            int mazeShortestPath = ((ClassicMaze) maze).getPathFinder().getShortestPath().size();
            if (agentShortestPath > mazeShortestPath * 1.1) {
                System.out.println("Number of mazes to find divergence: " + noMazesTried);
                break;
            }


        }
        printCoreData(output);
//        agent = getNewAgent();
    }

    public void testMoveFood(boolean includeControl) {

        agent = getNewAgent();

        maze.setGoal(maze.getCell(maze.getXdimension() - 1, maze.getYdimension() - 1));
        if (maze instanceof ClassicMaze) {
            ClassicMaze cmaze = (ClassicMaze) maze;
            cmaze.solveMaze();
        }

        AgentOutputData output = learn();
        printCoreData(output);

        agent.setupNewEpisode();

        maze.setGoal(maze.getCell(maze.getXdimension() - 1, 0));
        if (maze instanceof ClassicMaze) {
            ClassicMaze cmaze = (ClassicMaze) maze;
            cmaze.solveMaze();
        }

        AgentOutputData output2 = learn();
        printCoreData(output2);

        agent = getNewAgent();

        if (includeControl) {
            AgentOutputData output3 = learn();
            printCoreData(output3);
        }
    }

    public void moveFood() {
        int noReps = 50;
        ArrayList<ArrayList<Integer>> outputList1 = new ArrayList<>();
        ArrayList<Integer> rep1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> outputList2 = new ArrayList<>();
        ArrayList<Integer> rep2 = new ArrayList<>();

        ArrayList<AgentOutputData> outputData1 = new ArrayList<>();
        ArrayList<AgentOutputData> outputData2 = new ArrayList<>();

        for (int newMaze = 0; newMaze <= 100; newMaze++) {
//            maze = new WaterMaze(20, 20);

            maze = getNewMaze();
            for (int repetition = 0; repetition < noReps; repetition++) {

                System.out.println(newMaze);
                maze.setGoal(maze.getCell(maze.getXdimension() - 1, 0));

                agent = getNewAgent();
                AgentOutputData output = learn();
                output.setRepeitionNumber(repetition);
                outputData1.add(output);

                rep1.add(output.getNumEpisodes());
                printCoreData(output);
                //agent.setupNewEpisode();

                maze.setGoal(maze.getCell(0, maze.getYdimension() - 1));

                AgentOutputData output2 = learn();
                outputData2.add(output2);

                output2.setRepeitionNumber(repetition);
                rep2.add(output2.getNumEpisodes());
                printCoreData(output2);

            }
            outputList1.add(rep1);
            outputList2.add(rep2);
//            writeToFile(outputData1, "movefood1");
//            writeToFile(outputData2, "movefood2");

        }
        double output1avg = 0;
        double std1 = 0;
        for (ArrayList<Integer> rep : outputList1) {
            double avgofavg = 0;
            for (int x : rep)
                avgofavg += x;
            avgofavg /= rep.size();
            output1avg += avgofavg;
        }
        output1avg /= outputList1.size();

        System.out.println(output1avg);

        double output2avg = 0;
        for (ArrayList<Integer> rep : outputList2) {
            double avgofavg = 0;
            for (int x : rep)
                avgofavg += x;
            avgofavg /= rep.size();
            output2avg += avgofavg;
        }
        output2avg /= outputList2.size();

        System.out.println(output2avg);

    }

    public void moveFoodWithStaticRandomMoves() {
        compareEpsilonGreedyPolicy();
        maze.setGoal(maze.getCell(0, maze.getYdimension() - 1));
        compareEpsilonGreedyPolicy();
    }

    public void bareBones() {
        ArrayList<AgentOutputData> outputData = new ArrayList<>();
        for (int reps = 0; reps <= 50; reps++) {
            agent = getNewAgent();
            agent.settings().backTrack = false;
            agent.settings().pickNewPaths = false;
            agent.settings().onlyLearnJunctions = false;
            agent.settings().visualLearning = false;
            agent.settings().qTableDecay = false;
            AgentOutputData outputx = new AgentOutputData();
            outputx = learn();
            outputx.setRepeitionNumber(reps);
            printCoreData(outputx);

            outputData.add(outputx);
        }
        writeToFile(outputData, "bareBones1D - classic 12x12");
    }

    public void findMazeWhereAgentGetsLost() {
        while (true) {
//            maze =  getNewMaze(40, 40);
            agent = getNewAgent();
            AgentOutputData outputx = new AgentOutputData();
            outputx = learn();
            printCoreData(outputx);
            if (outputx.getNumEpisodes() > outputx.settings().episodeFailPoint - 1 || outputx.getNumEpisodes() < 12) {
                for (int x : outputx.getStepNumbersOfEpisodes())
                    System.out.println(x);
                return;
            }
        }
    }


    private void saveMaze() {
        Maze highestOrderMaze = null;
        int maxJunctions = 0;
        for (int i = 0; i < 10; i++) {
            maze = getNewMaze();
            int noJunc = maze.getNoJunctions();
            if (maxJunctions == 0 || noJunc > maxJunctions) {
                maxJunctions = noJunc;
                highestOrderMaze = maze;
            }
            System.out.println(maxJunctions);

        }
        try {
            FileOutputStream fileOut = new FileOutputStream("savedMazes/maze1.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(maze);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    private AgentOutputData learn() {
        AgentOutputData output = agent.learn();
        output.addMaze(maze);
        output.addSettings(agent.settings());
        return output;
    }

    private void printCoreData(AgentOutputData output) {
        System.out.println("Number of episodes to learn maze: " + output.getNumEpisodes());
        System.out.println("Shortest path: " + output.getLearnedPath().size());
        if (output.getMaze() instanceof ClassicMaze) {
            System.out.println("Best path : " + ((ClassicMaze) output.getMaze()).getPathFinder().getShortestPath().size());
        }

        System.out.println("Number of Junctions: " + output.getMaze().getNoJunctions());
        System.out.println("--------------------");
    }

    public void writeToFile(ArrayList<AgentOutputData> outputDataList, String fileName) {

        PrintWriter writer = null;
        File file = new File("C:/Users/Max's Laptop/Documents/University/4th year/Dissertation Project/experimentData/" + fileName + ".csv");
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String delimiter = ",";
//        for (String title : agent.settings().getCsvTitleLine())
//            writer.append(title + delimiter);
//        writer.println();
        for (AgentOutputData outputData : outputDataList) {
            String csvLine = "";
            csvLine += outputData.getRepeitionNumber() + delimiter;
            if (outputData.settings() == null)
                System.out.println("yoo");
            for (String str : outputData.settings().toCsv()) {
                if (str == null)
                    System.out.println("yoo");
                csvLine += str + delimiter;
            }
            if (!outputData.isFailed())
                csvLine += String.valueOf(outputData.getLearnedPath().size()) + delimiter;
            else
                csvLine += String.valueOf(outputData.settings().moveFailPoint) + delimiter;
            if (maze instanceof ClassicMaze) {
                ClassicMaze cMaze = (ClassicMaze) maze;
                csvLine += String.valueOf(cMaze.getPathFinder().getShortestPath().size()) + delimiter;
            } else if (maze instanceof WaterMaze) {
                csvLine += String.valueOf(maze.getXdimension() + maze.getYdimension()) + delimiter;
            } else {
                csvLine += "tmaze" + delimiter;
            }
            if (outputData.isFailed())
                csvLine += String.valueOf(outputData.settings().episodeFailPoint) + delimiter;
            else
                csvLine += String.valueOf(outputData.getStepNumbersOfEpisodes().size()) + delimiter;

            for (int stepNo : outputData.getStepNumbersOfEpisodes()) {
                csvLine += String.valueOf(stepNo) + delimiter;
            }

            writer.println(csvLine);


        }
        writer.close();
    }

    public void getRangeOfMazes(int height, int width) {
        int noMazesTested = 100;
        ArrayList<AgentOutputData> outputDataOrd = new ArrayList<>();
        for (int i = 0; i < noMazesTested; i++) {
            System.out.println(i);
            maze = getNewMaze(60, 60);
            agent = getNewAgent();
//            agent.settings().visualLearning = false;
//            agent.settings().backTrack = false;
//            agent.settings().onlyLearnJunctions = false;
//            agent.settings().pickNewPaths = false;
//            agent.settings().qTableDecay = false;
            AgentOutputData output = new AgentOutputData();
            output = learn();
            if (output.isFailed()) {
                System.out.println("return");
                return;
            }
            if (outputDataOrd.size() == 0)
                outputDataOrd.add(output);
            else if (output.getNumEpisodes() >= outputDataOrd.get(outputDataOrd.size() - 1).getNumEpisodes()) {
                outputDataOrd.add(output);
                continue;
            }
            int dataIndex = 0;
            for (AgentOutputData outputData : outputDataOrd) {
                if (output.getNumEpisodes() < outputData.getNumEpisodes()) {
                    outputDataOrd.add(dataIndex, output);
                    break;
                }
                dataIndex++;
            }


        }
        System.out.println("y0");
        int noFails = 0;
        for (AgentOutputData out : outputDataOrd)
            if (out.getNumEpisodes() == 501)
                noFails++;
        System.out.println(noFails);
    }

    public void fourway() {
        ArrayList<AgentOutputData> outputData = new ArrayList<>();
        int noReps = 10;
        for (double visualLearningAmount = 0; visualLearningAmount <= 5; visualLearningAmount += 0.5)
            for (double randomMoveAmount = 0.7; randomMoveAmount <= 1.01; randomMoveAmount += 0.1)
                for (double learningRate = 0.1; learningRate <= 1; learningRate += 0.1)
                    for (double discountRate = 0.1; discountRate <= 1; discountRate += 0.1)
                        for (int repetition = 0; repetition < noReps; repetition++) {
                            agent = getNewAgent();
                            agent.settings().staticRandomMove = true;
                            agent.settings().staticRandomMoveAmount = randomMoveAmount;
                            agent.settings().visualLearningMultiplier = visualLearningAmount;
                            agent.settings().learningRate = learningRate;
                            agent.settings().discountRate = discountRate;
                            AgentOutputData outputx = new AgentOutputData();
                            outputx = learn();

                            outputx.setRepeitionNumber(repetition);
                            System.out.println(visualLearningAmount);
                            System.out.println(randomMoveAmount);
                            System.out.println(learningRate);
                            System.out.println(discountRate);
                            printCoreData(outputx);
//                    if(outputx.getNumEpisodes() < 15)
//                        return;
                            if (outputx.settings() == null)
                                System.out.println("yooooooo");

                            outputData.add(outputx);
                        }

        writeToFile(outputData, "visual4way");
    }


}
