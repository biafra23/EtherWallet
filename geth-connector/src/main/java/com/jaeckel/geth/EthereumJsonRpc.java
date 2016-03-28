package com.jaeckel.geth;

import com.jaeckel.geth.json.EthAccountsResponse;
import com.jaeckel.geth.json.EthSyncingResponse;
import com.jaeckel.geth.json.PersonalListAccountsResponse;
import com.jaeckel.geth.json.PersonalNewAccountResponse;
import com.jaeckel.geth.json.PersonalUnlockAccountResponse;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;

import java.io.IOException;
import java.math.BigInteger;

import rx.Observable;

public interface EthereumJsonRpc {

    void ethSyncing(Callback<EthSyncingResponse> callback) throws IOException;


    void ethAccounts(Callback<EthAccountsResponse> callback) throws IOException;

    void personalListAccounts(Callback<PersonalListAccountsResponse> callback) throws IOException;

    void personalNewAccount(String password, Callback<PersonalNewAccountResponse> callback) throws IOException;

    void personalUnlockAccount(String address, String password, int timeInSeconds, Callback<PersonalUnlockAccountResponse> callback) throws IOException;

    Observable<BigInteger> ethGetBalance(String address, String blockParameter) throws IOException;

    Observable<Long> netPeerCount() throws IOException;

    Observable<Long> ethBlockNumber() throws IOException;

    interface Callback<T> {

        void onResult(T t);

        void onError(JSONRPC2Error error);
    }
}
