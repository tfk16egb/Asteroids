package Factory;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class BackgroundImageFactory {
    private Map<String, Background> imagesMap = new HashMap<>();
    private int WIDTH;
    private int HEIGHT;

    public BackgroundImageFactory(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    public BackgroundImageFactory(int WIDTH, int HEIGHT, String... images) {
        loadImages(images);
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    public void loadImages(String... imageNames) {
        Arrays.stream(imageNames).forEach(imageName -> {
            imagesMap.put(imageName,imageToBackground(new Image(imageName)));
        });
    }
    public Background getBackground(String key){
        if(imagesMap.isEmpty()){
            System.err.println("Please load images first");
            return null;
        }
        return imagesMap.get(key);
    }

    private Background imageToBackground(Image image) {
        return new Background(new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(WIDTH, HEIGHT, true, true, true, true)
        ));
    }

    @Override
    public String toString() {
        return "BackgroundImageFactory{" +
                "imagesMap=" + imagesMap +
                ", WIDTH=" + WIDTH +
                ", HEIGHT=" + HEIGHT +
                '}';
    }
}
