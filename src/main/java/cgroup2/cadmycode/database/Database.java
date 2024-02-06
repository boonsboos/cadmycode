package cgroup2.cadmycode.database;

import cgroup2.cadmycode.content.*;
import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.gui.SceneManager;
import cgroup2.cadmycode.user.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Webcast> getWebcasts() {
        return getWebcasts(0);
    }

    public static List<Webcast> getWebcasts(int offset) {

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

    public static List<Module> getModules(int offset) {

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

    public static List<Module> getModules() {
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

    public static void update(Module m) {
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

    public static List<Course> getCourses() {
        return getCourses(0);
    }

    public static List<Course> getCourses(int offset) {

        ArrayList<Course> list = new ArrayList<>();

        try {
            PreparedStatement selectModules = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Course\n"+
                "ORDER BY courseID ASC\n"+
                "OFFSET ? ROWS\n"+
                "FETCH NEXT 15 ROWS ONLY;"
            );

            selectModules.setInt(1, offset);

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

    public static List<Certificate> getCertificates() {
        return getCertificates(0);
    }

    public static List<Certificate> getCertificates(int offset) {

        ArrayList<Certificate> list = new ArrayList<>();

        try {
            PreparedStatement getCerts = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM CMCCertificate\n"+
                "ORDER BY certificateID ASC\n"+
                "OFFSET ? ROWS\n"+
                "FETCH NEXT 15 ROWS ONLY;"
            );

            getCerts.setInt(1, offset);
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

    public static void create(User u) {
        try {
            PreparedStatement createUser = databaseConnection.prepareStatement(
                "INSERT INTO CMCUser (username, email, adres, country, city, dateOfBirth, sex)\n"+
                "VALUES (?, ?, ?, ?, ?, ?, ?);"
            );

            createUser.setString(1, u.getName());
            createUser.setString(2, u.getEmail());
            createUser.setString(3, u.getAddress());
            createUser.setString(4, u.getCountry());
            createUser.setString(5, u.getCity());
            createUser.setDate(6, convertLocalDateToSQLDate(u.getDateOfBirth()));
            createUser.setInt(7, u.getSex().asInt());
            createUser.execute();
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }

    public static List<User> getUsers() {
        return getUsers(0);
    }

    public static List<User> getUsers(int offset) {

        ArrayList<User> list = new ArrayList<>();

        try {
            // avoiding naming conflicts like a pro
            PreparedStatement retrieveUsers = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM CMCUser\n"+
                "ORDER BY userID ASC\n"+
                "OFFSET ? ROWS\n"+
                "FETCH FIRST 15 ROWS ONLY;"
            );

            retrieveUsers.setInt(1, offset);

            ResultSet rs = retrieveUsers.executeQuery();
            while (rs.next()) {
                list.add(new User(
                    rs.getInt("userID"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("adres"),
                    rs.getString("country"),
                    rs.getString("city"),
                    rs.getDate("dateOfBirth").toLocalDate(),
                    Sex.fromInt(rs.getInt("sex"))
                ));
            }
        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage());
        }

        return list;
    }

    public static void update(User u) {
        try {
            PreparedStatement updateUser = databaseConnection.prepareStatement(
                "UPDATE CMCUser\n"+
                "SET username = ?,\n"+
                "email = ?,\n"+
                "adres = ?,\n"+
                "country = ?,\n"+
                "city = ?,\n"+
                "sex = ?\n"+
                "WHERE userID = ?;"
            );

            updateUser.setString(1, u.getName());
            updateUser.setString(2, u.getEmail());
            updateUser.setString(3, u.getAddress());
            updateUser.setString(4, u.getCountry());
            updateUser.setString(5, u.getCity());
            updateUser.setInt(6, u.getSex().asInt());
            updateUser.setInt(7, u.getUserID());
            updateUser.execute();

        } catch (SQLException e) {
            SceneManager.showErrorDialog(e.getMessage()+"\nNothing has changed.");
        }
    }

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
     * @return {@link:java.util.List} of {@link:java.lang.Double} with size 2.
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
            while (enrollmentsResult.next() && graduationsResult.next()) {
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

            while (enrollmentsResult.next() && graduationsResult.next()) {
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

    public static double getPercentageOfCourseCompletionBySex(Sex s) {
        try {
            PreparedStatement countEnrollments = databaseConnection.prepareStatement(
                "SELECT COUNT(*) AS enrolled\n"+
                "FROM Enrollment\n" +
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

            while (enrollmentsResult.next() && graduationsResult.next()) {
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

    public static List<Graduation> getGraduations() {
        return getGraduations(0);
    }

    public static List<Graduation> getGraduations(int offset) {

        ArrayList<Graduation> list = new ArrayList<>();

        try {
            PreparedStatement getGrads = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Graduation\n"+
                "ORDER BY graduationID ASC\n"+
                "OFFSET ? ROWS\n"+
                "FETCH FIRST 15 ROWS ONLY"
            );

            getGrads.setInt(1, offset);

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

    /*
        Enrollment queries
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

    public static List<Enrollment> getEnrollments() {
        return getEnrollments(0);
    }

    public static List<Enrollment> getEnrollments(int offset) {

        ArrayList<Enrollment> list = new ArrayList<>();

        try {
            PreparedStatement getEnrolls = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Enrollment\n"+
                "ORDER BY enrollmentTime DESC\n"+ // show the latest enrollments first
                "OFFSET ? ROWS\n"+
                "FETCH FIRST 15 ROWS ONLY"
            );

            getEnrolls.setInt(1, offset);

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

    // Enrollments do not need to be updated or deleted

    /*
        Viewed items queries
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

    public static List<ViewedItem> getViewedItems() {
        return getViewedItems(0);
    }

    public static List<ViewedItem> getViewedItems(int offset) {

        ArrayList<ViewedItem> list = new ArrayList<>();

        try {
            PreparedStatement getViewed = databaseConnection.prepareStatement(
                "SELECT *\n"+
                "FROM Enrollment\n"+
                "ORDER BY enrollmentTime DESC\n"+ // show the latest enrollments first
                "OFFSET ? ROWS\n"+
                "FETCH FIRST 15 ROWS ONLY"
            );

            getViewed.setInt(1, offset);

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

    // viewed items do not need to be deleted by the administrator
    // as when a user is deleted, the record cascades.

}
