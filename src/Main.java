import Controller.GameController;
import View.AsteroidsApplication;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main {
    public static void main(String[] args) throws Exception {
        GameController controller = new GameController();
        final AsteroidsApplication asteroidsApplication = new AsteroidsApplication(controller);
        asteroidsApplication.init();
        Platform.startup(() -> {
            try {
                asteroidsApplication.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}