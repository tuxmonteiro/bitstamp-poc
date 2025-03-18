package dev.tuxmonteiro.bitstamp.poc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

public class BitstampWebSocketService {

    private final String subscribeCommand = "bts:subscribe";        
    private final String bitstampWebsocketsServer = "wss://ws.bitstamp.net/";
    private final Gson gson = new Gson();

    private Optional<String> token = Optional.empty();
    private Optional<String> userId = Optional.empty();

    public void execute() {
        final URI wsUri;
        try {
            wsUri = new URI(bitstampWebsocketsServer);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        String liveTradesBtcusdChannel = "live_trades_btcusd";
        String liveOrdersBtcusdChannel = "live_orders_btcusd";
        
        //List<String> channels = List.of(liveTradesBtcusdChannel, liveOrdersBtcusdChannel);
        List<String> channels = List.of(liveTradesBtcusdChannel);

        channels.forEach(channel -> {
            final WebSocketClient clientEndPoint = new WebSocketClient(wsUri);
            clientEndPoint.addMessageHandler(new WebSocketClient.MessageHandler() {
                    public void handleMessage(String message) {
                        System.out.println(message);
                    }
            });

            String jsonMessage = gson.toJson(new WsMessage(subscribeCommand, new WsData(channel)));
            clientEndPoint.sendMessage(jsonMessage);
        });
    }

    public void updateTokenAndUserId(final BitstampAuthentication authentication) throws InvalidKeyException, NoSuchAlgorithmException, IOException, InterruptedException {
        String tokenJson = BitStampApiEntity.WebSocketsToken.execute(authentication);
        token = Optional.of(gson.fromJson(tokenJson, WebSocketsToken.class).getToken());
        userId = Optional.of(gson.fromJson(tokenJson, WebSocketsToken.class).getUserId());
    }

}
