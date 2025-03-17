package dev.tuxmonteiro.bitstamp.poc;

import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

public record BitstampAuthentication(String apiKey, String apiSecret, SecretKeySpec secretKeySpec) {

    public BitstampAuthentication {
        Objects.requireNonNull(apiKey);
        Objects.requireNonNull(apiSecret);
        Objects.requireNonNull(secretKeySpec);
    }

    public BitstampAuthentication(String apiKey, String apiSecret) {
        this(apiKey, apiSecret, new SecretKeySpec(apiSecret.getBytes(), DefaultString.SIGN_ALGORITHM.toString()));
    }

    @Override
    public String apiKey() {
        return String.format("%s %s", DefaultString.BITSTAMP_AUTH_KEY, this.apiKey);
    }

    @Override
    public String apiSecret() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
