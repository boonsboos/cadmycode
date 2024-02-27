package cgroup2.cadmycode.gui.course;

import cgroup2.cadmycode.content.Course;
import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.SceneWrapper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseStatisticsView extends SceneWrapper {

    private final Button close = new Button("Close");
    private ListView<String> progress = new ListView<>();

    private final Label relatedCourses = new Label();
    private final Label relatedLabel = new Label("Related:");

    private final Label amountOfGrads = new Label();
    private final Label gradsLabel = new Label("Total graduations:");

    public CourseStatisticsView(Stage stage, Course selected) {
        super(stage);

        HBox h1 = new HBox(relatedLabel, relatedCourses);
        h1.setSpacing(10);

        HBox h2 = new HBox(gradsLabel, amountOfGrads);
        h2.setSpacing(10);

        VBox root = new VBox(new Label(selected.getCourseName()), progress, h1, h2, close);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        close.setOnMouseClicked((e) -> this.stage.close());

        populate(selected);

        this.scene = new Scene(root);
    }

    private void populate(Course c) {
        // populate list view
        Map<Module, Integer> averages = Database.getAverageCourseCompletion(c);

        ObservableList<String> list = FXCollections.observableArrayList();

        for (Map.Entry<Module, Integer> entry : averages.entrySet()) {
            list.add(entry.getValue().toString() + "% | " + entry.getKey().getTitle());
        }

        progress.setPrefWidth(300);
        progress.setMinWidth(300);
        progress.setItems(list);

        // populate related courses
        List<Course> related = Database.getCoursesRelatedTo(c);

        if (related.isEmpty()) {
            relatedCourses.setText("None");
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (Course relatedCourse : related) {
            sb.append(relatedCourse.getCourseName());
            sb.append("\n");
        }

        relatedCourses.setText(sb.toString());

        // populate amount of graduations
        amountOfGrads.setText(String.valueOf(Database.getTotalGraduationsOfCourse(c)));
    }
}
