package cgroup2.cadmycode.user;

import java.time.LocalDateTime;

public class Enrollment {
    private int userID;
    private int courseID;
    private LocalDateTime time;

    public Enrollment(int userID, int courseID, LocalDateTime time) {
        this.userID = userID;
        this.courseID = courseID;
        this.time = time;
    }

    public int getUserID() {
        return userID;
    }

    public int getCourseID() {
        return courseID;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
