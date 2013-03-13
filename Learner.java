
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
            System.out.println("GenSize " + algo.population.size());
            System.out.println("BestFitness " + algo.getBest().getFitness());
            System.out.println("Parameters:\n" + algo.getBest().toString());
            setBestCandidate(algo.getBest());

        }
    }

    public void setBestCandidate(Chromosome c) {
        bestCandidate = c;
    }

    public static Chromosome getBestCandidate() {
        return bestCandidate;
    }
}