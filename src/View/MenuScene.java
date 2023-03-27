package View;

import Controller.GameController;
import Model.BackgroundImageConverter;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static View.AsteroidsApplication.*;

public class MenuScene {
    private Stage stage;
    private Scene menuScene;
    private GridPane menuPane;

    public MenuScene(Stage stage, BackgroundImageConverter bImgConverter) {
        this.stage = stage;
        Button start = new Button("START");
        Button scoreboard = new Button("Score");
        menuPane = new GridPane();
        menuPane.setHgap(10);
        menuPane.setVgap(10);
        menuPane.setPadding(new Insets(0,10,0,10));
        menuPane.add(start,20,20);
        menuPane.add(scoreboard,20,25);
        menuPane.setBackground(bImgConverter.getBackground("startscreen.png"));
        menuScene = new Scene(menuPane,WIDTH, HEIGHT );

        start.setOnAction(e -> {
            MainGameScene mainGame = new MainGameScene(new GameController());
            switchScene(mainGame.getScene());
            mainGame.startAnimation();
        });
    }

    private void switchScene(Scene scene){
        stage.setScene(scene);
    }

    public Scene getScene(){
        return this.menuScene;
    }
}
