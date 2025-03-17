package dev.tuxmonteiro.bitstamp.poc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

public class BitStampApiEntityTest {

    @Mock
    private HttpClient mockHttpClient;
    
    @Mock
    private HttpResponse<String> mockHttpResponse;

    private final SecretKeySpec secretKeySpec = new SecretKeySpec("secret".getBytes(), DefaultString.SIGN_ALGORITHM.toString());
    private final String responseSignature = "serverSignature";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetHttpMethod() {
        assertEquals(HttpMethod.GET, BitStampApiEntity.Currencies.getHttpMethod());
        assertEquals(HttpMethod.POST, BitStampApiEntity.AccountBalances.getHttpMethod());
    }

    @Test
    public void testGetContentType() {
        assertEquals(ContentType.JSON, BitStampApiEntity.Currencies.getContentType());
        assertEquals(ContentType.FORM_URLENCODED, BitStampApiEntity.AccountBalances.getContentType());
    }

    @Test
    public void testGetUrlPath() {
        assertEquals("/api/v2/currencies/", BitStampApiEntity.Currencies.getUrlPath());
        assertEquals("/api/v2/account_balances/", BitStampApiEntity.AccountBalances.getUrlPath());
    }

    @Test
    public void testGetUrlHost() {
        assertEquals("www.bitstamp.net", BitStampApiEntity.AccountBalances.getUrlHost());
    }

    @Test
    public void testExecute() throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{\"result\":\"success\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        
        BitstampAuthentication auth = new BitstampAuthentication("key", "secret", secretKeySpec);
        String response = BitStampApiEntity.WebSocketsToken.execute(mockHttpClient, auth, "", "");
        assertNotNull(response);
    }

    @Test
    public void testExecuteWithPayload() throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{\"result\":\"success\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        
        BitstampAuthentication auth = new BitstampAuthentication("key", "secret", secretKeySpec);
        String response = BitStampApiEntity.Currencies.execute(mockHttpClient, auth, "payload", "");
        assertNotNull(response);
    }

    @Test
    public void testExecuteWithPayloadAndQuery() throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{\"result\":\"success\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        
        BitstampAuthentication auth = new BitstampAuthentication("key", "secret", secretKeySpec);
        String response = BitStampApiEntity.Currencies.execute(mockHttpClient, auth, "payload", "query");
        assertNotNull(response);
    }

    @Test
    public void testInvalidKeyException() throws Exception {
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{\"result\":\"success\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        
        BitstampAuthentication auth = new BitstampAuthentication("key", "secret", secretKeySpec);
        assertThrows(InvalidKeyException.class, () -> {
            BitStampApiEntity.Currencies.execute(mockHttpClient, auth, "payload", "query");
        });
    }

    @Test
    public void testNoSuchAlgorithmException() throws Exception {
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{\"result\":\"success\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        
        BitstampAuthentication auth = new BitstampAuthentication("key", "secret", secretKeySpec);
        assertThrows(NoSuchAlgorithmException.class, () -> {
            BitStampApiEntity.Currencies.execute(mockHttpClient, auth, "payload", "query");
        });
    }

    @Test
    public void testIOException() throws Exception {
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{\"result\":\"success\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        
        BitstampAuthentication auth = new BitstampAuthentication("key", "secret", secretKeySpec);
        assertThrows(IOException.class, () -> {
            BitStampApiEntity.Currencies.execute(mockHttpClient, auth, "payload", "query");
        });
    }

    @Test
    public void testInterruptedException() throws Exception {
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{\"result\":\"success\"}");
        when(mockHttpResponse.headers()).thenReturn(HttpHeaders.of(Map.of("x-server-auth-signature", List.of(responseSignature), Header.CONTENT_TYPE.toString(), List.of(ContentType.JSON.toString())), (s1, s2) -> true));
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        
        BitstampAuthentication auth = new BitstampAuthentication("key", "secret", secretKeySpec);
        assertThrows(InterruptedException.class, () -> {
            BitStampApiEntity.Currencies.execute(mockHttpClient, auth, "payload", "query");
        });
    }
}
