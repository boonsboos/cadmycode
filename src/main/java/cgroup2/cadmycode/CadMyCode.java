package cgroup2.cadmycode;

import cgroup2.cadmycode.database.Database;
import cgroup2.cadmycode.database.TestDataCreator;
import cgroup2.cadmycode.gui.GuiMain;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * the Main class of the application that checks for password entry and tries a database connection
 * or catches an SQLException and prints a message
 */
public class CadMyCode {
    /**
     * creates an instance of {@link CadMyCode}
     * @param args an array of strings that stores the command-line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You have to provide your database password as a commandline argument.");
            System.exit(1);
        }

        try {
            DriverManager.setLoginTimeout(20);
            Database.connect(DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=dev_codecademy;encrypt=false", "sa", args[0]));
            if (args.length == 2 && args[1].equals("-generate")) {
                TestDataCreator.generate(); // creates the mock user data
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database. Try again later.\n"+e.getMessage());
        }

        Application.launch(GuiMain.class); // launch the GUI
    }

}
