package Controller;

import Model.*;
import Observer.AddObjectCallback;
import Observer.CollidedObjectCallback;
import View.AsteroidsApplication;
import javafx.scene.Node;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static View.AsteroidsApplication.*;

public class GameController {

    List<Asteroid> asteroids = new ArrayList<>();
    List<Projectile> projectiles = new ArrayList<>();
    List<Particle> particles = new ArrayList<>();
    List<EnemyShip> enemyShips = new ArrayList<>();
    List<EnemyProjectile> enemyProjectiles = new ArrayList<>();
    Ship ship;
    private CollidedObjectCallback collisionCallback;
    private AddObjectCallback addCallback;

    public GameController(){
        ship = new Ship(AsteroidsApplication.WIDTH / 2, HEIGHT / 2);

        for (int i = 0; i < 8; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH/3), rnd.nextInt(HEIGHT), PolygonFactory.AsteroidSize.LARGE);
            asteroids.add(asteroid);
        }

    }

    private void createParticles(int x, int y){
        List<Node> nodesToAdd = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            Particle particle = new Particle(x, y);
            particles.add(particle);
            nodesToAdd.add(particle.getNode());
        }

        if(addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public void updateParticle(){
        List<Node> nodesToRemove = new ArrayList<>();

        for(int i = 0; i < particles.size(); i++){
            Particle particle = particles.get(i);
            particle.move();
            if(!particle.isAlive()){
                nodesToRemove.add(particles.get(i).getNode());
                particles.remove(i);
                i--;
            }
        }

        particles.removeAll(particles
                .stream().filter(particle -> !particle.isAlive())
                .collect(Collectors.toList()));

        if (collisionCallback != null) {
            collisionCallback.onCollidedObjectsChanged(nodesToRemove);
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

    public void moveEnemies(){
        enemyShips.forEach(enemyShip -> {
            enemyShip.followShip(ship);
            enemyShip.move();
        });
    }

    public void moveEnemyProjectile(){
        enemyProjectiles.forEach(enemyProjectile -> enemyProjectile.move());
    }

    public void addAllProjectiles(){
        List<Node> nodesToAdd = new ArrayList<>();
        asteroids.forEach(asteroid -> nodesToAdd.add(asteroid.getNode()));
        if(addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public void enemyShipCollisions(){
        List<Node> nodesToRemove = new ArrayList<>();

        projectiles.stream()
                .filter(projectile -> !projectile.isAlive())
                .forEach(projectile -> nodesToRemove.add(projectile.getNode()));
        projectiles.removeAll(projectiles.stream()
                .filter(projectile -> !projectile.isAlive())
                .collect(Collectors.toList()));

        enemyProjectiles.stream()
                .filter(enemyProjectile -> !enemyProjectile.isAlive())
                .forEach(enemyProjectile -> nodesToRemove.add(enemyProjectile.getNode()));
        enemyProjectiles.removeAll(enemyProjectiles.stream()
                .filter(enemyProjectile -> !enemyProjectile.isAlive())
                .collect(Collectors.toList()));

        enemyShips.stream()
                .filter(enemyShip -> !enemyShip.isAlive())
                .forEach(enemyShip -> {
                    nodesToRemove.add(enemyShip.getNode());
                    createParticles((int) enemyShip.getCharacter().getTranslateX(), (int) enemyShip.getCharacter().getTranslateY());
                });


        enemyShips.removeAll(enemyShips.stream()
                .filter(enemyShip -> !enemyShip.isAlive())
                .collect(Collectors.toList()));
        projectiles.forEach(projectile -> {
            List<EnemyShip> collisions = enemyShips.stream()
                    .filter(enemyShip -> enemyShip.collide(projectile))
                    .collect(Collectors.toList());

            collisions.stream().forEach(collided -> {
                enemyShips.remove(collided);
                nodesToRemove.add(collided.getNode());
            });
        });


        if (collisionCallback != null) {
            collisionCallback.onCollidedObjectsChanged(nodesToRemove);
        }
    }
    public void removeProjectileThatHitAstroids(){
        List<Node> nodesToRemove = new ArrayList<>();
        List<Asteroid> asteroidToAdd = new ArrayList<>();

        projectiles.stream()
                .filter(projectile -> !projectile.isAlive())
                .forEach(projectile -> nodesToRemove.add(projectile.getNode()));
        projectiles.removeAll(projectiles.stream()
                .filter(projectile -> !projectile.isAlive())
                .collect(Collectors.toList()));

        asteroids.stream()
                .filter(asteroid -> !asteroid.isAlive())
                .forEach(asteroid -> {
                    nodesToRemove.add(asteroid.getNode());
                    createParticles((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY());
                    if(asteroid.getSize() == PolygonFactory.AsteroidSize.LARGE){
                        asteroidToAdd.add(new Asteroid((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(),
                                PolygonFactory.AsteroidSize.MEDIUM));
                        asteroidToAdd.add(new Asteroid((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(),
                                PolygonFactory.AsteroidSize.MEDIUM));
                    }
                    if(asteroid.getSize() == PolygonFactory.AsteroidSize.MEDIUM){
                        asteroidToAdd.add(new Asteroid((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(),
                                PolygonFactory.AsteroidSize.SMALL));
                        asteroidToAdd.add(new Asteroid((int) asteroid.getCharacter().getTranslateX(), (int) asteroid.getCharacter().getTranslateY(),
                                PolygonFactory.AsteroidSize.SMALL));
                    }
                });
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

        List<Node> nodesToAdd = new ArrayList<>();

        for(int i = 0; i < asteroidToAdd.size(); i++){
            asteroids.add(asteroidToAdd.get(i));
            nodesToAdd.add(asteroidToAdd.get(i).getNode());
        }

        if (collisionCallback != null) {
            collisionCallback.onCollidedObjectsChanged(nodesToRemove);
        }

        if (addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public void addEnemyShipAtRandom(){
        List<Node> nodesToAdd = new ArrayList<>();
        Random rand = new Random();

        EnemyShip enemyShip = new EnemyShip(rand.nextInt(HEIGHT), rand.nextInt(WIDTH));
        enemyShips.add(enemyShip);
        nodesToAdd.add(enemyShip.getNode());


        if(addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public void addAsteroidAtRandom(){
        List<Node> nodesToAdd = new ArrayList<>();
        List<PolygonFactory.AsteroidSize> asteroidSizes = new ArrayList<>();
        asteroidSizes.add(PolygonFactory.AsteroidSize.LARGE);
        asteroidSizes.add(PolygonFactory.AsteroidSize.MEDIUM);
        asteroidSizes.add(PolygonFactory.AsteroidSize.SMALL);
        Random random = new Random();

        if(Math.random() < 0.001) {
            PolygonFactory.AsteroidSize size = asteroidSizes.get(random.nextInt(3));
            Asteroid asteroid = new Asteroid(WIDTH, HEIGHT, size);
            if(!asteroid.collide(ship)) {
                asteroids.add(asteroid);
                nodesToAdd.add(asteroid.getNode());
            }
        }

        if(addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public int getEnemySize(){
        return enemyShips.size();
    }

    public void shootEnemyProjectiles(int i, long now){
        final long ENEMY_COOLDOWN_PERIOD = 1000_000_000;
        List<Node> nodesToAdd = new ArrayList<>();
        EnemyShip enemyShip = enemyShips.get(i);
        if(!(now - enemyShip.getLastShotTime() >= ENEMY_COOLDOWN_PERIOD)){
            return;
        }

        enemyShip.setLastShotTime(now);

        EnemyProjectile enemyProjectile = new EnemyProjectile((int) enemyShip.getCharacter().getTranslateX(), (int) enemyShip.getCharacter().getTranslateY());
        enemyProjectile.getCharacter().setRotate(enemyShip.getCharacter().getRotate());
        enemyProjectiles.add(enemyProjectile);
        enemyProjectile.accelerate();
        enemyProjectile.setMovement(enemyProjectile.getMovement().normalize().multiply(5));

        nodesToAdd.add(enemyProjectile.getNode());

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
        enemyShips.forEach(enemyShip -> {
            if (ship.collide(enemyShip)) {
                gameOver.set(true);
            }
        });
        enemyProjectiles.forEach(enemyProjectile -> {
            if (ship.collide(enemyProjectile)) {
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
                    score.addAndGet(asteroid.getSize().getScoreValue());
                }
            });
            enemyShips.forEach(enemyShip -> {
                final int ENEMY_SCORE = 500;
                if (projectile.collide(enemyShip)) {
                    projectile.setAlive(false);
                    enemyShip.setAlive(false);
                    score.addAndGet(ENEMY_SCORE);
                }
            });
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
