package cgroup2.cadmycode.content;

/**
 * the enum for the level of the course
 */
public enum CourseLevel {
    BEGINNER,
    INTERMEDIATE,
    EXPERT;

    /**
     * gets the level and returns an integer
     * @return the integer corresponding with the level, returns 0 if not found
     */
    public int asInt() {
        switch (this) {
            case BEGINNER:
                return 0;
            case INTERMEDIATE:
                return 1;
            case EXPERT:
                return 2;
            default:
                return 0;
        }
    }

    /**
     * returns the enum value that corresponds with the provided integer
     * @param a the integer corresponding with the level value
     * @return the level value corresponding with the integer, {@link CourseLevel#BEGINNER} if not found
     */
    public static CourseLevel fromInt(int a) {
        switch (a) {
            case 0:
                return BEGINNER;
            case 1:
                return INTERMEDIATE;
            case 2:
                return EXPERT;
            default:
                return BEGINNER;
        }
    }
}
