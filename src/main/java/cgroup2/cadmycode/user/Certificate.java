package cgroup2.cadmycode.user;

public class Certificate {

    private int userID;
    private String createdby;
    private int grade;
    private int certID;
    private Enrollment enrollment;

    public Certificate(int userID, String createdby, int grade, int certID, Enrollment enrollment) {
        this.userID = userID;
        this.createdby = createdby;
        this.grade = grade;
        this.certID = certID;
        this.enrollment = enrollment;
    }

    public int getUserID() {
        return userID;
    }

    public String getCreatedby() {
        return createdby;
    }

    public int getGrade() {
        return grade;
    }

    public int getCertID() {
        return certID;
    }
}
