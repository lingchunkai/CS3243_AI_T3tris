import java.util.Arrays;
import java.util.Random;

/**
 * Simple Chromosome
 *
 * Evaluation function is the weighted sum of feature values. Each feature has a range of [-0.5, 0.5).
 *
 * Crossover is defined SWAPPING the values of 2 selected weights.
 *
 * Mutation is defined as re-randomizing a weight (selected at random).
 */
public class SimpleChromosome extends Chromosome {

    public static final int NUM_FEATURES = Feature.values().length;
	public static final double MIN_RANGE = -0.5;
	public static final double MAX_RANGE = 0.5;
	// Not linearly independent
	// Minimum_altitude, altitude delta and maximum altitude
	//

	// Not used: contact area, min alt, weighted lines cleared, invertedWeightedHoleCount

	private enum Feature {
        LANDING_HEIGHT(0),
        LINES_CLEARED(1),
        ROW_TRANSITIONS(2),
        COLUMN_TRANSITIONS(3),
        HOLE_COUNT(4),
        TOTAL_WELL_DEPTH(5);

		private final int code;

        private Feature(int code){
            this.code = code;
        }

		public int getCode() {
			return code;
		}
	}

	protected final double weights[];

    private final Random random = new Random();

    /**
     * Constructor. Initialises weights with random values.
     */
    public SimpleChromosome() {
        weights = new double[NUM_FEATURES];
        for (int x = 0; x < NUM_FEATURES; x++) {
            weights[x] = (random.nextDouble() * (MAX_RANGE - MIN_RANGE)) + MIN_RANGE;
        }
    }

    public SimpleChromosome(double[] weights) {
        this.weights = Arrays.copyOf(weights, weights.length);
    }

    @Override
    public String toString() {
    	StringBuilder str = new StringBuilder();
    	str.append("Fitness: " + getFitness()+", Games played: "+numGames+", ");
    	str.append("[");
    	for (int x = 0; x < NUM_FEATURES; x++) {
    		str.append(weights[x]);

    		if (x < NUM_FEATURES - 1)
    			str.append(",");
    	}
    	str.append("]");
    	return str.toString();
    }

    @Override
    Chromosome Crossover(Chromosome other) {
        SimpleChromosome mate = (SimpleChromosome) (other);
        double [] childWeights = new double[NUM_FEATURES];
        // We use a purely random approach - the allele could come from either parent with equal prob.
        for (int x = 0; x < NUM_FEATURES; x++) {
        	if (random.nextBoolean()) {
        		childWeights[x] = weights[x];
        	} else {
        		childWeights[x] = mate.weights[x];
        	}
        }
        return new SimpleChromosome(childWeights);
    }

    @Override
    double evaluate(CopiedState s) {
    	double score = 0;
    	score += weights[Feature.LANDING_HEIGHT.getCode()] * s.landingHeight;
        score += weights[Feature.LINES_CLEARED.getCode()] * s.linesCleared;
        score += weights[Feature.ROW_TRANSITIONS.getCode()] * s.rowTransitions;
        score += weights[Feature.COLUMN_TRANSITIONS.getCode()] * s.columnTransitions;
        score += weights[Feature.HOLE_COUNT.getCode()] * s.holeCount;
        score += weights[Feature.TOTAL_WELL_DEPTH.getCode()] * s.TotalWellDepth;

        return score;
    }

    @Override
    void mutate() {
    	// Randomly choose a feature weight to mutate
    	int index = random.nextInt(NUM_FEATURES);
    	weights[index] = (random.nextDouble() * (MAX_RANGE - MIN_RANGE)) + MIN_RANGE;
    }

    // Length of the solution vector
    protected double getLength() {
    	double sum = 0;
    	for (int x = 0; x < NUM_FEATURES; x++) {
    		sum += weights[x] * weights[x];
    	}
    	return Math.sqrt(sum);
    }

    // Returns the dot product between 2 vectors
    protected double dotProduct(double[] v1, double[] v2) {
    	double sum = 0;
    	for (int x = 0; x < Math.min(v1.length, v2.length); x++) {
    		sum += v1[x] * v2[x];
    	}
    	return sum;
    }

	@Override
	public double similarityIndex(Chromosome compareChromosome) {
		SimpleChromosome other = (SimpleChromosome) compareChromosome;
		double product = dotProduct(weights, other.weights) / getLength() / other.getLength();
		if (product < -1) product = -1;
		if (product > 1) product = 1;
		// Cosine similarity.
		return Math.acos(product)/Math.PI;
	}
}
