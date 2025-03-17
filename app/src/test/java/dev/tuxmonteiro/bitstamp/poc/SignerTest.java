package dev.tuxmonteiro.bitstamp.poc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignerTest {

    private BitStampApiEntity apiEntity;
    private BitstampAuthentication authentication;
    private SecretKeySpec secretKeySpec;

    @BeforeEach
    void setUp() {
        apiEntity = mock(BitStampApiEntity.class);
        authentication = mock(BitstampAuthentication.class);
        secretKeySpec = new SecretKeySpec("secret".getBytes(), "HmacSHA256");

        when(apiEntity.getUrlHost()).thenReturn("api.bitstamp.net");
        when(apiEntity.getUrlPath()).thenReturn("/api/v2/ticker/btcusd/");
        when(apiEntity.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(apiEntity.getContentType()).thenReturn(ContentType.JSON);
        when(authentication.apiKey()).thenReturn("apiKey");
        when(authentication.secretKeySpec()).thenReturn(secretKeySpec);
    }

    @Test
    void testSignature() throws InvalidKeyException, NoSuchAlgorithmException {
        Signer signer = new Signer(
                Optional.of(apiEntity),
                Optional.of(authentication),
                Optional.of(""),
                Optional.of(""),
                Optional.of(UUID.randomUUID()),
                Optional.of(String.valueOf(System.currentTimeMillis()))
        );

        String signature = signer.signature();
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
    }

    @Test
    void testSignatureWithMissingApiEntity() {
        Signer signer = new Signer(
                Optional.empty(),
                Optional.of(authentication),
                Optional.of(""),
                Optional.of(""),
                Optional.of(UUID.randomUUID()),
                Optional.of(String.valueOf(System.currentTimeMillis()))
        );

        assertThrows(NoSuchElementException.class, signer::signature);
    }

    @Test
    void testSignatureWithMissingAuthentication() {
        Signer signer = new Signer(
                Optional.of(apiEntity),
                Optional.empty(),
                Optional.of(""),
                Optional.of(""),
                Optional.of(UUID.randomUUID()),
                Optional.of(String.valueOf(System.currentTimeMillis()))
        );

        assertThrows(NoSuchElementException.class, signer::signature);
    }

    @Test
    void testBuilder() {
        Signer signer = Signer.builder()
                .setApiEntity(apiEntity)
                .setAuthentication(authentication)
                .setUrlQuery("")
                .setPayloadString("")
                .build();

        assertNotNull(signer);
        assertTrue(signer.apiEntity().isPresent());
        assertTrue(signer.authentication().isPresent());
    }

    @Test
    void testBuilderWithMissingFields() {
        Signer.Builder builder = Signer.builder();

        assertThrows(NoSuchElementException.class, () -> builder.build().signature());
    }
}
