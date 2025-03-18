package dev.tuxmonteiro.bitstamp.poc;

import java.util.Objects;

import com.google.gson.Gson;

public class Main {

    private static final int TIMEOUT = 100000;

    public static void main(String[] args) {
        Object timeouObject = new Object();
        String apiKey = Objects.requireNonNull(System.getenv("BITSTAMP_APIKEY"));
        String apiKeySecret = Objects.requireNonNull(System.getenv("BITSTAMP_APISECRET"));
        
        try {
            BitstampAuthentication authentication = new BitstampAuthentication(apiKey, apiKeySecret);

            String accountBalance = BitStampApiEntity.AccountBalances.execute(authentication, "offset=1");
            System.out.println("account balance: " + accountBalance);

            final BitstampWebSocketService bitstampWebSocketService = new BitstampWebSocketService();
            bitstampWebSocketService.updateTokenAndUserId(authentication);
            bitstampWebSocketService.execute();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        synchronized (timeouObject) {
            try {
                timeouObject.wait(TIMEOUT);
            } catch (InterruptedException e) {
                return; // ignore
            }
        }
        
    }
}