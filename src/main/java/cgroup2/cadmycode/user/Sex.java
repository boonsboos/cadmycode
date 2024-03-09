package cgroup2.cadmycode.user;

/**
 * the enum for the sex of the user
 */
public enum Sex {
    FEMALE,
    MALE,
    UNKNOWN;

    /**
     * gets the sex and returns an integer
     * @return an integer corresponding with the sex, returns 2 if not found
     */
    public int asInt() {
        switch (this) {
            case FEMALE:
                return 1;
            case MALE:
                return 0;
            case UNKNOWN:
                return 2;
            default:
                return 2;
        }
    }

    /**
     * returns the enum value that corresponds with the provided integer
     * @param s the integer corresponding with a sex value
     * @return the corresponding Sex value, {@link Sex#UNKNOWN} if not found.
     */
    public static Sex fromInt(int s) {
        switch (s) {
            case 0:
                return MALE;
            case 1:
                return FEMALE;
            case 2:
                return UNKNOWN;
            default:
                return UNKNOWN;
        }
    }
}
