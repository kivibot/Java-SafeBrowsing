package fi.kivibot.sb.lookup;

import fi.kivibot.sb.lookup.cache.LookupCache;
import java.io.IOException;

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

    @Override
    public LookupResult lookupURL(String url_in) throws IOException {
        return cache.getCached(url_in, ttlSeconds, super::lookupURL);
    }

}
