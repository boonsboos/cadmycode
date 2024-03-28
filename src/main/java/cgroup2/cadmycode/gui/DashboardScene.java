package cgroup2.cadmycode.gui;

import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.user.Sex;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * shows a list of buttons that lead to the other scenes
 * and shows a table of the average percentage completion of content per sex
 */
public class DashboardScene extends SceneWrapper {

    private final Button webcast = new Button("Webcast");
    private final Button about = new Button("About");
    private final Button module = new Button("Module");
    private final Button course = new Button("Course");
    private final Button user = new Button("User");
    private final Button certificate = new Button("Certificate");

    private final Label averageViews = new Label();
    private final BarChart<String, Number> graduationsBySex;

    /**
     * creates an instance of {@link DashboardScene}
     * @param stage the stage on which the {@link DashboardScene} is to be drawn
     */
    public DashboardScene(Stage stage) {
        super(stage);

        Double malePercentage = Database.getPercentageOfCourseCompletionBySex(Sex.MALE);
        Double femalePercentage = Database.getPercentageOfCourseCompletionBySex(Sex.FEMALE);
        Double unknownPercentage = Database.getPercentageOfCourseCompletionBySex(Sex.UNKNOWN);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Sex");
        yAxis.setLabel("Percentage");

        graduationsBySex = new BarChart<>(xAxis, yAxis);
        graduationsBySex.setTitle("Percentage of Graduation by sex");

        XYChart.Series<String, Number> male = new XYChart.Series<>();
        male.setName("Male");
        male.getData().add(new XYChart.Data<>("", malePercentage));

        XYChart.Series<String, Number> female = new XYChart.Series<>();
        female.setName("Female");
        female.getData().add(new XYChart.Data<>("", femalePercentage));

        XYChart.Series<String, Number> unknown = new XYChart.Series<>();
        unknown.setName("Unknown");
        unknown.getData().add(new XYChart.Data<>("", unknownPercentage));

        graduationsBySex.getData().addAll(male, female, unknown);

        averageViews.setText(String.format("Average completion of content: %,.1f%%", Database.getAverageViewPercentage()));

        // this should become a list of buttons for the CRUD parts
        VBox v1 = new VBox(webcast, module, course, user, certificate);
        v1.setSpacing(10);

        TitledPane buttonMenu = new TitledPane("Overviews", v1);
        buttonMenu.setCollapsible(false);

        VBox v2 = new VBox(buttonMenu, about);
        v2.setSpacing(10);

        VBox statsVbox = new VBox(graduationsBySex, averageViews);
        v2.setSpacing(10);

        HBox root = new HBox(v2, statsVbox);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        webcast.setOnMouseClicked(this::onWebcastsButtonPressed);
        about.setOnMouseClicked(this::onAboutButtonPressed);
        module.setOnMouseClicked(this::onModuleButtonPressed);
        course.setOnMouseClicked(this:: onCourseButtonPressed);
        user.setOnMouseClicked(this::onUserButtonPressed);
        certificate.setOnMouseClicked(this::onCertificateButtonPressed);

        this.scene = new Scene(root);
    }

    /**
     * when triggered will open the {@link cgroup2.cadmycode.gui.webcast.WebcastScene} r
     * @param e represents the button that when clicked will trigger opening the {@link cgroup2.cadmycode.gui.webcast.WebcastScene}
     */
    private void onWebcastsButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.WEBCAST);
    }

    /**
     * when triggered will open the {@link AboutScene} r
     * @param e represents the button that when clicked will trigger opening the {@link AboutScene}
     */
    private void onAboutButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.ABOUT);
    }
    /**
     * when triggered will open the {@link cgroup2.cadmycode.gui.module.ModuleScene} r
     * @param e represents the button that when clicked will trigger opening the {@link cgroup2.cadmycode.gui.module.ModuleScene}
     */
    private void onModuleButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.MODULE);
    }
    /**
     * when triggered will open the {@link cgroup2.cadmycode.gui.course.CourseScene} r
     * @param e represents the button that when clicked will trigger opening the {@link cgroup2.cadmycode.gui.course.CourseScene}
     */
    private void onCourseButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.COURSE);
    }
    /**
     * when triggered will open the {@link cgroup2.cadmycode.gui.user.UserScene} r
     * @param e represents the button that when clicked will trigger opening the {@link cgroup2.cadmycode.gui.user.UserScene}
     */
    private void onUserButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.USER);
    }
    /**
     * when triggered will open the {@link cgroup2.cadmycode.content.Certificate} r
     * @param e represents the button that when clicked will trigger opening the {@link cgroup2.cadmycode.content.Certificate}
     */
    private void onCertificateButtonPressed(Event e) {
        GuiMain.SCENE_MANAGER.switchScene(SceneType.CERTIFICATE);
    }
}
