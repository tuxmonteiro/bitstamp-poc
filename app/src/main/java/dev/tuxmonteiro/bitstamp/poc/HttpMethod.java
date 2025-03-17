package dev.tuxmonteiro.bitstamp.poc;

public enum HttpMethod {
    GET("GET"), 
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    UNDEF("UNDEF");
    
    HttpMethod(String value) {
        this.value = value;
    }

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return value;
    }
}
