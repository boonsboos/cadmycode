package cgroup2.cadmycode.gui.certificate;

import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Certificate;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneManager;
import cgroup2.cadmycode.gui.SceneWrapper;
import cgroup2.cadmycode.except.FieldValidationException;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CertificateCreationForm extends SceneWrapper {

    private TextField nameField = new TextField();

    Button submit = new Button("Submit");

    public CertificateCreationForm(Stage stage) {
        super(stage);

        Label titleLabel = new Label("Name");

        HBox h1 = new HBox(titleLabel, nameField);
        HBox h2 = new HBox(new Label(), submit);

        VBox v = new VBox(h1, h2);

        for (var child : v.getChildren()) {
            ((HBox) child).setSpacing(10);
        }

        v.setSpacing(10.0);
        v.setPadding(new Insets(10.0));
        v.setAlignment(Pos.CENTER_LEFT);

        submit.setOnMouseClicked(this::onSubmit);

        this.scene = new Scene(v);
    }

    private void onSubmit(Event event) {
        try {
            Database.create(
                new Certificate(nameField.getText())
            );
        } catch (FieldValidationException | NumberFormatException e) {
            SceneManager.showErrorDialog(e.getMessage());
            return;
        }

        stage.close();
        // refresh table
        ((CertificateScene) GuiMain.SCENE_MANAGER.getCurrentScene()).loadData(new Event(EventType.ROOT));
    }

}
