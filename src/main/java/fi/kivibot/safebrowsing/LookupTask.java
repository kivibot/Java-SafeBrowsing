package fi.kivibot.safebrowsing;

import java.io.IOException;

/**
 *
 * @author Nicklas Ahlskog
 */
public interface LookupTask {

    public LookupResult lookupURL(String url_in) throws IOException;

}
