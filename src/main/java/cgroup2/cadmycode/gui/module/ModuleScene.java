package cgroup2.cadmycode.gui.module;

import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Module;
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

import java.time.LocalDate;

public class ModuleScene extends SceneWrapper {
    private Button home = new Button("Home");
    private Button create = new Button("Register");
    private Button edit = new Button("Edit");
    private Button delete = new Button("Delete");
    private Button refresh = new Button("Refresh");
    private TableView<Module> moduleTable = new TableView<>();

    private TableColumn<Module, Integer> attributeContentItemID = new TableColumn<>("ContentItemID");
    private TableColumn<Module, String> attributeTitle = new TableColumn<>("Title");
    private TableColumn<Module, String> attributeDescription = new TableColumn<>("Description");
    private TableColumn<Module, LocalDate> attributePublicationDate = new TableColumn<>("PublicationDate");
    private TableColumn<Module, ContentStatus> attributeStatus = new TableColumn<>("Status");
    private TableColumn<Module, String> attributeContactName = new TableColumn<>("ContactName");
    private TableColumn<Module, String> attributeContactEmail = new TableColumn<>("ContactEmail");
    private TableColumn<Module, Integer> attributeCourseID = new TableColumn<>("CourseID");
    private TableColumn<Module, Integer> attributeVersion = new TableColumn<>("Version");

    public ModuleScene(Stage stage) {
        super(stage);

        attributeContentItemID.setCellValueFactory(new PropertyValueFactory<>("ContentItemID"));
        attributeTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        attributeDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        attributePublicationDate.setCellValueFactory(new PropertyValueFactory<>("PublicationDate"));
        attributeStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        attributeContactName.setCellValueFactory(new PropertyValueFactory<>("ContactName"));
        attributeContactEmail.setCellValueFactory(new PropertyValueFactory<>("ContactEmail"));
        attributeCourseID.setCellValueFactory(new PropertyValueFactory<>("CourseID"));
        attributeVersion.setCellValueFactory(new PropertyValueFactory<>("Version"));

        moduleTable.getColumns().addAll(
                attributeContentItemID,
                attributeTitle,
                attributeDescription,
                attributePublicationDate,
                attributeStatus,
                attributeContactName,
                attributeContactEmail,
                attributeCourseID,
                attributeVersion
        );

        // make column titles fully visible
        attributeContentItemID.setPrefWidth(120);
        attributeContactName.setPrefWidth(100);
        attributePublicationDate.setPrefWidth(110);
        attributeContactEmail.setPrefWidth(100);

        VBox v1 = new VBox(home, refresh);
        v1.setSpacing(40);

        VBox v2 = new VBox(create, edit, delete);
        v2.setSpacing(10);

        HBox hBox = new HBox(v1, moduleTable, v2);
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
        moduleTable.setItems(FXCollections.observableArrayList(
                Database.getModules()
        ));
    }

    private void onCreateButtonPressed(Event e) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new ModuleCreationForm(dialog).show();

        dialog.show();
    }

    private void onEditButtonPressed(Event e) {
        if (moduleTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new ModuleEditForm(dialog, moduleTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }

    private void onDeleteButtonPressed(Event e) {
        if (moduleTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new ModuleDeletionPopup(dialog, moduleTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }

    private void onHomeButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.DASHBOARD);
    }
}
