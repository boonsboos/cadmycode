package cgroup2.cadmycode.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
* This class has utilities for handling switching scenes by passing around the main stage to
* classes that implement {@link SceneWrapper}
*/
public class SceneManager {

    public static void showErrorDialog(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE).show();
    }
}
