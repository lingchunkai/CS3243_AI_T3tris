import java.util.Random;


/**
 * Crossover redefined to one point crossover
 *
 */
public class BetterChromosome extends SimpleChromosome {

	public BetterChromosome(double[] new_weights) {
		super(new_weights);
	}

	public BetterChromosome() {
		super();
	}

	int MapToInt(double x) {
		return (int)((x - MIN_RANGE) / (MAX_RANGE - MIN_RANGE) * (double)Integer.MAX_VALUE);
	}

	double MapToDouble(int x) {
		return ((double)(x) / (double)(Integer.MAX_VALUE)) * (MAX_RANGE - MIN_RANGE) + MIN_RANGE;
	}



	Chromosome Crossover(Chromosome other) {
        BetterChromosome mate = (BetterChromosome) (other);
        double [] new_weights = new double[NUM_FEATURES];
        // We use a purely random approach - the allele could come from either parent with equal prob.
        for (int x = 0; x < NUM_FEATURES; x++) {
        	// Figure a good crossover point - the index of the bit where the second chromosome starts
        	int crossPoint = new Random().nextInt(31); // We must have at least one bit for the left-side chromosome
        	int thisWeight = MapToInt(weights[x]);
        	int otherWeight = MapToInt(mate.weights[x]);

        	int bitMask = (1<<(crossPoint+1))-1;
        	new_weights[x] = MapToDouble((otherWeight & bitMask) ^ (thisWeight & (~bitMask)));

        }
        return new BetterChromosome(new_weights);
    }
}
