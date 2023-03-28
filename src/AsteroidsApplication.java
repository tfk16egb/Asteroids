import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.InstantSource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


import javafx.util.Duration;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


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
        List<EnemyShip> enemyShips = new ArrayList<>();
        List<EnemyProjectile> enemyProjectiles = new ArrayList<>();



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
        Random rand = new Random();
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
        Timer enemySpawnTimer = new Timer();
        enemySpawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    EnemyShip enemyShip = new EnemyShip(rand.nextInt(HEIGHT), rand.nextInt(WIDTH));
                    enemyShips.add(enemyShip);
                    pane.getChildren().add(enemyShip.getCharacter());

                });
            }
        }, 5_000, 5_000);

        new AnimationTimer() {
            long lastShotTime = 0;
            final long COOLDOWN_PERIOD = 300_000_000; // 150 milliseconds in nanoseconds
            final int MAX_PROJECTILES = 4;
            int shotsFired = 0;
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
                enemyShips.forEach(enemyShip -> {
                    enemyShip.followShip(ship);

                    enemyShip.move();
                });
                for (EnemyShip enemyShip : enemyShips) {

                    if (now - lastShotTime >= COOLDOWN_PERIOD && shotsFired < MAX_PROJECTILES) {
                        EnemyProjectile enemyProjectile = new EnemyProjectile((int) enemyShip.getCharacter().getTranslateX(), (int) enemyShip.getCharacter().getTranslateY());
                        enemyProjectile.getCharacter().setRotate(enemyShip.getCharacter().getRotate());
                        enemyProjectiles.add(enemyProjectile);

                        enemyProjectile.accelerate();
                        enemyProjectile.setMovement(enemyProjectile.getMovement().normalize().multiply(5));
                        pane.getChildren().add(enemyProjectile.getCharacter());
                        lastShotTime = now;
                        shotsFired++;
                    }
                }
                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());
                enemyProjectiles.forEach(enemyProjectile -> enemyProjectile.move());
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
                        Text gameOverText = new Text( 50, 200, "GAME OVER\nScore: " + points);
                        gameOverText.setStyle("-fx-font-size: 100px");
                        gameOverText.setFill(Color.RED);
                        pane.getChildren().add(gameOverText);
                        stop();
                        enemySpawnTimer.cancel();
                        // koden nedan är för skrivande av poäng till fil.
                        // Read existing JSON objects from the file
                        JSONArray existingObjects = new JSONArray();
                        try (FileReader reader = new FileReader("example.json")) {
                            existingObjects = (JSONArray) new JSONParser().parse(reader);
                        } catch (FileNotFoundException e) {
                            // Ignore file not found errors, we will create the file later
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }

                        // Create a new JSON object for the current game score
                        JSONObject newObject = new JSONObject();
                        newObject.put("points", points);

                        // Add the new JSON object to the existing array
                        existingObjects.add(newObject);

                        // Write the updated JSON array to the file
                        try (FileWriter file = new FileWriter("example.json")) {
                            file.write(existingObjects.toJSONString());
                            file.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        existingObjects.sort((o1, o2) -> {
                            int points1 = Integer.parseInt(((JSONObject) o1).get("points").toString());
                            int points2 = Integer.parseInt(((JSONObject) o2).get("points").toString());
                            return Integer.compare(points2, points1);
                        });

                        //
                        int x = 50;
                        int y = 350;
                        for (int i = 0; i < Math.min(existingObjects.size(), 10); i++) {
                            JSONObject object = (JSONObject) existingObjects.get(i);
                            int points = Integer.parseInt(object.get("points").toString());
                            Text text = new Text(x, y, "Score " + (i + 1) + ": " + points);
                            text.setStyle("-fx-font-size: 30px");
                            text.setFill(Color.WHITE);
                            y += 40;
                            pane.getChildren().add(text);
                        }

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
