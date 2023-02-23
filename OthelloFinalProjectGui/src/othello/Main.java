package othello;

import java.util.Iterator;
import java.util.Scanner;
import position.XYLocation;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        String EMPTY = "-";
        String WHITE = "W";
        String BLACK = "B";
        String playerToMove = "B";
        String[] board = new String[64];

        for(int index = 0; index < board.length; ++index) {
            board[index] = EMPTY;
        }

        board[27] = BLACK;
        board[28] = BLACK;
        board[35] = BLACK;
        board[36] = BLACK;

        Scanner input = new Scanner(System.in);
        OthelloState state = new OthelloState(board, playerToMove);
        printValidMoves(state);
        System.out.println();
        printBoard(state);

        int blackScore;
        int whiteScore;
        while(!state.gameIsOver()) {
            System.out.println("enter column and row separated by one space");
            blackScore = input.nextInt();
            whiteScore = input.nextInt();
            state.mark(blackScore, whiteScore);
            printValidMoves(state);
            System.out.println();
            printBoard(state);
            System.out.print(displayScore(state));
            System.out.printf("number of marked position = %d%n", state.getNumberOfMarkedPositions());
            System.out.printf("is game over? %s%n", state.gameIsOver());
        }

        blackScore = state.getBlackScore();
        whiteScore = state.getWhiteScore();
        if (blackScore > whiteScore) {
            System.out.println("BLACK is the winner!");
        } else if (blackScore == whiteScore) {
            System.out.println("It's a tie");
        } else {
            System.out.println("White is the winner!");
        }

    }

    public static void printBoard(OthelloState state) {
        int row;
        for(row = 0; row < 8; ++row) {
            System.out.printf("%4d", row);
        }

        System.out.println();

        for(row = 0; row < 8; ++row) {
            System.out.printf("%d  ", row);

            for(int col = 0; col < 8; ++col) {
                System.out.printf("%-4s", state.getValue(col, row));
            }

            System.out.println();
        }

    }

    public static void printValidMoves(OthelloState state) {
        System.out.printf("It's %s's turn%n", state.getPlayerToMove() == "B" ? "black" : "white");
        System.out.println("valid moves: ");
        Iterator var1 = state.allValidMoves().iterator();

        while(var1.hasNext()) {
            XYLocation moves = (XYLocation)var1.next();
            System.out.printf("%s ", moves);
        }

        System.out.println();
    }

    public static String displayScore(OthelloState state) {
        return String.format("Black score = %d White score = %d%n", state.getBlackScore(), state.getWhiteScore());
    }
}
