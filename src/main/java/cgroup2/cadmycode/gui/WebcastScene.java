package cgroup2.cadmycode.gui;

import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.webcast.WebcastCreationForm;
import cgroup2.cadmycode.gui.webcast.WebcastDeletionPopup;
import cgroup2.cadmycode.gui.webcast.WebcastEditForm;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WebcastScene extends SceneWrapper {

    Button home = new Button("Home");
    Button create = new Button("+");
    Button edit = new Button("Edit");
    Button delete = new Button("Delete");
    Button refresh = new Button("Refresh");
    TableView<Webcast> webcastTable = new TableView<>();;
    TableColumn<Webcast, Integer> attributeContentItemId = new TableColumn<>("ContentItemID");
    TableColumn<Webcast, String> attributeTitle = new TableColumn<>("Title");
    TableColumn<Webcast, String> attributeDescription = new TableColumn<>("Description");
    TableColumn<Webcast, ContentStatus> attributeStatus = new TableColumn<>("Status");
    TableColumn<Webcast, Integer> attributeLength = new TableColumn<>("Length");
    TableColumn<Webcast, String> attributeURL = new TableColumn<>("URL");
    TableColumn<Webcast, String> attributeSpeaker = new TableColumn<>("Speaker");
    TableColumn<Webcast, String> attributeOrganisation = new TableColumn<>("Organisation");

    public WebcastScene(Stage stage) {
        super(stage);

        attributeContentItemId.setCellValueFactory(new PropertyValueFactory<>("ContentItemID"));
        attributeTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        attributeDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        attributeStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        attributeLength.setCellValueFactory(new PropertyValueFactory<>("Length"));
        attributeURL.setCellValueFactory(new PropertyValueFactory<>("URL"));
        attributeSpeaker.setCellValueFactory(new PropertyValueFactory<>("Speaker"));
        attributeOrganisation.setCellValueFactory(new PropertyValueFactory<>("Organisation"));

        // add the columns to the table
        webcastTable.getColumns().addAll(
                attributeContentItemId,
                attributeTitle,
                attributeDescription,
                attributeStatus,
                attributeLength,
                attributeURL,
                attributeSpeaker,
                attributeOrganisation
        );

        VBox v1 = new VBox(create, edit, delete);
        v1.setSpacing(10.0);

        VBox v2 = new VBox(home, refresh);
        v2.setSpacing(40);

        HBox hBox = new HBox(v2,webcastTable, v1);
        hBox.setPadding(new Insets(10.0));
        hBox.setSpacing(10.0);

        create.setOnMouseClicked(this::onCreateButtonPressed);
        refresh.setOnMouseClicked(this::loadData);
        edit.setOnMouseClicked(this::onEditButtonPressed);
        delete.setOnMouseClicked(this::onDeleteButtonPressed);

        // serialize all the database items and add them to the UI
        loadData(new Event(EventType.ROOT));

        stage.setScene(new Scene(hBox));
    }

    public void loadData(Event e) {
        webcastTable.setItems(FXCollections.observableArrayList(
                Database.getWebcasts()
        ));
    }

    private void onCreateButtonPressed(Event e) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        // shows the webcast form
        new WebcastCreationForm(dialog);

        dialog.show();
    }

    private void onEditButtonPressed(Event e) {
        if (webcastTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new WebcastEditForm(dialog, webcastTable.getSelectionModel().getSelectedItem());

        dialog.show();
    }

    private void onDeleteButtonPressed(Event e) {

        if (webcastTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        // shows the deletion popup
        new WebcastDeletionPopup(dialog, webcastTable.getSelectionModel().getSelectedItem());

        dialog.show();
    }
}
