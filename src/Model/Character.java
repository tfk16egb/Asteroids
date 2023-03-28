package Model;

import View.AsteroidsApplication;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {
    private Polygon character;
    private Point2D movement;

    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);

        this.movement = new Point2D(0, 0);
    }

    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    public Polygon getPolygon(){
        return this.character;
    }
    public void setPolygon(Polygon polygon){
        this.character = polygon;
    }

    public Polygon getCharacter() {
        return character;
    }

    public Node getNode(){
        return character;
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 5);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }

    public void move() {
        Bounds bounds = character.getBoundsInParent();
        double x = character.getTranslateX() + movement.getX();
        double y = character.getTranslateY() + movement.getY();

        double parentWidth = character.getParent().getLayoutBounds().getWidth();
        double parentHeight = character.getParent().getLayoutBounds().getHeight();

        if (x + bounds.getWidth() < 0) {
            x = parentWidth;
        }
        if (x - bounds.getWidth() > parentWidth) {
            x = -bounds.getWidth();
        }
        if (y + bounds.getHeight() < 0) {
            y = parentHeight;
        }
        if (y  - bounds.getHeight() > parentHeight) {
            y = -bounds.getHeight();
        }

        character.setTranslateX(x);
        character.setTranslateY(y);
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= 0.05;
        changeY *= 0.05;

        this.movement = this.movement.add(changeX, changeY);
    }

    public boolean collide(Character other) {
        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}
