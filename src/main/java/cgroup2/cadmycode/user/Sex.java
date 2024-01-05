package cgroup2.cadmycode.user;

public enum Sex {
    FEMALE,
    MALE,
    UNKNOWN;

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
