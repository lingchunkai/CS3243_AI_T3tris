public class Simulator {

    public int getNumLinesRemoved(Chromosome c) {
        State s = new State();
        while (!s.hasLost()) {
            s.makeMove(c.pickMove(s));
        }
        return s.getRowsCleared();
    }

    public int getNumTurnsPassed(Chromosome c) {
        State s = new State();

        while (!s.hasLost()) {
            s.makeMove(c.pickMove(s));
        }
        return s.getTurnNumber();
    }
}