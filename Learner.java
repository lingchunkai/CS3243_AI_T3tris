import java.util.ArrayList;
import java.util.List;

public class Learner {

    private GeneticAlgo algo;
    static Chromosome bestCandidate = null;

    public Learner(GeneticAlgo ga) {
        algo = ga;
    }

    public void Run(int numGenerations) {
        for (int x = 0; x < numGenerations; x++) {
            algo.generateNextGeneration();
            System.out.println("Gen " + x);
            System.out.println(algo.toString());
            setBestCandidate(algo.getBest());

        }
    }

    public void setBestCandidate(Chromosome c) {
        bestCandidate = c;
    }

    public static Chromosome getBestCandidate() {
        return bestCandidate;
    }

    public static void main(String[] args) {
        int populationSize = 100;
        int numGenerations = 1000;

        List<Chromosome> initialPop = new ArrayList<Chromosome>(populationSize);
        for (int x = 0; x < populationSize; x++) {
            initialPop.add(new BetterChromosome());
        }

        SimpleGA ga = new SimpleGA(initialPop, 13, 0.10, 0.6);

        Learner learner = new Learner(ga);
        learner.Run(numGenerations);
    }
}
