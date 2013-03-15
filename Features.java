
import java.util.*;

public class Features {

    public static int contactAreaScore(State s, int[] move) {
        int result = 0;
        //to be continued..
        return result;
    }

    //Maximum Altitude - The height of the tallest column on the game board
    //note that it does not care about whether the rows get clear anot
    public static int maximumAltitude(State s, int[] move) {
        int highest = -1;
        int nextPiece = s.getNextPiece();
        int orientation = move[0];
        int slot = move[1];
        int pieceHt = State.getpHeight()[nextPiece][orientation];

        for (int w = 0; w < State.getpWidth()[nextPiece][orientation]; w++) {
            highest = Math.max(s.getTop()[slot + w] + pieceHt, highest);
        }

        return highest;
    }

    //The difference in height between the tallest column and the shortest column
    public static int altitudeDelta(State s, int[] move) {
        return Features.maximumAltitude(s, move) - Features.findMinHeight(s);
    }

    //Total lines clear so far -_-
    public static int linesCleared(State s, int[] move) {
        return s.getRowsCleared() + computelinesClearScore(s, move);
    }

    public static int computelinesClearScore(State s, int[] move) {
        int nextPiece = s.getNextPiece();
        int orientation = move[0];
        int slot = move[1];
        //new height, the top of that particular row - the bottom of that particular piece
        //- becos 1 = a hole at the bottom
        int newHt = s.getTop()[slot] - State.getpBottom()[nextPiece][orientation][0];

        //for the tromino width, do the same thing
        for (int w = 1; w < State.getpWidth()[nextPiece][orientation]; w++) {
            //favor clearing the top rows first
            newHt = Math.max(s.getTop()[slot + w] - State.getpBottom()[nextPiece][orientation][w], newHt);
        }

        int[][] clonedField = cloneField(s.getField());

        for (int i = 0; i < State.getpWidth()[nextPiece][orientation]; i++) {
            for (int h = newHt + State.getpBottom()[nextPiece][orientation][i];
                    h < newHt + State.getpTop()[nextPiece][orientation][i]; h++) {

                if (h >= State.ROWS - 1) {
                    continue; //GGPOK already, dont consider -.-
                }
                clonedField[h][i + slot] = 1;
            }
        }

        int linesCleared = 0;
        //from the new height + the next piece height (-1 cos index starts from 0)
        //down to newHt
        for (int h = newHt + State.getpHeight()[nextPiece][orientation] - 1; h >= newHt; h--) {
            if (h >= State.ROWS - 1) // index start from 0
            {
                continue; //GGPOK already, dont consider -.-
            }
            boolean completeRow = true;
            for (int c = 0; c < State.COLS; c++) {
                if (clonedField[h][c] == 0) { //if got hole
                    completeRow = false;
                    break;
                }
            }
            if (completeRow) {
                linesCleared += 1;
            }
        }

        return linesCleared;
    }

    private static int findMinHeight(State s) {
        int[] top = s.getTop();
        int lowest = Integer.MAX_VALUE;
        for (int i : top) {
            lowest = Math.min(i, lowest);
        }

        return lowest;
    }

    private static int[][] cloneField(int[][] source) {
        //Clone a field
        int[][] clonedField = new int[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, clonedField[i], 0, source[i].length);
        }
        return clonedField;
    }

    private static int[][] computeHeuristicField(State s, int[] move) {

        int nextPiece = s.getNextPiece();
        int orientation = move[0];
        int slot = move[1];

        int newHt = s.getTop()[slot] - State.getpBottom()[nextPiece][orientation][0];
        int[][] clonedField = cloneField(s.getField());
        //for the tromino width, do the same thing
        for (int w = 1; w < State.getpWidth()[nextPiece][orientation]; w++) {
            //favor clearing the top rows first
            newHt = Math.max(s.getTop()[slot + w] - State.getpBottom()[nextPiece][orientation][w], newHt);
        }
        for (int i = 0; i < State.getpWidth()[nextPiece][orientation]; i++) {
            for (int h = newHt + State.getpBottom()[nextPiece][orientation][i];
                    h < newHt + State.getpTop()[nextPiece][orientation][i]; h++) {

                if (h >= State.ROWS - 1) {
                    continue; //GGPOK already, dont consider -.-
                }
                clonedField[h][i + slot] = 1;
            }
        }
        return clonedField;
    }
    //total number of filled spot count after executing a move using the new 

    public static int filledSpotCount(State s, int[] move) {

        int count = 0;
        int[][] resultantField = computeHeuristicField(s, move);
        for (int y = 0; y < resultantField.length; y++) {
            for (int x = 0; x < resultantField[y].length; x++) {
                int blockint = resultantField[y][x];
                if (blockint > 0) {
                    count++;
                }
            }
        }

        return count;
    }

    public static int weightedFilledSpotCount(State s, int[] move) {
        int count = 0;
        int[][] resultantField = computeHeuristicField(s, move);

        for (int y = 0; y < resultantField.length; y++) {
            for (int x = 0; x < resultantField[y].length; x++) {
                int blockint = resultantField[y][x];
                if (blockint > 0) {
                    count = count * (y + 1);
                }
            }
        }
        return count;
    }

    public static int highestHole(State s, int[] move) {
        int max = 0;
        int[][] resultantField = computeHeuristicField(s, move);

        for (int x = 0; x < resultantField[0].length; x++) {
            for (int y = s.getTop()[x] - 2; y >= 0; y--) {
                int cell = resultantField[y][x];
                int topcell = resultantField[y + 1][x];
                if (cell == 0 && topcell > 0) {
                    Math.max(y + 1, max);
                    break;
                }
            }
        }

        return max;
    }

    public static int connectedHoles(State s, int[] move) {
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
    }

    public static int holesCount(State s, int[] move) {
        int count = 0;
        int[][] resultantField = computeHeuristicField(s, move);
        for (int x = 0; x < resultantField[0].length; x++) {
            for (int y = s.getTop()[x] - 2; y >= 0; y--) {
                int cell = s.getField()[y][x];
                if (cell == 0) { //is hole
                    count++;
                }
            }
        }
        return count;
    }

    public static int weightedHolesCount(State s, int[] move) {
        int count = 0;
        int[][] resultantField = computeHeuristicField(s, move);
        for (int x = 0; x < resultantField[0].length; x++) {
            for (int y = s.getTop()[x] - 2; y >= 0; y--) {
                int cell = s.getField()[y][x];
                if (cell == 0) { //is hole
                    count += count * (y + 1);
                }
            }
        }
        return count;
    }

    public static int blocksAboveHighestHole(State s, int[] move) {
        int holex = 0;
        int holey = 0;
        int[][] resultantField = computeHeuristicField(s, move);
        for (int x = 0; x < resultantField[0].length; x++) {
            for (int y = s.getTop()[x] - 2; y >= 0; y--) {
                int cell = s.getField()[y][x];
                int topcell = resultantField[y + 1][x];
                if (cell == 0 && topcell > 0) {
                    if ((y + 1) > holey) {
                        holex = x;
                        holey = (y + 1);
                    }
                    break;
                }
            }
        }
        return s.getTop()[holex] - holey;
    }

    public static int maxWellDepth(State s, int[] move) {
        int max = 0;
        int[][] resultantField = computeHeuristicField(s, move);
        return max;
    }

    public static int wellCount(State s, int[] move) {
        int count = 0;
        int[][] resultantField = computeHeuristicField(s, move);
        return count;
    }

    public static int surfaceAreaRoughness(State s, int[] move) {
        int roughness = 0;
        int[][] resultantField = computeHeuristicField(s, move);
        return roughness;
    }
}

