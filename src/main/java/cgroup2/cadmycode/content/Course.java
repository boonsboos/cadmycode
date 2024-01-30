package cgroup2.cadmycode.content;

import java.util.ArrayList;

public class Course {
    private String courseName;
    private String subject;
    private String introductionText;
    private int courseID;
    private CourseLevel level;
    private ArrayList<Course> relatedCourses = new ArrayList<>();
    private int certificateID;

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

    public Course(String courseName, String subject, String introductionText, CourseLevel level, int certificateID) {
        this.courseName = courseName;
        this.subject = subject;
        this.introductionText = introductionText;
        this.level = level;
        this.certificateID = certificateID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSubject() {
        return subject;
    }

    public String getIntroductionText() {
        return introductionText;
    }

    public int getCourseID() {
        return courseID;
    }

    public CourseLevel getLevel() {
        return level;
    }

    public int getCertificateID() {
        return certificateID;
    }

    public ArrayList<Course> getRelatedCourses() {
        return relatedCourses;
    }

    public void addRelatedCourse(Course c) {
        this.relatedCourses.add(c);
    }

    public void addRelatedCourse(int courseID) {

    }
}
