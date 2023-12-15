package cgroup2.cadmycode.gui.webcast;

import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.SceneWrapper;
import cgroup2.cadmycode.gui.WebcastScene;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WebcastDeletionPopup extends SceneWrapper {

    private Webcast selected;

    Button yesButton = new Button("Yes");
    Button noButton = new Button("No");

    /**
        Shows a pop-up message to confirm whether you would like to delete the Webcast

        @param stage - the stage on which the popup is to be drawn
        @param selected - the selected webcast
     */
    public WebcastDeletionPopup(Stage stage, Webcast selected) {
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

        Scene dialogScene = new Scene(v);
        stage.setScene(dialogScene);
    }

    private void onYesPressed(Event e) {
        Database.deleteWebcastById(selected.getContentItemID());
        stage.close();
    }

    private void onNoPressed(Event e) {
        stage.close();
    }
}
