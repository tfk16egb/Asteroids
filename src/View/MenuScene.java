package View;

import Controller.DatabaseController;
import Model.BackgroundImageConverter;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static View.AsteroidsApplication.*;

public class MenuScene {
    private Stage stage;
    private Scene scene;
    private GridPane gridPane;
    private static final double BTN_WIDTH = 120;
    private static final double BTN_HEIGHT = 60;

    public MenuScene(Stage stage, BackgroundImageConverter bImgConverter) {
        dbInit();
        this.stage = stage;
        Button start = new Button("Start");
        Button scoreboard = new Button("Score");
        start.setPrefSize(BTN_WIDTH, BTN_HEIGHT);
        scoreboard.setPrefSize(BTN_WIDTH, BTN_HEIGHT);
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0,10,0,10));
        gridPane.add(start,22,21);
        gridPane.add(scoreboard,22,24);
        gridPane.setBackground(bImgConverter.getBackground("startscreen.png"));
        scene = new Scene(gridPane,WIDTH, HEIGHT );
        scene.getStylesheets().add("MainMenu.css");

        start.setOnAction(e -> {
            InsertNameScene insertNameScene = new InsertNameScene(stage, bImgConverter);
            switchScene(insertNameScene.getScene());
        });
        scoreboard.setOnAction(e ->{
            ScoreboardScene scoreboardScene = new ScoreboardScene(stage, bImgConverter);
            switchScene(scoreboardScene.getScene());
        });
    }

    private void dbInit(){
        DatabaseController db = new DatabaseController();
    }

    private void switchScene(Scene scene){
        stage.setScene(scene);
    }

    public Scene getScene(){
        return this.scene;
    }
}
