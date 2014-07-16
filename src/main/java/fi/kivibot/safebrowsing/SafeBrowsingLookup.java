package fi.kivibot.safebrowsing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A simple wrapper for Google's safe browsing lookup API.
 *
 * @author Nicklas Ahlskog
 */
public class SafeBrowsingLookup {

    private static final String API_URL_FORMAT = "https://sb-ssl.google.com/safebrowsing/api/lookup?client=api&apikey=%s&appver=1.0&pver=3.1&url=%s";

    private final String apiKey;

    /**
     *
     * @param apiKey the API key for this application
     */
    public SafeBrowsingLookup(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Lookup an URL
     *
     * @param url_in the URL to check.
     * @return true if the URL is trusted; otherwise false.
     * @throws MalformedURLException if the generated URL is not correct.
     * @throws IOException if any general IO problems occur.
     * @throws ServiceUnavailableException if the service in unavailable
     * @throws RuntimeException if the client receives any other error code or
     * the return code is unknown.
     */
    public boolean isTrusted(String url_in) throws MalformedURLException, IOException {
        URL url = new URL(String.format(API_URL_FORMAT, apiKey, URLEncoder.encode(url_in, "utf-8")));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.connect();
        int code = conn.getResponseCode();
        conn.disconnect();
        switch (code) {
            case 200:
                return false;
            case 204:
                return true;
            case 400:
                throw new RuntimeException("Error 400: Bad Request");
            case 401:
                throw new RuntimeException("Error 401: Not Authorized");
            case 503:
                throw new ServiceUnavailableException("Error 503: Service Unavailable");
            default:
                throw new RuntimeException("Unknown response code: " + code);
        }
    }

}
