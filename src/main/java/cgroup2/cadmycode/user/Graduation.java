package cgroup2.cadmycode.user;

public class Graduation {

    private int graduationID;
    private int userID;
    private String grantedBy;
    private int grade;
    private int certID;

    public Graduation(int graduationID, int userID, String grantedBy, int grade, int certID) {
        this.graduationID = graduationID;
        this.userID = userID;
        this.grantedBy = grantedBy;
        this.grade = grade;
        this.certID = certID;
    }

    public int getGraduationID() {
        return this.graduationID;
    }

    public int getUserID() {
        return userID;
    }

    public String getGrantedBy() {
        return grantedBy;
    }

    public int getGrade() {
        return grade;
    }

    public int getCertID() {
        return certID;
    }
}
