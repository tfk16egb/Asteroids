package Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.awt.*;
import java.util.Random;

public class Asteroid extends Character {
    private double rotationalMovement;
    private boolean isAlive;

    public Asteroid(int x, int y) {
        super(new PolygonFactory().createPolygon(), x, y);
        Polygon polygon = getPolygon();
        polygon.setFill(Color.GREY);
        setPolygon(polygon);
        setAlive(true);

        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return this.isAlive;
    }
}
