package adversarialSearch;

import java.util.List;

public interface Game<S, A, P>{


    S getInitialState();

    P[] getPlayers();

    P getPlayer(S state);

    List<A> getActions(S state);

    S getResult(S state, A action);

    boolean isTerminal(S state);

    double getUtility(S state, P player);
}
