package View;

import Controller.DatabaseController;
import Controller.GameController;
import Model.BackgroundImageConverter;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static View.AsteroidsApplication.*;


public class InsertNameScene {
    private final Stage stage;
    private final Scene scene;
    private final DatabaseController db;
    private final Pane pane;


    public InsertNameScene(Stage stage, BackgroundImageConverter bImgConverter) {
        this.stage = stage;
        this.db = new DatabaseController();
        pane = new Pane();

        Button backToMenu = new Button("Back");
        backToMenu.setAlignment(Pos.TOP_LEFT);
        backToMenu.setId("mainMenu");

        Button play = new Button("Play");
        play.setTranslateY(320);
        play.setTranslateX(250);
        play.setPrefSize(90,30);
        TextField textField = new TextField();

        textField.setTranslateY(270);
        textField.setTranslateX(180);


        pane.getChildren().addAll(backToMenu, play, textField);
        pane.setBackground(bImgConverter.getBackground("playgame.png"));
        scene = new Scene(pane, WIDTH, HEIGHT);
        scene.getStylesheets().add("InsertName.css");

        backToMenu.setOnAction(e -> {
            switchScene(new MenuScene(stage, bImgConverter).getScene());
        });


        play.setOnAction(e -> {
            db.registerPlayer(textField.getText());
            MainGameScene mainGame = new MainGameScene(new GameController(), stage);
            switchScene(mainGame.getScene());
            mainGame.startAnimation();

        });


    }

    private void switchScene(Scene scene) {
        stage.setScene(scene);
    }

    public Scene getScene() {
        return this.scene;
    }
}
