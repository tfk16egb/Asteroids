package View;

import Controller.GameController;
import Model.BackgroundImageConverter;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static View.AsteroidsApplication.*;

public class MainGameScene {

    private GameController controller;
    private Scene gameScene;
    private Pane gamePane;
    private Text text;
    private AtomicInteger points;
    private static Font font = Font.loadFont("file:res/retro_computer_personal_use.ttf", 40);
    private Stage stage;

    private void onCollidedObjectsChanged(List<Node> nodesToRemove) {
        gamePane.getChildren().removeAll(nodesToRemove);
    }

    public void onAddObjects(List<Node> nodesToAdd) {
        nodesToAdd.forEach(node -> {
            gamePane.getChildren().add(node);
        });
        text.toFront();
    }

    public MainGameScene(GameController controller, Stage stage) {
        this.stage = stage;
        gamePane = new Pane();
        text = new Text(30, 60, "0");
        text.setFill(Color.GREY);
        text.setFont(font);
        points = new AtomicInteger();
        this.controller = controller;
        controller.setCollisionCallback(this::onCollidedObjectsChanged);
        controller.setAddObjectCallback(this::onAddObjects);

        controller.removeProjectiles();

        gamePane.setPrefSize(WIDTH, HEIGHT);
        gamePane.setStyle("-fx-background-color: black;");

        gamePane.getChildren().add(controller.getShip().getNode());
        gamePane.getChildren().add(text);
        controller.addAllProjectiles();

        gameScene = new Scene(gamePane);

    }

    public void startAnimation() {
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        gameScene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        gameScene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

        new AnimationTimer() {
            long lastShotTime = 0;
            final long COOLDOWN_PERIOD = 150_000_000; // 150 milliseconds in nanoseconds
            final int MAX_PROJECTILES = 4;
            int shotsFired = 0;
            @Override
            public void handle(long now) {
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    controller.getShip().turnLeft();
                }

                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    controller.getShip().turnRight();
                }

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    controller.getShip().accelerate();
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false)) {
                    if (now - lastShotTime >= COOLDOWN_PERIOD && shotsFired < MAX_PROJECTILES) {
                        controller.shootProjectile();
                        lastShotTime = now;
                        shotsFired++;
                    }
                } else {
                    shotsFired = 0;
                }
                controller.getShip().move();
                controller.moveAsteroid();
                controller.moveProjectile();

                points.addAndGet(controller.calculateScore());
                text.setText("" + points.intValue());


                controller.removeProjectileThatHitAstroids();
                controller.updateParicle();

               // controller.addAsteroidAtRandom();

                if (controller.gameOver()) {
                    Text restart = new Text(130, 380, "PRESS R TO RESTART\nPRESS B FOR MAIN MENU");
                    Text gameOverText = new Text(130, 250, "GAME OVER\nScore: " + points);
                    gameOverText.setFont(font);
                    gameOverText.setStyle("-fx-font-size: 55px");
                    gameOverText.setFill(Color.WHITE);
                    restart.setFont(font);
                    restart.setStyle("-fx-font-size: 25px");
                    restart.setFill(Color.WHITE);
                    gamePane.getChildren().add(gameOverText);
                    gamePane.getChildren().add(restart);
                    stop();
                    gameOverAnimation();
                }
            }
        }.start();
    }

    private void gameOverAnimation(){
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        gameScene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        gameScene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

        new AnimationTimer(){
            @Override
            public void handle(long now) {
                if (pressedKeys.getOrDefault(KeyCode.R, false)) {
                    startAnimation();
                    initGame();
                    stop();
                }

                if (pressedKeys.getOrDefault(KeyCode.B, false)) {
                    BackgroundImageConverter bImgConverter =
                            new BackgroundImageConverter(WIDTH, HEIGHT,
                                    "startscreen.png",
                                    "playgame.png",
                                    "scoreboard.png");
                    switchScene(new MenuScene(stage, bImgConverter).getScene());
                    stop();
                }
            }
        }.start();
    }

    public void initGame(){
        gamePane.getChildren().clear();
        controller = new GameController();
        controller.setCollisionCallback(this::onCollidedObjectsChanged);
        controller.setAddObjectCallback(this::onAddObjects);
        controller.removeProjectiles();
        gamePane.getChildren().add(controller.getShip().getNode());
        gamePane.getChildren().add(text);
        controller.addAllProjectiles();
        points.set(0);
        text.setText("");
    }

    public void switchScene(Scene scene){
        stage.setScene(scene);
    }

    public Scene getScene() {
        return gameScene;
    }

}