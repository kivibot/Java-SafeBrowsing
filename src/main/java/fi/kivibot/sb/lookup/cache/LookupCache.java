package fi.kivibot.sb.lookup.cache;

import fi.kivibot.sb.lookup.LookupResult;
import fi.kivibot.sb.lookup.LookupTask;
import fi.kivibot.sb.lookup.exception.CacheException;
import fi.kivibot.sb.lookup.exception.LookupException;
import fi.kivibot.sb.lookup.exception.ServiceUnavailableException;
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
     * @throws fi.kivibot.sb.lookup.exception.ServiceUnavailableException
     */
    public LookupResult getCached(String url, long ttl_seconds, LookupTask task) throws IOException, ServiceUnavailableException, LookupException, CacheException;

}
