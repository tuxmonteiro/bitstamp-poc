package dev.tuxmonteiro.bitstamp.poc;

import java.util.Objects;

import com.google.gson.Gson;

public class Main {

    public static void main(String[] args) {
        String apiKey = Objects.requireNonNull(System.getenv("BITSTAMP_APIKEY"));
        String apiKeySecret = Objects.requireNonNull(System.getenv("BITSTAMP_APISECRET"));
        Gson gson = new Gson();

        try {
            BitstampAuthentication authentication = new BitstampAuthentication(apiKey, apiKeySecret);

            String accountBalance = BitStampApiEntity.AccountBalances.execute(authentication, "offset=1");
            System.out.println("account balance: " + accountBalance);
            
            String tokenJson = BitStampApiEntity.WebSocketsToken.execute(authentication);
            String token = gson.fromJson(tokenJson, WebSocketsToken.class).getToken();

            System.out.println("body is " + tokenJson);
            System.out.println("token is " + token);

        } catch (Exception e) {
            throw new RuntimeException("ERROR: ", e);
        }
    }
}