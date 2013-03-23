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
    	//double[] d = {-0.4074583533555105,-0.39592242146631973,-0.30568475702855313,0.18303328544235653,-0.003916222289412574,0.013483281800154567,0.32167287053488125,-0.4362985461566685,0.4863468144027401};
    	 double[] d = new double[]{-0.31435940126625794,-0.21563515333255528,-0.3729786695320991,0.021052304432286117,0.008910294859162615,0.02223342029481823,-0.38886237185861094,0.2137576521904011,0.44813680599822514};
    	Chromosome c = new SimpleChromosome(d);
    	// Chromosome c = new SimpleChromosome();
    	int i = c.pickMove(s);
        return i;
    }

    public static void main(String[] args) {

    	/*
        // UNCOMMENT FOR LEARNING
        List<Chromosome> initialPop = new ArrayList<Chromosome>();
        for (int x = 0; x < 100; x++) { // <------------modify population size here
        	initialPop.add(new BetterChromosome());
        }
        SimpleGA ga = new SimpleGA(initialPop, 16, 0.10, 0.6);
        Learner learner = new Learner(ga);

        System.out.println("Learner Made");

        // Number of generations
        learner.Run(1000);
        */


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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("You have completed " + s.getRowsCleared() + " rows.");
    }
}

