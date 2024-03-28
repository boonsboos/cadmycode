package cgroup2.cadmycode.gui;

import cgroup2.cadmycode.gui.certificate.CertificateScene;
import cgroup2.cadmycode.gui.course.CourseScene;
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

    /**
     * creates an instance of {@link SceneManager}
     * @param mainStage the stage on which the {@link SceneManager} is to be drawn
     */
    public SceneManager(Stage mainStage) {
        this.mainStage = mainStage;

        this.scenes = new HashMap<>(Map.ofEntries(
                Map.entry(SceneType.WEBCAST, new WebcastScene(mainStage)),
                Map.entry(SceneType.MODULE, new ModuleScene(mainStage)),
                Map.entry(SceneType.COURSE, new CourseScene(mainStage)),
                Map.entry(SceneType.DASHBOARD, new DashboardScene(mainStage)),
                Map.entry(SceneType.ABOUT, new AboutScene(mainStage)),
                Map.entry(SceneType.USER, new UserScene(mainStage)),
                Map.entry(SceneType.CERTIFICATE, new CertificateScene(mainStage))
        ));

        this.scenes.get(splash).show();
    }

    /**
     * switches the current scene being displayed to the one specified by the scene parameter.
     * @param scene the scene to be changed to the scene that is shown to the user
     */
    public void switchScene(SceneType scene) {
        this.scenes.get(scene).show();
        this.currentScene = scene;
    }

    /**
     * creates and displays an error popup with a provided error message and close button
     * @param message the message to be given as parameter for the method call
     */
    public static void showErrorDialog(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE).show();
    }

    /**
     * gets the current scene shown in the gui
     * @return the current scene
     */
    public SceneWrapper getCurrentScene() {
        return this.scenes.get(currentScene);
    }
}
