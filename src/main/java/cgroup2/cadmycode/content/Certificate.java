package cgroup2.cadmycode.content;

public class Certificate {

    private int certificateID;
    private String certificateName;

    public Certificate(int id, String name) {
        this.certificateID = id;
        this.certificateName = name;
    }

    public int getCertificateID() {
        return certificateID;
    }

    public String getCertificateName() {
        return certificateName;
    }
}
