package Observer;

import javafx.scene.Node;

import java.util.List;

@FunctionalInterface
public interface AddObjectCallback {
    void onAddObjects(List<Node> nodesToAdd);
}
