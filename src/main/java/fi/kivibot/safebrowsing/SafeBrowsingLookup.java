package fi.kivibot.safebrowsing;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.io.IOUtils;

/**
 * A simple wrapper for Google's safe browsing lookup API.
 *
 * @author Nicklas Ahlskog
 */
public class SafeBrowsingLookup {

    private static final String API_URL_FORMAT = "https://sb-ssl.google.com/safebrowsing/api/lookup?client=%s&apikey=%s&appver=%s&pver=3.1&url=%s";

    private final String client;
    private final String apiKey;
    private final String appver;
    private final String url_format;

    /**
     *
     * @param apiKey the API key for this application
     */
    public SafeBrowsingLookup(String apiKey) {
        this("api", apiKey, "1.0");
    }

    /**
     *
     * @param client
     * @param apiKey
     * @param appver
     */
    public SafeBrowsingLookup(String client, String apiKey, String appver) {
        this.client = client;
        this.apiKey = apiKey;
        this.appver = appver;
        try {
            this.url_format = String.format(API_URL_FORMAT,
                    URLEncoder.encode(client, "utf-8"),
                    URLEncoder.encode(apiKey, "utf-8"),
                    URLEncoder.encode(appver, "utf-8"), "%s");
        } catch (UnsupportedEncodingException ex) {
            //The system is supposed to support UTF-8
            throw new AssertionError(ex);
        }
    }

    /**
     * Lookup an URL
     *
     * @param url_in the URL to check.
     * @return true if the URL is trusted; otherwise false.
     * @throws IOException if any general IO problems occur.
     * @throws ServiceUnavailableException if the service in unavailable
     * @throws RuntimeException if the client receives any other error code or
     * the return code is unknown.
     */
    public boolean isTrusted(String url_in) throws IOException {
        return lookupURL(url_in).isTrusted();
    }

    /**
     * Lookup an URL
     *
     * @param url_in the URL to check.
     * @return new LookupResult containing the results
     * @throws IOException if any general IO problems occur.
     * @throws ServiceUnavailableException if the service in unavailable
     * @throws RuntimeException if the client receives any other error code or
     * the return code is unknown.
     */
    public LookupResult lookupURL(String url_in) throws IOException {
        URL url = new URL(String.format(url_format, URLEncoder.encode(url_in, "utf-8")));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.connect();
        int code = conn.getResponseCode();
        boolean trusted;
        String data = null;
        switch (code) {
            case 200:
                trusted = false;
                break;
            case 204:
                trusted = true;
                break;
            case 400:
                throw new RuntimeException("Error 400: Bad Request");
            case 401:
                throw new RuntimeException("Error 401: Not Authorized");
            case 503:
                throw new ServiceUnavailableException("Error 503: Service Unavailable");
            default:
                throw new RuntimeException("Unknown response code: " + code);
        }
        if (conn.getContentLength() > 0) {
            data = IOUtils.toString(conn.getInputStream());
        }
        conn.disconnect();
        return new LookupResult(code, data, trusted);
    }

}
