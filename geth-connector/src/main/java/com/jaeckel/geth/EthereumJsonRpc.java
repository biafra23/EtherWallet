package com.jaeckel.geth;

import com.jaeckel.geth.json.EthAccountsResult;
import com.jaeckel.geth.json.EthSyncingResponse;
import com.jaeckel.geth.json.NetPeerCountResponse;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;

import java.io.IOException;

public interface EthereumJsonRpc {

    void netPeerCount(Callback<NetPeerCountResponse> callback) throws IOException;

    void ethSyncing(Callback<EthSyncingResponse> callback) throws IOException;

    void ethAccounts(Callback<EthAccountsResult> callback) throws IOException;

    interface Callback<T> {

        void onResult(T t);

        void onError(JSONRPC2Error error);
    }
}
