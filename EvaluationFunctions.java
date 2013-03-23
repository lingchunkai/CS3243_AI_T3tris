
public class EvaluationFunctions {
    //This class are the list of heuristics that will be used for the AI Agent
    //State s = Current State
    //move = Action

    private int [][]resultantField;
    private int orientation, slot;
    private State prevState;
    public EvaluationFunctions(State s, int orientation, int slot)
    {
        this.prevState = s;
        resultantField = this.computeHeuristicField();
        this.orientation = orientation;
        this.slot = slot;
    }
    
    private int[][] cloneField(int[][] source) {
        //Clone a field
        int[][] clonedField = new int[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, clonedField[i], 0, source[i].length);
        }
        return clonedField;
    }

    //Compute c' from c and a..
    private  int[][] computeHeuristicField() {
        int nextPiece = prevState.getNextPiece();

        int newHt = prevState.getTop()[slot] - State.getpBottom()[nextPiece][orientation][0];
        int[][] clonedField = cloneField(prevState.getField());
        //for the tromino width, do the same thing
        for (int w = 1; w < State.getpWidth()[nextPiece][orientation]; w++) {
            //favor clearing the top rows first
            newHt = Math.max(prevState.getTop()[slot + w] - State.getpBottom()[nextPiece][orientation][w], newHt);
        }
        for (int i = 0; i < State.getpWidth()[nextPiece][orientation]; i++) {
            for (int h = newHt + State.getpBottom()[nextPiece][orientation][i];
                    h < newHt + State.getpTop()[nextPiece][orientation][i]; h++) {

                if (h >= State.ROWS - 1) {
                    continue; //GGPOK already, dont consider -.-
                }
                clonedField[h][i + slot] = prevState.getTurnNumber();
            }
        }
        return clonedField;
    }

    //The number of pieces used on that particular tromino * the number of line it is going to clear
    //More blocks used, more luck
    public int weightedLinesClearScore() {
        int nextPiece = prevState.getNextPiece();
        //new height, the top of that particular row - the bottom of that particular piece
        //- becos 1 = a hole at the bottom
        int newHt = prevState.getTop()[slot] - State.getpBottom()[nextPiece][orientation][0];

        //for the tromino width, do the same thing
        for (int w = 1; w < State.getpWidth()[nextPiece][orientation]; w++) {
            //favor clearing the top rows first
            newHt = Math.max(prevState.getTop()[slot + w] - State.getpBottom()[nextPiece][orientation][w], newHt);
        }

        int linesCleared = 0;
        int piecesUsed = 0;
        //from the new height + the next piece height (-1 cos index starts from 0)
        //down to newHt
        for (int h = newHt + State.getpHeight()[nextPiece][orientation] - 1; h >= newHt; h--) {
            if (h >= State.ROWS - 1) {// index start from 0
                continue; //GGPOK already, dont consider -.-
            }

            boolean completeRow = true;
            for (int c = 0; c < State.COLS; c++) {
                 if (resultantField[h][c] == prevState.getTurnNumber()) {
                    piecesUsed += 1;
                }
                if (resultantField[h][c] == 0) { //if got hole
                    completeRow = false;
                    break;
                }
            }
            if (completeRow) {
                linesCleared += 1;
            }
        }

        return linesCleared * piecesUsed;
    }

    //maximum contact area of a piece after executing a move
    public int maximumContactArea() {//added
        int count = 0;
        int nextPiece = prevState.getNextPiece();


        int newHt = prevState.getTop()[slot] - State.getpBottom()[nextPiece][orientation][0];
        int[][] currField = prevState.getField();
        //for the tromino width, do the same thing
        for (int w = 1; w < State.getpWidth()[nextPiece][orientation]; w++) {
            //favor clearing the top rows first
            newHt = Math.max(prevState.getTop()[slot + w] - State.getpBottom()[nextPiece][orientation][w], newHt);
        }

        for (int c = 0; c < State.getpWidth()[nextPiece][orientation]; c++) {
            for (int h = newHt + State.getpBottom()[nextPiece][orientation][c];
                    h < newHt + State.getpTop()[nextPiece][orientation][c]; h++) {

                if (h >= State.ROWS - 1) {
                    continue; //GGPOK already, dont consider -.-
                }
                //get the left, right and bottom sides
                int bottom = h - 1;
                int leftSide = c - 1 + slot;
                int rightSide = c + 1 + slot;

                //bottom check, to see if it touches
                if ((currField[bottom][c + slot] != 0) && (bottom >= 0)) {
                    count += 1;
                }
                //left side check to see if it touches
                if ((currField[h][leftSide] != 0) && (leftSide >= 0)) {
                    count += 1;
                }
                if (leftSide < 0) { //touch wall check
                    count += 1;
                }
                //Right side check to see if it touches
                if ((currField[h][rightSide] != 0) && (rightSide < State.COLS)) {
                    count += 1;
                }
                if (rightSide >= State.COLS) { //touch wall check
                    count += 1;
                }
            }
        }
        return count;
    }

}

