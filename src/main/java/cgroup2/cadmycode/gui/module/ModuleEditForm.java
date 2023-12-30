package cgroup2.cadmycode.gui.module;

import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneManager;
import cgroup2.cadmycode.gui.SceneWrapper;
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

import java.net.URI;
import java.net.URISyntaxException;

public class ModuleEditForm extends SceneWrapper {

    private TextField titleField = new TextField();
    private TextArea descriptionArea = new TextArea();
    private DatePicker publicationDate = new DatePicker();
    private ComboBox<ContentStatus> status = new ComboBox<>(FXCollections.observableArrayList(
            ContentStatus.ACTIVE,
            ContentStatus.CONCEPT,
            ContentStatus.ARCHIVE
    ));
    private TextField version = new TextField(); // can only contain a number
    private TextField contactName = new TextField();
    private TextField contactEmail = new TextField();

    Button submit = new Button("Submit edits");

    public ModuleEditForm(Stage stage, Module selected) {
        super(stage);

        Label titleLabel = new Label("Title");
        Label descriptionLabel = new Label("Description");
        Label pubDateLabel = new Label("Date of publication");
        Label statusLabel = new Label("Status");
        Label versionLabel = new Label("Version");
        Label contactLabel = new Label("Contact");
        Label emailLabel = new Label("Contact e-mail");

        HBox h1 = new HBox(titleLabel, titleField);
        HBox h2 = new HBox(descriptionLabel, descriptionArea);
        HBox h3 = new HBox(pubDateLabel, publicationDate);
        HBox h4 = new HBox(statusLabel, status);
        HBox h5 = new HBox(versionLabel, version);
        HBox h6 = new HBox(contactLabel, contactName);
        HBox h7 = new HBox(emailLabel, contactEmail);
        HBox h8 = new HBox(new Label(), submit);

        VBox v = new VBox(h1, h2, h3, h4, h5, h6, h7, h8);

        for (var child : v.getChildren()) {
            ((HBox) child).setSpacing(10);
        }

        v.setSpacing(10.0);
        v.setPadding(new Insets(10.0));
        v.setAlignment(Pos.CENTER_LEFT);

        submit.setOnMouseClicked(this::onSubmit);

        this.scene = new Scene(v);
    }

    private void onSubmit(Event e) {
        try {
            new URI("mailto:"+contactEmail);

            Database.update(
                new Module(
                    titleField.getText(),
                    descriptionArea.getText().replace("\n", " "),
                    publicationDate.getValue(),
                    status.getValue(),
                    contactName.getText(),
                    contactEmail.getText(),
                    Integer.parseInt(version.getText())
                )
            );
        }  catch (URISyntaxException | NumberFormatException exc) {
            SceneManager.showErrorDialog(exc.getMessage());
            return;
        }

        stage.close();

        // refresh table
        ((ModuleScene) GuiMain.SCENE_MANAGER.getCurrentScene()).loadData(new Event(EventType.ROOT));
    }
}