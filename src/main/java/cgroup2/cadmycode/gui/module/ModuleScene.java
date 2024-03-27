package cgroup2.cadmycode.gui.module;

import cgroup2.cadmycode.content.Certificate;
import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.DashboardScene;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneType;
import cgroup2.cadmycode.gui.SceneWrapper;
import cgroup2.cadmycode.gui.certificate.CertificateScene;
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

/**
 * shows a list of {@link Module} and buttons for creating, deleting and updating selected modules
 */
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

    /**
     * creates an instance of a {@link ModuleScene} scene
     * @param stage the stage on which the {@link ModuleScene} is to be drawn
     */
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

    /**
     * loads the certificates from the database to the list of modules in the {@link ModuleScene}
     * @param e the event that triggers retrieving data en loading it into the gui
     */
    public void loadData(Event e) {
        moduleTable.setItems(FXCollections.observableArrayList(
                Database.getModules()
        ));
    }

    /**
     * creates a popup for creating new modules
     * @param e represents the button that when clicked will trigger showing the register popup
     */
    private void onCreateButtonPressed(Event e) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new ModuleCreationForm(dialog).show();

        dialog.show();
    }

    /**
     * creates a popup for editing  selected modules
     * @param e represents the button that when clicked will trigger showing the edit popup
     */
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

    /**
     * creates a popup for deleting selected modules
     * @param e represents the button that when clicked will trigger showing the deletion popup
     */
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

    /**
     * when triggered will return to the {@link DashboardScene}
     * @param e represents the button that when clicked will trigger returning to the {@link DashboardScene}
     */
    private void onHomeButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.DASHBOARD);
    }
}
