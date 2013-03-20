import java.util.*;


/**
 * Simplest genetic algorithm possible
 * 
 * This algorithm creates a genetic algorithm with the following properties:
 * 1) Constant population size
 * 2) Flexible maximum height (set to infinity if we want to simulate full games)
 * 3) Crossover rate refers to the fraction of individuals which will be formed by child chromosomes
 * 4) Mutation rate refers to the fraction of *CHILDREN* which will be mutated
 * 5) We test each chromsome with 5 games per generation.
 * 6) Chromosomes to be chosen as parents on a weighted basis - chromosomes with a higher fitness are 
 * more likely to be chosen
 * 7) The same chromosome may be chosen to mate with itself - no restricions on that.
 * 
 * This initial population should be seeded by the user, and all chromosomes are assumed
 * to be have relevant crossover/mutation operators defined. If there are different classes
 * of chromosomes available, then there must be crossover operations defined for both of them (wrt each other)
 * 
 * 
 * 
 */
public class SimpleGA extends GeneticAlgo{

	private double mutationProb;
	private double crossoverRate;
	private int maxHeight;
	
    /**
     * @param initialPop
     * Initial population. Also determines the population size
     * @param maxHeight
     * Maxmimum height to use in simulations. Set to a low value to speed up simulation time at the expense of accuracy
     * @param mutationProb
     * Double ranging from 0-1 inclusive. Set to 0 if we do not want mutations, and 1, if we want all children to be mutated
     * @param crossoverRate
     * Double ranging from 0-1 inclusive. Set to 0 if we do not want to population to evolve (don't do this), or 1 if we want each 
     * generation to completely replace its parents. Bear in in mind that high crossover rates may result in the loss of possibly good genes.
     * It may also result in longer times for each generation.
     */
    SimpleGA(List<Chromosome> initialPop, int maxHeight, double mutationProb, double crossoverRate) {
    	super(initialPop);
    	this.mutationProb = mutationProb;
    	this.crossoverRate = crossoverRate;
    	this.maxHeight = maxHeight;
    }
     
    protected double computeFitnessLevel(Chromosome c) {
        // Run simulation 5 times, compute the number of lines removed, and take average
        for (int x = 0; x < 5; x++) {
        	Simulator sim = new Simulator(maxHeight); // Force death at maxHeight
            c.addGameScore(sim.getNumTurnsPassed(c));
        }
        return c.getFitness();
    }

    protected List<ParentPair> selection() {
        List<ParentPair> ret = new ArrayList<ParentPair>();
        int numChildren = (int)((population.size()) * crossoverRate);
        // Select 2 * children_required chromosomes, use brute force - we should probably optimize here
        double totalFitness = 0;
        for (Chromosome c : population) {
            totalFitness += c.getFitness();
        }

        for (int x = 0; x < numChildren; x++) {
            Chromosome parents[] = new Chromosome[2];
            for (int y = 0; y < 2; y++) {
                double rand = Math.random() * totalFitness;
                double cumulativeSum = 0;
                parents[y] = null;
                for (Chromosome c : population) {
                    cumulativeSum += c.getFitness();
                    if (cumulativeSum >= rand - 0.0000000000000001) {
                        parents[y] = c;
                        break;
                    }
                }
            }
            ret.add(new ParentPair(parents[0], parents[1]));
        }

        return ret;
    }

    protected List<Chromosome> acceptance(List<Chromosome> children) {
        // To maintain the population size, we remove the weakest parents.
    	int numParentsRequired = population.size() - children.size();
    	ArrayList<Chromosome> l = new ArrayList<Chromosome>();
    	Collections.sort(population);
    	List<Chromosome> oldPopSelected = population.subList(0, numParentsRequired);
    	// Retest all the old guard
    	for (Chromosome c : oldPopSelected) {
    		computeFitnessLevel(c);
    	}
    	children.addAll(oldPopSelected);
    	Collections.sort(children);
    	return children;
    }

    protected List<Chromosome> mutate(List<Chromosome> children) {
    	Random rng = new Random();
    	for (Chromosome c : children) {
    		if (rng.nextDouble() < mutationProb) {
    			c.mutate();
    		}
    	}
        return children;
    }

    protected List<Chromosome> crossover(List<ParentPair> parents) {
        List<Chromosome> ret = new ArrayList<Chromosome>();
        for (ParentPair p : parents) {
            ret.add(p.a.Crossover(p.b));
        }
        return ret;
    }
}