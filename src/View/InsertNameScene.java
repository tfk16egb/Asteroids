package View;

import Controller.GameController;
import Model.BackgroundImageConverter;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static View.AsteroidsApplication.*;


public class InsertNameScene {
    private final Stage stage;
    private final Scene scene;
    private final BorderPane borderPane;

    public InsertNameScene(Stage stage, BackgroundImageConverter bImgConverter) {
        this.stage = stage;
        Button backToMenu = new Button("Back");
        Button play = new Button("Play");
        TextArea textArea = new TextArea();
        textArea.setPrefColumnCount(10);
        borderPane = new BorderPane();
        borderPane.setCenter(textArea);
        borderPane.setCenter(play);
        borderPane.setLeft(backToMenu);
        borderPane.setBackground(bImgConverter.getBackground("playgame.png"));
        scene = new Scene(borderPane, WIDTH, HEIGHT);

        backToMenu.setOnAction(e -> {
            switchScene(new MenuScene(stage, bImgConverter).getScene());
        });
        play.setOnAction(e -> {
            MainGameScene mainGame = new MainGameScene(new GameController());
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
