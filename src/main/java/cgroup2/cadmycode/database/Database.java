package cgroup2.cadmycode.database;

import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.except.FieldValidationException;
import cgroup2.cadmycode.content.*;
import cgroup2.cadmycode.except.FieldValidationException;
import cgroup2.cadmycode.gui.SceneManager;
import cgroup2.cadmycode.user.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The database class provides a low level abstraction to access the database.
 * Since all its methods are static, creating an instance is not necessary.
 *
 * Do note that all methods that do not explicitly throw an exception, will do nothing if an exception occurs.
 * E.g. a failing insert will show the error to the user and then silently return.
 * If a select fails, it will return an empty list.
 */
public class Database {

    private static Connection databaseConnection;

    /**
     * Stores a database connection to use it for future queries.
     * @param conn the connection to use
     */
    public static void connect(Connection conn) {
        databaseConnection = conn;
    }

    /**
     * Attempts to close the database connection gracefully.
     * @throws SQLException if something went wrong.
     */
    public static void disconnect() throws SQLException {
        if (databaseConnection == null) return;
        databaseConnection.close();
    }

    // if you are adding a new query method, please use Connection#prepareStatement() !

    /**
     * Converts a {@link LocalDate} to {@link Date}
     * @param local the date to convert
     * @return the date in SQL format
     */
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

    /**
     * Gets the content ID of a webcast by its title
     * @param title the title of the webcast
     * @return the content ID of the website
     * @throws SQLException if something goes wrong on the RDBMS side
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

    /**
     * Saves a new webcast in the database
     * @param w the webcast to save
     */
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

    /**
     * Get a list of all webcasts
     * @return {@link List}&lt;{@link Webcast}&gt; containing all webcasts
     */
    public static List<Webcast> getWebcasts() {

        ArrayList<Webcast> list = new ArrayList<>();

        try {
            PreparedStatement selectWebcasts = databaseConnection.prepareStatement(
                    "SELECT *\n"+
                    "FROM Webcast\n"+
                    "JOIN Content ON Webcast.contentItemID = Content.contentItemID\n"+
                    "ORDER BY Webcast.contentItemID ASC;"
            );

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

    /**
     * Deletes the provided webcast from the database
     * @param w the webcast to delete
     */
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

    /**
     * Updates the webcast in the database with the new data
     * @param w the webcast to update
     */
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

    /**
     * Get all modules from the database
     * @return {@link List}&lt;{@link Module}&gt;
     */
    public static List<Module> getModules() {

        ArrayList<Module> list = new ArrayList<>();

        try {
            PreparedStatement selectModules = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Module\n"+
                "JOIN Content ON Module.contentItemID = Content.contentItemID\n"+
                "ORDER BY Content.contentItemID ASC;\n"
            );

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

        } catch (FieldValidationException | SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    /**
     * Get a list of modules that are in the provided course.
     * @param course the course to get the modules of.
     * @return {@link List}&lt;{@link Module}&gt;
     */
    public static List<Module> getModulesByCourse(Course course) {
        ArrayList<Module> list = new ArrayList<>();

        try {
            PreparedStatement selectModules = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Module\n"+
                "JOIN Content ON Module.contentItemID = Content.contentItemID\n"+
                "WHERE courseID = ?\n"+
                "ORDER BY Content.contentItemID ASC;\n"
            );

            selectModules.setInt(1, course.getCourseID());

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

        } catch (FieldValidationException | SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    /**
     * Gets the module content item ID by title
     * @param title the title of the module
     * @return the content item ID of the module
     * @throws SQLException
     */
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

    /**
     * Saves a new module in the database
     * @param m the module to save
     */
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
            System.out.println(e.getMessage());
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

    /**
     * Update the provided module in the database
     * @param m the module to update
     */
    public static void update(Module m) {

        System.out.println(m);
        try {
            PreparedStatement updateModule = databaseConnection.prepareStatement(
                "UPDATE Module\n" +
                "SET contactName = ?,\n"+
                "courseID = ?,\n"+
                "contactEmail = ?\n"+
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
            updateModule.setInt(2, m.getCourseID());
            updateModule.setString(3, m.getContactEmail());
            updateModule.setInt(4, m.getContentItemID());
            updateModule.execute();

            updateContent.setString(1, m.getTitle());
            updateContent.setString(2, m.getDescription());
            updateContent.setDate(3, convertLocalDateToSQLDate(m.getPublicationDate()));
            updateContent.setInt(4, ContentStatus.asInt(m.getStatus()));
            updateContent.setInt(5, m.getContentItemID());

            updateContent.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            SceneManager.showErrorDialog(e.getMessage());
        }
    }

    /**
     * Deletes a module from the database
     * @param m the module to delete
     */
    public static void delete(Module m) {
        try {
            PreparedStatement deleteModule = databaseConnection.prepareStatement(
                "DELETE FROM Module\n" +
                "WHERE Module.contentItemID = ?;"
            );

            deleteModule.setInt(1, m.getContentItemID());
            deleteModule.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }

    /*
        Course queries
     */

    /**
     * Saves a new course to the database
     * @param c the course to save
     */
    public static void create(Course c) {
        try {
            PreparedStatement insertInCourse = databaseConnection.prepareStatement(
                "INSERT INTO Course (courseName, subj, introductionText, courseLevel, certificateID)\n"+
                "VALUES (?, ?, ?, ?, ?);"
            );

            insertInCourse.setString(1, c.getCourseName());
            insertInCourse.setString(2, c.getSubject());
            insertInCourse.setString(3, c.getIntroductionText());
            insertInCourse.setInt(4, c.getLevel().asInt());
            insertInCourse.setInt(5, c.getCertificateID());
            insertInCourse.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"Nothing has changed.");
        }
    }

    /**
     * Small helper method to set the course ID of multiple modules at once
     * @param c - the course which the modules are to be part of
     * @param modules - the list of modules to be added to the course
     */
    public static void addModulesToCourse(Course c, List<Module> modules) {
        for (Module m : modules) {
            m.setCourseID(c.getCourseID());
            update(m);
        }
    }

    /**
     * Get a list of all courses
     * @return {@link List}&lt;{@link Course}&gt;
     */
    public static List<Course> getCourses() {

        ArrayList<Course> list = new ArrayList<>();

        try {
            PreparedStatement selectModules = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Course\n"+
                "ORDER BY courseID ASC;\n"
            );

            ResultSet rs = selectModules.executeQuery();

            while (rs.next()) {
                list.add(new Course(
                    rs.getString("courseName"),
                    rs.getString("subj"),
                    rs.getString("introductionText"),
                    rs.getInt("courseID"),
                    CourseLevel.fromInt(rs.getInt("courseLevel")),
                    rs.getInt("certificateID")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    /** Returns the average completion of the modules that are part of the course
     * @param c the course to get average completion for
     * @return {@link java.util.Map}<{@link cgroup2.cadmycode.content.Module}, {@link java.lang.Integer}>
     */
    public static Map<Module, Integer> getAverageCourseCompletion(Course c) {
        List<Module> modules = getModulesByCourse(c);

        Map<Module, Integer> map = new HashMap<>();

        for (Module m : modules) {
            try {
                PreparedStatement average = databaseConnection.prepareStatement(
                    "SELECT AVG(viewed) AS average\n"+
                    "FROM ViewedItems\n"+
                    "WHERE contentItemID = ?;"
                );

                average.setInt(1, m.getContentItemID());

                ResultSet rs = average.executeQuery();

                while (rs.next()) {
                    map.put(m, rs.getInt("average"));
                }

            } catch (SQLException e) {
                SceneManager.showErrorDialog(e.getMessage());
                System.out.println(e.getMessage());
            }
        }

        return map;
    }

    /**
     * Returns a list of courses related to the provided course
     * @param c the course to get the related courses of
     * @return {@link List}&lt;{@link Course}&gt;
     */
    public static List<Course> getCoursesRelatedTo(Course c) {
        List<Course> list = new ArrayList<>();

        try {
           PreparedStatement relatedCourses = databaseConnection.prepareStatement(
               "SELECT TOP 3 *\n"+
               "FROM Course\n"+
               "WHERE subj = ? AND courseName <> ?"
           );

           relatedCourses.setString(1, c.getSubject());
           relatedCourses.setString(2, c.getCourseName());

           ResultSet rs = relatedCourses.executeQuery();

           while (rs.next()) {
               list.add(new Course(
                   rs.getString("courseName"),
                   rs.getString("subj"),
                   rs.getString("introductionText"),
                   rs.getInt("courseID"),
                   CourseLevel.fromInt(rs.getInt("courseLevel")),
                   rs.getInt("certificateID")
               ));
           }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
            System.out.println(e.getMessage());
        }

        return list;
    }

    /**
     * Updates a course in the database
     * @param c the course to update
     */
    public static void update(Course c) {
        try {
            PreparedStatement updateCourse = databaseConnection.prepareStatement(
                "UPDATE Course\n"+
                "SET courseLevel = ?,\n"+
                "introductionText = ?,\n"+
                "subj = ?,\n"+
                "courseName = ?,\n"+
                "certificateID = ?\n"+
                "WHERE courseID = ?;"
            );

            updateCourse.setInt(1, c.getLevel().asInt());
            updateCourse.setString(2, c.getIntroductionText());
            updateCourse.setString(3, c.getSubject());
            updateCourse.setString(4, c.getCourseName());
            updateCourse.setInt(5, c.getCertificateID());
            updateCourse.setInt(6, c.getCourseID());
            updateCourse.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has been changed.");
        }
    }

    /**
     * Deletes a course from the database
     * @param c the course to delete
     */
    public static void delete(Course c) {
        try {
            PreparedStatement deleteCourse = databaseConnection.prepareStatement(
                "DELETE FROM Course\n"+
                "WHERE courseID = ?;"
            );

            deleteCourse.setInt(1, c.getCourseID());
            deleteCourse.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has been changed.");
        }
    }

    /*
        Certificate queries
     */

    /**
     * Saves a new certificate to the database
     * @param c the certificate to save
     */
    public static void create(Certificate c) {
        try {
            PreparedStatement createCertificate = databaseConnection.prepareStatement(
                "INSERT INTO CMCCertificate (certificateName)\n"+
                "VALUES (?);" // wow
            );

            createCertificate.setString(1, c.getCertificateName());
            createCertificate.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has been submitted.");
        }
    }

    /**
     * Gets a list of all certificates
     * @return {@link List}&lt;{@link Certificate}&gt;
     */
    public static List<Certificate> getCertificates() {

        ArrayList<Certificate> list = new ArrayList<>();

        try {
            PreparedStatement getCerts = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM CMCCertificate\n"+
                "ORDER BY certificateID ASC\n"
            );

            ResultSet rs = getCerts.executeQuery();
            while (rs.next()) {
                list.add(new Certificate(
                    rs.getInt("certificateID"),
                    rs.getString("certificateName")
                ));
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    /**
     * Updates a certificate in the database
     * @param c the certificate to update
     */
    public static void update(Certificate c) {
        try {
            PreparedStatement updateCert = databaseConnection.prepareStatement(
                "UPDATE CMCCertificate\n"+
                "SET certificateName = ?\n"+
                "WHERE certificateID = ?;"
            );

            updateCert.setString(1, c.getCertificateName());
            updateCert.setInt(2, c.getCertificateID());
            updateCert.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }

    /**
     * Deletes the provided certificate from the database
     * @param c the certificate to delete
     */
    public static void delete(Certificate c) {
        try {
            PreparedStatement deleteCert = databaseConnection.prepareStatement(
                "DELETE FROM CMCCertificate\n"+
                "WHERE certificateID = ?;"
            );

            deleteCert.setInt(1, c.getCertificateID());
            deleteCert.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }

    /*
        User queries
     */

    /**
     * Saves a new user to the database
     * @param u the user to save
     */
    public static void create(User u) {
        try {
            PreparedStatement createUser = databaseConnection.prepareStatement(
                "INSERT INTO CMCUser (username, email, adres, country, houseNumber, dateOfBirth, sex)\n"+
                "VALUES (?, ?, ?, ?, ?, ?, ?);"
            );

            createUser.setString(1, u.getName());
            createUser.setString(2, u.getEmail());
            createUser.setString(3, u.getPostCode());
            createUser.setString(4, u.getCountry());
            createUser.setString(5, u.getHouseNumber());
            createUser.setDate(6, convertLocalDateToSQLDate(u.getDateOfBirth()));
            createUser.setInt(7, u.getSex().asInt());
            createUser.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }

    /**
     * Gets a list of all users
     * @return {@link List}&lt;{@link User}&gt;
     */
    public static List<User> getUsers() {

        ArrayList<User> list = new ArrayList<>();

        try {
            // avoiding naming conflicts like a pro
            PreparedStatement retrieveUsers = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM CMCUser\n"+
                "ORDER BY userID ASC;\n"
            );

            ResultSet rs = retrieveUsers.executeQuery();
            while (rs.next()) {
                list.add(new User(
                    rs.getInt("userID"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("adres"),
                    rs.getString("country"),
                    rs.getString("houseNumber"),
                    rs.getDate("dateOfBirth").toLocalDate(),
                    Sex.fromInt(rs.getInt("sex"))
                ));
            }
        } catch (SQLException | FieldValidationException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    /**
     * Updates the user in the databsae
     * @param u the user to update
     */
    public static void update(User u) {
        try {
            PreparedStatement updateUser = databaseConnection.prepareStatement(
                "UPDATE CMCUser\n"+
                "SET username = ?,\n"+
                "email = ?,\n"+
                "adres = ?,\n"+
                "country = ?,\n"+
                "houseNumber = ?,\n"+
                "sex = ?\n"+
                "WHERE userID = ?;"
            );

            updateUser.setString(1, u.getName());
            updateUser.setString(2, u.getEmail());
            updateUser.setString(3, u.getPostCode());
            updateUser.setString(4, u.getCountry());
            updateUser.setString(5, u.getHouseNumber());
            updateUser.setInt(6, u.getSex().asInt());
            updateUser.setInt(7, u.getUserID());
            updateUser.execute();

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }

    /**
     * Deletes a user from the database
     * @param u the user to delete
     */
    public static void delete(User u) {
        try {
            PreparedStatement deleteUser = databaseConnection.prepareStatement(
                "DELETE FROM CMCUser\n"+
                "WHERE userID = ?;"
            ); // this should all be set to cascade

            deleteUser.setInt(1, u.getUserID());
            deleteUser.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }

    /**
     * Gets the percentage of courses that a user has graduated.
     *  formulas:
     *  <ul>
     *  <li>grad / enroll * 100%</li>
     *  <li>(enroll - grad) / enroll * 100%</li>
     *  </ul>
     * @param u the User to get the percentage of
     * @return {@link List}&lt;{@link Double}&gt; with size 2.
     * If the query fails, the list contains [0.0, 0.0]
     */
    public static List<Double> getPercentageOfCompletedCourses(User u) {
        try {
            PreparedStatement countEnrollments = databaseConnection.prepareStatement(
                "SELECT COUNT(certificateID) AS enrolled\n"+
                "FROM Course\n"+
                "WHERE courseID IN (\n"+
                    "SELECT courseID\n"+
                    "FROM Enrollment\n"+
                    "WHERE userID = ?\n"+
                ");"
            );

            countEnrollments.setInt(1, u.getUserID());
            ResultSet enrollmentsResult = countEnrollments.executeQuery();

            PreparedStatement countGraduations = databaseConnection.prepareStatement(
                "SELECT COUNT(certificateID) AS graduated\n"+
                "FROM Graduation\n"+
                "WHERE userID = ?;\n"
            );

            countGraduations.setInt(1, u.getUserID());
            ResultSet graduationsResult = countGraduations.executeQuery();
            if (enrollmentsResult.next() && graduationsResult.next()) {
                return new ArrayList<>(List.of(
                        (double) graduationsResult.getInt("graduated") / (double) enrollmentsResult.getInt("enrolled") * 100,
                        (double) (enrollmentsResult.getInt("enrolled") - graduationsResult.getInt("graduated")) / (double) enrollmentsResult.getInt("enrolled") * 100
                ));
            }

        } catch (SQLException e) {
            System.out.println(e);
            SceneManager.showErrorDialog(e.getMessage()+"\nData temporarily unavailable.");
        }

        return new ArrayList<>(List.of(0.0, 0.0));
    }

    /**
     * Gets the percentage of total course completion
     * @return {@link List}&lt;{@link Double}&gt;
     * of which index 0 is enrolled and graduated, and index 1 is enrolled but not graduated
     * If the query fails, the list contains [0.0, 0.0]
     */
    public static List<Double> getPercentageOfCourseCompletion() {
        try {
            PreparedStatement countEnrollments = databaseConnection.prepareStatement(
                "SELECT COUNT(*) AS enrolled\n"+
                "FROM Enrollment;"
            );

            PreparedStatement countGraduations = databaseConnection.prepareStatement(
                "SELECT COUNT(*) AS graduated\n"+
                "FROM Graduation;"
            );

            ResultSet enrollmentsResult = countEnrollments.executeQuery();
            ResultSet graduationsResult = countGraduations.executeQuery();

            if (enrollmentsResult.next() && graduationsResult.next()) {
                return new ArrayList<>(List.of(
                    (double) graduationsResult.getInt("graduated") / (double) enrollmentsResult.getInt("enrolled") * 100,
                    (double) (enrollmentsResult.getInt("enrolled") - graduationsResult.getInt("graduated")) / (double) enrollmentsResult.getInt("enrolled") * 100
                ));
            }

        } catch (SQLException e) {
            System.out.println(e);
            SceneManager.showErrorDialog(e.getMessage()+"\nData temporarily unavailable.");
        }

        return new ArrayList<>(List.of(0.0, 0.0));
    }

    /**
     * Performs a query to get the percentage of graduations for a certain sex.
     * @param s the {@link cgroup2.cadmycode.user.Sex} to check for
     * @return {@link Double#NEGATIVE_INFINITY} if no enrollments, otherwise the percentage from 0 to 100
     */
    public static double getPercentageOfCourseCompletionBySex(Sex s) {
        try {
            PreparedStatement countEnrollments = databaseConnection.prepareStatement(
                "SELECT COUNT(*) AS enrolled\n"+
                "FROM Enrollment\n"+
                "JOIN CMCUser on Enrollment.userID=CMCUser.userID\n" +
                "WHERE sex = ?;"
            );

            PreparedStatement countGraduations = databaseConnection.prepareStatement(
                "SELECT COUNT(*) AS graduated\n"+
                "FROM Graduation\n" +
                "JOIN CMCUser on Graduation.userID=CMCUser.userID\n" +
                "WHERE sex = ?;"
            );

            countEnrollments.setInt(1, s.asInt());
            countGraduations.setInt(1, s.asInt());

            ResultSet enrollmentsResult = countEnrollments.executeQuery();
            ResultSet graduationsResult = countGraduations.executeQuery();

            // no need for while loop, only one record is returned
            if (enrollmentsResult.next() && graduationsResult.next()) {

                if (enrollmentsResult.getInt("enrolled") == 0) {
                    return 0; // if there are no enrollments
                    // nobody is able to graduate
                    // ergo return 0;
                }

                return ((double) graduationsResult.getInt("graduated") / (double) enrollmentsResult.getInt("enrolled")) * 100;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
            SceneManager.showErrorDialog(e.getMessage()+"\nData on sex-specific course completion temporarily unavailable.");
        }

        return Double.NEGATIVE_INFINITY;
    }


    /*
        Graduation queries
     */

    /**
     * Saves a new graduation to the database
     * @param g the graduation to save
     */
    public static void create(Graduation g) {
        try {
            PreparedStatement createGraduation = databaseConnection.prepareStatement(
                "INSERT INTO Graduation (userID, certificateID, grantedBy, grade)\n"+
                "VALUES (?, ?, ?, ?);"
            );

            createGraduation.setInt(1, g.getUserID());
            createGraduation.setInt(2, g.getCertID());
            createGraduation.setString(3, g.getGrantedBy());
            createGraduation.setInt(4, g.getGrade());
            createGraduation.execute();
        } catch (SQLException e) {
            System.out.println(e);
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has been submitted.");
        }
    }

    // Graduation is not updatable nor deletable by the administrator
    // it is instead deleted when a user is deleted

    /**
     * Gets a list of all graduations
     * @return {@link List}&lt;{@link Graduation}&gt;
     */
    public static List<Graduation> getGraduations() {

        ArrayList<Graduation> list = new ArrayList<>();

        try {
            PreparedStatement getGrads = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Graduation\n"+
                "ORDER BY graduationID ASC;"
            );

            ResultSet rs = getGrads.executeQuery();
            while (rs.next()) {
                list.add(new Graduation(
                    rs.getInt("graduationID"),
                    rs.getInt("userID"),
                    rs.getString("grantedBy"),
                    rs.getInt("grade"),
                    rs.getInt("certificateID")
                ));
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    /**
     * Gets a list of the graduations of a user
     * @param user the user to get the graduations of
     * @return {@link List}&lt;{@link Graduation}&gt;
     */
    public static List<Graduation> getGraduationsOfUser(User user) {

        ArrayList<Graduation> list = new ArrayList<>();

        try {
            PreparedStatement getGrads = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Graduation\n"+
                "WHERE userID = ?\n"+
                "ORDER BY graduationID ASC\n"
            );

            getGrads.setInt(1, user.getUserID());

            ResultSet rs = getGrads.executeQuery();
            while (rs.next()) {
                list.add(new Graduation(
                    rs.getInt("graduationID"),
                    rs.getInt("userID"),
                    rs.getString("grantedBy"),
                    rs.getInt("grade"),
                    rs.getInt("certificateID")
                ));
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    /**
     * Gets a table of graduations and their certificates of a user
     * @param user the user to get the data of
     * @return {@link Map}&lt;{@link Graduation}, {@link Certificate}&gt;
     */
    public static Map<Graduation, Certificate> getGraduationsOfUserWithCertificate(User user) {

        HashMap<Graduation, Certificate> map = new HashMap<>();

        try {
            PreparedStatement getGrads = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Graduation\n"+
                "JOIN CMCCertificate ON Graduation.certificateID = CMCCertificate.certificateID\n"+
                "WHERE userID = ?\n"+
                "ORDER BY graduationID ASC\n"
            );

            getGrads.setInt(1, user.getUserID());

            ResultSet rs = getGrads.executeQuery();
            while (rs.next()) {
                map.put(new Graduation(
                        rs.getInt("graduationID"),
                        rs.getInt("userID"),
                        rs.getString("grantedBy"),
                        rs.getInt("grade"),
                        rs.getInt("certificateID")
                    ),
                    new Certificate(rs.getString("certificateName"))
                );
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return map;
    }

    /**
     * Gets the amount of graduations of the course
     * @param c the course
     * @return the total amount
     */
    public static int getTotalGraduationsOfCourse(Course c) {
        try {
            PreparedStatement totalGrads = databaseConnection.prepareStatement(
                "SELECT COUNT(*) as total\n"+
                "FROM Graduation\n"+
                "WHERE certificateID = ?"
            );

            totalGrads.setInt(1, c.getCertificateID());

            ResultSet rs = totalGrads.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return 0;
    }

    /*
        Enrollment queries
     */

    /**
     * Saves a new enrollment to the database
     * @param e the enrollment to save
     */
    public static void create(Enrollment e) {
        try {
            PreparedStatement createEnroll = databaseConnection.prepareStatement(
                "INSERT INTO Enrollment (userID, courseID, enrollmentTime)\n"+
                "VALUES (?, ?, ?)"
            );

            createEnroll.setInt(1, e.getUserID());
            createEnroll.setInt(2, e.getCourseID());
            createEnroll.setTimestamp(3, Timestamp.from(e.getTime().toInstant(ZoneOffset.UTC)));
            createEnroll.execute();
        } catch (SQLException exc) {
            System.out.println(exc.getMessage());
            SceneManager.showErrorDialog(exc.getMessage()+"\nNothing has been submitted.");
        }
    }

    /**
     * Gets a list of all enrollments
     * @return {@link List}&lt;{@link Enrollment}&gt;
     */
    public static List<Enrollment> getEnrollments() {

        ArrayList<Enrollment> list = new ArrayList<>();

        try {
            PreparedStatement getEnrolls = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Enrollment\n"+
                "ORDER BY enrollmentTime DESC;" // show the latest enrollments first
            );

            ResultSet rs = getEnrolls.executeQuery();
            while (rs.next()) {
                list.add(new Enrollment(
                    rs.getInt("userID"),
                    rs.getInt("courseID"),
                    rs.getTimestamp("enrollmentTime").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    /**
     * Gets all enrollments of a certain user
     * @param user the user to get the enrollments of
     * @return {@link List}&lt;{@link Course}&gt;
     */
    public static List<Enrollment> getEnrollmentsForUser(User user) {

        ArrayList<Enrollment> list = new ArrayList<>();

        try {
            PreparedStatement getEnrolls = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Enrollment\n"+
                "WHERE userID = ?\n"+
                "ORDER BY enrollmentTime DESC;"
            );

            getEnrolls.setInt(1, user.getUserID());

            ResultSet rs = getEnrolls.executeQuery();
            while (rs.next()) {
                list.add(new Enrollment(
                    rs.getInt("userID"),
                    rs.getInt("courseID"),
                    rs.getTimestamp("enrollmentTime").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    // Enrollments do not need to be updated or deleted manually

    /*
        Viewed items queries
     */

    /**
     * Saves a new {@link ViewedItem} to the database
     * @param v the item to save
     */
    public static void create(ViewedItem v) {
        try {
            PreparedStatement createViewed = databaseConnection.prepareStatement(
                "INSERT INTO ViewedItems (contentItemID, userID, viewed)\n"+
                "VALUES (?, ?, ?);"
            );

            createViewed.setInt(1, v.getContentItemID());
            createViewed.setInt(2, v.getUserID());
            createViewed.setInt(3, v.getViewed());
            createViewed.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has been submitted.");
        }
    }

    /**
     * Gets a list of all viewed items
     * @return {@link List}&lt;{@link ViewedItem}&gt;
     */
    public static List<ViewedItem> getViewedItems() {

        ArrayList<ViewedItem> list = new ArrayList<>();

        try {
            PreparedStatement getViewed = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM ViewedItems;\n"
            );

            ResultSet rs = getViewed.executeQuery();
            while (rs.next()) {
                list.add(new ViewedItem(
                    rs.getInt("contentItemID"),
                    rs.getInt("userID"),
                    rs.getInt("viewed")
                ));
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    /**
     * Fetches the ViewedItem of the provided content items for a specific user.
     * @param u the user to search for
     * @param contentItems the list of content items
     * @return list of {@link cgroup2.cadmycode.user.ViewedItem}
     */
    public static List<ViewedItem> getViewedItemOfContentItemByUser(User u, List<? extends EducationalContent> contentItems) {

        if (contentItems.isEmpty()) {
            return new ArrayList<>();
        }

        // convert content item list to ID list string
        StringBuilder builder = new StringBuilder();
        builder.append("(");

        for (EducationalContent item : contentItems) {
            builder.append(item.getContentItemID());
            builder.append(',');
        }

        // replace the trailing comma
        // this is the same as StringBuilder#replace, just slightly more clear
        builder.deleteCharAt(builder.length()-1);
        builder.append(")");

        ArrayList<ViewedItem> list = new ArrayList<>();

        try {
            PreparedStatement getViewed = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM ViewedItems\n"+
                "WHERE contentItemID IN " + builder + " AND userID = ?;\n" // unable to pass the string builder
            );

            getViewed.setInt(1, u.getUserID());
            ResultSet rs = getViewed.executeQuery();

            while (rs.next()) {
                list.add(new ViewedItem(
                    rs.getInt("contentItemID"),
                    rs.getInt("userID"),
                    rs.getInt("viewed")
                ));
            }

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Updates a viewed item in the database
     * @param v the item to update
     */
    public static void update(ViewedItem v) {
        try {
            PreparedStatement updateViewed = databaseConnection.prepareStatement(
                "UPDATE ViewedItems\n"+
                "SET viewed = ?\n"+
                "WHERE contentItemID = ?\n"+
                "AND userID = ?;"
            );

            updateViewed.setInt(1, v.getViewed());
            updateViewed.setInt(2, v.getContentItemID());
            updateViewed.setInt(3, v.getUserID());
            updateViewed.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has been changed.");
        }
    }

    /**
     * Gets the total average percentage of content viewed of all viewed items
     * @return the percentage
     */
    public static double getAverageViewPercentage() {
        try {
            PreparedStatement updateViewed = databaseConnection.prepareStatement(
                    "SELECT AVG(CAST(viewed as FLOAT)) as average\n"+
                    "FROM ViewedItems;"
            );

            ResultSet rs = updateViewed.executeQuery();
            if (rs.next()) {
                return rs.getFloat("average");
            }
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has been changed.");
        }

        return 0;
    }

    // viewed items do not need to be deleted by the administrator
    // as when a user is deleted, the record cascades.
}