package com.jaeckel.geth;

import com.jaeckel.geth.json.EthSyncingResult;
import com.segment.jsonrpc.JsonRPC;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface GethService {

    @JsonRPC("eth_getBalance")
    @POST("/")
    Observable<String> getBalance(@Body List<String> params);

    @JsonRPC("net_peerCount")
    @POST("/")
    Observable<String> netPeerCount(@Body String ignored);

    @JsonRPC("eth_blockNumber")
    @POST("/")
    Observable<String> ethBlockNumber(@Body String ignored);

    @JsonRPC("eth_syncing")
    @POST("/")
    Observable<EthSyncingResult> ethSyncing(@Body String ignored);

    @JsonRPC("eth_accounts")
    @POST("/")
    Observable<List<String>> ethAccounts(@Body String ignored);

    @JsonRPC("personal_listAccounts") //same as eth_accounts?
    @POST("/")
    Observable<List<String>> personalListAccounts(@Body String ignored);

    @JsonRPC("personal_unlockAccount")
    @POST("/")
    Observable<Void> personalUnlockAccount(@Body List<Object> params);

    @JsonRPC("personal_newAccount")
    @POST("/")
    Observable<String> personalNewAccount(@Body String password);

}
