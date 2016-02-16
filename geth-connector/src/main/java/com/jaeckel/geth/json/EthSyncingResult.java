package com.jaeckel.geth.json;

public class EthSyncingResult {

    //TODO: or false if not syncing :-(
    EthSyncingResponse result;

    @Override
    public String toString() {
        return "EthSyncingResult{" +
                "result=" + result +
                '}';
    }
}
