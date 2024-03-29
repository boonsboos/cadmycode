package cgroup2.cadmycode.gui.certificate;

import cgroup2.cadmycode.content.Certificate;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneManager;
import cgroup2.cadmycode.gui.SceneWrapper;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CertificateEditForm extends SceneWrapper {

    private TextField nameField = new TextField();
    private Button submit = new Button("Submit edits");

    private int id = 0;

    public CertificateEditForm(Stage stage, Certificate selected) {
        super(stage);
        stage.setTitle("edit certificate");
        nameField.setText(selected.getCertificateName());

        Label titleLabel = new Label("Name");

        HBox h1 = new HBox(titleLabel, nameField);
        HBox h2 = new HBox(submit);

        VBox v = new VBox(h1, h2);

        for (var child : v.getChildren()) {
            ((HBox) child).setSpacing(10);
        }

        v.setSpacing(10.0);
        v.setPadding(new Insets(10.0));
        v.setAlignment(Pos.CENTER_LEFT);

        submit.setOnMouseClicked(this::onSubmit);

        this.scene = new Scene(v);
        this.id = selected.getCertificateID();
    }

    private void onSubmit(Event e) {
        try {
            Database.update(
                new Certificate(id, nameField.getText())
            );
        }  catch (NumberFormatException exc) {
            SceneManager.showErrorDialog(exc.getMessage());
            return;
        }

        stage.close();

        // refresh table
        ((CertificateScene) GuiMain.SCENE_MANAGER.getCurrentScene()).loadData(new Event(EventType.ROOT));
    }
}