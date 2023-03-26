import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Projectile extends Character{

    private boolean isAlive;
    public Projectile(int x, int y) {
        super(new Polygon(4, -2, 4, 2, -2, 2, -2, -2), x, y);

        Polygon polygon = getPolygon();
        polygon.setFill(Color.LIGHTPINK);
        setAlive(true);
    }


    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return this.isAlive;
    }
}
