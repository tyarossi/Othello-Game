package othello;

import adversarialSearch.MinimaxSearch;
import java.util.Iterator;
import java.util.Scanner;
import position.XYLocation;

public class OthelloDemo {
    public OthelloDemo() {
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("MINI MAX DEMO\n");
        OthelloGame game = new OthelloGame();
        OthelloState currState = game.getInitialState();
        printValidMoves(currState);
        System.out.println();
        printBoard(currState);
        MinimaxSearch search = MinimaxSearch.createFor(game);

        while(!game.isTerminal(currState)) {
            XYLocation action = (XYLocation)search.makeDecision(currState);
            currState.mark(action);
            printBoard(currState);
            printValidMoves(currState);
            System.out.println("enter column and row separated by one space");
            int col = input.nextInt();
            int row = input.nextInt();
            currState.mark(col, row);
            printBoard(currState);
        }

        System.out.println("MINI MAX DEMO done");
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
}
