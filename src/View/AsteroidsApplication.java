package View;

import Controller.GameController;
import Model.BackgroundImageConverter;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    public static int WIDTH = 600;
    public static int HEIGHT = 600;
    //public static Font FONT = Font.loadFont("file:res/style/retro.ttf", 40);
    private GameController controller;

    public AsteroidsApplication(GameController controller){
        this.controller = controller;
    }

    @Override
    public void start(Stage stage){
        BackgroundImageConverter bImgConverter =
                new BackgroundImageConverter(WIDTH, HEIGHT,
                        "startscreen.png",
                        "playgame.png",
                        "scoreboard.png");


        MenuScene menuScene = new MenuScene(stage, bImgConverter);
        stage.setTitle("Asteroids!");
        stage.setScene(menuScene.getScene());
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}