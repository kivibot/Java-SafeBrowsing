package fi.kivibot.sb.lookup;

import fi.kivibot.sb.lookup.cache.LookupCache;
import fi.kivibot.sb.lookup.exception.CacheException;
import fi.kivibot.sb.lookup.exception.LookupException;
import fi.kivibot.sb.lookup.exception.ServiceUnavailableException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicklas Ahlskog
 */
public class CachedLookup extends SafeBrowsingLookup {

    private final LookupCache cache;
    private final long ttlSeconds;

    public CachedLookup(String apiKey, LookupCache cache, long ttlSeconds) {
        super(apiKey);
        this.cache = cache;
        this.ttlSeconds = ttlSeconds;
    }

    public CachedLookup(String client, String apiKey, String appver, LookupCache cache, long ttlSeconds) {
        super(client, apiKey, appver);
        this.cache = cache;
        this.ttlSeconds = ttlSeconds;
    }

   /**
    * 
    * @param url_in
    * @return null if a Caching error occurred; otherwise the new LookupResult
    * @throws IOException
    * @throws ServiceUnavailableException
    * @throws LookupException 
    */
    @Override
    public LookupResult lookupURL(String url_in) throws IOException, ServiceUnavailableException, LookupException {
        try {
            return cache.getCached(url_in, ttlSeconds, super::lookupURL);
        } catch (CacheException ex) {
            Logger.getLogger(CachedLookup.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
