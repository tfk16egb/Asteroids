package View;

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


    public MenuScene(Stage stage, BackgroundImageConverter bImgConverter) {
        this.stage = stage;
        Button start = new Button("START");
        Button scoreboard = new Button("Score");
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0,10,0,10));
        gridPane.add(start,20,20);
        gridPane.add(scoreboard,20,25);
        gridPane.setBackground(bImgConverter.getBackground("startscreen.png"));
        scene = new Scene(gridPane,WIDTH, HEIGHT );

        start.setOnAction(e -> {
            InsertNameScene insertNameScene = new InsertNameScene(stage, bImgConverter);
            switchScene(insertNameScene.getScene());
        });
        scoreboard.setOnAction(e ->{
            ScoreboardScene scoreboardScene = new ScoreboardScene(stage, bImgConverter);
            switchScene(scoreboardScene.getScene());
        });
    }

    private void switchScene(Scene scene){
        stage.setScene(scene);
    }

    public Scene getScene(){
        return this.scene;
    }
}
