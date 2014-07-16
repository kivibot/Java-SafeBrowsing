package fi.kivibot.safebrowsing;

/**
 *
 * @author Nicklas Ahlskog
 */
public class LookupResult {

    private final int returnCode;
    private final String data;
    private final boolean trusted;

    public LookupResult(int returnCode, String data, boolean trusted) {
        this.returnCode = returnCode;
        this.data = data;
        this.trusted = trusted;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getData() {
        return data;
    }

    public boolean isTrusted() {
        return trusted;
    }

}
