import java.util.List;
import java.util.Stack;
import java.util.ArrayList;

public class PlayerSkeleton {

	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		//Chromosome c = Learner.GetBestCandidate();
		// Chromosome c = new SimpleChromosome(-25.3, -40.4,-12.3,-14.9);
		Chromosome c = new SimpleChromosome(-27.6119, -47.503, -49.41, -2.52505);

		System.out.println(c.PickMove(s));
		return c.PickMove(s);
		// return 0;
	}
	



	public static void main(String[] args) {


		/* UNCOMMENT FOR LEARNING 
		List<Chromosome> initialPop = new ArrayList<Chromosome>();
		for (int x = 0; x < 100; x++) { // <------------modify population size here
			initialPop.add(new SimpleChromosome());
		}
		SimpleGA ga = new SimpleGA(initialPop);
		Learner learner = new Learner(ga);

		System.out.println("Learner Made");

		// Number of generations
		learner.Run(10);
		*/


		// Remember to remove top part
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}

// 
class Learner {
	private GeneticAlgo algo;
	static Chromosome bestCandidate = null;

	public Learner(GeneticAlgo ga) {
		algo = ga;
	}

	public void Run(int numGenerations) {
		for (int x = 0; x < numGenerations; x++) {
			algo.MakeNextGen();
			System.out.println("Gen "+x);
			System.out.println("GenSize "+algo.pop.size());
			System.out.println("BestFitness "+algo.GetBest().GetFitness());
			System.out.println("Parameters:\n"+algo.GetBest().GetStr());
			SetBestCandidate(algo.GetBest());

		}
	}

	public void SetBestCandidate(Chromosome c) {
		bestCandidate = c;
	}

	public static Chromosome GetBestCandidate() {
		return bestCandidate;
	}
}

// 
abstract class GeneticAlgo<T extends Chromosome> {

	public List<Chromosome> pop;

	void MakeNextGen() {

		//for (Chromosome i : pop){
		//	System.out.println(i.GetStr());
		//}
		List<ParentPair> parents = Selection();
		//for (ParentPair i : parents){
		//	System.out.println(i.a.GetStr() + " AND " + i.b.GetStr());
		//}
		List<Chromosome> children = Crossover(parents);
		children = Mutation(children);
		for (Chromosome c : children)
			c.SetFitness(ComputeFitness(c));
		pop = Acceptance(children);
	}

	// Defines a pair of parents
	class ParentPair {
		public Chromosome a, b;
		public ParentPair (Chromosome _a, Chromosome _b) {
			a = _a;
			b = _b;
		}
	}

	GeneticAlgo(List<Chromosome> initialPop) {
		pop = initialPop;
		for (Chromosome c : pop) {
			c.SetFitness(ComputeFitness(c));
		}
	}

	// Computes the fitness of a given Chromosome
	abstract protected double ComputeFitness(Chromosome c);

	// Selects which chromosomes to breed
	abstract protected List<ParentPair> Selection();

	// Creates the children from the parents chosen. Returns a list of children.
	abstract protected List<Chromosome> Crossover(List<ParentPair> parents);

	// Implements mutation. Leave blank if we do not wish for mutation to occur.
	abstract protected List<Chromosome> Mutation(List<Chromosome> children);

	// Merges the parent and children populations to give the next generation
	abstract protected List<Chromosome> Acceptance(List<Chromosome> children);

	// Gets the best chromosome in the current population
	public Chromosome GetBest() {
		Chromosome bestChromosome = null;
		for (Chromosome c : pop) {
			if (bestChromosome == null || bestChromosome.GetFitness() < c.GetFitness()) {
				bestChromosome = c;
			}
		}
		return bestChromosome;
	}

	public List<Chromosome> GetPop() {
		return pop;
	}

}

// 
abstract class Chromosome {
	static private Chromosome bestCandidate = null;

	private double fitness = 0;

	abstract Chromosome Crossover(Chromosome other);
	abstract double Evaluate(State s);
	abstract void Mutate();

	// Returns a string which describes the parameters of this chromosome 
	public String GetStr() {
		return ("This chromosome does not have any parameters to be printed");
	}

	// Choose the move which gives us the best resultant state after choosing it
	public int PickMove(State s) {
		Double bestScore = null;
		int bestMoveIndex = 1;
		for (int x = 0; x < s.legalMoves[s.getNextPiece()].length; x++) {
			// Choose the move with the best evaluation
			CopiedState newState = new CopiedState(s);
			newState.makeMove(x);
			double score = Evaluate(newState);
			if (bestScore == null || bestScore <= score) {
				bestScore = score;
				bestMoveIndex = x;
			}
		}
		return bestMoveIndex;
	}

	public double GetFitness() {
		return fitness;
	}

	public void SetFitness(double score) {
		fitness = score;
	}

}

// Don't change this //
class CopiedState extends State{
	CopiedState(State s){

		int[][] curField = getField();
		int[][] stateField = s.getField();
		for (int x = 0; x < curField.length; x++) {
			for (int y = 0; y < curField[x].length; y++)
				curField[x][y] = stateField[x][y];
		}

		int[] curTop = getTop();
		for (int x = 0; x < curTop.length; x++)
			curTop[x] = s.getTop()[x];

		nextPiece = s.getNextPiece();
	}

}


class Simulator {
	public int getNumLinesRemoved(Chromosome c) {
		State s = new State();
		while (!s.hasLost()) {
			s.makeMove(c.PickMove(s));
		}
		return s.getRowsCleared();
	}

	public int getNumTurnsPassed(Chromosome c) {
		State s = new State();
		
		while (!s.hasLost()) {
			s.makeMove(c.PickMove(s));
		}
		return s.getTurnNumber();
	}
}


///////////////////////////////////////////////////////////////////////////////////////
// IMPLEMENTATION OF TRAINER COMES HERE
// 	
///////////////////////////////////////////////////////////////////////////////////////
class SimpleGA<T extends Chromosome> extends GeneticAlgo<T> {

	SimpleGA(List<Chromosome> initialPop) {
		// GeneticAlgo(initialPop);
		super(initialPop);
	}

	protected double ComputeFitness(Chromosome c) {
		// Run simulation 10 times, compute the number of lines removed, and take average
		int tlines = 0;
		for (int x = 0; x < 10; x++) {
			Simulator sim = new Simulator();
			tlines += sim.getNumTurnsPassed(c);
		}
		return (double)(tlines) / (double)(10);
	}

	protected List<ParentPair> Selection() {
		List<ParentPair> ret = new ArrayList<ParentPair>();
		// Get 2 * pop_size parents, use brute force - we should probably optimize here
		double totalFitness = 0;
		for (Chromosome c : pop) 
			totalFitness += c.GetFitness();

		for (int x = 0; x < pop.size(); x++) {
			Chromosome parents[] = new Chromosome[2];
			for (int y = 0; y < 2; y++) {
				double rand = Math.random() * totalFitness;
				double cumulativeSum = 0;
				parents[y] = null;
				for (Chromosome c : pop) {
					cumulativeSum += c.GetFitness();
					if (cumulativeSum >= rand-0.0000000000000001) {
						parents[y] = c;
						break;
					}
				}
			}
			ret.add(new ParentPair(parents[0], parents[1]));
		}

		return ret;
	}

	protected List<Chromosome> Acceptance(List<Chromosome> children) {
		// Accept the whole new genreration ... but keep the top of the old generation
		children.add(GetBest());
		return children;
	}

	protected List<Chromosome> Mutation(List<Chromosome> children) {
		// Do not mutate at all
		return children;
	}

	protected List<Chromosome> Crossover(List<ParentPair> parents) {
		List<Chromosome> ret = new ArrayList<Chromosome>();
		for (ParentPair p : parents)
			ret.add(p.a.Crossover(p.b));
		return ret;
	}

}

class SimpleChromosome extends Chromosome {
	public double val_hole;
	public double val_height;
	public double val_well;
	public double val_delta;

	public SimpleChromosome (double _val_hole, double _val_height, double _val_well, double _val_delta) {
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

	public String GetStr() {
		return "Val_height: "+val_height+", Val_hole: "+val_hole+", Val_well: "+val_well+", Val_delta: "+val_delta;
	}

	Chromosome Crossover(Chromosome other) {
		SimpleChromosome mate = (SimpleChromosome)(other);
		return new SimpleChromosome((val_hole + mate.val_hole)/2, 
									(val_height + mate.val_height)/2,
									(val_well + mate.val_well)/2,
									(val_delta + mate.val_delta)/2);
	}

	double Evaluate(State s) {
		// Count number of holes
		int holes = 0;
		for (int x = 0; x < s.COLS; x++) {
			for (int y = s.getTop()[x]-1; y >= 0; y--) {
				if (s.getField()[y][x] == 0) {
					holes++;
				}
			}
		}

		// Count value of height 
		int[] top = s.getTop();
		int highest = -1;
		for (int i : top) {
			highest = Math.max(i, highest);
		}

		int num_wells = 0;
		// Count number of wells
		for (int x = 0; x < s.COLS; x++) {
			int numSidesCovered = 0;

			if (x == 0 || (top[x-1] >= top[x] + 4))
				numSidesCovered++;
			if (x == s.COLS-1 || (top[x+1] >= top[x] + 4))
				numSidesCovered++;
			if (numSidesCovered >= 2) {
				num_wells++;
			}
		}

		// Compute delta
		int lowest = 1000;
		for (int i : top) {
			lowest = Math.min(i, lowest);
		}
		int delta = highest - lowest;

		return (double)(highest) * val_height + (double)(holes) * val_hole + (double)(num_wells) * val_well + (double)(delta) * val_delta;
	}
	void Mutate() {}
}
