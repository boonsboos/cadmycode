package cgroup2.cadmycode.user;

import java.time.LocalDateTime;

/**
 * a record of a users enrollment
 */
public class Enrollment {
    private int userID;
    private int courseID;
    private LocalDateTime time;

    /**
     * creates an instance of an enrollment
     * @param userID the ID of a user
     * @param courseID the ID if a course
     * @param time the time of enrollment
     */
    public Enrollment(int userID, int courseID, LocalDateTime time) {
        this.userID = userID;
        this.courseID = courseID;
        this.time = time;
    }

    /**
     * gets the user ID
     * @return the user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * gets the course ID
     * @return
     */
    public int getCourseID() {
        return courseID;
    }

    /**
     * gets the time of enrollment
     * @return the time of enrollment
     */
    public LocalDateTime getTime() {
        return time;
    }
}
