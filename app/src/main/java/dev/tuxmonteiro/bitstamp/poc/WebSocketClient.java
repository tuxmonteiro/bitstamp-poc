package dev.tuxmonteiro.bitstamp.poc;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;


// Refs
// https://blogs.oracle.com/javamagazine/post/how-to-build-applications-with-the-websocket-api-for-java-ee-and-jakarta-ee
// https://www.baeldung.com/java-websockets

@ClientEndpoint
public class WebSocketClient {

    Session session = null;
    private MessageHandler handler;
    
    public WebSocketClient(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        try {
        session.getBasicRemote().sendText("Opening connection");
        } catch (IOException ex){
            System.out.println(ex);
        }
    }
    
    public void addMessageHandler(MessageHandler msgHandler) {
        this.handler = msgHandler;
    }
    
    @OnMessage
    public void processMessage(String message) {
        System.out.println("Received message in client: " + message);
    }
    
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            Logger.getLogger(WebSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public static interface MessageHandler {

        public void handleMessage(String message);
    }
}