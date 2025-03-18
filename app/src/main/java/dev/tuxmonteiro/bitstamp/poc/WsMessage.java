package dev.tuxmonteiro.bitstamp.poc;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public record WsMessage(String event, @SerializedName("data") WsData wsData) implements Serializable {
    private static final long serialVersionUID = -3305276997530613807L;
}
