package cgroup2.cadmycode.gui;

import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.database.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;

public class GuiMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Cadmycode");
        stage.show();

        new WebcastScene(stage);
    }

    @Override
    public void stop() throws Exception {
        Database.disconnect();
    }
}