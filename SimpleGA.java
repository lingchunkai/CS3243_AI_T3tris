import java.util.*;

/////////////////////////////////////////////////////////////////////////////////////////
//// IMPLEMENTATION OF TRAINER COMES HERE
////
/////////////////////////////////////////////////////////////////////////////////////////


public class SimpleGA<T extends Chromosome> extends GeneticAlgo<T> {

    SimpleGA(List<Chromosome> initialPop) {
        // GeneticAlgo(initialPop);
        super(initialPop);
    }

    protected double computeFitnessLevel(Chromosome c) {
        // Run simulation 10 times, compute the number of lines removed, and take average
        int tlines = 0;
        for (int x = 0; x < 10; x++) {
            Simulator sim = new Simulator();
            tlines += sim.getNumTurnsPassed(c);
        }
        return (double) (tlines) / (double) (10);
    }

    protected List<ParentPair> selection() {
        List<ParentPair> ret = new ArrayList<ParentPair>();
        // Get 2 * pop_size parents, use brute force - we should probably optimize here
        double totalFitness = 0;
        for (Chromosome c : population) {
            totalFitness += c.getFitness();
        }

        for (int x = 0; x < population.size(); x++) {
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
        // Accept the whole new genreration ... but keep the top of the old generation
        children.add(getBest());
        return children;
    }

    protected List<Chromosome> mutate(List<Chromosome> children) {
        // Do not mutate at all
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