import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartPage extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        VBox layout2 = new VBox();

        root.setAlignment(Pos.CENTER);
        layout2.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Image img = new Image("D:\\IntelliJ\\IntelliJ IDEA 2022.2.1\\Projects\\Asteroids\\res\\pixilart-drawing.png");
        BackgroundImage bImg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(WIDTH, HEIGHT, true, true, true, true)
        );
        Background background = new Background(bImg);
        root.setBackground(background);
        Scene scene2 = new Scene(layout2, WIDTH, HEIGHT);

        Label label1 = new Label("This is the First Scene");
        label1.setTextFill(Color.RED);
        Label label2 = new Label("This is the Second Scene");

        Button button = new Button("Forward");
        button.setOnAction(e -> primaryStage.setScene(scene2));

        Button button2 = new Button("Backwards");
        button2.setOnAction(e -> primaryStage.setScene(scene));

        TextField text = new TextField();
        text.setMaxWidth(100);

        root.getChildren().addAll(label1, button);
        layout2.getChildren().addAll(label2, button2, text);

        primaryStage.setTitle("CodersLegacy");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
