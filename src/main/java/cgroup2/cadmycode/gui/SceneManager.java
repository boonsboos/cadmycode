package cgroup2.cadmycode.gui;

import cgroup2.cadmycode.gui.certificate.CertificateScene;
import cgroup2.cadmycode.gui.module.ModuleScene;
import cgroup2.cadmycode.gui.user.UserScene;
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
    private SceneType currentScene = splash;

    public SceneManager(Stage mainStage) {
        this.mainStage = mainStage;

        this.scenes = new HashMap<>(Map.ofEntries(
                Map.entry(SceneType.WEBCAST, new WebcastScene(mainStage)),
                Map.entry(SceneType.MODULE, new ModuleScene(mainStage)),
                Map.entry(SceneType.DASHBOARD, new DashboardScene(mainStage)),
                Map.entry(SceneType.ABOUT, new AboutScene(mainStage)),
                Map.entry(SceneType.USER, new UserScene(mainStage)),
                Map.entry(SceneType.CERTIFICATE, new CertificateScene(mainStage))
        ));

        this.scenes.get(splash).show();
    }

    public void switchScene(SceneType scene) {
        this.scenes.get(scene).show();
        this.currentScene = scene;
    }

    public static void showErrorDialog(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE).show();
    }

    public SceneWrapper getCurrentScene() {
        return this.scenes.get(currentScene);
    }
}
