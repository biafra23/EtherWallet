package com.jaeckel.geth;

import com.jaeckel.geth.json.EthAccountsResponse;
import com.jaeckel.geth.json.EthBlockNumberResponse;
import com.jaeckel.geth.json.EthSyncingResponse;
import com.jaeckel.geth.json.NetPeerCountResponse;
import com.jaeckel.geth.json.PersonalListAccountsResponse;
import com.jaeckel.geth.json.PersonalNewAccountResponse;
import com.jaeckel.geth.json.PersonalUnlockAccountResponse;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;

import java.io.IOException;
import java.math.BigInteger;

public interface EthereumJsonRpc {

    void netPeerCount(Callback<NetPeerCountResponse> callback) throws IOException;

    void ethSyncing(Callback<EthSyncingResponse> callback) throws IOException;

    void ethBlockNumber(Callback<EthBlockNumberResponse> callback) throws IOException;

    void ethAccounts(Callback<EthAccountsResponse> callback) throws IOException;

    void personalListAccounts(Callback<PersonalListAccountsResponse> callback) throws IOException;

    void personalNewAccount(String password, Callback<PersonalNewAccountResponse> callback) throws IOException;

    void personalUnlockAccount(String address, String password, int timeInSeconds, Callback<PersonalUnlockAccountResponse> callback) throws IOException;

    void ethGetBalance(String address, String blockParameter, Callback<BigInteger> ethGetBalanceCallback) throws IOException;

    interface Callback<T> {

        void onResult(T t);

        void onError(JSONRPC2Error error);
    }
}
