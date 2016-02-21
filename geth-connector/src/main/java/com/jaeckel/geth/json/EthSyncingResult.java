package com.jaeckel.geth.json;

public class EthSyncingResult {

    //{"currentBlock":"0xdb5f5",
    // "highestBlock":"0xf5664",
    // "knownStates":"0x0",
    // "pulledStates":"0x0",
    // "startingBlock":"0x862ba"}

    @Hex
    public long currentBlock;
    @Hex
    public long highestBlock;
    @Hex
    public long knownStates;
    @Hex
    public long pulledStates;
    @Hex
    public long startingBlock;

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
