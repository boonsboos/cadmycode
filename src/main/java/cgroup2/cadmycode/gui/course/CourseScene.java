package cgroup2.cadmycode.gui.course;

import cgroup2.cadmycode.content.Course;
import cgroup2.cadmycode.content.CourseLevel;
import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import cgroup2.cadmycode.gui.SceneType;
import cgroup2.cadmycode.gui.SceneWrapper;
import cgroup2.cadmycode.gui.module.ModuleCreationForm;
import cgroup2.cadmycode.gui.module.ModuleDeletionPopup;
import cgroup2.cadmycode.gui.module.ModuleEditForm;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CourseScene extends SceneWrapper {
    private Button home = new Button("Home");
    private Button create = new Button("+");
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
        VBox v2 = new VBox(create, edit, delete, stats);
        v2.setSpacing(10);

        HBox h1 = new HBox(v1, courseTable, v2);

        h1.setPadding(new Insets(10.0));
        h1.setSpacing(10.0);

        create.setOnMouseClicked(this::onCreateButtonPressed);
        refresh.setOnMouseClicked(this::loadData);
        edit.setOnMouseClicked(this::onEditButtonPressed);
        delete.setOnMouseClicked(this::onDeleteButtonPressed);
        home.setOnMouseClicked(this::onHomeButtonPressed);
        stats.setOnMouseClicked(this::onStatsButtonPressed);

        // serialize all the database items and add them to the UI
        loadData(new Event(EventType.ROOT));

        this.scene = new Scene(h1);
    }
    public void loadData(Event e) {
        courseTable.setItems(FXCollections.observableArrayList(
                Database.getCourses()
        ));
    }

    private void onCreateButtonPressed(Event e) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        new CourseCreationForm(dialog).show();

        dialog.show();
    }

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

    private void onHomeButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.DASHBOARD);
    }

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

