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

	public static final int NUM_ATTRIBUTES = 17;
	public static final double MIN_RANGE = -0.5;
	public static final double MAX_RANGE = 0.5;
	// Not linearly independent
	// Minimum_altitude, altitude delta and maximum altitude
	//

	// Not used: contact area, min alt, weighted lines cleared, invertedWeightedHoleCount

	private enum Feature {
		HOLE_COUNT(0),
		MAX_ALTITUDE(1),
		WELL_COUNT(2),
		ALTITUDE_DELTA(3),
		FILLED_SPOT_COUNT(4),
		DEPTH_HIGHEST_HOLE(5),
		HEIGHT_HIGHEST_HOLE(6),
		BLOCKADE_COUNT(7),
		CONNECTED_HOLE_COUNT(8),
		WEIGHTED_HOLE_COUNT(9),
		MAX_WELL_DEPTH(10),
		TOTAL_WELL_DEPTH(11),
		SURFACE_AREA_ROUGHNESS(12),
        CONTACT_AREA(13),
        MIN_ALTITUDE(14),
        WEIGHTED_LINES_CLEARED(15),
        INVERTED_WEIGHTED_HOLE_COUNT(16);

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
        weights = new double[NUM_ATTRIBUTES];
        for (int x = 0; x < NUM_ATTRIBUTES; x++) {
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
    	for (int x = 0; x < NUM_ATTRIBUTES; x++) {
    		str.append(weights[x]);

    		if (x < NUM_ATTRIBUTES - 1)
    			str.append(",");
    	}
    	str.append("]");
    	return str.toString();
    }

    @Override
    Chromosome Crossover(Chromosome other) {
        SimpleChromosome mate = (SimpleChromosome) (other);
        double [] childWeights = new double[NUM_ATTRIBUTES];
        // We use a purely random approach - the allele could come from either parent with equal prob.
        for (int x = 0; x < NUM_ATTRIBUTES; x++) {
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
    	score += weights[Feature.HOLE_COUNT.getCode()] * s.holeCount;
    	score += weights[Feature.MAX_ALTITUDE.getCode()] * s.maximumAltitude;
    	score += weights[Feature.WELL_COUNT.getCode()] * s.wellCount;
    	score += weights[Feature.ALTITUDE_DELTA.getCode()] * s.altitudeDelta;
    	score += weights[Feature.FILLED_SPOT_COUNT.getCode()] * s.filledSpotCount;
    	score += weights[Feature.DEPTH_HIGHEST_HOLE.getCode()] * s.blocksAboveHighestHoleCount;
    	score += weights[Feature.HEIGHT_HIGHEST_HOLE.getCode()] * s.highestHole;
    	score += weights[Feature.BLOCKADE_COUNT.getCode()] * s.blockadeCount;
    	score += weights[Feature.CONNECTED_HOLE_COUNT.getCode()] * s.connectedHoleCount;
    	score += weights[Feature.WEIGHTED_HOLE_COUNT.getCode()] * s.weightedHoleCount;
    	score += weights[Feature.MAX_WELL_DEPTH.getCode()] * s.maxWellDepth;
    	score += weights[Feature.TOTAL_WELL_DEPTH.getCode()] * s.TotalWellDepth;
    	score += weights[Feature.SURFACE_AREA_ROUGHNESS.getCode()] * s.surfaceAreaRoughness;
        score += weights[Feature.CONTACT_AREA.getCode()] * s.maxContactArea;
        score += weights[Feature.MIN_ALTITUDE.getCode()] * s.minimumAltitude;
        score += weights[Feature.WEIGHTED_LINES_CLEARED.getCode()] * s.weightedLinesCleared;
        score += weights[Feature.INVERTED_WEIGHTED_HOLE_COUNT.getCode()] * s.invertedWeightedHoleCount;

        return score;
    }

    @Override
    void mutate() {
    	// Randomly choose a feature weight to mutate
    	int index = random.nextInt(NUM_ATTRIBUTES);
    	weights[index] = (random.nextDouble() * (MAX_RANGE - MIN_RANGE)) + MIN_RANGE;
    }

    // Length of the solution vector
    protected double getLength() {
    	double sum = 0;
    	for (int x = 0; x < NUM_ATTRIBUTES; x++) {
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
