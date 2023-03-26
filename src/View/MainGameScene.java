package View;

import Controller.GameController;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static View.AsteroidsApplication.*;

public class MainGameScene{

    private GameController controller;
    private Scene gameScene;
    private Pane  gamePane;
    private Text text;
    private AtomicInteger points;

    private void onCollidedObjectsChanged(List<Node> nodesToRemove) {
        gamePane.getChildren().removeAll(nodesToRemove);
    }

    public void onAddObjects(List<Node> nodesToAdd){
        nodesToAdd.forEach(node -> gamePane.getChildren().add(node));
    }
    public MainGameScene(GameController controller){
        gamePane = new Pane();
        text = new Text(10, 20, "Score: 0");
        text.setFill(Color.WHITE);
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

    public void startAnimation(){
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        gameScene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        gameScene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

        new AnimationTimer() {
            long lastShotTime = 0;
            int shotsFired = 0; // added counter
            @Override
            public void handle(long now) {

                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    controller.getShip().turnLeft();
                }

                if(pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    controller.getShip().turnRight();
                }

                if(pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    controller.getShip().accelerate();
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && now - lastShotTime >= 150_000_000 && shotsFired < 4) {
                    // we shoot
                    lastShotTime = now;
                    controller.shootProjectile();
                    shotsFired++; // increment counter
                } else if (!pressedKeys.getOrDefault(KeyCode.SPACE, false)) {
                    shotsFired = 0; // reset counter when space is released
                }
                controller.getShip().move();
                controller.moveAsteroid();
                controller.moveProjectile();

                points.addAndGet(controller.calculateScore());
                text.setText("Points: " + points.intValue());

                controller.removeProjectileThatHitAstroids()    ;
                controller.addAsteroidAtRandom();

                if(controller.gameOver()){
                    Text gameOverText = new Text( 50, 200, "GAME OVER\nScore: " + points);
                    gameOverText.setStyle("-fx-font-size: 100px");


                    gameOverText.setFill(Color.RED);
                    gamePane.getChildren().add(gameOverText);
                    stop();
                }
            }
        }.start();
    }

    public Scene getScene(){
        return gameScene;
    }

}
