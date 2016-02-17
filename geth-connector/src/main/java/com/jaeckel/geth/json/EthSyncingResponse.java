package com.jaeckel.geth.json;

public class EthSyncingResponse {

    //{"currentBlock":"0xdb5f5",
    // "highestBlock":"0xf5664",
    // "knownStates":"0x0",
    // "pulledStates":"0x0",
    // "startingBlock":"0x862ba"}

    @Hex
    long currentBlock;
    @Hex
    long highestBlock;
    @Hex
    long knownStates;
    @Hex
    long pulledStates;
    @Hex
    long startingBlock;

    @Override
    public String toString() {
        return "EthSyncingResponse{" +
                "currentBlock=" + currentBlock +
                ", highestBlock=" + highestBlock +
                ", knownStates=" + knownStates +
                ", pulledStates=" + pulledStates +
                ", startingBlock=" + startingBlock +
                '}';
    }
}
