package Model;

import javafx.scene.shape.Polygon;

import java.util.Random;

public class Asteroid extends Entity {
    private double rotationalMovement;
    private boolean isAlive;
    private PolygonFactory.AsteroidSize size;

    public Asteroid(int x, int y, PolygonFactory.AsteroidSize size) {
        super(new PolygonFactory().createPolygon(size), x, y);
        Polygon polygon = getPolygon();
        polygon.setStyle("-fx-fill: black; -fx-stroke: white; -fx-stroke-width: 2;");
        setPolygon(polygon);
        setAlive(true);

        Random rnd = new Random();

        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        for(int i = 0; i < size.getAcceleration(); i++){
            accelerate();
        }

        this.size = size;
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

    public PolygonFactory.AsteroidSize getSize(){
        return size;
    }
}
