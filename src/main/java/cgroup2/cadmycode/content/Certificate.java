package cgroup2.cadmycode.content;

/**
 * the certificate that will be handed out by a course
 */
public class Certificate {

    private int certificateID;
    private String certificateName;

    /**
     * creates an instance of certificate
     * @param id the ID of the course
     * @param name the name of the course
     */
    public Certificate(int id, String name) {
        this.certificateID = id;
        this.certificateName = name;
    }

    /**
     * creates an instance of certificate
     * @param certificateName the name of the certificate
     */
    public Certificate(String certificateName) {
        this.certificateID = 0;
        this.certificateName = certificateName;
    }

    /**
     * gets the certificate ID
     * @return the certificate ID
     */
    public int getCertificateID() {
        return certificateID;
    }

    /**
     * gets the certificate name
     * @return the certificate name
     */
    public String getCertificateName() {
        return certificateName;
    }

    /**
     * renders a string
     * @return a string
     */
    @Override
    public String toString() {
        return this.getCertificateName();
    }
}
