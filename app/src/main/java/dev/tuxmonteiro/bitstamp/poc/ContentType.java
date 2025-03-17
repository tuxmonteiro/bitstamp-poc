package dev.tuxmonteiro.bitstamp.poc;

public enum ContentType {
    JSON("application/json"), 
    FORM_URLENCODED("application/x-www-form-urlencoded"),
    NULL("");

    private final String value;

    private ContentType(String value) {
        this.value = value;
    }

    @Override 
    public String toString() { 
        return this.value; 
    }
}
