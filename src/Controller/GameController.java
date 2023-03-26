package Controller;

import Model.Asteroid;
import Model.Projectile;
import Model.Ship;
import Observer.AddObjectCallback;
import Observer.CollidedObjectCallback;
import View.AsteroidsApplication;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static View.AsteroidsApplication.*;

public class GameController {

    List<Asteroid> asteroids = new ArrayList<>();
    List<Projectile> projectiles = new ArrayList<>();
    Ship ship;
    private CollidedObjectCallback collisionCallback;
    private AddObjectCallback addCallback;

    public GameController(){
        ship = new Ship(AsteroidsApplication.WIDTH / 2, HEIGHT / 2);

        for (int i = 0; i < 8; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH/3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }
    }

    public Ship getShip(){
        return ship;
    }

    public void moveAsteroid(){
        asteroids.forEach(asteroid -> asteroid.move());
    }

    public void moveProjectile(){
        projectiles.forEach(projectile -> projectile.move());
    }

    public void addAllProjectiles(){
        List<Node> nodesToAdd = new ArrayList<>();
        asteroids.forEach(asteroid -> nodesToAdd.add(asteroid.getNode()));
        if(addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public void removeProjectileThatHitAstroids(){
        List<Node> nodesToRemove = new ArrayList<>();

        projectiles.stream()
                .filter(projectile -> !projectile.isAlive())
                .forEach(projectile -> nodesToRemove.add(projectile.getNode()));
        projectiles.removeAll(projectiles.stream()
                .filter(projectile -> !projectile.isAlive())
                .collect(Collectors.toList()));

        asteroids.stream()
                .filter(asteroid -> !asteroid.isAlive())
                .forEach(asteroid -> nodesToRemove.add(asteroid.getNode()));
        asteroids.removeAll(asteroids.stream()
                .filter(asteroid -> !asteroid.isAlive())
                .collect(Collectors.toList()));
        projectiles.forEach(projectile -> {
            List<Asteroid> collisions = asteroids.stream()
                    .filter(asteroid -> asteroid.collide(projectile))
                    .collect(Collectors.toList());

            collisions.stream().forEach(collided -> {
                asteroids.remove(collided);
                nodesToRemove.add(collided.getNode());
            });
        });

        if (collisionCallback != null) {
            collisionCallback.onCollidedObjectsChanged(nodesToRemove);
        }
    }

    public void addAsteroidAtRandom(){
        List<Node> nodesToAdd = new ArrayList<>();
        if(Math.random() < 0.01) {
            Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
            if(!asteroid.collide(ship)) {
                asteroids.add(asteroid);
                nodesToAdd.add(asteroid.getNode());
            }
        }

        if(addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public void shootProjectile(){
        List<Node> nodesToAdd = new ArrayList<>();

        Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
        projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
        projectiles.add(projectile);

        projectile.accelerate();
        projectile.setMovement(projectile.getMovement().normalize().multiply(5));

        nodesToAdd.add(projectile.getNode());
        if(addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public int getProjectileSize(){
        return projectiles.size();
    }

    public boolean gameOver(){
        AtomicBoolean gameOver = new AtomicBoolean(false);

        asteroids.forEach(asteroid -> {
            if (ship.collide(asteroid)) {
                gameOver.set(true);
            }
        });

        return gameOver.get();
    }

    public int calculateScore(){
        AtomicInteger score = new AtomicInteger();
        projectiles.forEach(projectile -> {
            asteroids.forEach(asteroid -> {
                if(projectile.collide(asteroid)) {
                    projectile.setAlive(false);
                    asteroid.setAlive(false);
                }
            });
            if(!projectile.isAlive()) {
                score.addAndGet(100);
            }
        });

        return score.intValue();
    }

    public void removeProjectiles(){
        List<Node> nodesToRemove = new ArrayList<>();

        List<Projectile> projectilesToRemove = projectiles.stream().filter(projectile -> {
            List<Asteroid> collisions = asteroids.stream()
                    .filter(asteroid -> asteroid.collide(projectile))
                    .collect(Collectors.toList());

            if(collisions.isEmpty()) {
                return false;
            }

            collisions.stream().forEach(collided -> {
                asteroids.remove(collided);
                nodesToRemove.add(collided.getCharacter());
            });

            return true;
        }).collect(Collectors.toList());

        projectilesToRemove.forEach(projectile -> {
            projectiles.remove(projectile);
        });

        if (collisionCallback != null) {
            collisionCallback.onCollidedObjectsChanged(nodesToRemove);
        }
    }

    public void setCollisionCallback(CollidedObjectCallback callback) {
        this.collisionCallback = callback;
    }

    public void setAddObjectCallback(AddObjectCallback callback){
        this.addCallback = callback;
    }
}
