public class Simulator {
	private int simulatedHeight;
	
	Simulator() {
		simulatedHeight = State.ROWS;
	}
	
	Simulator(int simulatedHeight) {
		this.simulatedHeight = simulatedHeight; 
	}
	
    public int getNumLinesRemoved(Chromosome c) {
        State s = new CopiedState(new State(), simulatedHeight);
        while (!s.hasLost()) {
            s.makeMove(c.pickMove(s));
        }
        return s.getRowsCleared();
    }

    public int getNumTurnsPassed(Chromosome c) {
        State s = new CopiedState(new State(), simulatedHeight);

        while (!s.hasLost()) {
            s.makeMove(c.pickMove(s));
        }
        return s.getTurnNumber();
    }
}