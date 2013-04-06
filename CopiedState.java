
import java.util.HashSet;
import java.util.Set;

public class CopiedState{

	// START COPIED FROM BRYAN
    public static final int COLS = 10;
    public static final int ROWS = 21;
    public static final int N_PIECES = 7;
    public boolean lost = false;
    public TLabel label;
    //current turn
    private int turn = 0;
    private int cleared = 0;
    //each square in the grid - int means empty - other values mean the turn it was placed
    private int[][] field = new int[ROWS][COLS];
    //top row+1 of each column
    //0 means empty
    private int[] top = new int[COLS];
    //number of next piece
    protected int nextPiece;
    //all legal moves - first index is piece type - then a list of 2-length arrays
    protected static int[][][] legalMoves = new int[N_PIECES][][];
    //indices for legalMoves
    public static final int ORIENT = 0;
    public static final int SLOT = 1;
    //possible orientations for a given piece type
    protected static int[] pOrients = {1, 2, 4, 4, 4, 2, 2};
    //the next several arrays define the piece vocabulary in detail
    //width of the pieces [piece ID][orientation]
    protected static int[][] pWidth = {
        {2},
        {1, 4},
        {2, 3, 2, 3},
        {2, 3, 2, 3},
        {2, 3, 2, 3},
        {3, 2},
        {3, 2}
    };
    //height of the pieces [piece ID][orientation]
    private static int[][] pHeight = {
        {2},
        {4, 1},
        {3, 2, 3, 2},
        {3, 2, 3, 2},
        {3, 2, 3, 2},
        {2, 3},
        {2, 3}
    };
    //bottom of the pieces [pieceID][orientation][0 for no hole made, 1 for got hole]
    //use this for hole computation..
    private static int[][][] pBottom = {
        {{0, 0}}, //square 	[][]                 0
                         //	[][]
        {{0}, {0, 0, 0, 0}}, //the long stick   1
        //[], [][][][]
        //[]
        //[]
        //[]
        {{0, 0}, {0, 1, 1}, {2, 0}, {0, 0, 0}}, //2
        // []     [][][]     [][]         []
        // []     []           []     [][][]
        // [][]                []
        {{0, 0}, {0, 0, 0}, {0, 2}, {1, 1, 0}}, //3
        //   []   []         [][]    [][][]
        //   []   [][][]     []          []
        // [][]              []
        {{0, 1}, {1, 0, 1}, {1, 0}, {0, 0, 0}},
        // []     [][][]        []     []
        // [][]     []        [][]   [][][]
        // []                   []
        {{0, 0, 1}, {1, 0}},
        //   [][]     []
        // [][]       [][]
        //              []
        {{1, 0, 0}, {0, 1}}
        // [][]         []
        //   [][]     [][]
        //            []
    };
    //top of the pieces [pieceID][orientation][ht for that col in piece]
    private static int[][][] pTop = {
        {{2, 2}},//square [][]                      0
                        //[][]
        {{4}, {1, 1, 1, 1}},//the long stick        1
        //[], [][][][]
        //[]
        //[]
        //[]
        {{3, 1}, {2, 2, 2}, {3, 3}, {1, 1, 2}}, //2
        // []     [][][]     [][]         []
        // []     []           []     [][][]
        // [][]                []
        {{1, 3}, {2, 1, 1}, {3, 3}, {2, 2, 2}}, //3
        //   []   []         [][]    [][][]
        //   []   [][][]     []          []
        // [][]              []
        {{3, 2}, {2, 2, 2}, {2, 3}, {1, 2, 1}}, //4
        // []     [][][]        []     []
        // [][]     []        [][]   [][][]
        // []                   []
        {{1, 2, 2}, {3, 2}},                    //5
        //   [][]     []
        // [][]       [][]
        //              []
        {{2, 2, 1}, {2, 3}}                     //6
        // [][]         []
        //   [][]     [][]
        //            []
    };

    //initialize legalMoves
    {
        //for each piece type
        for (int i = 0; i < N_PIECES; i++) {
            //figure number of legal moves
            int n = 0;
            for (int j = 0; j < pOrients[i]; j++) {
                //number of locations in this orientation
                n += COLS + 1 - pWidth[i][j];
            }
            //allocate space
            legalMoves[i] = new int[n][2];
            //for each orientation
            n = 0;
            for (int j = 0; j < pOrients[i]; j++) {
                //for each slot
                for (int k = 0; k < COLS + 1 - pWidth[i][j]; k++) {
                    legalMoves[i][n][ORIENT] = j;
                    legalMoves[i][n][SLOT] = k;
                    n++;
                }
            }
        }

    }
    
    public boolean hasLost() {
        return lost;
    }
    
    
    
    public void makeMove(int move) {
        makeMove(legalMoves[nextPiece][move]);
    }
    
    public void makeMove(int[] move) {
        makeMove(move[ORIENT], move[SLOT]);
    }
    
    //random integer, returns 0-6
    private int randomPiece() {
        return (int) (Math.random() * N_PIECES);
    }
    
    // END COPIED FROM BRYAN
	
    int simulatedHeight; // Height after which the game is considered to be lost.

    public double maximumAltitude,
            altitudeDelta,
            minimumAltitude,
            filledSpotCount,
            highestHole,
            connectedHoleCount,
            holeCount,
            weightedHoleCount,
            blocksAboveHighestHoleCount,
            blockadeCount,
            invertedWeightedHoleCount,
            maxWellDepth,
            TotalWellDepth,
            wellCount,
            surfaceAreaRoughness,
            weightedLinesCleared,
            maxContactArea;

    public double rowTransitions;
    public double columnTransitions;
    public double linesCleared;
    public double landingHeight;

    private int prevFilledSpotCount;

    //total no. of heuristic: 16, please let us know if there is any one with
    //not updated, might have missed it


    CopiedState(State s) {
        this(s, ROWS);
    }

    CopiedState(State s, int perceivedHeight) {
        simulatedHeight = Math.min(ROWS, perceivedHeight);

        
        int[][] stateField = s.getField();
        for (int y = 0; y < stateField.length; y++) {
            System.arraycopy(stateField[y], 0, field[y], 0, stateField[y].length);
        }
       
        // Copy Top
        System.arraycopy(s.getTop(), 0, top, 0, s.getTop().length);
        
        nextPiece = s.getNextPiece();
    }

    private void computeFeatureScores() {
        computeHoleFeatures();
        // computeAltitudeFeatures();
        // computeConnectedHoleFeatures();
        // computeBlockades();
        computeWellFeatures();
        // computeWellCount();
        // computeSurfaceAreaRoughness();
        computeTransitions();
    }

    public boolean makeMove(int orient, int slot) {
        //This 2 heuristics depends on the prev state.
        //EvaluationFunctions eval = new EvaluationFunctions(this, orient, slot);
        //this.maxContactArea = eval.maximumContactArea();
        //this.weightedLinesCleared = eval.weightedLinesClearScore();

        // Needed to determine lines cleared
        prevFilledSpotCount = filledSpots();
        computeLandingHeight(orient, slot);
        
        
        // ADAPTED FROM BRYAN //
        
        boolean alive = copiedMakeMove(orient, slot);
        
        // END COPIED FROM BRYAN
        
        
        computeFeatureScores();
        if (maximumAltitude > simulatedHeight) {
            alive = false;
            lost = true;
        }
    	
         return alive;
    }


    
    private void computeLandingHeight(int orient, int slot) {
		// Adapted from Bryan's code 
    	//height if the first column makes contact
        int height = top[slot] - pBottom[nextPiece][orient][0];
        //for each column beyond the first in the piece
        for (int c = 1; c < pWidth[nextPiece][orient]; c++) {
            height = Math.max(height, top[slot + c] - pBottom[nextPiece][orient][c]);
        }
        landingHeight = height + (double)(pHeight[nextPiece][orient]/2);
    	
	}

	//get hightest,weighted and hole count
    public void computeHoleFeatures() {
        int Holecount = 0;
        int Highesthole = 0;
        int weightedHole = 0;
        int BlocksAboveHighestHole = 0;
        int BlockCount = 0;
        int InvertedWeightedHoleCount = 0;

        for (int x = 0; x < top.length; x++) {
            boolean firsthole = true;
            int colblockcount = 0;
            for (int y = top[x] - 2; y >= 0; y--) {
                int cell = field[y][x];

                if (cell == 0) { //is hole
                    Holecount++;
                    //y=height need +1
                    weightedHole += (y + 1);
                    InvertedWeightedHoleCount = InvertedWeightedHoleCount + (top[x] - y - 1);

                    if (firsthole == true) {
                        Highesthole = Math.max(y + 1, Highesthole);
                        firsthole = false;
                        BlocksAboveHighestHole = top[x] - Highesthole;
                    }
                } else {
                    colblockcount++;
                }
            }
            if (top[x] >= 1) {
                colblockcount++;
            }
            BlockCount += colblockcount;
        }
        highestHole = Highesthole;
        holeCount = Holecount;
        weightedHoleCount = weightedHole;
        blocksAboveHighestHoleCount = BlocksAboveHighestHole;
        filledSpotCount = BlockCount;
        invertedWeightedHoleCount = InvertedWeightedHoleCount;
    }

    // Get highest, lowest and altitude deltas
    private void computeAltitudeFeatures() {
        int highest = -1, lowest = ROWS + 1;
        for (int i : top) {
            highest = Math.max(i, highest);
            lowest = Math.min(i, lowest);
        }
        maximumAltitude = highest;
        minimumAltitude = lowest;
        altitudeDelta = highest - lowest;
    }
    /*
    private void computeConnectedHoleFeatures() {

        Set<String> holes = new HashSet<String>();
        int count = 0;
        for (int x = 0; x < this.getField()[0].length; x++) {
            for (int y = getTop()[x] - 2; y >= 0; y--) {
                int cell = this.getField()[y][x];
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
                        while (tempx < getTop().length && this.getField()[y][tempx] == 0) {
                            int tempy = y;
                            while (getTop()[tempx] > tempy && this.getField()[tempy][tempx] == 0) {
                                holes.add(tempx + ":" + tempy);
                                tempy++;
                            }
                            tempx++;
                        }
                    }
                }
            }
        }
        connectedHoleCount = count;
    }
	
    private void computeBlockades() {
        blockadeCount = 0;
        int[][] field = getField();
        for (int x = 0; x < COLS; x++) {
            boolean holeAlreadyFound = false;
            for (int y = getTop()[x] - 2; y >= 0; y--) {
                if (field[y][x] == 0) { //is hole
                    if (holeAlreadyFound) {
                        blockadeCount++;
                    }
                    holeAlreadyFound = true;
                }
            }
        }
    }
	*/
    //maximum depth of a well after executing a move
    private void computeWellFeatures() {
        int maxWellDep = 0;
        int totalWellDep = 0;
        int col = State.COLS - 1;

        for (int x = 0; x < State.COLS; x++) {
            //if check if the side of the column to see if it is enclosed and does not have
            if ((x == 0 || top[x - 1] > top[x]) && (x == col || top[x + 1] > top[x])) {
                int wellDepth = 0;
                if (x == 0)//first column
                {
                    wellDepth = top[x + 1] - top[x];
                } else if (x == col)//last column
                {
                    wellDepth = top[x - 1] - top[x];
                } else {
                    wellDepth = Math.min(top[x - 1], top[x + 1]) - top[x];
                }
                maxWellDep = Math.max(wellDepth, maxWellDep);
                totalWellDep += wellDepth;
            }
        }

        maxWellDepth = maxWellDep;
        this.TotalWellDepth = totalWellDep;
    }

    //no. of wells after executing a move
    private void computeWellCount() {
        int count = 0;

        for (int x = 0; x < State.COLS; x++) {

            int startCount = 0;
            int depth = 0;
            int wellSpotted = 0;

            for (int y = top.length - 1; y >= 0; y--) {
                int cell = field[y][x];
                if (cell != 0) { //if the top hole is not filled start to count
                    startCount = 1;
                }

                if (startCount == 1) {
                    depth++;
                }
                if (depth > 3) {
                    if (cell != 0) {
                        wellSpotted = 1;
                    }
                    if (wellSpotted == 1) {
                        if (cell == 0) {
                            wellSpotted = 0;
                            count++;
                        }
                    }
                }
            }
        }
        wellCount = count;
    }

    //Surface area roughness after executing a move
    private void computeSurfaceAreaRoughness() {
        int roughness = 0;
        int maxTop = 0;

        for (int c = 0; c < top.length; c++)//by column
        {
            if (top[c] > maxTop) {
                maxTop = top[c];
            }
        }

        for (int c = 0; c < top.length; c++)//by column
        {
            roughness += Math.abs(maxTop - top[c]);
        }
        surfaceAreaRoughness = roughness;
        //return roughness;
    }

    private void computeTransitions() {
        int rt = 0, ct = 0;
        for (int i = 0; i < simulatedHeight; i++) {
            boolean prevOccupied = true;
            for (int j = 0; j < COLS; j++) {
                if (prevOccupied != (field[i][j] != 0)) {
                    prevOccupied = (field[i][j] != 0);
                    rt++;
                }
            }
            if (!prevOccupied) rt++;
        }

        for (int j = 0; j < COLS; j++) {
            boolean prevOccupied = true;
            for (int i = 0; i < simulatedHeight; i++) {
                if (prevOccupied != (field[i][j] != 0)) {
                    prevOccupied = (field[i][j] != 0);
                    ct++;
                }
            }
            // if (!prevOccupied) ct++;
        }

        rowTransitions = rt;
        columnTransitions = ct;
    }

    private int filledSpots() {
        int count = 0;
        for (int i = 0; i < simulatedHeight; i++) {
            for (int j = 0; j < COLS; j++) {
                if (field[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public boolean copiedMakeMove(int orient, int slot) {
        turn++;
        //height if the first column makes contact
        int height = top[slot] - pBottom[nextPiece][orient][0];
        //for each column beyond the first in the piece
        for (int c = 1; c < pWidth[nextPiece][orient]; c++) {
            height = Math.max(height, top[slot + c] - pBottom[nextPiece][orient][c]);
        }

        //check if game ended
        if (height + pHeight[nextPiece][orient] >= ROWS) {
            lost = true;
            return false;
        }


        //for each column in the piece - fill in the appropriate blocks
        for (int i = 0; i < pWidth[nextPiece][orient]; i++) {

            //from bottom to top of brick
            for (int h = height + pBottom[nextPiece][orient][i]; h < height + pTop[nextPiece][orient][i]; h++) {
                field[h][i + slot] = turn;
            }
        }

        //adjust top
        for (int c = 0; c < pWidth[nextPiece][orient]; c++) {
            top[slot + c] = height + pTop[nextPiece][orient][c];
        }

        linesCleared = 0;

        //check for full rows - starting at the top
        for (int r = height + pHeight[nextPiece][orient] - 1; r >= height; r--) {
            //check all columns in the row
            boolean full = true;
            for (int c = 0; c < COLS; c++) {
                if (field[r][c] == 0) {
                    full = false;
                    break;
                }
            }
            //if the row was full - remove it and slide above stuff down
            if (full) {
                linesCleared++;
                cleared++;
                //for each column
                for (int c = 0; c < COLS; c++) {

                    //slide down all bricks
                    for (int i = r; i < top[c]; i++) {
                        field[i][c] = field[i + 1][c];
                    }
                    //lower the top
                    top[c]--;
                    while (top[c] >= 1 && field[top[c] - 1][c] == 0) {
                        top[c]--;
                    }
                }
            }
        }
        //pick a new piece
        nextPiece = randomPiece();
        return true;
    }
}


