package fi.kivibot.safebrowsing.cache;

import fi.kivibot.safebrowsing.LookupResult;
import fi.kivibot.safebrowsing.LookupTask;
import java.io.IOException;

/**
 *
 * @author Nicklas Ahlskog
 */
public interface LookupCache {

    /**
     *
     * @param url an url
     * @param ttl_seconds time to live in seconds
     * @param task
     * @return the cached object if in cache; otherwise new LookupResult
     * @throws java.io.IOException
     */
    public LookupResult getCached(String url, long ttl_seconds, LookupTask task) throws IOException;

}
