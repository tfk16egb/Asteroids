import Factory.BackgroundImageFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;



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
        BackgroundImageFactory bImgFactory = new BackgroundImageFactory(WIDTH, HEIGHT);
        bImgFactory.loadImages(
                "playgame.png",
                "startscreen.png",
                "scoreboard.png");

        Button startBtn = new Button("START");
        Button playGameBtn = new Button("PLAY");
        playGameBtn.setTranslateY(20);
        Button mainMenuBtn = new Button("MAIN MENU");
        Button scoreBtn = new Button("SCOREBOARD");
        scoreBtn.setTranslateY(20);

        TextField text = new TextField();
        text.setMaxWidth(100);


        StackPane mainPane = new StackPane();
        StackPane playPane = new StackPane();
        StackPane scorePane = new StackPane();


        //Main menu setup
        Scene mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainPane.setBackground(bImgFactory.getBackground("startscreen.png"));

        //Insert name and play menu setup
        Scene playScene = new Scene(playPane, WIDTH, HEIGHT);
        playPane.setBackground(bImgFactory.getBackground("playgame.png"));

        //Scoreboard menu setup
        Scene scoreScene = new Scene(scorePane,WIDTH, HEIGHT);
        scorePane.setBackground(bImgFactory.getBackground("scoreboard.png"));


        startBtn.setOnAction(e -> primaryStage.setScene(playScene));
        mainMenuBtn.setOnAction(e -> primaryStage.setScene(mainScene));
        scoreBtn.setOnAction(e -> primaryStage.setScene((scoreScene)));

        mainPane.getChildren().addAll(startBtn, scoreBtn);
        playPane.getChildren().addAll(mainMenuBtn, text, playGameBtn);
        scorePane.getChildren().add(mainMenuBtn);

        primaryStage.setTitle("Asteroid Shooter");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
