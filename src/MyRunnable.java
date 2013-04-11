/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nick
 */
public class MyRunnable implements Runnable  {

     private Chromosome c;
     private int maxHeight;
     private int n;

  MyRunnable(Chromosome incc, int maxHeight, int n) {
    this.c = incc;
    this.maxHeight = maxHeight;
    this.n = n;

  }
    @Override
  public void run() {
        for (int x = 0; x < n; x++) {
            Simulator sim = new Simulator(maxHeight); // Force death at maxHeight
            c.addGameScore(sim.getNumTurnsPassed(c));
        }
    }

//    for (int x = 0; x < n; x++) {
//        	Simulator sim = new Simulator(maxHeight); // Force death at maxHeight
//            c.addGameScore(sim.getNumTurnsPassed(c));
//        }
//        return c.getFitness();

}
