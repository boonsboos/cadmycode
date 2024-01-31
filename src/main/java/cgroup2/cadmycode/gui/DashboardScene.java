package cgroup2.cadmycode.gui;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardScene extends SceneWrapper {

    private final Button webcast = new Button("Webcast");
    private final Button about = new Button("About");
    private final Button module = new Button("Module");
    private final Button user = new Button("User");
    private final Button certificate = new Button("Certificate");

    public DashboardScene(Stage stage) {
        super(stage);

        // this should become a list of buttons for the CRUD parts
        VBox v1 = new VBox(webcast, module, user, certificate);
        v1.setSpacing(10);

        TitledPane buttonMenu = new TitledPane("Overviews", v1);
        buttonMenu.setCollapsible(false);

        VBox v2 = new VBox(buttonMenu, about);
        v2.setSpacing(10);

        HBox root = new HBox(v2);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        webcast.setOnMouseClicked(this::onWebcastsButtonPressed);
        about.setOnMouseClicked(this::onAboutButtonPressed);
        module.setOnMouseClicked(this::onModuleButtonPressed);
        user.setOnMouseClicked(this::onUserButtonPressed);
        certificate.setOnMouseClicked(this::onCertificateButtonPressed);

        this.scene = new Scene(root);
    }

    private void onWebcastsButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.WEBCAST);
    }

    private void onAboutButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.ABOUT);
    }

    private void onModuleButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.MODULE);
    }

    private void onUserButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.USER);
    }

    private void onCertificateButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.CERTIFICATE);
    }
}
