package com.jaeckel.geth.json;

public class NetPeerCountResponse {

    @Hex
    public long result;

    @Override
    public String toString() {
        return "NetPeerCountResponse{" +
                "result=" + result +
                '}';
    }

}
