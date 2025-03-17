package dev.tuxmonteiro.bitstamp.poc;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public record Signer (
        Optional<BitStampApiEntity> apiEntity,
        Optional<BitstampAuthentication> authentication,
        Optional<String> urlQuery,
        Optional<String> payloadString,
        Optional<UUID> nonce,
        Optional<String> timestamp
) {

    private static final Mac mac;
    
    static {
        try {
            mac = Mac.getInstance(DefaultString.SIGN_ALGORITHM.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not get "+ DefaultString.SIGN_ALGORITHM + " instance:", e);
        }
    }

    public static Mac getMac() {
        return mac;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Optional<BitStampApiEntity> apiEntity = Optional.empty();
        private Optional<BitstampAuthentication> authentication = Optional.empty();
        private Optional<String> urlQuery = Optional.empty();
        private Optional<String> payloadString = Optional.empty();
        
        public Builder setApiEntity(BitStampApiEntity apiEntity) {
            this.apiEntity = Optional.of(apiEntity);
            return this;
        }

        public Builder setAuthentication(BitstampAuthentication authentication) {
            this.authentication = Optional.of(authentication);
            return this;
        }

        public Builder setUrlQuery(String urlQuery) {
            this.urlQuery = Optional.of(urlQuery);
            return this;
        }

        public Builder setPayloadString(String payloadString) {
            this.payloadString = Optional.of(payloadString);
            return this;
        }

        public Signer build() {
            return new Signer(apiEntity, authentication, urlQuery, payloadString,
                Optional.of(UUID.randomUUID()),
                Optional.of(String.valueOf(System.currentTimeMillis())));
        }
    }

    // private String getPath() {
    //     return String.format("/api/%s", apiEntity.orElseThrow().toString());
    // }

    public String signature() throws InvalidKeyException, NoSuchAlgorithmException {
        BitStampApiEntity apiEntity = this.apiEntity.orElseThrow();
        String urlHost = apiEntity.getUrlHost();
        String urlPath = apiEntity.getUrlPath();
        String httpMethod = apiEntity.getHttpMethod().toString();
        String contentType = apiEntity.getContentType().toString();
        String apiKey = authentication.orElseThrow().apiKey();
        SecretKeySpec secretKeySpec = authentication.orElseThrow().secretKeySpec();

        String rawSignature = String.format("%s%s%s%s%s%s%s%s%s%s", 
            apiKey,
            httpMethod, 
            urlHost,
            urlPath,
            urlQuery.orElse(""), 
            contentType, 
            nonce.orElseThrow().toString(), 
            timestamp.orElseThrow(),
            DefaultString.BITSTAMP_VERSION.toString(), 
            payloadString.orElse(""));
       
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(rawSignature.getBytes());
        return new String(Hex.encodeHex(rawHmac)).toUpperCase();
    }

}
