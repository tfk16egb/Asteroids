import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class EnemyProjectile extends Character {
    private boolean isAlive;
    public EnemyProjectile(int x, int y) {
        super(new Polygon(4, -2, 4, 2, -2, 2, -2, -2), x, y);

        Polygon polygon = getPolygon();
        polygon.setFill(Color.ORANGERED);
        setAlive(true);
    }


    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return this.isAlive;
    }
}
