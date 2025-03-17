package dev.tuxmonteiro.bitstamp.poc;

public interface Header {
    String X_AUTH = "X-Auth";
    String X_AUTH_SIGNATURE = "X-Auth-Signature";
    String X_SERVER_AUTH_SIGNATURE = "x-server-auth-signature";
    String X_AUTH_NONCE = "X-Auth-Nonce";
    String X_AUTH_TIMESTAMP = "X-Auth-Timestamp";
    String X_AUTH_VERSION = "X-Auth-Version";
    String CONTENT_TYPE = "Content-Type";
}
