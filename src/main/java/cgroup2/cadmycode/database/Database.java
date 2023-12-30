package cgroup2.cadmycode.database;

import cgroup2.cadmycode.content.ContentStatus;
import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.content.Webcast;
import cgroup2.cadmycode.gui.SceneManager;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

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

    private static Date convertLocalDateToSQLDate(LocalDate local) {
        return new Date(
                local.getYear() - 1900,
                local.getMonthValue() - 1,
                local.getDayOfMonth()
        );
    }

    /*
        Webcast queries
     */

    public static int getWebcastContentIdByTitle(String title) throws SQLException {
        PreparedStatement s = databaseConnection.prepareStatement(
                "SELECT contentItemID\n"+
                "FROM Content\n"+
                "WHERE title = ? AND contentVersion = 0"
        );

        s.setString(1, title);

        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            return rs.getInt("contentItemID");
        }

        return -1;
    }

    public static void create(Webcast w) {
        try {
            PreparedStatement insertInContent = databaseConnection.prepareStatement(
                    "INSERT INTO Content(title, abstract, publicationDate, contentStatus, contentVersion)\n" +
                            "VALUES(?, ?, ?, ?, ?)"
            );
            insertInContent.setString(1, w.getTitle());
            insertInContent.setString(2, w.getDescription());
            insertInContent.setDate(3, convertLocalDateToSQLDate(w.getPublicationDate()));
            insertInContent.setInt(4, ContentStatus.asInt(w.getStatus()));
            insertInContent.setInt(5, 0);

            insertInContent.execute(); // we first need to have a valid ID
        } catch (SQLException e){
            SceneManager.showErrorDialog(e.getMessage());
        }

        // if the first conflicts we can still try to add it to webcast
        try {
            PreparedStatement insertInWebcast = databaseConnection.prepareStatement(
                    "INSERT INTO Webcast\n"+
                    "VALUES (?, ?, ?, ?, ?);"
            );
            int id = Database.getWebcastContentIdByTitle(w.getTitle());
            if (id < 1) {
                SceneManager.showErrorDialog("Either the content ID is already taken or your webcast doesn't exist yet");
                return;
            }

            insertInWebcast.setInt(1, id);
            insertInWebcast.setInt(2, w.getLength());
            insertInWebcast.setString(3, w.getURL());
            insertInWebcast.setString(4, w.getOrganisation());
            insertInWebcast.setString(5, w.getSpeaker());

            insertInWebcast.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }
    }

    public static ArrayList<Webcast> getWebcasts() {
        return getWebcasts(0);
    }

    public static ArrayList<Webcast> getWebcasts(int offset) {

        ArrayList<Webcast> list = new ArrayList<>();

        try {
            PreparedStatement selectWebcasts = databaseConnection.prepareStatement(
                    "SELECT *\n"+
                    "FROM Webcast\n"+
                    "JOIN Content ON Webcast.contentItemID = Content.contentItemID\n"+
                    "ORDER BY Webcast.contentItemID ASC\n"+
                    "OFFSET ? ROWS\n"+
                    "FETCH NEXT 15 ROWS ONLY;"
            );

            selectWebcasts.setInt(1, offset);

            ResultSet rs = selectWebcasts.executeQuery();

            while (rs.next()) {
                list.add(new Webcast(
                        rs.getInt("contentItemID"),
                        rs.getString("title"),
                        rs.getString("abstract"),
                        rs.getDate("publicationDate").toLocalDate(),
                        ContentStatus.fromInt(rs.getInt("contentStatus")),
                        rs.getInt("contentLength"),
                        rs.getString("webAddress"),
                        rs.getString("speaker"),
                        rs.getString("organisation")
                ));
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    public static void delete(Webcast w) {
        try {
            PreparedStatement deleteWebcast = databaseConnection.prepareStatement(
                    "DELETE FROM Webcast\n" +
                    "WHERE Webcast.contentItemID = ?;"
            );

            deleteWebcast.setInt(1, w.getContentItemID());
            deleteWebcast.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }

    public static void update(Webcast w) {
        try {
            int id = getWebcastContentIdByTitle(w.getTitle());

            PreparedStatement updateWebcast = databaseConnection.prepareStatement(
                    "UPDATE Webcast\n" +
                    "SET contentLength = ?,\n"+
                    "webAddress = ?,\n"+
                    "speaker = ?,\n"+
                    "organisation = ?\n"+
                    "WHERE contentItemID = ?;"
            );

            PreparedStatement updateContent = databaseConnection.prepareStatement(
                    "UPDATE Content\n" +
                    "SET title = ?,\n"+
                    "abstract = ?,\n"+
                    "publicationDate = ?,\n"+
                    "contentStatus = ?\n"+
                    "WHERE contentItemID = ?;"
            );

            updateWebcast.setInt(1, w.getLength());
            updateWebcast.setString(2, w.getURL());
            updateWebcast.setString(3, w.getSpeaker());
            updateWebcast.setString(4, w.getOrganisation());
            updateWebcast.setInt(5, id);
            updateWebcast.execute();

            updateContent.setString(1, w.getTitle());
            updateContent.setString(2, w.getDescription());
            updateContent.setDate(3, convertLocalDateToSQLDate(w.getPublicationDate()));
            updateContent.setInt(4, ContentStatus.asInt(w.getStatus()));
            updateContent.setInt(5, id);

            updateContent.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }
    }

    /*
        Module queries
     */

    public static ArrayList<Module> getModules(int offset) {

        ArrayList<Module> list = new ArrayList<>();

        try {
            PreparedStatement selectModules = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Module\n"+
                "JOIN Content ON Module.contentItemID = Content.contentItemID\n"+
                "ORDER BY Content.contentItemID ASC\n"+
                "OFFSET ? ROWS\n"+
                "FETCH NEXT 15 ROWS ONLY;"
            );

            selectModules.setInt(1, offset);

            ResultSet rs = selectModules.executeQuery();

            while (rs.next()) {
                list.add(new Module(
                    rs.getInt("contentItemID"),
                    rs.getString("title"),
                    rs.getString("abstract"),
                    rs.getDate("publicationDate").toLocalDate(),
                    ContentStatus.fromInt(rs.getInt("contentStatus")),
                    rs.getString("contactName"),
                    rs.getString("contactEmail"),
                    rs.getInt("courseID"),
                    rs.getInt("contentVersion")
                ));
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    public static ArrayList<Module> getModules() {
        return getModules(0);
    }

    public static int getModuleContentIdByTitle(String title) throws SQLException {
        PreparedStatement s = databaseConnection.prepareStatement(
            "SELECT contentItemID\n"+
            "FROM Content\n"+
            "WHERE title = ? AND contentVersion > 0;" // modules version always non-zero
        );

        s.setString(1, title);

        ResultSet rs = s.executeQuery();
        while (rs.next()) {
            return rs.getInt("contentItemID");
        }

        return -1;
    }

    public static void create(Module m) {
        try {
            PreparedStatement insertInContent = databaseConnection.prepareStatement(
                "INSERT INTO Content(title, abstract, publicationDate, contentStatus, contentVersion)\n" +
                "VALUES(?, ?, ?, ?, ?)"
            );
            insertInContent.setString(1, m.getTitle());
            insertInContent.setString(2, m.getDescription());
            insertInContent.setDate(3, convertLocalDateToSQLDate(m.getPublicationDate()));
            insertInContent.setInt(4, ContentStatus.asInt(m.getStatus()));
            insertInContent.setInt(5, m.getVersion());

            insertInContent.execute();
        } catch (SQLException e){
            SceneManager.showErrorDialog(e.getMessage());
        }

        try {
            PreparedStatement insertInModule = databaseConnection.prepareStatement(
                "INSERT INTO Module\n"+
                "VALUES (?, ?, ?, ?);"
            );

            int id = Database.getModuleContentIdByTitle(m.getTitle());
            if (id < 1) {
                SceneManager.showErrorDialog("Either the content ID is already taken or your module doesn't exist yet");
                return;
            }

            insertInModule.setInt(1, id);
            insertInModule.setString(2, m.getContactName());
            insertInModule.setString(3, m.getContactEmail());
            insertInModule.setNull(4, Types.INTEGER);
            // we can't have a course for a module that has just been created
            // a new module always has no course ID

            insertInModule.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }
    }

    public static void update(Module m) {
        try {
            int id = getModuleContentIdByTitle(m.getTitle());

            PreparedStatement updateModule = databaseConnection.prepareStatement(
                    "UPDATE Webcast\n" +
                    "SET contactName = ?,\n"+
                    "contactEmail = ?,\n"+
                    "WHERE contentItemID = ?;"
            );

            PreparedStatement updateContent = databaseConnection.prepareStatement(
                    "UPDATE Content\n" +
                    "SET title = ?,\n"+
                    "abstract = ?,\n"+
                    "publicationDate = ?,\n"+
                    "contentStatus = ?\n"+
                    "WHERE contentItemID = ?;"
            );

            updateModule.setString(1, m.getContactName());
            updateModule.setString(3, m.getContactEmail());
            updateModule.setInt(4, id);
            updateModule.execute();

            updateContent.setString(1, m.getTitle());
            updateContent.setString(2, m.getDescription());
            updateContent.setDate(3, convertLocalDateToSQLDate(m.getPublicationDate()));
            updateContent.setInt(4, ContentStatus.asInt(m.getStatus()));
            updateContent.setInt(5, id);

            updateContent.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }
    }

    public static void delete(Module m) {
        try {
            PreparedStatement deleteWebcast = databaseConnection.prepareStatement(
                    "DELETE FROM Webcast\n" +
                            "WHERE Webcast.contentItemID = ?;"
            );

            deleteWebcast.setInt(1, m.getContentItemID());
            deleteWebcast.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }
}
