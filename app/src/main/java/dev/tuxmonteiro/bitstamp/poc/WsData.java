package dev.tuxmonteiro.bitstamp.poc;

import java.io.Serializable;

public record WsData(String channel, String auth) implements Serializable {
    private static final long serialVersionUID = -3305276997530613807L;

    public WsData(String channel) {
        this(channel, null);
    }

}
