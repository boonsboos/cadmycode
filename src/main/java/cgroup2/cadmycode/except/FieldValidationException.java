package cgroup2.cadmycode.except;

/**
 * a custom exception class for handling FieldValidationExceptions
 */
public class FieldValidationException extends Exception {
    /**
     * creates an instance of {@link FieldValidationException}
     * @param message the message given as parameter for the method call
     */
    public FieldValidationException(String message) {
        super(message);
    }
}
