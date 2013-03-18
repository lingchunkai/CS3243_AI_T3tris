import java.util.HashSet;
import java.util.Set;


public class CopiedState extends State {

	int simulatedHeight;
	
	public double 	maximumAltitude = 0, 
					altitudeDelta = 0,
					minimumAltitude = 0,
					filledSpotCount = 0,
					highestHole = 0,
					connectedHoleCount = 0,
					holeCount = 0,
					blocksAboveHighestHoleCount = 0,
					wellCount = 0,
					sinkCount = 0,
					blockadeCount = 0;
			
    CopiedState(State s) {
    	simulatedHeight = ROWS;
    	
        int[][] curField = getField();
        int[][] stateField = s.getField();
        for (int x = 0; x < curField.length; x++) {
            for (int y = 0; y < curField[x].length; y++) {
                curField[x][y] = stateField[x][y];
            }
        }

        int[] curTop = getTop();
        for (int x = 0; x < curTop.length; x++) {
            curTop[x] = s.getTop()[x];
        }

        nextPiece = s.getNextPiece();
    }
    
    CopiedState(State s, int perceivedHeight) {
    	this(s);
    	simulatedHeight = perceivedHeight;
    }
    
    private void ComputeFeatureScores() {
    	getAltitudes();
    	filledSpotCount();
    	highestHole();
    	connectedHoles();
    	holesCount();
    	wellCount();
    }
    
    public boolean makeMove(int orient, int slot) {
    	boolean result = super.makeMove(orient, slot);
    	ComputeFeatureScores();
    	if (maximumAltitude > simulatedHeight)
    	{
    		result = false; // lost
    		lost = true;
    	}
    	return result;
    }
    
    // Get highest, lowest and altitude deltas
    private void getAltitudes() {
    	int highest = -1, lowest = ROWS+1;
        for (int i : getTop()) {
        	highest = Math.max(i, highest);
        	lowest = Math.min(i, lowest);
        }
        maximumAltitude = highest;
        minimumAltitude = lowest;
        altitudeDelta 	= highest - lowest;
    }
    
    // Returns the number of non-empty spots
    private void filledSpotCount() {
        int count = 0;
        int[][] field = getField();
        for (int x = 0; x < COLS; x++) {
        	for (int y = getTop()[x] - 1; y >= 0; y--) {
	            if (field[y][x] > 0) {
	                count++;
	            }
        	}
        }
        filledSpotCount = count;
    }    
    
    // Computes height of highest hole. 0 if there is no hole. Eg, if the only hole 
    // is in the bottommost row, then the highest hole will be of height 1.
    // Also computes the number of blocks above said highest hole(will be 0 if hole does not exist)
    private void highestHole() {
        int max = 0;
        int holeDepth = 0;
        int[][] field = getField();

        for (int x = 0; x < COLS; x++) {
        	if (max > getTop()[x])		// Impossible for highest hole to be higher than max
        		continue;
        	int blocksAbove = 0;		// Number of blocks above "hole" (if existant)
        	for (int y = getTop()[x]-1; y >= 0; y--) {
        		if (field[y][x] == 0) {	// hole was found
        			if (max > y+1) { 
        				max = y+1;
        				holeDepth = blocksAbove;
        				break;
        			}else if (max == y+1) {
        				// If there is a tie in the hole height, then get the hole that is deeper
        				holeDepth = Math.max(holeDepth, blocksAbove);    			
        				break;
        			}
        		}else
        			blocksAbove++;
        	}
        }
        highestHole = max;
        blocksAboveHighestHoleCount = holeDepth;
    }
    
    private void connectedHoles() {
    	/*
        Set<String> holes = new HashSet<String>();
        int count = 0;
        int[][] resultantField = computeHeuristicField(s, move);
        for (int x = 0; x < resultantField[0].length; x++) {
            for (int y = s.getTop()[x] - 2; y >= 0; y--) {
                int cell = resultantField[y][x];
                if (cell == 0) {//is empty  //is hole
                    String cur = x + ":" + y;
                    holes.add(cur);
                    String top = x + ":" + (y + 1);
                    String btm = x + ":" + (y - 1);
                    String left = (x - 1) + ":" + (y);
                    String right = (x + 1) + ":" + (y);

                    if (!holes.contains(top) && !holes.contains(btm) && !holes.contains(left) && !holes.contains(right)) {
                        count++;
                        int tempx = (x + 1);
                        while (tempx < s.getTop().length && resultantField[y][tempx] == 0) {
                            int tempy = y;
                            while (s.getTop()[tempx] > tempy && resultantField[tempy][tempx] == 0) {
                                holes.add(tempx + ":" + tempy);
                                tempy++;
                            }
                            tempx++;
                        }
                    }
                }


            }
        }
        return count;
        */
    	connectedHoleCount = 0;
    }

    
    private void holesCount() {
        holeCount = 0;
        blockadeCount = 0;
        int[][] field = getField();
        for (int x = 0; x < COLS; x++) {
        	boolean holeAlreadyFound = false;
            for (int y = getTop()[x] - 2; y >= 0; y--) {
                if (field[y][x] == 0) { //is hole
                    holeCount++;
                    if (holeAlreadyFound) {
                    	blockadeCount++;
                    }
                    holeAlreadyFound = true;
                }
            }
        }
    }
    
    
    // Counts number of wells (depth of 3 or more)
    // Counts number of sinks (depth of 2)
    private void wellCount() {
    	wellCount = 0;
    	int[] top = getTop();
	    // Count number of wells
	    for (int x = 0; x < COLS; x++) { // for each column
	    	
	    	// Wells
	        int numSidesCovered = 0;
	        if (x == 0 || (top[x - 1] >= top[x] + 3))
	            numSidesCovered++;
	        if (x == COLS - 1 || (top[x + 1] >= top[x] + 3))
	            numSidesCovered++;
	        if (numSidesCovered >= 2)
	            wellCount++;
	        numSidesCovered = 0;
	        if (x == 0 || (top[x - 1] == top[x] + 2))
	            numSidesCovered++;
	        if (x == COLS - 1 || (top[x + 1] == top[x] + 3))
	            numSidesCovered++;
	        if (numSidesCovered >= 2)
	            sinkCount++;
	    }
    }
    
    
}
