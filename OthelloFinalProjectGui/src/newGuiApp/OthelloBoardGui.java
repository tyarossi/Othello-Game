 package newGuiApp;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OthelloBoardGui extends Application {
    //global variables
    public static final String BOARD_NAME = "Othello";
    public static final int PIECE_LENGTH = 8;
    public static final int PIECE_SIZE = 20;
    private final static Color TRANSPARENT = new Color(0, 0, 0, 0);

    class OthelloPane extends GridPane{
        private int boardSize;
        private int boxSize;



        public OthelloPane(int boardSize, int buttonSize){
            for (int i  = 0; i < boardSize; i++){
                getRowConstraints().add(new RowConstraints(boxSize));
            }
            for (int i = 0; i< boardSize; i++){
                getColumnConstraints().add( new ColumnConstraints(boxSize));
            }
            for (int i = 0; i < boardSize; i++){
                for (int j = 0; j < boardSize; j++){
                    Pane box = new Pane();
                    box.setStyle("-fx-background-color: " + Color.GREEN + ";");
                }
            }
        }
    }

    //board

    //pieces



    @Override
    public void start(Stage primaryStage) {

    }
}
