package cgroup2.cadmycode.content;

import java.time.LocalDate;

public class Webcast extends EducationalContent{
    private int length;
    private String URL;
    private String speaker;
    private String organisation;

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

    public int getLength() {
        return length;
    }

    public String getURL() {
        return URL;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getOrganisation() {
        return organisation;
    }
}
