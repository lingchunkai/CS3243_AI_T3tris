import java.util.*;

/**
 * The simplest genetic algorithm possible.
 *
 * This algorithm creates a genetic algorithm with the following properties:
 * 1) Constant population size
 * 2) Flexible maximum height (set to infinity if we want to simulate full games)
 * 3) Crossover rate refers to the fraction of individuals which will be formed by child chromosomes
 * 4) Mutation rate refers to the fraction of *CHILDREN* which will be mutated
 * 5) We test each chromosome with 5 games per generation.
 * 6) Chromosomes to be chosen as parents on a weighted basis - chromosomes with a higher fitness are
 * more likely to be chosen
 * 7) The same chromosome may be chosen to mate with itself - no restrictionss on that.
 *
 * This initial population should be seeded by the user, and all chromosomes are assumed
 * to be have relevant crossover/mutation operators defined. If there are different classes
 * of chromosomes available, then there must be crossover operations defined for both of them (wrt each other)
 */
public class SimpleGA extends GeneticAlgo {

    private int maxHeight;
    private double mutationProb;
    private double crossoverRate;

    /**
     * @param initialPop    Initial population. It also determines the population size.
     * @param maxHeight     Maximum height to use in simulations. Set to a low value to speed up simulation time at the
     *                      expense of accuracy.
     * @param mutationProb  Double ranging from 0-1 inclusive. Set to 0 if we do not want mutations, and 1, if we want
     *                      all children to be mutated.
     * @param crossoverRate Double ranging from 0-1 inclusive. Set to 0 if we do not want to population to evolve (don't
     *                      do this), or 1 if we want each generation to completely replace its parents. Bear in in mind
     *                      that high crossover rates may result in the loss of possibly good genes. It may also result
     *                      in longer times for each generation.
     */
    SimpleGA(List<Chromosome> initialPop, int maxHeight, double mutationProb, double crossoverRate) {
    	super(initialPop);
        this.maxHeight = maxHeight;
        this.mutationProb = mutationProb;
        this.crossoverRate = crossoverRate;
    }

    @Override
    protected void computeFitnessLevel(Chromosome c,List<Thread> threads, int n) {
            Runnable task = new MyRunnable(c, maxHeight, n);
            Thread worker = new Thread(task);
            worker.start();
            threads.add(worker);

            //computeFitnessLevel(c, 5);
    	//return 0;
    }
//
//    protected void computeFitnessLevel(Chromosome c) {
//    
//            computeFitnessLevel(c, 5);
//    	//return 0;
//    }
//    
//    protected void computeFitnessLevel(Chromosome c, int n) {
//        // Run simulation 5 times, compute the number of lines removed, and take average
//        for (int x = 0; x < n; x++) {
//        	Simulator sim = new Simulator(maxHeight); // Force death at maxHeight
//            c.addGameScore(sim.getNumTurnsPassed(c));
//        }
//        //return c.getFitness();
//    }

    

    @Override
    protected List<ParentPair> selection() {
        // Calculate number of parents required. Every parent will create a children.
        int numParentPairs = (int) ((population.size()) * crossoverRate);
        List<ParentPair> parentPairs = new ArrayList<ParentPair>(numParentPairs);

        // Brute force weighted selection based on fitness
        List<Double> totals = new ArrayList<Double>(population.size());
        double runningTotal = 0;

        for (Chromosome c : population) {
            runningTotal += c.getFitness();
            totals.add(runningTotal);
        }

        for (int i = 0; i < numParentPairs; i++) {
            Chromosome parents[] = new Chromosome[2];
            for (int j = 0; j < 2; j++) {
                double rand = Math.random() * runningTotal;
                // TODO: we could do binary search here
                for (int k = 0; k < totals.size(); k++) {
                    if (rand < totals.get(k)) {
                        parents[j] = population.get(k);
                        break;
                    }
                    // This should never happen; but be safe anyway.
                    if (parents[j] == null) {
                        parents[j] = population.get(population.size() - 1);
                    }
                }
            }
            parentPairs.add(new ParentPair(parents[0], parents[1]));
        }

        return parentPairs;
    }

    @Override
    protected List<Chromosome> acceptance(List<Chromosome> children) {
        // To maintain the population size, we remove the weakest parents.
    	int numParentsRequired = population.size() - children.size();
    	Collections.sort(population);
    	List<Chromosome> oldPopSelected = population.subList(0, numParentsRequired);
    	// Retest all the old guard

        List<Thread> threads = new ArrayList<Thread>();
    	for (Chromosome c : oldPopSelected) {
    		//computeFitnessLevel(c, 1);
            Runnable task = new MyRunnable(c, maxHeight, 1);
            Thread worker = new Thread(task);
            worker.start();
            threads.add(worker);
    	}

        waitTillDone(threads);
        
    	children.addAll(oldPopSelected);
    	Collections.sort(children);
    	return children;
    }

    @Override
    protected List<Chromosome> mutate(List<Chromosome> children) {
    	Random rng = new Random();
    	for (Chromosome c : children) {
    		if (rng.nextDouble() < mutationProb) {
    			c.mutate();
    		}
    	}
        return children;
    }

    @Override
    protected List<Chromosome> crossover(List<ParentPair> parents) {
        List<Chromosome> ret = new ArrayList<Chromosome>();
        for (ParentPair p : parents) {
            ret.add(p.a.Crossover(p.b));
        }
        return ret;
    }

	@Override
	protected List<Chromosome> filter(List<Chromosome> children) {
		return children;
	}

	@Override
	public String toString() {
    	StringBuilder str = new StringBuilder();
    	str.append("PopSize " + population.size() + "\n");

        List<Chromosome> pop = population;
        str.append("POPULATION\n");
        for (Chromosome c : pop) {
        	str.append(c.toString() + "\n");
        }
        
        str.append("Best:\n" + getBest().toString() + "\n");
        
        return str.toString();
	}
}
