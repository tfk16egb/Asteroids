package View;

import Model.BackgroundImageConverter;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import static View.AsteroidsApplication.*;

import javafx.stage.Stage;

public class ScoreboardScene {
    private final Stage stage;

    private final Scene scene;

    private final BorderPane borderPane;

    public ScoreboardScene(Stage stage, BackgroundImageConverter bImgConverter) {
        this.stage = stage;
        Button backToMenu = new Button("Back");
        //backToMenu.setFont(FONT);


        ListView listView = new ListView<>();
        listView.setStyle("-fx-background-color: transparent");

        for (int i = 1; i <= 10; i++) {

            listView.getItems().add("Item[" + i + "]");
        }
        listView.setMaxHeight(250);
        listView.setMaxWidth(200);


        borderPane = new BorderPane();
        borderPane.setLeft(backToMenu);
        borderPane.setCenter(listView);
        borderPane.setBackground(bImgConverter.getBackground("scoreboard.png"));
        scene = new Scene(borderPane, WIDTH, HEIGHT);
        scene.getStylesheets().add("style/scoreboard.css");

        backToMenu.setOnAction(e -> switchScene(new MenuScene(stage, bImgConverter).getScene()));
    }

    private void switchScene(Scene scene) {
        stage.setScene(scene);
    }

    public Scene getScene() {
        return this.scene;
    }
}
