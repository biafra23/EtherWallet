package com.jaeckel.geth;

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

}
