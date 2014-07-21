package fi.kivibot.sb.lookup;

import fi.kivibot.sb.lookup.exception.LookupException;
import fi.kivibot.sb.lookup.exception.ServiceUnavailableException;
import java.io.IOException;

/**
 *
 * @author Nicklas Ahlskog
 */
public interface LookupTask {

    public LookupResult lookupURL(String url_in) throws IOException, ServiceUnavailableException, LookupException;

}
