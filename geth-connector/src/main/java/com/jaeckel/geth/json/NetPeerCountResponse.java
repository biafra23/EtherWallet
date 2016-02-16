package com.jaeckel.geth.json;

public class NetPeerCountResponse {

    @Hex
    long result;

    @Override
    public String toString() {
        return "NetPeerCountResponse{" +
                "result=" + result +
                '}';
    }
}
