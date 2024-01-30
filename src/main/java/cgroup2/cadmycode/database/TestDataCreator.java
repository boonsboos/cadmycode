package cgroup2.cadmycode.database;

import cgroup2.cadmycode.content.*;
import cgroup2.cadmycode.content.Module;
import cgroup2.cadmycode.user.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

public class TestDataCreator {

    /** Generates data for testing in this order:
     * <ol>
     * <li>content</li>
     * <li>module</li>
     * <li>webcast</li>
     * <li>certificates</li>
     * <li>courses</li>
     * <li>courses -> modules</li>
     * <li>users</li>
     * <li>enrollments</li>
     * <li>progress</li>
     * <li>graduations</li>
     * </ol>
     *
     * For this, it uses the classes defined in
     * {@link cgroup2.cadmycode.content} and {@link cgroup2.cadmycode.user}
     */
    public static void generate() {

        System.out.println("Inserting test data...");

        /*
         Content table is inserted into at the same time as module and webcasts
        */

        List<Module> bobenismModules = new ArrayList<>(List.of(
            new Module(
                "Bobenism 101 - Introduction", "Introduction to Bobenistic programming - Basic concepts",
                LocalDate.of(2008, 10, 20), ContentStatus.ACTIVE, "B. Boben", "bope@bobenism.nl", 1
            ),
            new Module(
                "Bobenism 101 - Standard library", "Introduction to Bobenistic programming",
                LocalDate.of(2008, 10, 21), ContentStatus.ACTIVE, "B. Boben", "bope@bobenism.nl", 1
            ),
            new Module(
                "Bobenism 101 - Using C from Bobenistic programs", "Introduction to Bobenistic programming",
                LocalDate.of(2008, 10, 23), ContentStatus.ACTIVE, "B. Boben", "bope@bobenism.nl", 1
            ),
            new Module(
                "Bobenism 101 - Writing C bindings for Bobenistic programs", "Introduction to Bobenistic programming",
                LocalDate.of(2008, 10, 26), ContentStatus.ACTIVE, "B. Boben", "bope@bobenism.nl", 1
            ),
            new Module(
                "Bobenism 101 - Levaraging assembly to optimize code paths", "Introduction to Bobenistic programming",
                LocalDate.of(2008, 11, 1), ContentStatus.ACTIVE, "B. Boben", "bope@bobenism.nl", 1
            )
        ));
        for (Module m : bobenismModules) {
            Database.create(m);
        }

        List<Module> cModules = new ArrayList<>(List.of(
            new Module(
                "The C Programming Language 2.0 - Language basics", "Explaining the C Programming Language",
                LocalDate.of(1978, 2, 19), ContentStatus.ARCHIVE, "D. Ritchie", "dritchie@bell-labs.com", 2
            ),
            new Module(
                "The C Programming Language 2.0 - Leveraging POSIX", "Using POSIX methods to make a simple command line utility in C",
                LocalDate.of(1978, 2, 20), ContentStatus.ARCHIVE, "B. Kernighan", "bkernighan@bell-labs.com", 2
            )
        ));
        for (Module m : cModules) {
            Database.create(m);
        }

        List<Module> webModules = new ArrayList<>(List.of(
            new Module(
                "Basic page with HTML5", "Introductory HTML course.",
                LocalDate.of(2023, 1, 1), ContentStatus.ACTIVE, "TheNetNinja", "ninja@netninja.com", 5
            ),
            new Module(
                "Writing a todo app with React", "Expanding on HTML5 by writing a todo app using the React library",
                LocalDate.of(2023, 1, 2), ContentStatus.CONCEPT, "J. Delaney", "jeff@fireship.io", 5
            )
        ));
        for (Module m : webModules) {
            Database.create(m);
        }

        /*
         Webcasts
        */
        Database.create(new Webcast(
            "Wat", "by Destroy All Software", LocalDate.of(2012, 5, 2), ContentStatus.ACTIVE,
            257, "https://www.destroyallsoftware.com/talks/wat", "Gary Bernhardt", "Destroy All Software"
        ));
        Database.create(new Webcast(
            "What's new in Compiler Explorer?", "Showing cool new features in Compiler Explorer", LocalDate.of(2023, 9, 2), ContentStatus.ACTIVE,
            3332, "https://www.youtube.com/watch?v=O5sEug_iaf4", "Matt Godbolt", "godbolt.org"
        ));
        Database.create(new Webcast(
            "Ending Racism", "David Guetta explains how he plans to end racism", LocalDate.of(2023, 9, 2), ContentStatus.ARCHIVE,
            30, "https://www.youtube.com/watch?v=dEI7oX0XxJw", "David Guetta", ""
        ));

        /*
         Certificates
        */
        Database.create(new Certificate("Bobenistic Programming Expert 2008"));
        Database.create(new Certificate("The C Programming Language 2.0 Certification"));
        Database.create(new Certificate("Modern Web Development"));

        /*
         Courses

         Hacky and not easily extensible at the moment, but it works.
        */
        for (Certificate cert : Database.getCertificates()) {
            switch (cert.getCertificateName()) {
                case "Bobenistic Programming Expert 2008":
                    Database.create(new Course(
                        "Bobenism 101", "programming",
                        "This course serves as an upper-intermediate level introduction to Bobenistic programming.", CourseLevel.INTERMEDIATE,
                        cert.getCertificateID()
                    ));
                    break;
                case "The C Programming Language 2.0 Certification":
                    Database.create(new Course(
                        "The C Programming Language 2.0", "programming",
                        "This course serves as a beginner introduction to writing code in C.", CourseLevel.BEGINNER,
                        cert.getCertificateID()
                    ));
                    break;
                case "Modern Web Development":
                    Database.create(new Course(
                        "Modern Web Development", "web",
                        "Expert level course to improve your web development skills!", CourseLevel.EXPERT,
                        cert.getCertificateID()
                    ));
                    break;
                default:
                    break; // noop
            }
        }

        /*
         Add modules to the courses

         Yes, same thing.
        */
        List<Integer> courseIDs = new ArrayList<>();

        try {
            for (Course course: Database.getCourses()) {
                if (course.getCourseName().contains("Bobenism")) {
                    for (Module m : bobenismModules) {
                        m.setCourseID(Database.getModuleContentIdByTitle(m.getTitle()));
                    }
                    Database.addModulesToCourse(course, bobenismModules);
                }
                if (course.getCourseName().contains("C Programming Language 2.0")){
                    for (Module m : cModules) {
                        m.setCourseID(Database.getModuleContentIdByTitle(m.getTitle()));
                    }
                    Database.addModulesToCourse(course, cModules);
                }
                if (course.getCourseName().equals("Modern Web Development")) {
                    for (Module m : webModules) {
                        m.setCourseID(Database.getModuleContentIdByTitle(m.getTitle()));
                    }
                    Database.addModulesToCourse(course, webModules);
                }
                courseIDs.add(course.getCourseID());
            }
        } catch (SQLException e) {
            System.out.println("Failed to add modules to course:"+ e.getMessage());
            e.printStackTrace();
        }

        /*
         Users
        */
        Database.create(new User(
            "Niels Plug", "niels@joobiden.nl", "P.C. Hooftstraat 1", "NL", "Amsterdam", LocalDate.of(2002, 7, 1), Sex.MALE
        ));
        Database.create(new User(
            "Jonas Dingemans", "boons@boonsboos.nl", "Coolsingel 7", "NL", "Rotterdam", LocalDate.of(2005, 12, 13), Sex.MALE
        ));
        Database.create(new User(
            "Felix Baeten", "fb99fire@joobiden.nl", "Lange Poten 18", "NL", "Den Haag", LocalDate.of(1999, 7, 2), Sex.MALE
        ));

        /*
         Enrollments
        */
        for (User u : Database.getUsers()) {
            Database.create(new Enrollment(u.getUserID(), courseIDs.get(0), LocalDateTime.now()));
            Database.create(new Enrollment(u.getUserID(), courseIDs.get(1), LocalDateTime.now()));
            Database.create(new Enrollment(u.getUserID(), courseIDs.get(2), LocalDateTime.now()));
        }

        /*
         Progress
        */
        int boonsID = 0;

        for (User u: Database.getUsers()) {
            if (u.getEmail().equals("boons@boonsboos.nl")) {
                boonsID = u.getUserID();
                for (Module m : Database.getModules()) {
                    Database.create(new ViewedItem(m.getContentItemID(), u.getUserID(), 100));
                }
            } else {
                for (Module m : Database.getModules()) {
                    Database.create(new ViewedItem(m.getContentItemID(), u.getUserID(), 76));
                }
            }
        }

        /*
         Graduations
        */
        int bobenismCertID = 0;
        for (Certificate c : Database.getCertificates()) {
            if (c.getCertificateName().equals("Bobenistic Programming Expert 2008")) {
                bobenismCertID = c.getCertificateID();
            }
        }

        if (bobenismCertID == 0) return;
        Database.create(new Graduation(boonsID, "boons", 8, bobenismCertID));

        System.out.println("Complete!");
    }
}
