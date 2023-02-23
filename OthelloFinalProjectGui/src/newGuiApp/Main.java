package newGuiApp;

import adversarialSearch.Game;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import metrics.Metrics;
import othello.OthelloGame;
import othello.OthelloState;
import position.XYLocation;
import adversarialSearch.AdversarialSearch;
import adversarialSearch.MinimaxSearch;

import java.util.*;

public class Main extends Application {

    private final static int PIECE_LENGTH = 8;
    private final static int PIECE_SIZE = 30;
    private static boolean ENDOFGAME = false;

    //Board Game Node Map
    private final static Map<Integer, Button> boardPieces = new HashMap<>();
    private final static Map<Button, Coordinate> boardCoordinates = new HashMap<>();

    //othello logic
    private static String[] board;
    private static GridPane othelloBoard;
    private static Label blackScores;
    private static Label whiteScores;

    private static OthelloState state;
    private static Metrics searchMetrics;
    private static OthelloGame game;

    private final static String WHITE = "W";
    private final static String BLACK = "B";
    private final static String EMPTY = "-";

    static void createNewBoard(){
        System.out.println("Resetting Game");
        board = new String[64];
        for(int i = 0; i < board.length; i++){
            board[i] = EMPTY;
        }
        board[27] = WHITE;
        board[28] = BLACK;
        board[35] = BLACK;
        board[36] = WHITE;
        state = new OthelloState(board, BLACK);
    }



    // board setter getters
    private static int arrayMemoryHelper(int x, int y) {
        return x + y * PIECE_LENGTH;
    }

    private static void setBoardPiece(int x, int y, Button piece) {
        int memoryMapping = arrayMemoryHelper(x, y);
        boardPieces.put(memoryMapping, piece);
    }

    private static Button getBoardPiece(int x, int y) {
        int memoryMapping = arrayMemoryHelper(x, y);
        return boardPieces.get(memoryMapping);
    }

    private static void setPieceCoordinate(Coordinate c,  Button piece) {
        boardCoordinates.put(piece, c);
    }

    private static Coordinate getPieceCoordinate(Button piece) {
        return boardCoordinates.get(piece);
    }


    private static Color stringToColor(String color) {
        switch (color.toLowerCase()) {
            case "-":
                return Color.TRANSPARENT;
            case "w":
                return Color.WHITE;
            case "b":
                return Color.BLACK;
            default:
                throw new RuntimeException("Color conversion not supported!");
        }
    }

    private static void setPieceColor(int x, int y, String color) {
        Color convertedColor = stringToColor(color);
        Button piece = getBoardPiece(x, y);
        Circle newColor = new Circle(PIECE_SIZE, convertedColor);
        piece.setGraphic(newColor);
    }


    static void refreshView() {
        blackScores.setText(Integer.toString(state.getBlackScore()));
        whiteScores.setText(Integer.toString(state.getWhiteScore()));



        for (int y = 0; y < PIECE_LENGTH; y++) {
            for (int x = 0; x < PIECE_LENGTH; x++) {
                int loc = arrayMemoryHelper(x, y);
//                Button currentPiece = boardPieces.get(loc);

                //example string of piece
                String colorFromOthelloState = state.getValue(y, x);
                setPieceColor(x, y, colorFromOthelloState);
            }
        }
        othello.Main.printBoard(state);
        if (state.gameIsOver() || ENDOFGAME == true){
            if (state.getBlackScore() > state.getWhiteScore()){
                ENDOFGAME = false;
                EOGBox.display("Game Over", "Black Wins!\nWould you like to play again?");
                refreshView();
            }else if (state.getWhiteScore() > state.getBlackScore()){
                ENDOFGAME = false;
                EOGBox.display("Game Over", "White Wins!\nWould you like to play again?");
                refreshView();
            }else {
                ENDOFGAME = false;
                EOGBox.display("game Over", "Tie Game!\n Wyould you like to play again?");
                refreshView();
            }
        }
    }

    private static void setTransparent(Button button) {
        BackgroundFill blankBackground = new BackgroundFill(Color.TRANSPARENT, null,null);
        Background background = new Background(blankBackground);
        button.setBackground(background);
    }

    private static Button createCirclePiece(int radius, Color color) {
        Circle circlePiece = new Circle(radius, color);
        Button buttonPiece = new Button("", circlePiece);
        Main.setTransparent(buttonPiece);
        return buttonPiece;
    }

    private static void addCirclePieceToBoard(int x, int y, Color color,  GridPane board) {
        Button circlePiece = Main.createCirclePiece(PIECE_SIZE, color);
        setBoardPiece(x, y, circlePiece);
        Coordinate loc = new Coordinate(x, y);
        setPieceCoordinate(loc, circlePiece);

        // Piece Action
        circlePiece.setOnAction(e -> {
           Button currentPiece = (Button) e.getTarget();
           Coordinate coordinate = boardCoordinates.get(currentPiece);
           System.out.println(coordinate);
           int row = coordinate.getY();
           int col = coordinate.getX();
           System.out.println("is game over? " + state.gameIsOver());
           state.mark(row, col);

           refreshView();

        });

        board.add(circlePiece, x, y);
    }


    private static void initializeBoard(GridPane board) {
        game = new OthelloGame();

        System.out.println("Initializing board");
        for (int y = 0; y < PIECE_LENGTH; y++) {
            for (int x = 0; x < PIECE_LENGTH; x++) {
                addCirclePieceToBoard(x, y, Color.TRANSPARENT,  board);
            }
        }
        createNewBoard();
        refreshView();
    }

//    private static void popup(board board){
//        main.initializeBoard(board);
//    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Othello");

        //Create Board

        othelloBoard = new GridPane();
        othelloBoard.setPadding(new Insets(10, 10, 10, 10));



        // reset button
        Button reset = new Button("     Reset    ");
        othelloBoard.add(reset, 1, 8);

        reset.setOnAction(e -> {
                createNewBoard();
                refreshView();
        });

        Button help = new Button("     Help      ");
        othelloBoard.add(help, 7, 8);

        help.setOnAction(e -> {
            HelpBox.display("help", "Each piece played must be laid adjacent to an opponent's piece so that the opponent's piece\nor a row of opponent's pieces is flanked by the new piece and another piece of the player's colour.\nAll of the opponent's pieces between these two pieces are 'captured' and turned over to match the player's colour.\n");

        });




        StackPane bgPane = new StackPane();
        bgPane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        StackPane bg2Pane = new StackPane();
        bg2Pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        StackPane bg3Pane = new StackPane();
        bg3Pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        StackPane bg4Pane = new StackPane();
        bg4Pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        StackPane bg5Pane = new StackPane();
        bg5Pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        othelloBoard.add(bgPane, 0, 8);
        othelloBoard.add(bg2Pane, 3, 8);
        othelloBoard.add(bg3Pane, 4, 8);
        othelloBoard.add(bg4Pane, 5, 8);
        othelloBoard.add(bg5Pane, 6, 8);

        Label blackScoreView = new Label("Black: ");
        Label whiteScoreView = new Label("White: ");
        othelloBoard.add(blackScoreView, 3, 8);
        othelloBoard.add(whiteScoreView, 5, 8);

        othelloBoard.setHalignment(blackScoreView, HPos.CENTER);
        othelloBoard.setHalignment(whiteScoreView, HPos.CENTER);

        blackScores = new Label();
        whiteScores = new Label();

        othelloBoard.add(blackScores, 4, 8);
        othelloBoard.add(whiteScores, 6, 8);

        blackScores.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        othelloBoard.setHalignment(blackScores, HPos.CENTER);
        othelloBoard.setHalignment(whiteScores, HPos.CENTER);

        Button endGame = new Button(" End Game ");
        othelloBoard.add(endGame, 2, 8);

        endGame.setOnAction(e -> {
            ENDOFGAME = true;
        });

        //ai move button


        othelloBoard.setGridLinesVisible(true);
        Background bg = new Background(new BackgroundFill(Color.GREEN, null, null));
        othelloBoard.setBackground(bg);
        Main.initializeBoard(othelloBoard);

        Button aiMove = new Button("   AI Move  ");
        othelloBoard.add(aiMove, 0, 8);

        aiMove.setOnAction(e -> {
            AdversarialSearch<OthelloState, XYLocation> search = MinimaxSearch.createFor(Main.game);
            if(search == null){
                System.out.println("Search is null");
            }
            XYLocation action = (XYLocation) search.makeDecision(state);
            this.searchMetrics = search.getMetrics();
            this.state = this.game.getResult(this.state, action);
            refreshView();
        });



        // GridPane.setConstraints();

        // Set Viewable
        primaryStage.setScene(new Scene(othelloBoard));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
