package Model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Particle extends Character{

    private boolean isAlive;
    private int lifetime;
    private int age;

    public Particle(int x, int y){
        super(createPolygon(), (int)x, (int)y);
        setMovement(new Point2D(Math.random()- 0.5, Math.random() - 0.5).normalize().multiply(Math.random() * 2));
        this.lifetime = 50 + (int) (Math.random() * 10);
        this.age = 0;
        isAlive = true;
    }

    private static Polygon createPolygon() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                0.0, 0.0,
                4.0, 0.0,
                2.0, 2.0,
                0.0, 4.0,
                -2.0, 2.0,
                0.0, 0.0
        });
        polygon.setFill(Color.WHITE);
        return polygon;
    }

    @Override
    public void move() {
        super.move();

        // Decrease the particle's speed gradually over time
        getMovement().multiply(0.95);

        // Increase the particle's age and check if it's still alive
        this.age++;
        if (this.age > this.lifetime) {
            isAlive = false;
        }
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return this.isAlive;
    }
}
