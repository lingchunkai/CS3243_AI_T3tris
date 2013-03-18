import java.util.*;

public class PlayerSkeleton {

    //implement this function to have a working system
    public int pickMove(State s, int[][] legalMoves) {
        //Chromosome c = Learner.GetBestCandidate();
        //Chromosome c = new SimpleChromosome(-25.3, -40.4, -12.3, -14.9);
        //Chromosome c = new SimpleChromosome(-27.6119, -47.503, -49.41, -2.52505);
    	

		// double[] d = new double[]{-20.0, -10.0, -10.0, 0, 0, 0, 0};
    	// double[] d = new double[]{0, -10, 0, 0, 0, 0, 0};
    	// double[] d = new double[]{-0.4255216464128099,-0.09169136063804906,-0.39914340921207014,-0.056224417464258325,-0.004315414795928674,-0.46854982136532564,-0.4564261368872249};
    	// double[] d = {-0.4186641840188783,-0.11658629534372345,-0.46462935406919725,-0.047555870194355365,0.010479259832076426,0.22926620387114072,0.06520258917420518};
    	// double[] d = {-0.454944595225789,-0.31117463749768126,-0.26798448005579933,0.03458328418424961,-0.3654701646392101,0.3390836793124713,-0.40477254493737713};
    	// double[] d = {-0.4980295305135991,-0.4315627025645594,-0.45286302929418853,0.020816590047498806,-0.22605251742078147,-0.3285249035676846,0.20842924213682446,-0.050628747107058,0.49217281372994803};
    	double[] d = {-0.470511522176548,-0.1966525545667246,-0.33620333699058047,-0.0894927869459442,-0.16656795236755517,0.15326643156580888,-0.06457261016773275,0.27651755483882867,0.2614950351494273};
    	Chromosome c = new SimpleChromosome(d);
    	// Chromosome c = new SimpleChromosome();
    	int i = c.pickMove(s);
        return i;
    }

    public static void main(String[] args) {

    	
        // UNCOMMENT FOR LEARNING
        List<Chromosome> initialPop = new ArrayList<Chromosome>();
        for (int x = 0; x < 100; x++) { // <------------modify population size here
        	initialPop.add(new SimpleChromosome());
        }
        SimpleGA ga = new SimpleGA(initialPop, 0.02, 0.6);
        Learner learner = new Learner(ga);

        System.out.println("Learner Made");

        // Number of generations
        learner.Run(100);
        


        // Remember to remove top part
        State s = new State();
        new TFrame(s);
        PlayerSkeleton p = new PlayerSkeleton();
        while (!s.hasLost()) {
            System.out.println("------------------------------------------------------------------------------------------");
            s.makeMove(p.pickMove(s, s.legalMoves()));
            s.draw();
            s.drawNext(0, 0);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("You have completed " + s.getRowsCleared() + " rows.");
    }
}

