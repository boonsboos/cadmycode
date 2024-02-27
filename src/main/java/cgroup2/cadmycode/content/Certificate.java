package cgroup2.cadmycode.content;

public class Certificate {

    private int certificateID;
    private String certificateName;

    public Certificate(int id, String name) {
        this.certificateID = id;
        this.certificateName = name;
    }

    public Certificate(String certificateName) {
        this.certificateID = 0;
        this.certificateName = certificateName;
    }

    public int getCertificateID() {
        return certificateID;
    }

    public String getCertificateName() {
        return certificateName;
    }

    @Override
    public String toString() {
        return this.getCertificateName();
    }
}
