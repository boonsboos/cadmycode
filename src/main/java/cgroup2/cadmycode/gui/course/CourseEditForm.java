package cgroup2.cadmycode.gui.course;

import cgroup2.cadmycode.content.Certificate;
import cgroup2.cadmycode.content.Course;
import cgroup2.cadmycode.content.CourseLevel;
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

/**
 * Shows a pop-up message where a selected {@link Course} can be edited and submitted
 */
public class CourseEditForm extends SceneWrapper {
    private TextField courseName = new TextField();
    private TextField subject = new TextField();
    private TextArea introductionText = new TextArea();
    private TextField courseID = new TextField();
    private ComboBox<CourseLevel> levelBox = new ComboBox<>(FXCollections.observableArrayList(
            CourseLevel.BEGINNER,
            CourseLevel.INTERMEDIATE,
            CourseLevel.EXPERT
    ));
    private TextField certificateID = new TextField();

    Button submit = new Button("Submit");

    /**
     * creates an instance of an edit popup
     * @param stage the stage on which the popup is to be drawn
     * @param selected the selected {@link Course}
     */
    public CourseEditForm(Stage stage, Course selected) {
        super(stage);
        stage.setTitle("edit course");

        courseName.setText(selected.getCourseName());
        subject.setText(selected.getSubject());
        introductionText.setText(selected.getIntroductionText());
        courseID.setText(String.valueOf(selected.getCourseID()));
        levelBox.setValue(selected.getLevel());
        certificateID.setText(String.valueOf(selected.getCertificateID()));

        Label courseNameLabel = new Label("courseName");
        Label subjectLabel = new Label("subject");
        Label introductionTextLabel = new Label("introduction");
        Label courseIDLabel = new Label("courseID");
        Label levelBoxLabel = new Label("level");
        Label certificateIDLabel = new Label("certificateID");

        HBox h1 = new HBox(courseNameLabel, courseName);
        HBox h2 = new HBox(subjectLabel, subject);
        HBox h3 = new HBox(introductionTextLabel, introductionText);
        HBox h4 = new HBox(courseIDLabel, courseID);
        HBox h5 = new HBox(levelBoxLabel, levelBox);
        HBox h6 = new HBox(certificateIDLabel, certificateID);
        HBox h7 = new HBox(submit);

        VBox v = new VBox(h1, h2, h3, h4, h5, h6, h7);

        for (var child : v.getChildren()) {
            ((HBox) child).setSpacing(10);
        }

        v.setSpacing(10.0);
        v.setPadding(new Insets(10.0));
        v.setAlignment(Pos.CENTER_LEFT);

        submit.setOnMouseClicked(this::onSubmit);

        this.scene = new Scene(v);
    }

    /**
     * when triggered will update the selected {@link Course}
     * @param event represents the button that when clicked will trigger updating the selected {@link Course}
     */
    private void onSubmit(Event event) {
        try {
            Database.update(
                    new Course(
                            courseName.getText(),
                            subject.getText(),
                            introductionText.getText().replace("\n", " "),
                            Integer.parseInt(courseID.getText()),
                            levelBox.getValue(),
                            Integer.parseInt(certificateID.getText())
                    )
            );
        } catch (NumberFormatException e) {
            SceneManager.showErrorDialog(e.getMessage());
            return;
        }
        stage.close();
        // refresh table
        ((CourseScene) GuiMain.SCENE_MANAGER.getCurrentScene()).loadData(new Event(EventType.ROOT));
    }
}
