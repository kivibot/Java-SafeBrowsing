package fi.kivibot.sb.lookup;

import java.util.Objects;

/**
 *
 * @author Nicklas Ahlskog
 */
public class LookupResult {

    private final String url;
    private final int returnCode;
    private final String data;
    private final boolean trusted;

    public LookupResult(String url, int returnCode, String data, boolean trusted) {
        this.url = url;
        this.returnCode = returnCode;
        this.data = data;
        this.trusted = trusted;
    }

    public String getUrl() {
        return url;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.url);
        hash = 59 * hash + this.returnCode;
        hash = 59 * hash + Objects.hashCode(this.data);
        hash = 59 * hash + (this.trusted ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LookupResult other = (LookupResult) obj;
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (this.returnCode != other.returnCode) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        return this.trusted == other.trusted;
    }

    @Override
    public String toString() {
        return "LookupResult{" + "url=" + url + ", returnCode=" + returnCode + ", data=" + data + ", trusted=" + trusted + '}';
    }

}
