package cgroup2.cadmycode.gui.user;

import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneWrapper;
import cgroup2.cadmycode.gui.webcast.WebcastScene;
import cgroup2.cadmycode.user.User;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserDeletionPopup extends SceneWrapper {
    private User selected;

    private Button yesButton = new Button("Yes");
    private Button noButton = new Button("No");
    public UserDeletionPopup(Stage stage, User selected){
        super(stage);
        this.selected = selected;

        HBox h = new HBox(yesButton, noButton);
        h.setSpacing(10);
        h.setAlignment(Pos.CENTER);

        VBox v = new VBox(new Label("Are you sure you want to delete this? (It's permanent)"), h);
        v.setSpacing(20);
        v.setAlignment(Pos.CENTER);
        v.setPadding(new Insets(20));

        yesButton.setOnMouseClicked(this::onYesPressed);
        noButton.setOnMouseClicked(this::onNoPressed);

        this.scene = new Scene(v);
    }
    private void onYesPressed(Event e) {
        Database.delete(selected);
        stage.close();

        // refresh table
        ((UserScene) GuiMain.SCENE_MANAGER.getCurrentScene()).loadData(new Event(EventType.ROOT));
    }

    private void onNoPressed(Event e) {
        stage.close();
    }
}
