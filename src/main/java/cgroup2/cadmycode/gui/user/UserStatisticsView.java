package cgroup2.cadmycode.gui.user;

import cgroup2.cadmycode.content.Certificate;
import cgroup2.cadmycode.content.Course;
import cgroup2.cadmycode.content.EducationalContent;
import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.SceneWrapper;
import cgroup2.cadmycode.gui.course.CourseStatisticsView;
import cgroup2.cadmycode.user.Enrollment;
import cgroup2.cadmycode.user.Graduation;
import cgroup2.cadmycode.user.User;
import cgroup2.cadmycode.user.ViewedItem;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * shows the statistics of the selected User
 */
public class UserStatisticsView extends SceneWrapper {

    private Button close = new Button("Close");
    private PieChart completedCourses = new PieChart();

    private ComboBox<String> courseSelect = new ComboBox<>();
    private ListView<String> courseProgress = new ListView<>();

    private ListView<Certificate> graduationsList = new ListView<>();

    private final User selectedUser;
    private HashMap<String, Course> coursesMap = new HashMap<>();

    /**
     * creates an instance of a {@link UserStatisticsView}
     * @param stage the stage on which the {@link UserStatisticsView} is to be drawn
     * @param selected the selected {@link User}
     */
    public UserStatisticsView(Stage stage, User selected) {
        super(stage);

        this.selectedUser = selected;

        completedCourses.setTitle("Completed out of enrolled");

        HBox h1 = new HBox(completedCourses, graduationsList, new VBox(courseSelect, courseProgress));
        h1.setSpacing(20);

        VBox root = new VBox(h1, close);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        close.setOnMouseClicked(this::onCloseButtonPressed);

        this.scene = new Scene(root);

        populate();

        // not in populate because it depends on the selection
        courseSelect.getSelectionModel().selectedItemProperty().addListener((options, old, newSelection) ->
            calculateAverageProgress(newSelection) // calculate the average progress when the value changes
        );
    }

    /**
     * populates the {@link UserStatisticsView} with the percentages, completed and incomplete, of all the courses
     */
    private void populate() {
        // filter enrollments for the user
        gatherCourseEnrollments();
        courseSelect.setItems(FXCollections.observableList(new ArrayList<>(coursesMap.keySet())));

        List<Double> chartData = Database.getPercentageOfCompletedCourses(selectedUser);

        completedCourses.setData(
            FXCollections.observableList(List.of(
                new PieChart.Data("Complete: " + String.format("%,.1f",chartData.get(0)) + "%", chartData.get(0)),
                new PieChart.Data("Incomplete: "+String.format("%,.1f",chartData.get(1)) + "%", chartData.get(1))
            ))
        );

        Map<Graduation, Certificate> grads = Database.getGraduationsOfUserWithCertificate(selectedUser);
        graduationsList.setItems(FXCollections.observableList(new ArrayList<>(grads.values())));
    }

    /**
     * gathers the course enrollments
     */
    private void gatherCourseEnrollments() {
        for (Course c : Database.getCourses()) {
            coursesMap.put(c.getCourseName(), c);
        }

        List<Enrollment> enrollments = Database.getEnrollmentsForUser(selectedUser);
        List<Integer> enrolledCourseIDs = new ArrayList<>();
        // get course IDs for enrollments
        for (Enrollment e : enrollments) {
            enrolledCourseIDs.add(e.getCourseID());
        }

        // remove courses that the user is not enrolled in
        for (var courseEntry : coursesMap.entrySet()) {
            if (!enrolledCourseIDs.contains(courseEntry.getValue().getCourseID()))
                coursesMap.remove(courseEntry.getKey());
        }
    }

    /**
     * calculates the average progress in percentage
     * @param courseName the name of the course
     */
    private void calculateAverageProgress(String courseName) {

        // look up all modules with this course ID
        List<Module> modules = Database.getModulesByCourse(coursesMap.get(courseName));

        // get viewed items of the modules
        List<ViewedItem> items = Database.getViewedItemOfContentItemByUser(selectedUser, modules);

        List<String> progress = new ArrayList<>();
        for (ViewedItem item : items) {
            for (Module m : modules) { // this makes it a bit slow
                if (m.getContentItemID() == item.getContentItemID())
                    progress.add(String.format("%d%%", item.getViewed())  + " | " + m.getTitle());
            }
        }

        courseProgress.setItems(FXCollections.observableList(progress));
    }

    /**
     * closes the {@link UserStatisticsView}
     * @param e represents the button that when clicked will trigger closing the popup
     */
    private void onCloseButtonPressed(Event e) {
        this.stage.close();
    }
}
