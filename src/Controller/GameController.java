package Controller;

import Model.*;
import Model.Entity;
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
    List<Bullet> bullets = new ArrayList<>();
    List<Particle> particles = new ArrayList<>();
    List<EnemyShip> enemyShips = new ArrayList<>();
    List<EnemyBullet> enemyBullets = new ArrayList<>();
    Ship ship;
    private CollidedObjectCallback collisionCallback;
    private AddObjectCallback addCallback;

    public GameController(){
        ship = new Ship(AsteroidsApplication.WIDTH / 2, HEIGHT / 2);

        initAsteroid();
    }

    private void createAsteroid(int amount, PolygonFactory.AsteroidSize size){
        for (int i = 0; i < amount; i++) {
            boolean cantAdd = true;
            while(cantAdd){
                Random rnd = new Random();
                Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT), size);
                if(!isEntityClose(asteroid, 100)){
                    asteroids.add(asteroid);
                    cantAdd = false;
                }
            }
        }
    }

    public void initAsteroid(){
        boolean cantAdd = true;

        createAsteroid(9, PolygonFactory.AsteroidSize.LARGE);
        createAsteroid(3, PolygonFactory.AsteroidSize.SMALL);
        createAsteroid(2, PolygonFactory.AsteroidSize.MEDIUM);
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
        bullets.forEach(bullet -> bullet.move());
    }

    public void moveEnemies(){
        enemyShips.forEach(enemyShip -> {
            enemyShip.followShip(ship);
            enemyShip.move();
        });
    }

    public void moveEnemyProjectile(){
        enemyBullets.forEach(enemyBullet -> enemyBullet.move());
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

        bullets.stream()
                .filter(bullet -> !bullet.isAlive())
                .forEach(bullet -> nodesToRemove.add(bullet.getNode()));
        bullets.removeAll(bullets.stream()
                .filter(bullet -> !bullet.isAlive())
                .collect(Collectors.toList()));

        enemyBullets.stream()
                .filter(enemyBullet -> !enemyBullet.isAlive())
                .forEach(enemyBullet -> nodesToRemove.add(enemyBullet.getNode()));
        enemyBullets.removeAll(enemyBullets.stream()
                .filter(enemyBullet -> !enemyBullet.isAlive())
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
        bullets.forEach(bullet -> {
            List<EnemyShip> collisions = enemyShips.stream()
                    .filter(enemyShip -> enemyShip.collide(bullet))
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

        bullets.stream()
                .filter(bullet -> !bullet.isAlive())
                .forEach(bullet -> nodesToRemove.add(bullet.getNode()));
        bullets.removeAll(bullets.stream()
                .filter(bullet -> !bullet.isAlive())
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
        bullets.forEach(bullet -> {
            List<Asteroid> collisions = asteroids.stream()
                    .filter(asteroid -> asteroid.collide(bullet))
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

    public boolean isEntityClose(Entity entity, int radius){
        double distance = Math.sqrt(Math.pow(ship.getCharacter().getTranslateX() - entity.getCharacter().getTranslateX(), 2)
                + Math.pow(ship.getCharacter().getTranslateY() - entity.getCharacter().getTranslateY(), 2));
        if (distance > radius) {
            return false;
        }
        return true;
    }

    public void addEnemyShipAtRandom(){
        List<Node> nodesToAdd = new ArrayList<>();
        Random rand = new Random();

        EnemyShip enemyShip = null;

        boolean cantAdd = true;

        while (cantAdd){
            enemyShip = new EnemyShip(rand.nextInt(HEIGHT), rand.nextInt(WIDTH));
            if (!isEntityClose(enemyShip, 300)) {
                cantAdd = false;
                break;
            }
        }

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
            System.out.println(size.name());
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

        EnemyBullet enemyBullet = new EnemyBullet((int) enemyShip.getCharacter().getTranslateX(), (int) enemyShip.getCharacter().getTranslateY());
        enemyBullet.getCharacter().setRotate(enemyShip.getCharacter().getRotate());
        enemyBullets.add(enemyBullet);
        enemyBullet.accelerate();
        enemyBullet.setMovement(enemyBullet.getMovement().normalize().multiply(5));

        nodesToAdd.add(enemyBullet.getNode());

        if(addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public void shootProjectile(){
        List<Node> nodesToAdd = new ArrayList<>();

        Bullet bullet = new Bullet((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
        bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
        bullets.add(bullet);

        bullet.accelerate();
        bullet.setMovement(bullet.getMovement().normalize().multiply(5));

        nodesToAdd.add(bullet.getNode());
        if(addCallback != null){
            addCallback.onAddObjects(nodesToAdd);
        }
    }

    public int getProjectileSize(){
        return bullets.size();
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
        enemyBullets.forEach(enemyBullet -> {
            if (ship.collide(enemyBullet)) {
                gameOver.set(true);
            }
        });

        return gameOver.get();
    }

    public int calculateScore(){
        AtomicInteger score = new AtomicInteger();
        bullets.forEach(bullet -> {
            asteroids.forEach(asteroid -> {
                if(bullet.collide(asteroid)) {
                    bullet.setAlive(false);
                    asteroid.setAlive(false);
                    score.addAndGet(asteroid.getSize().getScoreValue());
                }
            });
            enemyShips.forEach(enemyShip -> {
                final int ENEMY_SCORE = 500;
                if (bullet.collide(enemyShip)) {
                    bullet.setAlive(false);
                    enemyShip.setAlive(false);
                    score.addAndGet(ENEMY_SCORE);
                }
            });
        });

        return score.intValue();
    }

    public void removeProjectiles(){
        List<Node> nodesToRemove = new ArrayList<>();

        List<Bullet> projectilesToRemove = bullets.stream().filter(bullet -> {
            List<Asteroid> collisions = asteroids.stream()
                    .filter(asteroid -> asteroid.collide(bullet))
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

        projectilesToRemove.forEach(bullet -> {
            bullets.remove(bullet);
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
