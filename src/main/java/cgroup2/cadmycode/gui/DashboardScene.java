package cgroup2.cadmycode.gui;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class DashboardScene extends SceneWrapper {

    Button webcast = new Button("Webcast");

    public DashboardScene(Stage stage) {
        super(stage);

        webcast.setOnMouseClicked(this::onWebcastsButtonPressed);

        HBox h = new HBox(new Label("See webcasts"), webcast);

        h.setSpacing(10);
        h.setPadding(new Insets(10));

        this.scene = new Scene(h);
    }

    private void onWebcastsButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.WEBCAST);
    }
}
