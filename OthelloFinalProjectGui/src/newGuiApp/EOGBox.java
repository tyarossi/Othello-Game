package newGuiApp;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class EOGBox extends Main {


    public static void display(String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);


        Label label = new Label();
        label.setText(message);

        Button reset = new Button("Reset");
        reset.setOnAction(e -> {
            createNewBoard();
            refreshView();

            window.close();
        });

        VBox layout = new VBox(10);
            layout.getChildren().addAll(label, reset);
            layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

}
