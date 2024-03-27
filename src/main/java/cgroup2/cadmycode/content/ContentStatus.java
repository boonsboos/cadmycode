package cgroup2.cadmycode.content;

import cgroup2.cadmycode.user.Sex;

/**
 * an enum for the status of the educational content
 */
public enum ContentStatus {
    CONCEPT,
    ACTIVE,
    ARCHIVE;

    /**
     * returns and integer the corresponds with the provided content status
     * @param s a content status corresponding with a content status value
     * @return an integer corresponding with the content status, returns 0 if not found
     */
    public static int asInt(ContentStatus s) {
        switch (s) {
            case CONCEPT: return 0;
            case ACTIVE: return 1;
            case ARCHIVE: return 2;
            default: return 0;
        }
    }

    /**
     * returns the enum value that corresponds with the provided integer
     * @param a an int value coresponding with a content status value
     * @return the corresponding Sex value, {@link ContentStatus#CONCEPT} if not found.
     */
    public static ContentStatus fromInt(int a) {
        switch (a) {
            case 0: return CONCEPT;
            case 1: return ACTIVE;
            case 2: return ARCHIVE;
            default: return CONCEPT;
        }
    }
}
