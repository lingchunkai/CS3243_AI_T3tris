import java.util.Random;


/**
 * Simple Chromosome
 * 
 * Evaluation function is the weighted sum of all features. each feature has a range of -0.5 to 0.5
 * 
 * Crossover is defined SWAPPING the values of 2 select weights.
 * 
 * Mutation is defined as re-randomizing a weight (selected at random)
 * 
 *
 */
public class SimpleChromosome extends Chromosome {

	public static final int NUM_ATTRIBUTES = 13;
	public static double MIN_RANGE = -0.5;
	public static double MAX_RANGE = 0.5;
	// Not linearly independant
	// Minimum_altitude, altitude delta and maximum altitude
	//
	
	// Not used: contact area, min alt, weighted lines cleared, invertedWeightedHoleCount
	
	public enum FEATURES {
		INDEX_NUM_HOLES(0),
		INDEX_HEIGHT(1),
		INDEX_NUM_WELLS(2),
		INDEX_ALTITUDE_DELTA(3),
		INDEX_FILLED_SPOT_COUNT(4),
		INDEX_DEPTH_HIGHEST_HOLE(5),
		INDEX_HEIGHT_HIGHEST_HOLE(6),
		INDEX_BLOCKADE_COUNT(7),
		INDEX_CONNECTED_HOLE_COUNT(8),
		INDEX_WEIGHTED_HOLE_COUNT(9),
		INDEX_MAX_WELL_DEPTH(10),
		INDEX_TOTAL_WELL_DEPTH(11),
		INDEX_SURFACE_AREA_ROUGHNESS(12);
		
		
		public final int Value;
		
		public int getCode() {
			return Value;
		}
		
		private FEATURES(int value){
			Value = value;
		}
	}
	public double weights[];
    
	// Randomized weights
	public SimpleChromosome() {
		Random r = new Random();
		weights = new double[NUM_ATTRIBUTES];
		for (int x = 0; x < NUM_ATTRIBUTES; x++) {
			weights[x] = (r.nextDouble()*(MAX_RANGE-MIN_RANGE))+MIN_RANGE;
			
		}
	}
	
    public SimpleChromosome(double[] initial_weights) {
        weights = initial_weights.clone();
    }

    public String toString() {
        // 
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

    Chromosome Crossover(Chromosome other) {
        SimpleChromosome mate = (SimpleChromosome) (other);
        double [] new_weights = new double[NUM_ATTRIBUTES];
        // We use a purely random approach - the allele could come from either parent with equal prob.
        for (int x = 0; x < NUM_ATTRIBUTES; x++) {
        	Random r = new Random();
        	if (r.nextInt() % 2 == 0) {
        		// The allele follows this chromosome
        		new_weights[x] = weights[x];
        	} else {
        		// Allele follows the partner
        		new_weights[x] = mate.weights[x];
        	}
        }
        return new SimpleChromosome(new_weights);
    }

    double evaluate(CopiedState s) {

    	double score = 0;
    	score += weights[FEATURES.INDEX_NUM_HOLES.getCode()] * s.holeCount;
    	score += weights[FEATURES.INDEX_HEIGHT.getCode()] * s.maximumAltitude;
    	score += weights[FEATURES.INDEX_NUM_WELLS.getCode()] * s.wellCount;
    	score += weights[FEATURES.INDEX_ALTITUDE_DELTA.getCode()] * s.altitudeDelta;
    	score += weights[FEATURES.INDEX_FILLED_SPOT_COUNT.getCode()] * s.filledSpotCount;
    	score += weights[FEATURES.INDEX_DEPTH_HIGHEST_HOLE.getCode()] * s.blocksAboveHighestHoleCount;
    	score += weights[FEATURES.INDEX_HEIGHT_HIGHEST_HOLE.getCode()] * s.highestHole;
    	score += weights[FEATURES.INDEX_BLOCKADE_COUNT.getCode()] * s.blockadeCount;
    	score += weights[FEATURES.INDEX_CONNECTED_HOLE_COUNT.getCode()] * s.connectedHoleCount;
    	score += weights[FEATURES.INDEX_WEIGHTED_HOLE_COUNT.getCode()] * s.weightedHoleCount;
    	score += weights[FEATURES.INDEX_MAX_WELL_DEPTH.getCode()] * s.maxWellDepth;
    	score += weights[FEATURES.INDEX_TOTAL_WELL_DEPTH.getCode()] * s.TotalWellDepth;
    	score += weights[FEATURES.INDEX_SURFACE_AREA_ROUGHNESS.getCode()] * s.surfaceAreaRoughness;
    	
        return score;
    }

    void mutate() {
    	// Choose random attribute to randomize
    	Random rng = new Random();
    	int attributeIndex = (rng.nextInt()%NUM_ATTRIBUTES+NUM_ATTRIBUTES)%NUM_ATTRIBUTES;
    	weights[attributeIndex] = (rng.nextDouble() * (MAX_RANGE - MIN_RANGE)) + MIN_RANGE;
    }
}