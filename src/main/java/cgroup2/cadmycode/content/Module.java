package cgroup2.cadmycode.content;

import java.time.LocalDate;

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
                  int courseID
    ) {
        super(contentItemID, title, description, publicationDate, status);
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.courseID = courseID;
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
}
