import Factory.BackgroundImageFactory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class StartPage extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    private Button startBtn, scoreBtn, playGameBtn, mainMenuBtn_1, mainMenuBtn_2;
    private BackgroundImageFactory bImgFactory;
    private GridPane mainPane, playPane, scorePane;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        bImgFactory = new BackgroundImageFactory(WIDTH, HEIGHT);
        bImgFactory.loadImages(
                "playgame.png",
                "startscreen.png",
                "scoreboard.png");

        Scene mainScene = getMainMenuScene();
        Scene playScene = getPlayScene();
        Scene scoreScene = getScoreScene();

        mainMenuBtn_1.setOnAction(e -> primaryStage.setScene(mainScene));
        mainMenuBtn_2.setOnAction(e -> primaryStage.setScene(mainScene));
        startBtn.setOnAction(e -> primaryStage.setScene(playScene));
        scoreBtn.setOnAction(e -> primaryStage.setScene(scoreScene));


        playPane.getChildren().add(mainMenuBtn_1);
        scorePane.getChildren().add(mainMenuBtn_2);

        primaryStage.setTitle("Asteroid Shooter");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private Scene getMainMenuScene() {
        startBtn = new Button("START");
        scoreBtn = new Button("SCOREBOARD");
        scoreBtn.setTranslateY(20);
        mainPane = new GridPane();
        mainPane.setHgap(10);
        mainPane.setVgap(10);
        mainPane.setPadding(new Insets(0, 20, 0, 20));
        mainPane.add(startBtn, 20, 20);
        mainPane.add(scoreBtn, 20, 25);

        Scene mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainPane.setBackground(bImgFactory.getBackground("startscreen.png"));

        return mainScene;
    }

    private Scene getPlayScene() {
        playPane = new GridPane();
        playGameBtn = new Button("PLAY");
        playGameBtn.setTranslateY(20);
        mainMenuBtn_1 = new Button("MAIN MENU");

        Scene playScene = new Scene(playPane, WIDTH, HEIGHT);
        playPane.setBackground(bImgFactory.getBackground("playgame.png"));

        return playScene;
    }

    private Scene getScoreScene() {
        scorePane = new GridPane();
        mainMenuBtn_2 = new Button("MAIN MENU");
        Scene scoreScene = new Scene(scorePane, WIDTH, HEIGHT);
        scorePane.setBackground(bImgFactory.getBackground("scoreboard.png"));

        return scoreScene;
    }
}
