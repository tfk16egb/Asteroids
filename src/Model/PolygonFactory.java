package Model;

import java.util.Random;
import java.util.stream.DoubleStream;

import javafx.scene.shape.Polygon;
public class PolygonFactory {
    public enum AsteroidSize {
        SMALL(10, 15),
        MEDIUM(15, 20),
        LARGE(25, 40);

        private int minSize;
        private int maxSize;

        AsteroidSize(int minSize, int maxSize) {
            this.minSize = minSize;
            this.maxSize = maxSize;
        }

        public int getMinSize() {
            return minSize;
        }

        public int getMaxSize() {
            return maxSize;
        }
    }

    public Polygon createPolygon(AsteroidSize size) {
        Random rnd = new Random();

        int numVertices = 5 + rnd.nextInt(6);

        double[] vertices = new double[numVertices * 2];

        double asteroidSize = size.getMinSize() + rnd.nextInt(size.getMaxSize() - size.getMinSize());

        for (int i = 0; i < numVertices; i++) {
            double angle = Math.PI * 2 * i / numVertices;
            double radius = asteroidSize + rnd.nextInt(10) - 5;

            vertices[i*2] = radius * Math.cos(angle);
            vertices[i*2+1] = radius * Math.sin(angle);
        }

        Polygon polygon = new Polygon();
        DoubleStream.of(vertices).forEach(polygon.getPoints()::add);

        return polygon;
    }
}
