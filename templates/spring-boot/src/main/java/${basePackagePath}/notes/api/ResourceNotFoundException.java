package ${basePackage}.notes.api;

/**
 * Exception indicating that a resource was not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        this("Not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}