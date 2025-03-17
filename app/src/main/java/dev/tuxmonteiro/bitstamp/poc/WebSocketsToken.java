package dev.tuxmonteiro.bitstamp.poc;

public class WebSocketsToken {
    private String token;
    private String validSec;
    private String userId;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getValidSec() {
        return validSec;
    }
    public void setValidSec(String validSec) {
        this.validSec = validSec;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
