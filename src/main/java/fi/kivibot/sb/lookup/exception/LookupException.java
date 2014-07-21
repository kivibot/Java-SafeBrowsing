package fi.kivibot.sb.lookup.exception;

/**
 * Thrown to indicate that something went wrong with the lookup. An IOException
 * is thrown if there was a problem with the connection.
 *
 * @author Nicklas Ahlskog
 */
public class LookupException extends RuntimeException {

    public LookupException(String message) {
        super(message);
    }

}
