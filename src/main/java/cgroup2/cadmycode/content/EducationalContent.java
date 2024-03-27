package cgroup2.cadmycode.content;

import java.time.LocalDate;

/**
 * educational content that holds both modules and webcasts
 */
public abstract class EducationalContent {

    private int contentItemID;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private ContentStatus status;
    private int version;

    /**
     * creates an instance of a content item
     * @param contentItemID the content item ID
     * @param title the title of a content item
     * @param description the description of a content item
     * @param publicationDate the publication date of a content item
     * @param status the status of a content item
     * @param version the version of a content item
     */
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

    /**
     * creates an instance of a content item
     * @param contentItemID the content item ID
     * @param title the title of a content item
     * @param description the description of a content item
     * @param publicationDate the publication date of a content item
     * @param status the status of a content item
     */
    EducationalContent(int contentItemID,
                       String title,
                       String description,
                       LocalDate publicationDate,
                       ContentStatus status
    ) {
        this.contentItemID = contentItemID;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.status = status;
        this.version = 0; // webcast
    }

    /**
     * creates an instance of a content item
     * @param title the title of a content item
     * @param description the description of a content item
     * @param publicationDate the publication date of a content item
     * @param status the status of a content item
     * @param version the version of a content item
     */
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

    /**
     * creates an instance of a content item
     * @param title the title of a content item
     * @param description the description of a content item
     * @param publicationDate the publication date of a content item
     * @param status the status of a content item
     */
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

    /**
     * gets the content item ID
     * @return the content item ID
     */
    public int getContentItemID() {
        return contentItemID;
    }

    /**
     * gets the title
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * gets the description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * gets the publication date
     * @return the publication date
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    /**
     * gets the status
     * @return returns the {@link ContentStatus}
     */
    public ContentStatus getStatus() {
        return status;
    }

    /**
     * sets the title
     * @param title the new title of the content item
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * sets the description
     * @param description the new description of the content item
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * sets the status
     * @param status the new {@link ContentStatus} of the content item
     */
    public void setStatus(ContentStatus status) {
        this.status = status;
    }

    /**
     * gets the version
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * sets  the version
     * @param version the new version of the content item
     */
    public void setVersion(int version) {
        this.version = version;
    }
}
