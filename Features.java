public class Features {

    public static float calHeightScore(State s, int[] move) {

        return -1;
    }

    public static int findMaxHeight(State s) {
        int[] top = s.getTop();
        int highest = -1;
        for (int i : top) {
            highest = Math.max(i, highest);
        }

        return highest;
    }

    public static int findMinHeight(State s) {
        int[] top = s.getTop();
        int lowest = Integer.MAX_VALUE;
        for (int i : top) {
            lowest = Math.min(i, lowest);
        }

        return lowest;
    }
}
