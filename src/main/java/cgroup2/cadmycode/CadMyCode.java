package cgroup2.cadmycode;

import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.database.TestDataCreator;
import cgroup2.cadmycode.gui.GuiMain;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.DriverManager;
import java.sql.SQLException;

public class CadMyCode {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You have to provide your database password as a commandline argument.");
            System.exit(1);
        }
        
        if (args.length == 2 && args[1].equals("-generate")) {
            TestDataCreator.generate(); // creates the mock user data
        }
        try {
            DriverManager.setLoginTimeout(20);
            Database.connect(DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=dev_codecademy;encrypt=false", "sa", args[0]));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to connect to the database. Try again later.\n" + e.getMessage(),
                    ButtonType.CLOSE).show();
        }

        Application.launch(GuiMain.class); // launch the GUI
    }

}
