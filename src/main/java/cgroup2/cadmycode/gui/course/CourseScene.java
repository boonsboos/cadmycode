package cgroup2.cadmycode.gui.course;

import cgroup2.cadmycode.content.Certificate;
import cgroup2.cadmycode.content.Course;
import cgroup2.cadmycode.content.CourseLevel;
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

/**
 * shows a list of {@link Course} and buttons for creating, deleting and updating selected users
 */
public class CourseScene extends SceneWrapper {
    private Button home = new Button("Home");
    private Button create = new Button("Register");
    private Button edit = new Button("Edit");
    private Button delete = new Button("Delete");
    private Button refresh = new Button("Refresh");
    private Button stats = new Button("Statistics");
    private TableView<Course> courseTable = new TableView<>();

    private TableColumn<Course, String> atributeCourseName = new TableColumn<>("courseName");
    private TableColumn<Course, String> atributeSubject = new TableColumn<>("subject");
    private TableColumn<Course, String> atributeIntroductionText = new TableColumn<>("introductionText");
    private TableColumn<Course, Integer> atributeCourseID = new TableColumn<>("courseID");
    private TableColumn<Course, CourseLevel> atributeLevel = new TableColumn<>("level");
    private TableColumn<Course, Integer> atributeCertificateID = new TableColumn<>("certificateID");

    /**
     * creates an instance of a {@link CourseScene} scene
     * @param stage the stage on which the {@link CourseScene} is to be drawn
     */
    public CourseScene(Stage stage) {
        super(stage);
        atributeCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        atributeSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        atributeIntroductionText.setCellValueFactory(new PropertyValueFactory<>("introductionText"));
        atributeCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        atributeLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        atributeCertificateID.setCellValueFactory(new PropertyValueFactory<>("certificateID"));

        courseTable.getColumns().addAll(
                atributeCourseName,
                atributeSubject,
                atributeIntroductionText,
                atributeCourseID,
                atributeLevel,
                atributeCertificateID
        );
        courseTable.setMinWidth(1050);

        VBox v1 = new VBox(home, refresh);
        v1.setSpacing(40);

        VBox v2 = new VBox(create, edit, delete, stats);
        v2.setSpacing(10);

        HBox hBox = new HBox(v1, courseTable, v2);
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        create.setOnMouseClicked(this::onCreateButtonPressed);
        refresh.setOnMouseClicked(this::loadData);
        edit.setOnMouseClicked(this::onEditButtonPressed);
        delete.setOnMouseClicked(this::onDeleteButtonPressed);
        home.setOnMouseClicked(this::onHomeButtonPressed);
        stats.setOnMouseClicked(this::onStatsButtonPressed);

        // serialize all the database items and add them to the UI
        loadData(new Event(EventType.ROOT));

        this.scene = new Scene(hBox);
    }

    /**
     * loads the certificates from the database to the list of certificates in the {@link CourseScene}
     * @param e the event that triggers retrieving data en loading it into the gui
     */
    public void loadData(Event e) {
        courseTable.setItems(FXCollections.observableArrayList(
                Database.getCourses()
        ));
    }

    /**
     * creates a popup for creating new courses
     * @param e represents the button that when clicked will trigger showing the register popup
     */
    private void onCreateButtonPressed(Event e) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new CourseCreationForm(dialog).show();

        dialog.show();
    }

    /**
     * creates a popup for editing selected courses
     * @param e represents the button that when clicked will trigger showing the edit popup
     */
    private void onEditButtonPressed(Event e) {
        if (courseTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new CourseEditForm(dialog, courseTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }

    /**
     * creates a popup for deleting selected courses
     * @param e represents the button that when clicked will trigger showing the deletion popup
     */
    private void onDeleteButtonPressed(Event e) {
        if (courseTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new CourseDeletionPopup(dialog, courseTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }

    /**
     * when triggered will return to the {@link DashboardScene}
     * @param e represents the button that when clicked will trigger returning to the {@link DashboardScene}
     */
    private void onHomeButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.DASHBOARD);
    }

    /**
     * when triggered will open the {@link CourseStatisticsView}
     * @param e represents the button that when clicked will trigger opening the {@link CourseStatisticsView}
     */
    private void onStatsButtonPressed(Event e) {
        if (courseTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new CourseStatisticsView(dialog, courseTable.getSelectionModel().getSelectedItem()).show();

        dialog.show();
    }
}