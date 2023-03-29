package Model;

import javafx.scene.shape.Polygon;

public class Ship extends Entity {

    public Ship(int x, int y) {
        super(new Polygon(-10, -10, 20, 0, -10, 10), x, y);
        Polygon polygon = getPolygon();
        polygon.setStyle("-fx-fill: black; -fx-stroke: white; -fx-stroke-width: 2;");
        setPolygon(polygon);
    }



}
