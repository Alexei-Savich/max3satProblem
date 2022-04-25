package max3SetProblem;

public class Main {
    public static void main(String[] args) {
        // path to the file
        String path = "./src/max3SetProblem/max3sat/350/m3s_350_0.txt";
        // number of brackets in the file
        int brackets = 1494;
        // number of numbers in one bracket
        int numbersInOneBracket = 3;
        // splitter between the numbers in each bracket
        String splitter = "  ";
        // population size
        int popSize = 1000;
        // number of iterations
        int iters = 25000;
        // chance of mutation for each individual
        int mutChance = 10;
        // percentage of mutated bits for mutated individual
        double mutationPercent = 1;
        // maximal number in the set
        int maxN = 350;
        Algorithm genetic = new Algorithm(path, brackets, numbersInOneBracket, splitter, popSize,
                iters, mutChance, mutationPercent, maxN);
        boolean debug = true;
        genetic.start(debug);
    }
}
