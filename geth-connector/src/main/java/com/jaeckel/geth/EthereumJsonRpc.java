package com.jaeckel.geth;

import com.jaeckel.geth.json.EthSyncingResult;

import java.math.BigInteger;
import java.util.List;

import rx.Observable;

public interface EthereumJsonRpc {

    Observable<List<String>> ethAccounts();

    Observable<List<String>> personalListAccounts();

    Observable<String> personalNewAccount(String password);

    Observable<Void> personalUnlockAccount(String address, String password, int timeInSeconds);

    Observable<BigInteger> ethGetBalance(String address, String blockParameter);

    Observable<Long> netPeerCount();

    Observable<Long> ethBlockNumber();

    Observable<EthSyncingResult> ethSyncing();
}
