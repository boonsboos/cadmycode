package cgroup2.cadmycode.user;

/**
 * An object with a user that has a certificate
 */
public class Graduation {

    private int graduationID;
    private int userID;
    private String grantedBy;
    private int grade;
    private int certID;

    /**
     * Creates an instance of a graduation
     * @param graduationID the ID of a graduation
     * @param userID the ID of a user
     * @param grantedBy the person that grants the graduation
     * @param grade the grade of the user
     * @param certID the ID of the certificate
     */
    public Graduation(int graduationID, int userID, String grantedBy, int grade, int certID) {
        this.graduationID = graduationID;
        this.userID = userID;
        this.grantedBy = grantedBy;
        this.grade = grade;
        this.certID = certID;
    }

    /**
     * Creates an instance of a graduation
     * @param userID the ID of a user
     * @param grantedBy the person that grants the graduation
     * @param grade the grade of the user
     * @param certID the ID of the certificate
     */
    public Graduation(int userID, String grantedBy, int grade, int certID) {
        this.userID = userID;
        this.grantedBy = grantedBy;
        this.grade = grade;
        this.certID = certID;
    }

    /**
     * gets the graduation ID
     * @return the graduation ID
     */
    public int getGraduationID() {
        return this.graduationID;
    }

    /**
     * gets the user ID
     * @return the user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * gets the granted by
     * @return the granted by
     */
    public String getGrantedBy() {
        return grantedBy;
    }

    /**
     * gets the grade
     * @return the grade
     */
    public int getGrade() {
        return grade;
    }

    /**
     * gets the certificate ID
     * @return the certificate ID
     */
    public int getCertID() {
        return certID;
    }
}
