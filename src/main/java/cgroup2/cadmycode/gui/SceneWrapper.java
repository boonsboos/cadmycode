package cgroup2.cadmycode.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * an abstract class managing and displaying scenes within a stage
 */
public abstract class SceneWrapper {

    protected Stage stage;
    protected Scene scene;

    /**
     * creates an instance of {@link SceneWrapper}
     * @param stage the stage on which the {@link SceneWrapper} is to be drawn
     */
    public SceneWrapper(Stage stage) {
        this.stage = stage;
    }

    /**
     * sets the content of the stage to the scene managed by the {@link SceneWrapper}.
     */
    public void show() {
        this.stage.setScene(this.scene);
    }
}
