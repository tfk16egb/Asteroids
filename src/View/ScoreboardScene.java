package View;

import Controller.DatabaseController;
import Model.BackgroundImageConverter;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import static View.AsteroidsApplication.*;

import javafx.stage.Stage;
import org.json.simple.JSONObject;

public class ScoreboardScene {
    private final Stage stage;

    private final Scene scene;

    private final BorderPane borderPane;

    private final DatabaseController db;

    public ScoreboardScene(Stage stage, BackgroundImageConverter bImgConverter) {
        this.stage = stage;
        this.db = new DatabaseController();
        Button backToMenu = new Button("Back");



        ListView listView = new ListView<>();
        db.getAll().forEach(json -> {
            listView.getItems().add(json.get("Points") + ": " + json.get("Name"));
        });



        listView.setMaxHeight(250);
        listView.setMaxWidth(300);


        borderPane = new BorderPane();
        borderPane.setLeft(backToMenu);
        borderPane.setCenter(listView);
        borderPane.setBackground(bImgConverter.getBackground("scoreboard.png"));
        scene = new Scene(borderPane, WIDTH, HEIGHT);
        scene.getStylesheets().add("Scoreboard.css");

        backToMenu.setOnAction(e -> switchScene(new MenuScene(stage, bImgConverter).getScene()));
    }

    private void switchScene(Scene scene) {
        stage.setScene(scene);
    }

    public Scene getScene() {
        return this.scene;
    }
}
