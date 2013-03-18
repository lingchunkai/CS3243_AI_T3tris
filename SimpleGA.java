import java.util.*;

/////////////////////////////////////////////////////////////////////////////////////////
//// IMPLEMENTATION OF TRAINER COMES HERE
////
/////////////////////////////////////////////////////////////////////////////////////////


public class SimpleGA<T extends Chromosome> extends GeneticAlgo<T> {

	private double mutationProb;
	private double crossoverRate;
	
    SimpleGA(List<Chromosome> initialPop, double mutationProb, double crossoverRate) {
    	super(initialPop);
    	this.mutationProb = mutationProb;
    	this.crossoverRate = crossoverRate;
    }

    protected double computeFitnessLevel(Chromosome c) {
        // Run simulation 10 times, compute the number of lines removed, and take average
        for (int x = 0; x < 5; x++) {
            // Simulator sim = new Simulator(16); // Force death at height 16.
        	Simulator sim = new Simulator(16);
            c.addGameScore(sim.getNumLinesRemoved(c));
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
    	// Mutation probability of 2%.
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