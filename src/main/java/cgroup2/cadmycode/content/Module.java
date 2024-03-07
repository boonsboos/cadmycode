package cgroup2.cadmycode.content;

import java.time.LocalDate;

import cgroup2.cadmycode.except.FieldValidationException;

public class Module extends EducationalContent {
    private String contactName;
    private String contactEmail;
    private int courseID;

    public Module(int contentItemID,
                  String title,
                  String description,
                  LocalDate publicationDate,
                  ContentStatus status,
                  String contactName,
                  String contactEmail,
                  int courseID,
                  int version
    ) throws FieldValidationException {
        super(contentItemID, title, description, publicationDate, status, version);
        if (!(validateContactEmail(contactEmail))) {
            throw new FieldValidationException("This is not a valid email address");
        }
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.courseID = courseID;
    }

    public Module(String title,
                  String description,
                  LocalDate publicationDate,
                  ContentStatus status,
                  String contactName,
                  String contactEmail,
                  int version
    ) throws FieldValidationException {
        super(title, description, publicationDate, status, version);
        if (!(validateContactEmail(contactEmail))) {
            throw new FieldValidationException("This is not a valid email address");
        }
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public Module(int contentItemID,
                  String title,
                  String description,
                  LocalDate publicationDate,
                  ContentStatus status,
                  String contactName,
                  String contactEmail,
                  int version
    )  throws FieldValidationException {
        super(contentItemID, title, description, publicationDate, status, version);
        if (!(validateContactEmail(contactEmail))) {
            throw new FieldValidationException("This is not a valid email address");
        }
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public static boolean validateContactEmail(String email) {
        return email.matches("\\w+@\\w+[.]\\w+");
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Module{");
        sb.append("contactName='").append(contactName).append('\'');
        sb.append(", contactEmail='").append(contactEmail).append('\'');
        sb.append(", courseID=").append(courseID);
        sb.append('}');
        return sb.toString();
    }
}
