import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AsteroidsApplication extends Application {

    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        Text text = new Text(10, 20, "Score: 0");
        text.setFill(Color.WHITE);
        AtomicInteger points = new AtomicInteger();

        List<Asteroid> asteroids = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        List<Projectile> projectilesToRemove = projectiles.stream().filter(projectile -> {
            List<Asteroid> collisions = asteroids.stream()
                    .filter(asteroid -> asteroid.collide(projectile))
                    .collect(Collectors.toList());

            if(collisions.isEmpty()) {
                return false;
            }

            collisions.stream().forEach(collided -> {
                asteroids.remove(collided);
                pane.getChildren().remove(collided.getCharacter());
            });

            return true;
        }).collect(Collectors.toList());

        projectilesToRemove.forEach(projectile -> {
            pane.getChildren().remove(projectile.getCharacter());
            projectiles.remove(projectile);
        });

        pane.setPrefSize(WIDTH, HEIGHT);
        pane.setStyle("-fx-background-color: black;");

        Ship ship = new Ship(WIDTH/2, HEIGHT/2);
        for (int i = 0; i < 8; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH/3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }

        pane.getChildren().add(ship.getCharacter());
        pane.getChildren().add(text);
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));



        Scene scene = new Scene(pane);

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {


                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if(pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if(pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 10) {
                    // we shoot
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(5));

                    pane.getChildren().add(projectile.getCharacter());
                }
                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if(projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
                    if(!projectile.isAlive()) {
                        text.setText("Points: " + points.addAndGet(100));
                    }
                });

                projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));
                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));

                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .collect(Collectors.toList()));
                projectiles.forEach(projectile -> {
                    List<Asteroid> collisions = asteroids.stream()
                            .filter(asteroid -> asteroid.collide(projectile))
                            .collect(Collectors.toList());

                    collisions.stream().forEach(collided -> {
                        asteroids.remove(collided);
                        pane.getChildren().remove(collided.getCharacter());
                    });
                });
                if(Math.random() < 0.05) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if(!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }

                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        Text gameOverText = new Text(WIDTH /4, HEIGHT /2, "GAME OVER\nScore: " + points);
                        gameOverText.setStyle("-fx-font-size:400px");
                        gameOverText.setStyle("-fx-font-family: 'Courier New'");
                        gameOverText.setStyle("-fx-text-alignment: 'center'");
                        gameOverText.setFill(Color.RED);
                        pane.getChildren().add(gameOverText);
                        stop();
                    }
                });
            }
        }.start();

        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
