package dev.tuxmonteiro.bitstamp.poc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RequesterTest {

    @Mock
    private HttpClient mockHttpClient;
    @Mock
    private HttpResponse<String> mockHttpResponse;
    @Mock
    private BitstampAuthentication mockAuthentication;
    @Mock
    private BitStampApiEntity mockApiEntity;
    @Mock
    private Signer mockSigner;

    private final UUID uuid = UUID.randomUUID();

    private final SecretKeySpec secretKeySpec = new SecretKeySpec("secret".getBytes(), "HmacSHA256");

    private final String responseSignature = "serverSignature";

    private Requester requester;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        requester = new Requester();
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        when(mockSigner.nonce()).thenReturn(Optional.of(uuid));
        when(mockSigner.timestamp()).thenReturn(Optional.of("timestamp"));
        when(mockSigner.signature()).thenReturn("signature");
        when(mockAuthentication.apiKey()).thenReturn("apiKey");
        when(mockAuthentication.secretKeySpec()).thenReturn(secretKeySpec);
        when(mockApiEntity.getUrlHost()).thenReturn("www.bitstamp.net");
        when(mockApiEntity.getUrlPath()).thenReturn("/api/v2/ticker/btcusd/");
        when(mockApiEntity.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(mockApiEntity.getContentType()).thenReturn(ContentType.JSON);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{\"result\":\"success\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        
        String response = requester.setClient(mockHttpClient).execute(mockAuthentication, mockApiEntity, "", "");

        assertEquals("{\"result\":\"success\"}", response);
    }

    @Test
    public void testExecuteInvalidResponseSignature() throws Exception {
        when(mockSigner.nonce()).thenReturn(Optional.of(uuid));
        when(mockSigner.timestamp()).thenReturn(Optional.of("timestamp"));
        when(mockSigner.signature()).thenReturn("signature");
        when(mockAuthentication.apiKey()).thenReturn("apiKey");
        when(mockAuthentication.secretKeySpec()).thenReturn(secretKeySpec);
        when(mockApiEntity.getUrlHost()).thenReturn("www.bitstamp.net");
        when(mockApiEntity.getUrlPath()).thenReturn("/api/v2/ticker/btcusd/");
        when(mockApiEntity.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(mockApiEntity.getContentType()).thenReturn(ContentType.JSON);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{\"result\":\"success\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));

        assertThrows(InvalidKeyException.class, () -> {
            requester.execute(mockAuthentication, mockApiEntity, "", "");
        });
    }

    @Test
    public void testExecuteStatusCodeNot200() throws Exception {
        when(mockSigner.nonce()).thenReturn(Optional.of(uuid));
        when(mockSigner.timestamp()).thenReturn(Optional.of("timestamp"));
        when(mockSigner.signature()).thenReturn("signature");
        when(mockAuthentication.apiKey()).thenReturn("apiKey");
        when(mockAuthentication.secretKeySpec()).thenReturn(secretKeySpec);
        when(mockApiEntity.getUrlHost()).thenReturn("www.bitstamp.net");
        when(mockApiEntity.getUrlPath()).thenReturn("/api/v2/ticker/btcusd/");
        when(mockApiEntity.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(mockApiEntity.getContentType()).thenReturn(ContentType.JSON);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(500);
        when(mockHttpResponse.body()).thenReturn("{\"error\":\"Internal Server Error\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));

        assertThrows(InvalidKeyException.class, () -> {
            requester.execute(mockAuthentication, mockApiEntity, "", "");
        });
    }

    @Test
    public void testExecuteIOException() throws Exception {
        when(mockSigner.nonce()).thenReturn(Optional.of(uuid));
        when(mockSigner.timestamp()).thenReturn(Optional.of("timestamp"));
        when(mockSigner.signature()).thenReturn("signature");
        when(mockAuthentication.apiKey()).thenReturn("apiKey");
        when(mockAuthentication.secretKeySpec()).thenReturn(secretKeySpec);
        when(mockApiEntity.getUrlHost()).thenReturn("www.bitstamp.net");
        when(mockApiEntity.getUrlPath()).thenReturn("/api/v2/ticker/btcusd/");
        when(mockApiEntity.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(mockApiEntity.getContentType()).thenReturn(ContentType.JSON);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(new IOException("Network error"));

        assertThrows(InvalidKeyException.class, () -> {
            requester.execute(mockAuthentication, mockApiEntity, "", "");
        });
    }

}
