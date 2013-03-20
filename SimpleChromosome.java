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

	public static final int NUM_ATTRIBUTES = 9;
	public static double MIN_RANGE = -0.5;
	public static double MAX_RANGE = 0.5;
	public enum FEATURES {
		INDEX_NUM_HOLES(0),
		INDEX_HEIGHT(1),
		INDEX_NUM_WELLS(2),
		INDEX_ALTITUDE_DELTA(3),
		INDEX_FILLED_SPOT_COUNT(4),
		INDEX_DEPTH_HIGHEST_HOLE(5),
		INDEX_HEIGHT_HIGHEST_HOLE(6),
		INDEX_NUM_SINKS(7),
		INDEX_BLOCKADE_COUNT(8);
		
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
    	
        //use Features Class Thx
//        // Count number of holes
//        int holes = 0;
//        for (int x = 0; x < s.COLS; x++) {
//            for (int y = s.getTop()[x] - 1; y >= 0; y--) {
//                if (s.getField()[y][x] == 0) {
//                    holes++;
//                }
//            }
//        }
//
//        // Count value of height
//        int[] top = s.getTop();
//        int highest = -1;
//        for (int i : top) {
//            highest = Math.max(i, highest);
//        }
//
//        int num_wells = 0;
//        // Count number of wells
//        for (int x = 0; x < s.COLS; x++) {
//            int numSidesCovered = 0;
//
//            if (x == 0 || (top[x - 1] >= top[x] + 4)) {
//                numSidesCovered++;
//            }
//            if (x == s.COLS - 1 || (top[x + 1] >= top[x] + 4)) {
//                numSidesCovered++;
//            }
//            if (numSidesCovered >= 2) {
//                num_wells++;
//            }
//        }
//
//        // Compute delta
//        int lowest = 1000;
//        for (int i : top) {
//            lowest = Math.min(i, lowest);
//        }
//        int delta = highest - lowest;
//
//        return (double) (highest) * val_height + (double) (holes) * val_hole + (double) (num_wells) * val_well + (double) (delta) * val_delta;

        return score;
    }

    void mutate() {
    	// Choose random attribute to randomize
    	Random rng = new Random();
    	int attributeIndex = (rng.nextInt()%NUM_ATTRIBUTES+NUM_ATTRIBUTES)%NUM_ATTRIBUTES;
    	weights[attributeIndex] = (rng.nextDouble() * (MAX_RANGE - MIN_RANGE)) + MIN_RANGE;
    }
}