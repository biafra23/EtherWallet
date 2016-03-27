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

}
