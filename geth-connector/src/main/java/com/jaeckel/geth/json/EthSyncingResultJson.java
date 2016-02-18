package com.jaeckel.geth.json;

public class EthSyncingResultJson {
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
