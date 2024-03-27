package cgroup2.cadmycode.gui.webcast;

import cgroup2.cadmycode.content.Certificate;
import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneWrapper;
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

/**
 * Shows a pop-up message to confirm whether you would like to delete the {@link Webcast}
 */
public class WebcastDeletionPopup extends SceneWrapper {

    private Webcast selected;

    private Button yesButton = new Button("Yes");
    private Button noButton = new Button("No");

    /**
     * creates an instance of a deletion popup
     @param stage - the stage on which the popup is to be drawn
     @param selected - the selected {@link Webcast}
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

        this.scene = new Scene(v);
    }
    /**
     * when triggered will delete the selected {@link Webcast}
     * @param e represents the button that when clicked will trigger deleting the selected {@link Webcast}
     */
    private void onYesPressed(Event e) {
        Database.delete(selected);
        stage.close();

        // refresh table
        ((WebcastScene) GuiMain.SCENE_MANAGER.getCurrentScene()).loadData(new Event(EventType.ROOT));
    }

    /**
     * when triggered will close the deletion popup
     * @param e represents the button that when clicked will trigger the closing of the popup
     */
    private void onNoPressed(Event e) {
        stage.close();
    }
}
