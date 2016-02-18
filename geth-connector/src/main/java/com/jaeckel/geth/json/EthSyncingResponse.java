package com.jaeckel.geth.json;

public class EthSyncingResponse {

    boolean syncing;
    //TODO: or false if not syncing :-(
    EthSyncingResult result;

    @Override
    public String toString() {
        return "EthSyncingResult{" +
                "syncing=" + syncing +
                ", result=" + result +
                '}';
    }
}
