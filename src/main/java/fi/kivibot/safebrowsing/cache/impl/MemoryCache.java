package fi.kivibot.safebrowsing.cache.impl;

import fi.kivibot.safebrowsing.cache.LookupCache;
import fi.kivibot.safebrowsing.LookupResult;
import fi.kivibot.safebrowsing.LookupTask;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Call .start() to enable GC
 *
 * @author Nicklas Ahlskog
 */
public class MemoryCache extends Thread implements LookupCache {

    private class Pair {

        private final LookupResult result;
        private final long time;

        public Pair(LookupResult result, long time) {
            this.result = result;
            this.time = time;
        }

        public LookupResult getResult() {
            return result;
        }

        public long getTime() {
            return time;
        }
    }

    private final Map<String, Pair> map = new ConcurrentHashMap<>();
    private final long gcTTLSeconds;
    private final long gcSleepSeconds;

    public MemoryCache() {
        this(30 * 60, 5 * 60);
    }

    public MemoryCache(long gcTTLSeconds, long gcSleepSeconds) {
        this.gcTTLSeconds = gcTTLSeconds;
        this.gcSleepSeconds = gcSleepSeconds;
    }

    @Override
    public LookupResult getCached(String url, long ttl_seconds, LookupTask task) throws IOException {
        Pair lt = map.get(url);
        if (lt == null || System.currentTimeMillis() - lt.getTime() >= ttl_seconds * 1000) {
            lt = new Pair(task.lookupURL(url), System.currentTimeMillis());
            map.put(url, lt);
        }
        return lt.getResult();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.gcSleepSeconds * 1000);
            } catch (InterruptedException ex) {
                break;
            }
            for (Map.Entry<String, Pair> e : map.entrySet()) {
                if (System.currentTimeMillis() - e.getValue().getTime() >= gcTTLSeconds * 1000) {
                    map.remove(e.getKey());
                }
            }
        }
    }

}
