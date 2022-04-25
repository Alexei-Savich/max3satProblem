package max3SetProblem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Algorithm {

    private HashMap<Integer, Boolean> best;
    private int bestFitenss = 0;
    private ArrayList<HashMap<Integer, Boolean>> currentGeneration;
    private int[] currFitnesses;
    private int generation = 0;
    private int indexOfTheBest = 0;
    private int indexOfTheWorst = 0;
    private int indexOfTheSecondBest = 0;
    private int[][] data;
    private final int[] allNums;
    private final int maxN;
    private final int numbersInOneBracket;
    private final int generationSize;
    private final int iterations;
    private final int mutationChance;
    private final int maxBitsToMutate;
    private int numOfBrackets;

    public Algorithm(String path, int numOfBrackets, int numbersInOneBracket, String splitter,
                     int populationSize, int iterations, int mutationChance, double mutatePercent, int maxN) {
        this.allNums = new int[maxN];
        for (int i = 0; i < maxN; i++) {
            allNums[i] = i + 1;
        }
        this.maxN = maxN;
        this.numbersInOneBracket = numbersInOneBracket;
        data = new int[numOfBrackets][numbersInOneBracket];
        this.generationSize = populationSize;
        this.iterations = iterations;
        this.mutationChance = mutationChance;
        this.maxBitsToMutate = (int) ((mutatePercent) / 100 * maxN);
        this.currFitnesses = new int[populationSize];
        currentGeneration = new ArrayList<>();
        parse(path, splitter);
    }

    public void nextGenerationDebug() {
        double lowestFitness = Integer.MAX_VALUE;
        double highestFitness = Integer.MIN_VALUE;
        double averageFitness = 0;
        int indexOfTheWorst = -1;
        int indexOfTheBest = -1;
        int secondBest = -1;
        ArrayList<HashMap<Integer, Boolean>> temporalEncoders = new ArrayList<>();
        for (int i = 0; i < generationSize; i++) {
            HashMap<Integer, Boolean> enc = crossover();
            Random rand = new Random();
            double mutationNumber = rand.nextDouble() * 100;
            if (mutationChance < mutationNumber) {
                for (int j = 0; j < maxBitsToMutate; j++) {
                    int num = allNums[rand.nextInt(allNums.length)];
                    boolean val = !enc.get(num);
                    enc.put(num, val);
                }
            }
            currFitnesses[i] = evaluateSample(enc);
            temporalEncoders.add(enc);
            if (currFitnesses[i] > highestFitness) {
                secondBest = indexOfTheBest;
                highestFitness = currFitnesses[i];
                indexOfTheBest = i;
            }
            if (currFitnesses[i] < lowestFitness) {
                lowestFitness = currFitnesses[i];
                indexOfTheWorst = i;
            }
            averageFitness += currFitnesses[i];
        }
        averageFitness = averageFitness / generationSize;
        generation++;
        if (generation % 100 == 0) {
            System.out.println("Generation: " + generation + "; best: " + highestFitness + "; average: "
                    + averageFitness + "; worst: " + lowestFitness);
        }
        currentGeneration.clear();
        for (int i = 0; i < temporalEncoders.size(); i++) {
            currentGeneration.add(temporalEncoders.get(i));
        }
        this.indexOfTheBest = indexOfTheBest;
        this.indexOfTheWorst = indexOfTheWorst;
        int temp = Integer.MIN_VALUE;
        if (secondBest == -1) {
            for (int i = 0; i < generationSize; i++) {
                if (currFitnesses[i] >= temp && i != indexOfTheBest) {
                    secondBest = i;
                    temp = currFitnesses[i];
                }
            }
        }
        this.indexOfTheSecondBest = secondBest;
        if (bestFitenss < highestFitness) {
            best = currentGeneration.get(indexOfTheBest);
            bestFitenss = currFitnesses[indexOfTheBest];
        }
    }

    public void firstGenerationDebug() {
        double lowestFitness = Integer.MAX_VALUE;
        double highestFitness = Integer.MIN_VALUE;
        double averageFitness = 0;
        for (int i = 0; i < generationSize; i++) {
            Random rand = new Random();
            HashMap<Integer, Boolean> map = new HashMap<>();
            for (int num : allNums) {
                map.put(num, rand.nextBoolean());
            }
            currFitnesses[i] = evaluateSample(map);
            if (currFitnesses[i] > highestFitness) {
                indexOfTheSecondBest = indexOfTheBest;
                highestFitness = currFitnesses[i];
                indexOfTheBest = i;
            }
            if (currFitnesses[i] < lowestFitness) {
                lowestFitness = currFitnesses[i];
                indexOfTheWorst = i;
            }
            averageFitness += currFitnesses[i];
            currentGeneration.add(map);
        }
        averageFitness = averageFitness / generationSize;
        generation++;
        System.out.println("Generation: " + generation + "; best: " +
                currFitnesses[indexOfTheBest] + "; average: " + averageFitness + "; worst: "
                + currFitnesses[indexOfTheWorst]);
        best = currentGeneration.get(indexOfTheBest);
        bestFitenss = currFitnesses[indexOfTheBest];
    }

    public void nextGeneration() {
        double lowestFitness = Integer.MAX_VALUE;
        double highestFitness = Integer.MIN_VALUE;
        int indexOfTheWorst = -1;
        int indexOfTheBest = -1;
        int secondBest = -1;
        ArrayList<HashMap<Integer, Boolean>> temporalEncoders = new ArrayList<>();
        for (int i = 0; i < generationSize; i++) {
            HashMap<Integer, Boolean> enc = crossover();
            Random rand = new Random();
            double mutationNumber = rand.nextDouble() * 100;
            if (mutationChance > mutationNumber) {
                for (int j = 0; j < maxBitsToMutate; j++) {
                    int num = allNums[rand.nextInt(allNums.length)];
                    boolean val = !enc.get(num);
                    enc.put(num, val);
                }
            }
            currFitnesses[i] = evaluateSample(enc);
            temporalEncoders.add(enc);
            if (currFitnesses[i] > highestFitness) {
                secondBest = indexOfTheBest;
                highestFitness = currFitnesses[i];
                indexOfTheBest = i;
            }
            if (currFitnesses[i] < lowestFitness) {
                lowestFitness = currFitnesses[i];
                indexOfTheWorst = i;
            }
        }
        generation++;
        currentGeneration.clear();
        currentGeneration.addAll(temporalEncoders);
        int temp = Integer.MIN_VALUE;
        if (secondBest == -1) {
            for (int i = 0; i < generationSize; i++) {
                if (currFitnesses[i] >= temp && i != indexOfTheBest) {
                    secondBest = i;
                    temp = currFitnesses[i];
                }
            }
        }
        this.indexOfTheSecondBest = secondBest;
        this.indexOfTheBest = indexOfTheBest;
        this.indexOfTheWorst = indexOfTheWorst;
        if (bestFitenss < highestFitness) {
            best = currentGeneration.get(indexOfTheBest);
            bestFitenss = currFitnesses[indexOfTheBest];
        }
    }

    public void firstGeneration() {
        double lowestFitness = Integer.MAX_VALUE;
        double highestFitness = Integer.MIN_VALUE;
        for (int i = 0; i < generationSize; i++) {
            Random rand = new Random();
            HashMap<Integer, Boolean> map = new HashMap<>();
            for (int num : allNums) {
                map.put(num, rand.nextBoolean());
            }
            currFitnesses[i] = evaluateSample(map);
            if (currFitnesses[i] > highestFitness) {
                indexOfTheSecondBest = indexOfTheBest;
                highestFitness = currFitnesses[i];
                indexOfTheBest = i;
            }
            if (currFitnesses[i] < lowestFitness) {
                lowestFitness = currFitnesses[i];
                indexOfTheWorst = i;
            }
            currentGeneration.add(map);
        }
        generation++;
        best = currentGeneration.get(indexOfTheBest);
        bestFitenss = currFitnesses[indexOfTheBest];
    }

    private void parse(String toParse, String splitter) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(toParse)))) {
            String line;
            int currBracket = 0;
            //adding all brackets to 2x2 matrix and to Map
            while ((line = br.readLine()) != null) {
                line = line.substring(2, line.length() - 3);
                String[] dataInLine = line.split(splitter);
                //adding to matrix
                int[] nums = new int[numbersInOneBracket];
                for (int i = 0; i < dataInLine.length; i++) {
                    if (dataInLine[i].equals("-0")) {
                        nums[i] = -1 * allNums.length;
                    } else if (dataInLine[i].equals("0")) {
                        nums[i] = allNums.length;
                    } else {
                        nums[i] = Integer.parseInt(dataInLine[i]);
                    }
                }
                data[currBracket] = nums;
                currBracket++;
            }
            numOfBrackets = currBracket;
        } catch (IOException e) {
            System.err.println(e);
            System.exit(0);
        }
    }

    public HashMap<Integer, Boolean> crossover() {
//        HashMap<Integer, Boolean> enc1 = rouletteWheel();
//        HashMap<Integer, Boolean> enc2 = rouletteWheel();
        HashMap<Integer, Boolean> enc1 = currentGeneration.get(indexOfTheBest);
        HashMap<Integer, Boolean> enc2 = currentGeneration.get(indexOfTheSecondBest);
        HashMap<Integer, Boolean> offspring = new HashMap<>();
        Random rand = new Random();
        for (int num : allNums) {
            if (rand.nextBoolean()) {
                offspring.put(num, enc1.get(num));
            } else {
                offspring.put(num, enc2.get(num));
            }
        }
        return offspring;
    }

    public HashMap<Integer, Boolean> rouletteWheel() {
        double sum = 0;
        for (int i = 0; i < generationSize; i++) {
            int curr = currFitnesses[i];
            sum += ((double) curr) * curr * curr * curr / numOfBrackets / numOfBrackets / numOfBrackets / numOfBrackets;
        }

        Random rand = new Random();
        double partialSum = 0;
        double randNumber = rand.nextDouble() * sum;
        for (int i = 0; i < generationSize; i++) {
            int curr = currFitnesses[i];
            partialSum += ((double) curr) * curr * curr * curr / numOfBrackets / numOfBrackets
                    / numOfBrackets / numOfBrackets;
            if (partialSum >= randNumber) {
                return currentGeneration.get(i);
            }
        }
        return null;
    }

    public int evaluateSample(HashMap<Integer, Boolean> encoder) {
        int numOfTrues = 0;
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] < 0) {
                    if (!encoder.get(-1 * data[i][j])) {
                        numOfTrues++;
                        break;
                    }
                } else {

                    if (encoder.get(data[i][j])) {
                        numOfTrues++;
                        break;
                    }
                }

            }
        }
        return numOfTrues;
    }

    public HashMap<Integer, Boolean> getBest() {
        return best;
    }

    public void start(boolean debug) {
        if (debug) {
            long startTime = System.currentTimeMillis();
            firstGenerationDebug();
            for (int i = 0; i < iterations; i++) {
                nextGenerationDebug();
                if (i % 1000 == 0)
                    System.out.println("Time in seconds: " + (double) (System.currentTimeMillis() - startTime) / 1000);
            }
            System.out.println(best);
            System.out.println(bestFitenss);
            long endTime = System.currentTimeMillis();
            System.out.println("Time in seconds: " + (double) (endTime - startTime) / 1000);
            for (int i = 0; i < data.length; i++) {
                boolean isTrue = false;
                for (int j = 0; j < data[i].length; j++) {
                    if (data[i][j] < 0) {
                        boolean test = !best.get(-1 * data[i][j]);
                        if (!best.get(-1 * data[i][j])) {
                            isTrue = true;
                            break;
                        }
                    } else {
                        boolean test = best.get(data[i][j]);
                        if (best.get(data[i][j])) {
                            isTrue = true;
                            break;
                        }
                    }
                }
                System.out.println(Arrays.toString(data[i]) + ": " + isTrue);
            }
        } else {
            long startTime = System.currentTimeMillis();
            firstGeneration();
            for (int i = 0; i < iterations; i++) {
                nextGeneration();
            }
            System.out.println(best);
            System.out.println(bestFitenss);
            long endTime = System.currentTimeMillis();
            System.out.println("Time spent: " + (double) (endTime - startTime) / 1000);
            for (int i = 0; i < data.length; i++) {
                boolean isTrue = false;
                for (int j = 0; j < data[i].length; j++) {
                    if (data[i][j] < 0) {
                        if (!best.get(-1 * data[i][j])) {
                            isTrue = true;
                            break;
                        }
                    } else {
                        if (best.get(data[i][j])) {
                            isTrue = true;
                            break;
                        }
                    }
                }
                System.out.println(Arrays.toString(data[i]) + ": " + isTrue);
            }
        }
    }
}
