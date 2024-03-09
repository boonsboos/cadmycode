package cgroup2.cadmycode.content;

import java.time.LocalDate;

/**
 * a webcast object that a user can enroll in
 */
public class Webcast extends EducationalContent{
    private int length;
    private String URL;
    private String speaker;
    private String organisation;

    /**
     * creates an instance of a webcast
     * @param contentItemID the ID of a content item
     * @param title the title of a webcast
     * @param description the description of a webcast
     * @param publicationDate the publication date of a webcast
     * @param status the {@link ContentStatus} of a webcast
     * @param length the length of a webcast
     * @param URL the URL of a webcast
     * @param speaker the speaker of a webcast
     * @param organisation the organisation of the speaker
     */
    public Webcast(int contentItemID,
                   String title,
                   String description,
                   LocalDate publicationDate,
                   ContentStatus status,
                   int length,
                   String URL,
                   String speaker,
                   String organisation
    ) {
        super(contentItemID, title, description, publicationDate, status);
        this.length = length;
        this.URL = URL;
        this.speaker = speaker;
        this.organisation = organisation;
    }

    /**
     * creates an instance of a webcast
     * @param title the title of a webcast
     * @param description the description of a webcast
     * @param publicationDate the publication date of a webcast
     * @param status the {@link ContentStatus} of a webcast
     * @param length the length of a webcast
     * @param URL the URL of a webcast
     * @param speaker the speaker of a webcast
     * @param organisation the organisation of the speaker
     */
    public Webcast(String title,
                   String description,
                   LocalDate publicationDate,
                   ContentStatus status,
                   int length,
                   String URL,
                   String speaker,
                   String organisation) {
        super(title, description, publicationDate, status);
        this.length = length;
        this.URL = URL;
        this.speaker = speaker;
        this.organisation = organisation;
    }

    /**
     * gets the length
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * gets the URL
     * @return the URL
     */
    public String getURL() {
        return URL;
    }

    /**
     * gets the speaker
     * @return the speaker
     */
    public String getSpeaker() {
        return speaker;
    }

    /**
     * gets the organisation
     * @return organisation
     */
    public String getOrganisation() {
        return organisation;
    }
}
