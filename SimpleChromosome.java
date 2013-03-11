
public class SimpleChromosome extends Chromosome {

    public double val_hole;
    public double val_height;
    public double val_well;
    public double val_delta;

    public SimpleChromosome(double _val_hole, double _val_height, double _val_well, double _val_delta) {
        val_hole = _val_hole;
        val_height = _val_height;
        val_well = _val_well;
        val_delta = _val_delta;
    }

    public SimpleChromosome() {
        val_height = Math.random() * 100.0 - 70.0;
        val_hole = Math.random() * 100.0 - 70.0;
        val_well = Math.random() * 100.0 - 70.0;
        val_delta = Math.random() * 100.0 - 70.0;
    }

    public String toString() {
        return "Val_height: " + val_height + ", Val_hole: " + val_hole + ", Val_well: " + val_well + ", Val_delta: " + val_delta;
    }

    Chromosome Crossover(Chromosome other) {
        SimpleChromosome mate = (SimpleChromosome) (other);
        return new SimpleChromosome((val_hole + mate.val_hole) / 2,
                (val_height + mate.val_height) / 2,
                (val_well + mate.val_well) / 2,
                (val_delta + mate.val_delta) / 2);
    }

    double evaluate(State s) {

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

        return 0;
    }

    void mutate() {
    }
}