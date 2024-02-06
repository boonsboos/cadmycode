package cgroup2.cadmycode.gui.user;

import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.SceneWrapper;
import cgroup2.cadmycode.user.User;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class UserStatisticsView extends SceneWrapper {

    private Button close = new Button("Close");
    private PieChart completedCourses = new PieChart();

    private final User selectedUser;

    public UserStatisticsView(Stage stage, User selected) {
        super(stage);

        this.selectedUser = selected;

        completedCourses.setTitle("Completed out of enrolled");

        VBox root = new VBox(completedCourses, close);
        root.setSpacing(20);
        root.setPadding(new Insets(10));

        close.setOnMouseClicked(this::onCloseButtonPressed);

        this.scene = new Scene(root);

        populate();
    }

    private void populate() {
        List<Double> chartData = Database.getPercentageOfCompletedCourses(selectedUser);

        completedCourses.setData(
            FXCollections.observableList(List.of(
                new PieChart.Data("Complete: " + String.format("%,.1f",chartData.get(0)) + "%", chartData.get(0)),
                new PieChart.Data("Incomplete: "+String.format("%,.1f",chartData.get(1)) + "%", chartData.get(1))
            ))
        );
    }

    private void onCloseButtonPressed(Event e) {
        this.stage.close();
    }
}
