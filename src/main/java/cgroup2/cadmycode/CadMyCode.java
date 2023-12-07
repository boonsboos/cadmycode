package cgroup2.cadmycode;

import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.gui.GuiMain;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.DriverManager;
import java.sql.SQLException;

public class CadMyCode {

    public static void main(String[] args) {
        try {
            DriverManager.setLoginTimeout(20);
            Database.connect(DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=dev_codecademy;encrypt=false", "sa", args[0]));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to connect to the database. Try again later.\n"+e.getMessage(), ButtonType.CLOSE).show();
        }

        Application.launch(GuiMain.class); // launch the GUI
    }

}
