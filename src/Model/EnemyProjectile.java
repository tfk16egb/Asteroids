package Model;

import View.AsteroidsApplication;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static View.AsteroidsApplication.HEIGHT;

public class EnemyProjectile extends Character {
    private boolean isAlive;
    private double traveledDistance;

    public EnemyProjectile(int x, int y) {
        super(new Polygon(4, -2, 4, 2, -2, 2, -2, -2), x, y);

        Polygon polygon = getPolygon();
        polygon.setFill(Color.ORANGERED);
        setAlive(true);
        traveledDistance = 0;
    }


    @Override
    public void move() {
        Polygon character = getCharacter();
        Point2D movement = getMovement();

        Point2D initialPosition = new Point2D(character.getTranslateX(), character.getTranslateY());

        character.setTranslateX(character.getTranslateX() + movement.getX());
        character.setTranslateY(character.getTranslateY() + movement.getY());

        Point2D currentPosition = new Point2D(character.getTranslateX(), character.getTranslateY());

        if (character.getTranslateX() < 0) {
            character.setTranslateX(character.getTranslateX() + AsteroidsApplication.HEIGHT);
        }

        if (character.getTranslateX() > AsteroidsApplication.HEIGHT) {
            character.setTranslateX(character.getTranslateX() % AsteroidsApplication.HEIGHT);
        }

        if (character.getTranslateY() < 0) {
            character.setTranslateY(character.getTranslateY() + AsteroidsApplication.WIDTH);
        }

        if (character.getTranslateY() > AsteroidsApplication.WIDTH) {
            character.setTranslateY(character.getTranslateY() % AsteroidsApplication.WIDTH);
        }

        traveledDistance += Math.abs(initialPosition.distance(currentPosition));

        if (traveledDistance > 0.6 * AsteroidsApplication.WIDTH || traveledDistance > 0.6 * HEIGHT){
            setAlive(false);
        }
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return this.isAlive;
    }
}