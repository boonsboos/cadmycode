package cgroup2.cadmycode.gui;

import cgroup2.cadmycode.gui.webcast.WebcastScene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
* This class has utilities for handling switching scenes by passing around the main stage to
* classes that implement {@link SceneWrapper}
*/
public class SceneManager {

    /**
     * This is the scene that is shown when the application is started
     */
    private final SceneType splash = SceneType.DASHBOARD;

    private HashMap<SceneType, SceneWrapper> scenes;
    private Stage mainStage;
    private SceneType currentScene;

    public SceneManager(Stage mainStage) {
        this.mainStage = mainStage;

        this.scenes = new HashMap<>(Map.ofEntries(
                Map.entry(SceneType.WEBCAST, new WebcastScene(mainStage)),
                Map.entry(SceneType.DASHBOARD, new DashboardScene(mainStage))
        ));

        this.scenes.get(splash).show();
    }

    public void switchScene(SceneType scene) {
        this.scenes.get(scene).show();
    }

    public static void showErrorDialog(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE).show();
    }
}
