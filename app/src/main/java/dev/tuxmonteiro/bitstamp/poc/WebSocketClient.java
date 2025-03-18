package dev.tuxmonteiro.bitstamp.poc;

import java.io.IOException;
import java.net.URI;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
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

    @SuppressWarnings("unused")
    private MessageHandler handler;
    
    public WebSocketClient(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void addMessageHandler(MessageHandler msgHandler) {
        this.handler = msgHandler;
    }
    
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        System.out.println(">>> Opening connection");
    }
    
    @OnMessage
    public void processMessage(String message) {
        System.out.println(message);
    }
    
    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println(">>> Closing connection");
    }

    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

     public static interface MessageHandler {
        public void handleMessage(String message);
    }
}