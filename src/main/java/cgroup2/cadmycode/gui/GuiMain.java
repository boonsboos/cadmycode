package cgroup2.cadmycode.gui;

import cgroup2.cadmycode.database.Database;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * entry point for the CadMyCode GUI application. Initializes the stage, manages scene transitions via SceneManager,
 * and ensures database disconnection on application shutdown.
 */
public class GuiMain extends Application {

    public static SceneManager SCENE_MANAGER;

    /**
     * sets the initial stage and it creates a new SceneManager which handles the scene transitions
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Cadmycode");
        stage.show();

        SCENE_MANAGER = new SceneManager(stage);
    }

    /**
     *  ensures disconnection from the database when the GUI is closed .
     * @throws Exception if an error occurs during the disconnection process.
     */
    @Override
    public void stop() throws Exception {
        Database.disconnect();
    }
}