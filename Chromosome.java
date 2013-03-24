
import java.util.*;


public abstract class Chromosome implements Comparable<Chromosome>{

    static private Chromosome bestCandidate = null;
    private double fitness = 0;
    protected double numGames = 0;
    
    abstract Chromosome Crossover(Chromosome other);

    abstract double evaluate(CopiedState s);

    abstract void mutate();

    // Returns a string which describes the parameters of this chromosome
    @Override
    public String toString() {
        return ("This chromosome does not have any parameters to be printed");
    }

    // Choose the move which gives us the best resultant state after choosing it
    //Please do not use NULL for any kind of numeric value, use -oo or oo depending
    //on circumstances
    public int pickMove(State s) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int bestMoveIndex = 1;
        for (int x = 0; x < s.legalMoves().length; x++) {
            // Choose the move with the best evaluation
            CopiedState newState = new CopiedState(s);
            newState.makeMove(x);
            if (newState.lost) continue;	// Avoid losing at all costs.
            double score = evaluate(newState);
            
            if (score > bestScore) {
                bestScore = score;
                bestMoveIndex = x;
            } else if (score == bestScore) {
                //if they are equal, then using this 1 or that 1 doesn't matter
                //if its an even number, use the new 1, else use old.
                 Random rng = new Random();
                 if (rng.nextInt(100) % 2 == 0){
                     bestMoveIndex = x;
                 }else{
                    //do nth
                 }
            }
        }
        return bestMoveIndex;

    }

    public double getFitness() {
        return fitness;
    }

    // Adds a particular game score and recomputes the average score (to give the fitness) 
    public void addGameScore(double score) {
    	fitness = (fitness * numGames + score) / (numGames + 1);
    	numGames++;
    }
    
    public void setFitness(double score) {
        fitness = score;
    }
    
    
    // Compares between the fitness of two Chromosomes such that they will be sorted
    // in DESCENDING order.
    public int compareTo(Chromosome compareChromosome) {
    	if (this.fitness < compareChromosome.fitness)
    		return 1;
    	else if (this.fitness == compareChromosome.fitness)
    		return 0;
    	
    	return -1;
    }
    
    // Returns how similar 2 chromosomes are. -1 for most unsimilar, and 1 for being the most similar.
    public abstract double similarityIndex(Chromosome compareChromosome);
}

