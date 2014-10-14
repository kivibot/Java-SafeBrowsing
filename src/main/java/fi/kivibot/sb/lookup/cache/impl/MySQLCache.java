package fi.kivibot.sb.lookup.cache.impl;

import fi.kivibot.sb.lookup.LookupResult;
import fi.kivibot.sb.lookup.LookupTask;
import fi.kivibot.sb.lookup.cache.LookupCache;
import fi.kivibot.sb.lookup.exception.CacheException;
import fi.kivibot.sb.lookup.exception.LookupException;
import fi.kivibot.sb.lookup.exception.ServiceUnavailableException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author Nicklas Ahlskog
 */
public class MySQLCache implements LookupCache {

    private final Connection connection;

    public MySQLCache(String host, String database, String uname, String passwd) throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, uname, passwd);
        try (Statement s = connection.createStatement()) {
            s.execute("create table if not exists lookup "
                    + "(url varchar(255) primary key,"
                    + " returncode int,"
                    + " data varchar(32),"
                    + " trusted boolean,"
                    + " timestamp timestamp);");
        }
    }

    @Override
    public LookupResult getCached(String url, long ttl_seconds, LookupTask task) throws IOException, ServiceUnavailableException, LookupException, CacheException {
        ResultSet result;
        try {
            try (PreparedStatement statement = connection.prepareStatement("select * from lookup where url=?")) {
                statement.setString(1, url);
                result = statement.executeQuery();
            }
            LookupResult lr;
            if (result.next() && System.currentTimeMillis() - result.getTimestamp("timestamp").getTime() < ttl_seconds * 1000) {
                lr = new LookupResult(url, result.getInt("returncode"), result.getString("data"), result.getBoolean("trusted"));
            } else {
                lr = task.lookupURL(url);
                try (PreparedStatement statement = connection.prepareStatement("replace into lookup values (?, ?, ?, ?, ?)")) {
                    statement.setString(1, url);
                    statement.setInt(2, lr.getReturnCode());
                    statement.setString(3, lr.getData());
                    statement.setBoolean(4, lr.isTrusted());
                    statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    statement.executeUpdate();
                }
            }
            return lr;
        } catch (SQLException ex) {
            throw new CacheException(ex);
        }
    }

}
