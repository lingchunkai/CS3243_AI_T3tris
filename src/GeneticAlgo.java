import java.util.*;

public abstract class GeneticAlgo {

    public List<Chromosome> population;

    void generateNextGeneration() {

        List<ParentPair> parents = selection();
        List<Chromosome> children = crossover(parents);
        children = mutate(children);
        children = filter(children);
//        for (Chromosome c : children) {
//            computeFitnessLevel(c);
//        }
        List<Thread> threads = new ArrayList<Thread>();
        for (Chromosome c : children) {
            computeFitnessLevel(c,threads, 5);
//            Runnable task = new MyRunnable(c, 1);
//            Thread worker = new Thread(task);
//              worker.start();
//            threads.add(worker);
        }
        waitTillDone(threads);


    
        population = acceptance(children);
    }

    protected void waitTillDone(List<Thread> threads)
    {
        boolean finish = false;
    do {

     finish = true;
      for (Thread thread : threads) {
        if (thread.isAlive()) {
            finish =false;
          break;
        }
      }
      //System.out.println("We have " + running + " running threads. ");
    } while (finish != true);
    }


    // Defines a pair of parents
    class ParentPair {

        public Chromosome a, b;

        public ParentPair(Chromosome a, Chromosome b) {
            this.a = a;
            this.b = b;
        }
    }

    GeneticAlgo(List<Chromosome> initialPop) {
        population = initialPop;
        List<Thread> threads = new ArrayList<Thread>();
        for (Chromosome c : population) {
            computeFitnessLevel(c,threads, 5);
        }

        waitTillDone(threads);
    }

    // Computes the fitness of a given Chromosome
    abstract protected void computeFitnessLevel(Chromosome c,List<Thread> threads, int n);

    // Selects which chromosomes to breed
    abstract protected List<ParentPair> selection();

    // Creates the children from the parents chosen. Returns a list of children.
    abstract protected List<Chromosome> crossover(List<ParentPair> parents);
    
    // Implements filtering. Leave blank if we do not wish for filtering to occur.
    abstract protected List<Chromosome> filter(List<Chromosome> children);

    // Implements mutation. Leave blank if we do not wish for mutation to occur.
    abstract protected List<Chromosome> mutate(List<Chromosome> children);

    // Merges the parent and children populations to give the next generation
    abstract protected List<Chromosome> acceptance(List<Chromosome> children);

    // Gets the best chromosome in the current population
    public Chromosome getBest() {
        Chromosome bestChromosome = null;
        for (Chromosome c : population) {
            if (bestChromosome == null || bestChromosome.getFitness() < c.getFitness()) {
                bestChromosome = c;
            }
        }
        return bestChromosome;
    }

    public List<Chromosome> getPopulation() {
        return population;
    }
    
    public abstract String toString();
}
