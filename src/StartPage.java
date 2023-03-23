import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class StartPage extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 600;
    private Image startImg = new Image("startscreen.png");
    private Image playImg = new Image("playgame.png");

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        VBox layout2 = new VBox();

        //root.setAlignment(Pos.CENTER);
        //layout2.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        BackgroundImage bImg = new BackgroundImage(
                startImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(WIDTH, HEIGHT, true, true, true, true)
        );
        Background startBImg = new Background(bImg);
        root.setBackground(startBImg);

        Scene scene2 = new Scene(layout2, WIDTH, HEIGHT);
        BackgroundImage bImg0 = new BackgroundImage(
                playImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(WIDTH, HEIGHT, true, true, true, true)
        );
        Background playBImg = new Background(bImg0);
        layout2.setBackground(playBImg);

        Button startBtn = new Button("START");
        startBtn.setTranslateY(-45);
        startBtn.setOnAction(e -> primaryStage.setScene(scene2));

        Button backwards = new Button("Backwards");
        backwards.setTranslateY(10);
        backwards.setTranslateX(10);
        backwards.setOnAction(e -> primaryStage.setScene(scene));

        TextField text = new TextField();
        text.setMaxWidth(100);

        root.getChildren().addAll(startBtn);
        layout2.getChildren().addAll(backwards, text);

        primaryStage.setTitle("Asteroid Shooter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
