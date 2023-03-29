package Model;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public class EnemyShip extends Entity {

    long lastShotTime;
    private boolean isAlive;

    public EnemyShip(int x, int y) {
        super(new Polygon(-10, -10, 20, 0, -10, 10), x, y);
        Polygon polygon = getPolygon();
        polygon.setStyle("-fx-fill: black; -fx-stroke: red; -fx-stroke-width: 2;");
        setPolygon(polygon);

        lastShotTime = 0L;
        setAlive(true);
    }
    public void followShip(Ship ship) {
        Point2D toShip = new Point2D(
                ship.getCharacter().getTranslateX() - getCharacter().getTranslateX(),
                ship.getCharacter().getTranslateY() - getCharacter().getTranslateY()
        );
        Point2D direction = toShip.normalize();
        setMovement(direction);
        double angle = Math.atan2(toShip.getY(), toShip.getX()) * 180 / Math.PI;
        getCharacter().setRotate(angle);
    }

    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

}