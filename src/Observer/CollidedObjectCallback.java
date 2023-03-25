package Observer;

import javafx.scene.Node;
import java.util.List;

@FunctionalInterface
public interface CollidedObjectCallback {
    void onCollidedObjectsChanged(List<Node> nodesToRemove);
}
