package cgroup2.cadmycode.gui.webcast;

import cgroup2.cadmycode.content.Certificate;
import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.*;
import cgroup2.cadmycode.gui.certificate.CertificateScene;
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

/**
 * shows a list of {@link Webcast} and buttons for creating, deleting and updating selected certificates
 */
public class WebcastScene extends SceneWrapper {

    private Button home = new Button("Home");
    private Button create = new Button("Register");
    private Button edit = new Button("Edit");
    private Button delete = new Button("Delete");
    private Button refresh = new Button("Refresh");
    private TableView<Webcast> webcastTable = new TableView<>();

    private TableColumn<Webcast, Integer> attributeContentItemId = new TableColumn<>("ContentItemID");
    private TableColumn<Webcast, String> attributeTitle = new TableColumn<>("Title");
    private TableColumn<Webcast, String> attributeDescription = new TableColumn<>("Description");
    private TableColumn<Webcast, ContentStatus> attributeStatus = new TableColumn<>("Status");
    private TableColumn<Webcast, Integer> attributeLength = new TableColumn<>("Length");
    private TableColumn<Webcast, String> attributeURL = new TableColumn<>("URL");
    private TableColumn<Webcast, String> attributeSpeaker = new TableColumn<>("Speaker");
    private TableColumn<Webcast, String> attributeOrganisation = new TableColumn<>("Organisation");
    /**
     * creates an instance of a {@link WebcastScene}
     * @param stage the stage on which the {@link WebcastScene} is to be drawn
     */
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
        home.setOnMouseClicked(this::onHomeButtonPressed);

        // serialize all the database items and add them to the UI
        loadData(new Event(EventType.ROOT));

        this.scene = new Scene(hBox);
    }
    /**
     * loads the certificates from the database to the list of webcasts in the {@link Webcast}
     * @param e the event that triggers retrieving data en loading it into the gui
     */
    public void loadData(Event e) {
        webcastTable.setItems(FXCollections.observableArrayList(
                Database.getWebcasts()
        ));
    }

    /**
     * creates a popup for creating new webcasts
     * @param e represents the button that when clicked will trigger showing the register popup
     */
    private void onCreateButtonPressed(Event e) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        // shows the webcast form
        new WebcastCreationForm(dialog).show();

        dialog.show();
    }
    /**
     * creates a popup for editing  selected webcasts
     * @param e represents the button that when clicked will trigger showing the edit popup
     */
    private void onEditButtonPressed(Event e) {
        if (webcastTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new WebcastEditForm(dialog, webcastTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }

    /**
     * creates a popup for deleting selected webcasts
     * @param e represents the button that when clicked will trigger showing the deletion popup
     */
    private void onDeleteButtonPressed(Event e) {
        if (webcastTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        // shows the deletion popup
        new WebcastDeletionPopup(dialog, webcastTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }
    /**
     * when triggered will return to the {@link DashboardScene}
     * @param e represents the button that when clicked will trigger returning to the {@link DashboardScene}
     */
    private void onHomeButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.DASHBOARD);
    }
}
