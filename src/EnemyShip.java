import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
public class EnemyShip extends Character {

    public EnemyShip(int x, int y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y);
        Polygon polygon = getPolygon();
        polygon.setFill(Color.RED);
        setPolygon(polygon);
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
}
