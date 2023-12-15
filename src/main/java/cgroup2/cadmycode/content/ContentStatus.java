package cgroup2.cadmycode.content;

public enum ContentStatus {
    CONCEPT,
    ACTIVE,
    ARCHIVE;

    public static int asInt(ContentStatus s) {
        switch (s) {
            case CONCEPT: return 0;
            case ACTIVE: return 1;
            case ARCHIVE: return 2;
            default: return 0;
        }
    }
    public static ContentStatus fromInt(int a) {
        switch (a) {
            case 0: return CONCEPT;
            case 1: return ACTIVE;
            case 2: return ARCHIVE;
            default: return CONCEPT;
        }
    }
}
