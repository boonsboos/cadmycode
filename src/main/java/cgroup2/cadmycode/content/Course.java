package cgroup2.cadmycode.content;

import java.util.ArrayList;

/**
 * a course object that a user can enroll in
 */
public class Course {
    private String courseName;
    private String subject;
    private String introductionText;
    private int courseID;
    private CourseLevel level;
    private ArrayList<Course> relatedCourses = new ArrayList<>();
    private int certificateID;

    /**
     * creates an instant of a course
     * @param courseName the course name
     * @param subject the course subject
     * @param introductionText the introduction text
     * @param courseID the course ID
     * @param level the {@link CourseLevel} level
     * @param certificateID the certificate ID
     */
    public Course(
            String courseName,
            String subject,
            String introductionText,
            int courseID,
            CourseLevel level,
            int certificateID
    ) {
        this.courseName = courseName;
        this.subject = subject;
        this.introductionText = introductionText;
        this.courseID = courseID;
        this.level = level;
        this.certificateID = certificateID;
    }

    /**
     * creates an instant of a course
     * @param courseName the course name
     * @param subject the course subject
     * @param introductionText the introduction text
     * @param level the {@link CourseLevel} level
     * @param certificateID the certificate ID
     */
    public Course(String courseName, String subject, String introductionText, CourseLevel level, int certificateID) {
        this.courseName = courseName;
        this.subject = subject;
        this.introductionText = introductionText;
        this.level = level;
        this.certificateID = certificateID;
    }

    /**
     * gets the course name
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * gets the course subject
     * @return the course subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * gets the introduction text
     * @return the introduction text
     */
    public String getIntroductionText() {
        return introductionText;
    }

    /**
     * gets the course ID
     * @return the course ID
     */
    public int getCourseID() {
        return courseID;
    }

    /**
     * gets the course level
     * @return the course level
     */
    public CourseLevel getLevel() {
        return level;
    }

    /**
     * gets the certificate ID
     * @return the certificate ID
     */
    public int getCertificateID() {
        return certificateID;
    }

    /**
     * gets the related courses
     * @return the related courses
     */
    public ArrayList<Course> getRelatedCourses() {
        return relatedCourses;
    }

    /**
     * ads related courses
     * @param c the related course to be added
     */
    public void addRelatedCourse(Course c) {
        this.relatedCourses.add(c);
    }

    /**
     * ads related courses
     * @param courseID the course ID to be added
     */
    public void addRelatedCourse(int courseID) {

    }
}
