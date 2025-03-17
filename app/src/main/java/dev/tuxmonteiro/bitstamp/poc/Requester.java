package dev.tuxmonteiro.bitstamp.poc;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Mac;

import org.apache.commons.codec.binary.Hex;

public class Requester {

    private Optional<HttpClient> client = Optional.empty();

    public Requester setClient(final HttpClient client) {
        this.client = Optional.of(client);
        return this;
    }

    public String execute(BitstampAuthentication authentication, BitStampApiEntity apiEntity, String payloadString, String urlQuery) 
        throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        Signer signer = Signer.builder()
            .setApiEntity(apiEntity)
            .setAuthentication(authentication)
            .setPayloadString(payloadString)
            .setUrlQuery(urlQuery)
            .build();

        String nonce = signer.nonce().orElseThrow().toString();
        String timestamp = signer.timestamp().orElseThrow();
        String uriSchema = DefaultString.SCHEMA_HTTPS.toString();

        Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(String.format("%s://%s%s", uriSchema, apiEntity.getUrlHost(), apiEntity.getUrlPath())))
            .setHeader(Header.X_AUTH, authentication.apiKey())
            .setHeader(Header.X_AUTH_SIGNATURE, signer.signature())
            .setHeader(Header.X_AUTH_NONCE, nonce)
            .setHeader(Header.X_AUTH_TIMESTAMP, timestamp)
            .setHeader(Header.X_AUTH_VERSION, DefaultString.BITSTAMP_VERSION.toString())
            .method(apiEntity.getHttpMethod().toString(), HttpRequest.BodyPublishers.ofString(payloadString));

        // Fix BitStamp API BUG when request WebSockets Token: Content-Type must not be set.
        if (!ContentType.NULL.equals(apiEntity.getContentType())) {
            requestBuilder.setHeader(Header.CONTENT_TYPE, apiEntity.getContentType().toString());
        };

        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = client.orElseThrow().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(String.format("Status code not 200: %s\n%s", response.statusCode(), response.body()));
        }

        if (!validateResponse(response, signer)) {
            throw new RuntimeException("Invalid response signature.");
        }

        return response.body();
    }

    private boolean validateResponse(HttpResponse<String> response, Signer signer) throws InvalidKeyException, IllegalStateException {
        final HttpHeaders headers = response.headers();
        final Map<String, List<String>> mapHeaders = headers.map();
        final List<String> headerServerAuthSignature = mapHeaders.get(Header.X_SERVER_AUTH_SIGNATURE);
        final List<String> headerResponseContentType = mapHeaders.get(Header.CONTENT_TYPE);
        final String serverSignature = headerServerAuthSignature.getFirst();
        final String responseContentType = headerResponseContentType.getFirst();

        final String nonce = signer.nonce().orElseThrow().toString();
        final String timestamp = signer.timestamp().orElseThrow();
        final String stringToSign = nonce + timestamp + responseContentType + response.body();
        
        Mac mac = Signer.getMac();
        BitstampAuthentication authentication = signer.authentication().orElseThrow();
        mac.init(authentication.secretKeySpec());
        byte[] rawHmacServerCheck = mac.doFinal(stringToSign.getBytes());
        String newSignature = new String(Hex.encodeHex(rawHmacServerCheck));
        System.out.println("SIGN: " + newSignature);
        
        return newSignature.equals(serverSignature);
    }

}
