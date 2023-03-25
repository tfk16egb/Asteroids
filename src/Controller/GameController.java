package Controller;

import Model.Asteroid;
import Model.Projectile;
import Model.Ship;
import View.AsteroidsApplication;
import View.GameStageGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameController {

    GameStageGUI gameGUI;
    List<Asteroid> asteroids = new ArrayList<>();
    List<Projectile> projectiles = new ArrayList<>();

    Ship ship;



    public GameController(GameStageGUI gameGUI){
        this.gameGUI = gameGUI;
        ship = new Ship(AsteroidsApplication.WIDTH / 2, AsteroidsApplication.HEIGHT / 2);

        for (int i = 0; i < 8; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH/3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }
    }

    public void removeProjectiles(){
        List<Projectile> projectilesToRemove = projectiles.stream().filter(projectile -> {
            List<Asteroid> collisions = asteroids.stream()
                    .filter(asteroid -> asteroid.collide(projectile))
                    .collect(Collectors.toList());

            if(collisions.isEmpty()) {
                return false;
            }

            collisions.stream().forEach(collided -> {
                asteroids.remove(collided);
                //pane.getChildren().remove(collided.getCharacter());
            });

            return true;
        }).collect(Collectors.toList());

        projectilesToRemove.forEach(projectile -> {
            //pane.getChildren().remove(projectile.getCharacter());
            projectiles.remove(projectile);
        });
    }

}
