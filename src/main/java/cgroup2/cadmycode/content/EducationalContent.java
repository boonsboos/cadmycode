package cgroup2.cadmycode.content;

import java.time.LocalDate;

public abstract class EducationalContent {

    private int contentItemID;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private ContentStatus status;
    private int version;

    EducationalContent(int contentItemID,
                       String title,
                       String description,
                       LocalDate publicationDate,
                       ContentStatus status,
                       int version
    ) {
        this.contentItemID = contentItemID;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.status = status;
        this.version = version;
    }

    EducationalContent(String title, String description, LocalDate publicationDate, ContentStatus status, int version) {
        this.contentItemID = 0;
        // IMPORTANT: make sure to always do a 0 check on contentItemID when inserting anything
        // 0 is *NOT* a valid ID
        // it's already handled in the database
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.status = status;
        this.version = version;
    }

    EducationalContent(String title, String description, LocalDate publicationDate, ContentStatus status) {
        this.contentItemID = 0;
        // IMPORTANT: make sure to always do a 0 check on contentItemID when inserting anything
        // 0 is *NOT* a valid ID
        // it's already handled in the database
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.status = status;
        this.version = 0; // this is ONLY valid for
    }


    public int getContentItemID() {
        return contentItemID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public ContentStatus getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(ContentStatus status) {
        this.status = status;
    }
}
