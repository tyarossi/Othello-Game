package adversarialSearch;

import metrics.Metrics;

public class MinimaxSearch<S, A, P> implements AdversarialSearch<S, A>
{
    public final static String METRICS_NODES_EXPANDED = "nodesExpanded";

    private Game<S, A, P> game;
    private Metrics metrics = new Metrics();

    /**
     * Creates a new search object for a given othello.
     */
    public static <S, A, P> MinimaxSearch<S, A, P> createFor(Game<S, A, P> game) {
        return new MinimaxSearch<>(game);
    }

    public MinimaxSearch(Game<S, A, P> game) {
        this.game = game;
    }

    @Override
    public A makeDecision(S state) {
        if(state == null){
            System.out.println("State = null");
            throw new NullPointerException();
        }
        if(game == null){
            System.out.println("game == null");
            throw new NullPointerException();
        }
        metrics = new Metrics();
        A result = null;
        double resultValue = Double.NEGATIVE_INFINITY;
        P player = game.getPlayer(state);
        for (A action : game.getActions(state)) {
            double value = minValue(game.getResult(state, action), player, 3);
            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
        }
        return result;
    }

//	  Note: This version looks cleaner but expands almost twice as much nodes (Comparator...)
//    @Override
//    public A makeDecision(S state) {
//        metrics = new Metrics();
//        P player = othello.getPlayer(state);
//        return othello.getActions(state).stream()
//                .max(Comparator.comparing(action -> minValue(othello.getResult(state, action), player)))
//                .orElse(null);
//    }

    public double maxValue(S state, P player, int depth) { // returns an utility value
        metrics.incrementInt(METRICS_NODES_EXPANDED);
        if (depth == 0 || game.isTerminal(state))
            return game.getUtility(state, player);
        return game.getActions(state).stream()
                .mapToDouble(action -> minValue(game.getResult(state, action), player, depth-1))
                .max().orElse(Double.NEGATIVE_INFINITY);
    }

    public double minValue(S state, P player, int depth) { // returns an utility value
        metrics.incrementInt(METRICS_NODES_EXPANDED);
        if (depth == 0 || game.isTerminal(state))
            return game.getUtility(state, player);
        return game.getActions(state).stream()
                .mapToDouble(action -> maxValue(game.getResult(state, action), player, depth-1))
                .min().orElse(Double.POSITIVE_INFINITY);
    }

    @Override
    public Metrics getMetrics() {
        return metrics;
    }
}
