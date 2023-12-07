package cgroup2.cadmycode.content;

import java.util.ArrayList;

public class Course {
    private String courseName;
    private String subject;
    private String introductionText;
    private int courseID;
    private CourseLevel level;
    private ArrayList<Course> relatedCourses;

    public Course(String courseName, String subject, String introductionText, int courseID, CourseLevel level) {
        this.courseName = courseName;
        this.subject = subject;
        this.introductionText = introductionText;
        this.courseID = courseID;
        this.level = level;
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

    public ArrayList<Course> getRelatedCourses() {
        return relatedCourses;
    }

    public void addRelatedCourse(Course c) {
        this.relatedCourses.add(c);
    }

    public void addRelatedCourse(int courseID) {

    }
}
