package cgroup2.cadmycode.database;

import java.sql.Connection;
import java.sql.SQLException;

/** de API van deze klasse is static.
 * een instance maken is niet nodig.
 * je kan dus gewoon Database.getUserByID ofzo doen.
 */
public class Database {

    private static Connection databaseConnection;

    public static void connect(Connection conn) {
        databaseConnection = conn;
    }

    public static void disconnect() throws SQLException {
        if (databaseConnection == null) return;
        databaseConnection.close();
    }

    // gebruik alsjeblieft voor alle query methods Connection#prepareStatement() !
}
