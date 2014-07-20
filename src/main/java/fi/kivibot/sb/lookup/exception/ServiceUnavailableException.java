package fi.kivibot.sb.lookup.exception;

/**
 * Thrown to indicate that the underlying service is not available.
 *
 * @author Nicklas Ahlskog
 */
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String message) {
        super(message);
    }

}
