package fi.kivibot.safebrowsing.cache.impl;

import fi.kivibot.safebrowsing.LookupResult;
import fi.kivibot.safebrowsing.LookupTask;
import fi.kivibot.safebrowsing.cache.LookupCache;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author Nicklas Ahlskog
 */
public class RedisCache implements LookupCache {

    @Message
    static class LookupResultMessage {

        public int returnCode;
        public String data;
        public boolean trusted;
    }

    private final JedisPool pool;
    private final MessagePack mp;

    public RedisCache() {
        this(new JedisPool("127.0.0.1"));
    }

    public RedisCache(String host, int port) {
        this(new JedisPool(new JedisPoolConfig(), host, port));
    }

    public RedisCache(JedisPool pool) {
        this.pool = pool;
        mp = new MessagePack();
        mp.register(LookupResultMessage.class);
    }

    @Override
    public LookupResult getCached(String url, long ttl_seconds, LookupTask task) throws IOException {
        Jedis j = pool.getResource();
        LookupResult rs;
        byte[] bytes;
        try {
            bytes = j.get(url.getBytes());
        } catch (JedisConnectionException e) {
            pool.returnBrokenResource(j);
            throw e;
        }
        if (bytes != null) {
            LookupResultMessage lrm = mp.read(bytes, LookupResultMessage.class);
            rs = new LookupResult(url, lrm.returnCode, lrm.data, lrm.trusted);
        } else {
            rs = task.lookupURL(url);

            LookupResultMessage lrm = new LookupResultMessage();
            lrm.returnCode = rs.getReturnCode();
            lrm.data = rs.getData();
            lrm.trusted = rs.isTrusted();

            try {
                j.setex(url.getBytes(), (int) ttl_seconds, mp.write(lrm));
            } catch (JedisConnectionException e) {
                pool.returnBrokenResource(j);
                throw e;
            }
        }
        pool.returnResource(j);
        return rs;
    }

}
