package othello;

import java.util.List;
import java.util.Objects;

import adversarialSearch.Game;
import position.XYLocation;

public class OthelloGame implements Game<OthelloState, XYLocation, String> {

    private OthelloState initialState = new OthelloState();

    @Override
    public OthelloState getInitialState() {
        return initialState;
    }

    @Override
    public String[] getPlayers() {
        return new String[]{OthelloState.BLACK, OthelloState.WHITE};
    }

    @Override
    public String getPlayer(OthelloState state) {
        return state.getPlayerToMove();
    }

    @Override
    public List<XYLocation> getActions(OthelloState state) {
        return state.allValidMoves();
    }

    @Override
    public OthelloState getResult(OthelloState state, XYLocation action) {
        OthelloState result = state.clone();
        result.mark(action);
        return result;
    }

    @Override
    public boolean isTerminal(OthelloState state) {
        return state.gameIsOver();
    }

    @Override
    public double getUtility(OthelloState state, String player) {

        double result;
        int blackScore = state.getBlackScore();
        int whiteScore = state.getWhiteScore();

        if (Objects.equals(player, OthelloState.WHITE))
            result = whiteScore - blackScore;
        else {
            result = blackScore - whiteScore;
        }
        return result;
    }
}
