package View;

import Controller.GameController;
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

        MainGameScene mainGame = new MainGameScene(controller);
        mainGame.startAnimation();

        stage.setTitle("Asteroids!");
        stage.setScene(mainGame.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
