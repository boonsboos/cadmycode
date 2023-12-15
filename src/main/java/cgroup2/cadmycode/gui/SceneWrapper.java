package cgroup2.cadmycode.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class SceneWrapper {

    protected Stage stage;
    public SceneWrapper(Stage stage) {
        this.stage = stage;
    }
}
