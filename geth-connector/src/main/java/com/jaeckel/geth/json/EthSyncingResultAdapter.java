package com.jaeckel.geth.json;

import com.jaeckel.geth.json.FalseToNullFactory.FalseToNull;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class EthSyncingResultAdapter {

    @ToJson
    EthSyncingResultJson toJson(@FalseToNull EthSyncingResult in) {
        EthSyncingResultJson out = new EthSyncingResultJson();
        out.currentBlock = in.currentBlock;
        out.highestBlock = in.highestBlock;
        out.knownStates = in.knownStates;
        out.pulledStates = in.pulledStates;
        out.startingBlock = in.startingBlock;
        return out;
    }

    @FromJson
    @FalseToNull
    EthSyncingResult fromJson(EthSyncingResultJson in) {
        EthSyncingResult result = new EthSyncingResult();
        result.currentBlock = in.currentBlock;
        result.highestBlock = in.highestBlock;
        result.knownStates = in.knownStates;
        result.pulledStates = in.pulledStates;
        result.startingBlock = in.startingBlock;
        return result;
    }
}
