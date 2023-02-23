package adversarialSearch;

import metrics.Metrics;

public interface AdversarialSearch<S, A> {

    /**
     * Returns the action which appears to be the best at the given state.
     */
    A makeDecision(S state);

    /**
     * Returns all the metrics of the search.
     *
     * @return all the metrics of the search.
     */
    Metrics getMetrics();
}
