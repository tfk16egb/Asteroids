import javafx.scene.shape.Polygon;

public class Asteroid extends Character{

    public Asteroid(int x, int y) {
        super(new Polygon(20, -20, 20, 20, -20, 20, -20, -20), x, y);
    }
}
