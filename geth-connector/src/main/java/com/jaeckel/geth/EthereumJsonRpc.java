package com.jaeckel.geth;

import com.jaeckel.geth.json.EthAccountsResponse;
import com.jaeckel.geth.json.EthSyncingResult;
import com.jaeckel.geth.json.PersonalListAccountsResponse;
import com.jaeckel.geth.json.PersonalNewAccountResponse;
import com.jaeckel.geth.json.PersonalUnlockAccountResponse;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;

import java.io.IOException;
import java.math.BigInteger;

import rx.Observable;

public interface EthereumJsonRpc {



    void ethAccounts(Callback<EthAccountsResponse> callback) throws IOException;

    void personalListAccounts(Callback<PersonalListAccountsResponse> callback) throws IOException;

    void personalNewAccount(String password, Callback<PersonalNewAccountResponse> callback) throws IOException;

    void personalUnlockAccount(String address, String password, int timeInSeconds, Callback<PersonalUnlockAccountResponse> callback) throws IOException;

    Observable<BigInteger> ethGetBalance(String address, String blockParameter);

    Observable<Long> netPeerCount();

    Observable<Long> ethBlockNumber();

    Observable<EthSyncingResult> ethSyncing();

    interface Callback<T> {

        void onResult(T t);

        void onError(JSONRPC2Error error);
    }
}
