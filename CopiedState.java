
public class CopiedState extends State {

    CopiedState(State s) {

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
}