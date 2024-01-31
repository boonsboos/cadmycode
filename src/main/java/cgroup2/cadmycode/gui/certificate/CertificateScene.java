package cgroup2.cadmycode.gui.certificate;

import cgroup2.cadmycode.content.Certificate;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneType;
import cgroup2.cadmycode.gui.SceneWrapper;
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

public class CertificateScene extends SceneWrapper {
    private Button home = new Button("Home");
    private Button create = new Button("+");
    private Button edit = new Button("Edit");
    private Button delete = new Button("Delete");
    private Button refresh = new Button("Refresh");
    private TableView<Certificate> certificateTable = new TableView<>();

    private TableColumn<Certificate, Integer> certificateID = new TableColumn<>("certificateID");
    private TableColumn<Certificate, String> certificateName = new TableColumn<>("certificateName");

    public CertificateScene(Stage stage) {
        super(stage);

        certificateID.setCellValueFactory(new PropertyValueFactory<>("certificateID"));
        certificateName.setCellValueFactory(new PropertyValueFactory<>("certificateName"));

        certificateTable.getColumns().addAll(certificateID, certificateName);

        // make column titles fully visible
        certificateID.setPrefWidth(120);

        VBox v1 = new VBox(home, refresh);
        v1.setSpacing(40);

        VBox v2 = new VBox(create, edit, delete);
        v2.setSpacing(10);

        HBox hBox = new HBox(v1, certificateTable, v2);
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
    public void loadData(Event e) {
        certificateTable.setItems(FXCollections.observableArrayList(
                Database.getCertificates()
        ));
    }

    private void onCreateButtonPressed(Event e) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new CertificateCreationForm(dialog).show();

        dialog.show();
    }

    private void onEditButtonPressed(Event e) {
        if (certificateTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new CertificateEditForm(dialog, certificateTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }

    private void onDeleteButtonPressed(Event e) {
        if (certificateTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new CertificateDeletionPopup(dialog, certificateTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }

    private void onHomeButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.DASHBOARD);
    }
}
