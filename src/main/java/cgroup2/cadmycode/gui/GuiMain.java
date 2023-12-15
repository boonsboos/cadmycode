package cgroup2.cadmycode.gui;

import cgroup2.cadmycode.database.Database;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiMain extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Cadmycode");
        stage.show();

        new WebcastScene(stage);
    }

    @Override
    public void stop() throws Exception {
        Database.disconnect();
    }
}