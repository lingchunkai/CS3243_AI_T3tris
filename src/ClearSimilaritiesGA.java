import java.util.ArrayList;
import java.util.List;


public class ClearSimilaritiesGA extends SimpleGA {

	public ClearSimilaritiesGA(List<Chromosome> initialPop, int maxHeight,
			double mutationProb, double crossoverRate) {
		super(initialPop, maxHeight, mutationProb, crossoverRate);
	}

	
	// Returns the level of diversity between the whole population. The closer to 1, the more uniform
	protected double getPopulationSimilarity() {
		double sumSimilarity = 0;
		double maxSimilarity = population.size() * (population.size() - 1);
		for (Chromosome a : population) {
			for (Chromosome b : population) {
				if (a == b) continue;
				sumSimilarity += Math.abs(a.similarityIndex(b));
			}
		}
		return sumSimilarity / maxSimilarity;
	}
	
//	@Override
//	protected void computeFitnessLevel(Chromosome c) {
//		// TODO Auto-generated method stub
//		super.computeFitnessLevel(c);
//	}

	@Override
	protected List<ParentPair> selection() {
		// TODO Auto-generated method stub
		return super.selection();
	}

	@Override
	protected List<Chromosome> filter(List<Chromosome> children) {
		List<Chromosome> newChildren = new ArrayList<Chromosome>();
		int repeats = 0;
		// Remove children which are too similar
		for (Chromosome c : children) {
			boolean unique = true;
			for (Chromosome c2 : children) {
				if (c == c2) continue;
				if (Math.abs(c.similarityIndex(c2)) < 0.001) { // too similar
					unique = false;
					break;
				}
			}
			if (unique) {
				// remove children which are too similar to parents
				for (Chromosome p : population) {
					if (Math.abs(c.similarityIndex(p)) < 0.001) {
						unique = false;
						break;
					}
				}
			}
			if (unique)
				newChildren.add(c);
			else {
				repeats++;
				try {
					// Add replacement freshie and collect its fitness
					Chromosome newGuy = c.getClass().getConstructor().newInstance();
					newChildren.add(newGuy);
				} catch (java.lang.Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Repeats: " + repeats);
		
		return newChildren;
	}

	@Override
	protected List<Chromosome> mutate(List<Chromosome> children) {
		// TODO Auto-generated method stub
		return super.mutate(children);
	}

	@Override
	protected List<Chromosome> crossover(List<ParentPair> parents) {
		// TODO Auto-generated method stub
		return super.crossover(parents);
	}
	
	@Override
	protected List<Chromosome> acceptance(List<Chromosome> children) {
		return super.acceptance(children);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder(super.toString());
		return str.append("POP_SIMILARITY: " + getPopulationSimilarity() + "\n").toString();
	}
}
