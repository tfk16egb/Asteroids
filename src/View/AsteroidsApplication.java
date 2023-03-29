package View;

import Controller.DatabaseController;
import Controller.GameController;
import Model.BackgroundImageConverter;
import javafx.application.Application;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    public static int WIDTH = 600;
    public static int HEIGHT = 600;
    //public static Font FONT = Font.loadFont("file:res/style/retro.ttf", 40);
    private GameController controller;


    @Override
    public void start(Stage stage){
        BackgroundImageConverter bImgConverter =
                new BackgroundImageConverter(WIDTH, HEIGHT,
                        "startscreen.png",
                        "playgame.png",
                        "scoreboard.png");


        MenuScene menuScene = new MenuScene(stage, bImgConverter);
        stage.setTitle("Asteroid Shooter");
        stage.setScene(menuScene.getScene());
        stage.setResizable(false);
        stage.show();
    }

    public AsteroidsApplication(GameController controller){
        this.controller = controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}