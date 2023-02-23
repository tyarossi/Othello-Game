package othello;

import java.util.*;

import position.XYLocation;

public class OthelloState implements Cloneable {

    public static final String WHITE = "W";
    public static final String BLACK = "B";
    public static final String EMPTY = "-";
    private HashMap<XYLocation, boolean[]> allPossibleMoves;
    //
    private String[] board;

    private String playerToMove;

    public OthelloState() {

        board = new String[64];
        Arrays.fill(board, EMPTY);

        board[27] = WHITE;
        board[28] = BLACK;
        board[35] = BLACK;
        board[36] = WHITE;

        playerToMove = BLACK;
        this.allPossibleMoves = generateAllPossibleMoves(playerToMove);
    }

    public OthelloState(String[] board, String playerToMove) {
        this.board = board;
        //   this.playerToMove = (Objects.equals(playerToMove, BLACK) ? WHITE : BLACK);
        //   analyzeUtility();
        this.playerToMove = playerToMove;
        this.allPossibleMoves = generateAllPossibleMoves(this.playerToMove);
    }

    public String getPlayerToMove() {
        return playerToMove;
    }

    public boolean isEmpty(int col, int row) {
        return Objects.equals(board[getAbsPosition(col, row)], EMPTY);
    }

    public String getValue(int col, int row) {
        return board[getAbsPosition(col, row)];
    }


    public void mark(XYLocation action) {
        mark(action.getX(), action.getY());
    }

    public void mark(int col, int row) {
        if (!gameIsOver() && isValidMove(col, row)) {
            board[getAbsPosition(col, row)] = playerToMove;
            flipDiscs(col, row);
            String nextPlayer = (Objects.equals(playerToMove, BLACK) ? WHITE : BLACK);
            //generate all possible moves for next player
            HashMap<XYLocation, boolean[]> possibleMoves = generateAllPossibleMoves(nextPlayer);

            if (!possibleMoves.isEmpty()) {
                playerToMove = nextPlayer;
                allPossibleMoves = possibleMoves;
            }
            else {
                this.allPossibleMoves = generateAllPossibleMoves(playerToMove);
            }
        }

    }

    public int getNumberOfMarkedPositions() {
        int retVal = 0;
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                if (!(isEmpty(col, row))) {
                    retVal++;
                }
            }
        }
        return retVal;
    }


    @Override
    public OthelloState clone() {
        OthelloState copy = null;
        try {
            copy = (OthelloState) super.clone();
            copy.board = Arrays.copyOf(board, board.length);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace(); // should never happen...
        }
        return copy;
    }

    @Override
    public boolean equals(Object anObj) {
        if (anObj != null && anObj.getClass() == getClass()) {
            OthelloState anotherState = (OthelloState) anObj;
            for (int i = 0; i < 64; i++) {
                if (!Objects.equals(board[i], anotherState.board[i]))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // Need to ensure equal objects have equivalent hashcodes (Issue 77).
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                builder.append(getValue(col, row)).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    //
    // PRIVATE METHOD
    //

    private int getAbsPosition(int col, int row) {
        return row * 8 + col;
    }

    //----------------------------------------------------------------
    //return the number of blacks on the board
    public int getBlackScore() {
        int score = 0;
        for (String element : board) {
            if (Objects.equals(element, BLACK))
                score++;
        }
        return score;
    }

    //return the number of whites on the board
    public int getWhiteScore() {
        int score = 0;
        for (String element : board) {
            if (Objects.equals(element, WHITE))
                score++;
        }
        return score;
    }

    public boolean gameIsOver() {
        String currentPlayer = getPlayerToMove();

        if (getNumberOfMarkedPositions() == 64) {
            return true;
        }

        if (allPossibleMoves.isEmpty()) {
            String oppositePlayer = (Objects.equals(currentPlayer, BLACK) ? WHITE : BLACK);
            HashMap<XYLocation, boolean[]> opValidMoves = generateAllPossibleMoves(oppositePlayer);

            return opValidMoves.isEmpty();
        }

        return false;
    }

    private HashMap<XYLocation, boolean[]> generateAllPossibleMoves(String currentPlayer) {
        HashMap<XYLocation, boolean[]> allPossibleMoves = new HashMap<>();
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (Objects.equals(board[getAbsPosition(column, row)], currentPlayer)) {
                    validMoves(allPossibleMoves, currentPlayer, column, row);
                }
            }
        }
        return allPossibleMoves;
    }

    // returns a list of of all possible moves for the current player
    public ArrayList<XYLocation> allValidMoves() {

        return new ArrayList<>(allPossibleMoves.keySet());
    }

    private void validMoves(HashMap<XYLocation, boolean[]> moves, String currentPlayer, int column, int row) {
        // directions[0] = up, directions[1] = down, directions[2] = right, directions[3] = left,
        //directions[4] = up-left, directions[5] = down-right, directions[6] = up-right,
        //directions[7] = down-left,
        boolean[] direction1 = {false, false, false, false, false, false, false, false};
        XYLocation validPosition = up(currentPlayer, column, row);

        if (validPosition != null) {
            if (moves.get(validPosition) == null) {
                direction1[0] = true;
                moves.put(validPosition, direction1);
            } else {
                boolean[] values = moves.get(validPosition);
                values[0] = true;
                moves.put(validPosition, values);
            }
        }

        boolean[] direction2 = {false, false, false, false, false, false, false, false};
        validPosition = down(currentPlayer, column, row);

        if (validPosition != null) {
            if (moves.get(validPosition) == null) {
                direction2[1] = true;
                moves.put(validPosition, direction2);
            } else {
                boolean[] values = moves.get(validPosition);
                values[1] = true;
                moves.put(validPosition, values);
            }
        }

        boolean[] direction3 = {false, false, false, false, false, false, false, false};
        validPosition = right(currentPlayer, column, row);

        if (validPosition != null) {
            if (moves.get(validPosition) == null) {
                direction3[2] = true;
                moves.put(validPosition, direction3);
            } else {
                boolean[] values = moves.get(validPosition);
                values[2] = true;
                moves.put(validPosition, values);
            }
        }

        boolean[] direction4 = {false, false, false, false, false, false, false, false};
        validPosition = left(currentPlayer, column, row);

        if (validPosition != null) {
            if (moves.get(validPosition) == null) {
                direction4[3] = true;
                moves.put(validPosition, direction4);
            } else {
                boolean[] values = moves.get(validPosition);
                values[3] = true;
                moves.put(validPosition, values);
            }
        }
        //---------------------------
        boolean[] direction5 = {false, false, false, false, false, false, false, false};
        validPosition = upLeft(currentPlayer, column, row);

        if (validPosition != null) {
            if (moves.get(validPosition) == null) {
                direction5[4] = true;
                moves.put(validPosition, direction5);
            } else {
                boolean[] values = moves.get(validPosition);
                values[4] = true;
                moves.put(validPosition, values);
            }
        }

        boolean[] direction6 = {false, false, false, false, false, false, false, false};
        validPosition = downRight(currentPlayer, column, row);

        if (validPosition != null) {
            if (moves.get(validPosition) == null) {
                direction6[5] = true;
                moves.put(validPosition, direction6);
            } else {
                boolean[] values = moves.get(validPosition);
                values[5] = true;
                moves.put(validPosition, values);
            }
        }

        boolean[] direction7 = {false, false, false, false, false, false, false, false};
        validPosition = upRight(currentPlayer, column, row);

        if (validPosition != null) {
            if (moves.get(validPosition) == null) {
                direction7[6] = true;
                moves.put(validPosition, direction7);
            } else {
                boolean[] values = moves.get(validPosition);
                values[6] = true;
                moves.put(validPosition, values);
            }
        }

        boolean[] direction8 = {false, false, false, false, false, false, false, false};
        validPosition = dowLeft(currentPlayer, column, row);

        if (validPosition != null) {
            if (moves.get(validPosition) == null) {
                direction8[7] = true;
                moves.put(validPosition, direction8);
            } else {
                boolean[] values = moves.get(validPosition);
                values[7] = true;
                moves.put(validPosition, values);
            }
        }
    }

    private XYLocation right(String currentPlayer, int col, int row) {
        XYLocation position = new XYLocation(col, row);

        if (position.right().getX() < 7) {
            XYLocation right = position.right();

            int rightPosition = getAbsPosition(right.getX(), right.getY());
            String oppositePlayer = (Objects.equals(currentPlayer, BLACK) ? WHITE : BLACK);

            while (Objects.equals(board[rightPosition], oppositePlayer) && right.getX() < 7) {
                position = new XYLocation(right.getX(), right.getY());
                right = position.right();
                rightPosition = getAbsPosition(right.getX(), right.getY());
            }
            if (right.getX() <= 7 && Objects.equals(board[rightPosition], EMPTY) && (right.getX() - 1) != col) {
                return new XYLocation(right.getX(), right.getY());
            }
        }
        return null;
    }

    private XYLocation left(String currentPlayer, int col, int row) {
        XYLocation position = new XYLocation(col, row);

        if (position.left().getX() > 0) {
            XYLocation left = position.left();
            // System.out.println("left position " + left);
            int leftPosition = getAbsPosition(left.getX(), left.getY());
            String oppositePlayer = (Objects.equals(currentPlayer, BLACK) ? WHITE : BLACK);

            while (Objects.equals(board[leftPosition], oppositePlayer) && left.getX() > 0) {
                position = new XYLocation(left.getX(), left.getY());
                left = position.left();
                leftPosition = getAbsPosition(left.getX(), left.getY());
            }
            if (left.getX() >= 0 && Objects.equals(board[leftPosition], EMPTY) && (left.getX() + 1) != col) {
                //   System.out.println("left value"  + left);
                return new XYLocation(left.getX(), left.getY());
            }
        }
        return null;
    }

    private XYLocation up(String currentPlayer, int col, int row) {
        XYLocation position = new XYLocation(col, row);

        if (position.up().getY() > 0) {
            XYLocation up = position.up();

            int upPosition = getAbsPosition(up.getX(), up.getY());
            String oppositePlayer = (Objects.equals(currentPlayer, BLACK) ? WHITE : BLACK);

            while (Objects.equals(board[upPosition], oppositePlayer) && up.getY() > 0) {
                position = new XYLocation(up.getX(), up.getY());
                up = position.up();
                upPosition = getAbsPosition(up.getX(), up.getY());
            }
            if (up.getY() >= 0 && Objects.equals(board[upPosition], EMPTY) && (up.getY() + 1) != row) {
                return new XYLocation(up.getX(), up.getY());
            }
        }
        return null;
    }

    private XYLocation down(String currentPlayer, int col, int row) {
        XYLocation position = new XYLocation(col, row);

        if (position.down().getY() < 7) {
            XYLocation down = position.down();

            int downPosition = getAbsPosition(down.getX(), down.getY());
            String oppositePlayer = (Objects.equals(currentPlayer, BLACK) ? WHITE : BLACK);

            while (Objects.equals(board[downPosition], oppositePlayer) && down.getY() < 7) {
                position = new XYLocation(down.getX(), down.getY());
                down = position.down();
                downPosition = getAbsPosition(down.getX(), down.getY());
            }
            if (down.getY() <= 7 && Objects.equals(board[downPosition], EMPTY) && (down.getY() - 1) != row) {
                return new XYLocation(down.getX(), down.getY());
            }
        }
        return null;
    }

    private XYLocation upLeft(String currentPlayer, int col, int row) {
        XYLocation position = new XYLocation(col, row);

        if (position.left().getX() > 0 && position.left().up().getY() > 0) {
            XYLocation left = position.left();
            XYLocation up = left.up();

            int upLeftPosition = getAbsPosition(up.getX(), up.getY());
            String oppositePlayer = (Objects.equals(currentPlayer, BLACK) ? WHITE : BLACK);
            while (Objects.equals(board[upLeftPosition], oppositePlayer) && up.left().getX() >= 0
                    && up.up().getY() >= 0) {
                position = up;
                left = position.left();
                up = left.up();
                upLeftPosition = getAbsPosition(up.getX(), up.getY());
            }
            if (Objects.equals(board[upLeftPosition], EMPTY) && ((up.getX() + 1 != col) && (up.getY() + 1 != row))) {
                return new XYLocation(up.getX(), up.getY());
            }
        }
        return null;
    }

    private XYLocation downRight(String currentPlayer, int col, int row) {
        XYLocation position = new XYLocation(col, row);

        if (position.right().getX() < 7 && position.right().down().getY() < 7) {
            XYLocation right = position.right();
            XYLocation down = right.down();

            int downRightPosition = getAbsPosition(down.getX(), down.getY());
            String oppositePlayer = (Objects.equals(currentPlayer, BLACK) ? WHITE : BLACK);
            while (Objects.equals(board[downRightPosition], oppositePlayer) && down.right().getX() <= 7
                    && down.down().getY() <= 7) {
                position = down;
                right = position.right();
                down = right.down();
                downRightPosition = getAbsPosition(down.getX(), down.getY());
            }
            if (Objects.equals(board[downRightPosition], EMPTY) && ((down.getX() - 1 != col) && (down.getY() - 1 != row))) {
                return new XYLocation(down.getX(), down.getY());
            }
        }
        return null;
    }

    private XYLocation upRight(String currentPlayer, int col, int row) {
        XYLocation position = new XYLocation(col, row);

        if (position.right().getX() < 7 && position.right().up().getY() > 0) {
            XYLocation right = position.right();
            XYLocation up = right.up();

            int upLeftPosition = getAbsPosition(up.getX(), up.getY());
            String oppositePlayer = (Objects.equals(currentPlayer, BLACK) ? WHITE : BLACK);
            while (Objects.equals(board[upLeftPosition], oppositePlayer) && up.right().getX() <= 7 && up.up().getY() >= 0) {
                position = up;
                right = position.right();
                up = right.up();
                upLeftPosition = getAbsPosition(up.getX(), up.getY());
            }
            if (Objects.equals(board[upLeftPosition], EMPTY) && ((up.getX() - 1 != col) && (up.getY() + 1 != row))) {
                return new XYLocation(up.getX(), up.getY());
            }
        }
        return null;
    }

    private XYLocation dowLeft(String currentPlayer, int col, int row) {
        XYLocation position = new XYLocation(col, row);

        if (position.left().getX() > 0 && position.left().down().getY() < 7) {
            XYLocation left = position.left();
            XYLocation down = left.down();

            int downRightPosition = getAbsPosition(down.getX(), down.getY());
            String oppositePlayer = (Objects.equals(currentPlayer, BLACK) ? WHITE : BLACK);
            while (Objects.equals(board[downRightPosition], oppositePlayer) && down.left().getX() >= 0
                    && down.down().getY() <= 7) {
                position = down;
                left = position.left();
                down = left.down();
                downRightPosition = getAbsPosition(down.getX(), down.getY());
            }
            if (Objects.equals(board[downRightPosition], EMPTY) && ((down.getX() + 1 != col) && (down.getY() - 1 != row))) {
                return new XYLocation(down.getX(), down.getY());
            }
        }
        return null;
    }

    public boolean isValidMove(int col, int row) {
        XYLocation location = new XYLocation(col, row);
        return allPossibleMoves.get(location) != null;

    }

    private void flipDiscs(int col, int row) {
        XYLocation key = new XYLocation(col, row);
        int position = getAbsPosition(col, row);
        String oppositePlayer = (Objects.equals(playerToMove, BLACK) ? WHITE : BLACK);
        //flip discs in the downward direction
        boolean[] directions = allPossibleMoves.get(key);

        if (directions[0]) {
            while (Objects.equals(board[position + 8], oppositePlayer)) {
                board[position + 8] = playerToMove;
                position += 8;
            }
        }

        position = getAbsPosition(col, row);
        //flip discs in the upward direction
        if (directions[1]) {
            while (Objects.equals(board[position - 8], oppositePlayer)) {
                board[position - 8] = playerToMove;
                position -= 8;
            }
        }
        position = getAbsPosition(col, row);
        //flip discs to the left
        if (directions[2]) {
            while (Objects.equals(board[position - 1], oppositePlayer)) {
                board[position - 1] = playerToMove;
                position -= 1;
            }
        }
        position = getAbsPosition(col, row);
        //flip discs to the right
        if (directions[3]) {
            while (Objects.equals(board[position + 1], oppositePlayer)) {
                board[position + 1] = playerToMove;
                position += 1;
            }
        }
        position = getAbsPosition(col, row);
        //flip discs to the down-right direction
        if (directions[4]) {
            while (Objects.equals(board[position + 9], oppositePlayer)) {
                board[position + 9] = playerToMove;
                position += 9;
            }
        }
        position = getAbsPosition(col, row);
        //flip discs to the up-left direction
        if (directions[5]) {
            while (Objects.equals(board[position - 9], oppositePlayer)) {
                board[position - 9] = playerToMove;
                position -= 9;
            }
        }
        position = getAbsPosition(col, row);
        //flip discs to the down-left direction
        if (directions[6]) {
            while (Objects.equals(board[position + 7], oppositePlayer)) {
                board[position + 7] = playerToMove;
                position += 7;
            }
        }
        position = getAbsPosition(col, row);
        //flip discs to the up-right direction
        if (directions[7]) {
            while (Objects.equals(board[position - 7], oppositePlayer)) {
                board[position - 7] = playerToMove;
                position -= 7;
            }
        }
    }
}
