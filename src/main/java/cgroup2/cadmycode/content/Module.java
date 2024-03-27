package cgroup2.cadmycode.content;

import java.time.LocalDate;
import cgroup2.cadmycode.except.FieldValidationException;

/**
 * a course item that a user has to complete
 */
public class Module extends EducationalContent {
    private String contactName;
    private String contactEmail;
    private int courseID;

    /**
     * creates an instant of a module
     * @param contentItemID the ID of a content item
     * @param title the title of a module
     * @param description the description of a module
     * @param publicationDate the publication date of a module
     * @param status the {@link ContentStatus} of a module
     * @param contactName the contact name of a module
     * @param contactEmail the contact email of a module
     * @param courseID the ID of a course
     * @param version the version of a course
     */
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

    /**
     * creates an instant of a module
     * @param title the title of a module
     * @param description the description of a module
     * @param publicationDate the publication date of a module
     * @param status the {@link ContentStatus} of a module
     * @param contactName the contact name of a module
     * @param contactEmail the contact email of a module
     * @param version the version of a course
     */
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

    /**
     *
     * @param contentItemID the ID of a content item
     * @param title the title of a module
     * @param description the description of a module
     * @param publicationDate the publication date of a module
     * @param status the {@link ContentStatus} of a module
     * @param contactName the contact name of a module
     * @param contactEmail the contact email of a module
     * @param version the version of a course
     */
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

    /**
     * gets the contact name
     * @return the contact name
     */
    public static boolean validateContactEmail(String email) {
        return email.matches("\\w+@\\w+[.]\\w+");
    }

    public String getContactName() {
        return contactName;
    }

    /**
     * gets the contact email
     * @return the contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * gets the course ID
     * @return the course ID
     */
    public int getCourseID() {
        return courseID;
    }

    /**
     * sets the contact name
     * @param contactName the new contact name of de module
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * sets the contact email
     * @param contactEmail the new contact email of de module
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * sets the course ID
     * @param courseID the new course ID of the module
     */
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    /**
     * renders a string
     * @return a string
     */
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
