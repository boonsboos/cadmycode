package cgroup2.cadmycode.database;

import cgroup2.cadmycode.content.ContentStatus;
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

    public static void createNewWebcast(
            String title,
            String description,
            LocalDate pubDate,
            ContentStatus status,
            int length,
            URL location,
            String organisation,
            String speaker
    ) {
        try {
            PreparedStatement insertInContent = databaseConnection.prepareStatement(
                    "INSERT INTO Content(title, abstract, publicationDate, contentStatus, contentVersion)\n" +
                            "VALUES(?, ?, ?, ?, ?)"
            );
            insertInContent.setString(1, title);
            insertInContent.setString(2, description);
            insertInContent.setDate(3, new Date(pubDate.getYear() - 1900 /* ??? what */, pubDate.getMonthValue()-1, pubDate.getDayOfMonth()));
            insertInContent.setInt(4, ContentStatus.asInt(status));
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
            int id = Database.getWebcastContentIdByTitle(title);
            if (id < 1) {
                SceneManager.showErrorDialog("Either the content ID is already taken or your webcast doesn't exist yet");
                return;
            }

            insertInWebcast.setInt(1, id);
            insertInWebcast.setInt(2, length);
            insertInWebcast.setString(3, location.toString());
            insertInWebcast.setString(4, organisation);
            insertInWebcast.setString(5, speaker);

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
            System.out.println(e.getMessage());
            // show error message
        }

        return list;
    }

    public static void deleteWebcastById(int id) {
        try {
            PreparedStatement deleteWebcast = databaseConnection.prepareStatement(
                    "DELETE FROM Webcast\n" +
                    "WHERE Webcast.contentItemID = ?;"
            );

            deleteWebcast.setInt(1, id);
            deleteWebcast.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }
    }

    public static void updateWebcast(Webcast w) {
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
            updateContent.setDate(3, new Date(
                    w.getPublicationDate().getYear() - 1900,
                    w.getPublicationDate().getMonthValue()-1,
                    w.getPublicationDate().getDayOfMonth()
            ));
            updateContent.setInt(4, ContentStatus.asInt(w.getStatus()));
            updateContent.setInt(5, id);

            updateContent.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }
    }
}
