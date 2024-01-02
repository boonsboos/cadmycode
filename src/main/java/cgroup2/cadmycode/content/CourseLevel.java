package cgroup2.cadmycode.content;

public enum CourseLevel {
    BEGINNER,
    INTERMEDIATE,
    EXPERT;

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
