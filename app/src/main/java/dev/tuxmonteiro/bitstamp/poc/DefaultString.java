package dev.tuxmonteiro.bitstamp.poc;

public enum DefaultString {
    BITSTAMP_VERSION("v2"),
    SCHEMA_HTTPS("https"),
    BITSTAMP_DOMAIN("bitstamp.net"),
    BITSTAMP_AUTH_KEY("BITSTAMP"),
    SIGN_ALGORITHM("HmacSHA256");

    private final String value;

    private DefaultString(String value) {
        this.value = value;
    }

    @Override public String toString() { 
        return this.value; 
    }
}
