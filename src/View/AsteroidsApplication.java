package View;

import Controller.GameController;
import Model.BackgroundImageConverter;
import javafx.application.Application;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    private GameController controller;

    public AsteroidsApplication(GameController controller){
        this.controller = controller;
    }

    @Override
    public void start(Stage stage) throws Exception {
        BackgroundImageConverter bImgConverter =
                new BackgroundImageConverter(WIDTH, HEIGHT,
                        "startscreen.png",
                        "playgame.png",
                        "scoreboard.png");
        //MainGameScene mainGame = new MainGameScene(controller);
        //mainGame.startAnimation();
        MenuScene menuScene = new MenuScene(stage, bImgConverter);

        stage.setTitle("Asteroids!");
        //stage.setScene(mainGame.getScene());
        stage.setScene(menuScene.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}