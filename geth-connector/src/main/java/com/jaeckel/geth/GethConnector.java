package com.jaeckel.geth;

import com.jaeckel.geth.json.EthAccountsResult;
import com.jaeckel.geth.json.EthSyncingResponse;
import com.jaeckel.geth.json.EthSyncingResultAdapter;
import com.jaeckel.geth.json.FalseToNullFactory;
import com.jaeckel.geth.json.HexAdapter;
import com.jaeckel.geth.json.NetPeerCountResponse;
import com.jaeckel.geth.json.SendTransactionresult;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class GethConnector implements EthereumJsonRpc {

    private static final String METHOD_NET_PEER_COUNT = "net_peerCount";
    private static final String METHOD_ETH_SYNCING = "eth_syncing";
    private static final String JSONRPC_ENDPOINT = "http://localhost:8545/";
    private static final String METHOD_ETH_ACCOUNTS = "eth_accounts";
    private static Integer requestId = 1;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient httpClient;

    public GethConnector() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    private Moshi moshi = new Moshi.Builder()
            .add(new HexAdapter())
            .add(new FalseToNullFactory())
            .add(new EthSyncingResultAdapter())
            .build();

    public void netPeerCount(Callback<NetPeerCountResponse> callback) throws IOException {

        Response response = httpClient.newCall(new Request.Builder().url(JSONRPC_ENDPOINT)
                                                       .post(RequestBody.create(JSON, createRequest(METHOD_NET_PEER_COUNT)))
                                                       .build()).execute();
//        String jsonString = response.body().string();
//        try {
//            JSONRPC2Response jsonRpc2Response = JSONRPC2Response.parse(jsonString);

//            if (jsonRpc2Response.indicatesSuccess()) {

        JsonAdapter<NetPeerCountResponse> jsonAdapter = moshi.adapter(NetPeerCountResponse.class);
        NetPeerCountResponse netPeerCountResponse = jsonAdapter.fromJson(response.body().source());
        callback.onResult(netPeerCountResponse);
//            } else {
//                callback.onError(jsonRpc2Response.getError());
//            }
//        } catch (JSONRPC2ParseException e) {
//            System.out.println("JSONRPC2ParseException");
//            e.printStackTrace();
//        }
    }

    public void ethSyncing(Callback<EthSyncingResponse> callback) throws IOException {

        Response response = httpClient.newCall(
                new Request.Builder().url(JSONRPC_ENDPOINT)
                        .post(RequestBody.create(JSON, createRequest(METHOD_ETH_SYNCING)))
                        .build())
                .execute();
        JsonAdapter<EthSyncingResponse> jsonAdapter = moshi.adapter(EthSyncingResponse.class);
        EthSyncingResponse ethSyncingResponse = jsonAdapter.fromJson(response.body().source());
        callback.onResult(ethSyncingResponse);
    }

    public void ethAccounts(Callback<EthAccountsResult> callback) throws IOException {

        Response response = httpClient.newCall(new Request.Builder().url(JSONRPC_ENDPOINT)
                                                       .post(RequestBody.create(JSON, createRequest(METHOD_ETH_ACCOUNTS)))
                                                       .build()).execute();

//        String jsonString = response.body().string();
//        System.out.println("Response: " + jsonString);
//        try {
//            JSONRPC2Response jsonRpc2Response = JSONRPC2Response.parse(jsonString);
//            if (jsonRpc2Response.indicatesSuccess()) {
        JsonAdapter<EthAccountsResult> jsonAdapter = moshi.adapter(EthAccountsResult.class);
        EthAccountsResult ethAccountsResult = jsonAdapter.fromJson(response.body().source());

        callback.onResult(ethAccountsResult);

//            } else {
//
//                callback.onError(jsonRpc2Response.getError());
//            }
//        } catch (JSONRPC2ParseException e) {
//            System.out.println("JSONRPC2ParseException");
//            e.printStackTrace();
//        }
    }

    public static String sendTransaction(String from, String to, long wei, Callback<SendTransactionresult> callback) throws IOException {

        Response response = httpClient.newCall(
                new Request.Builder().url(JSONRPC_ENDPOINT)
                        .post(RequestBody.create(JSON, createTxRequest(from, to, wei)))
                        .build()
        ).execute();

        String result = response.body()
                .string();
        System.out.println("Response: " + result);

        return result;

    }

    public static String createTxRequest(String from, String to, long wei) {
        Map<String, Object> params = new HashMap<>();

        Integer id = requestId++;
        JSONRPC2Request reqOut = new JSONRPC2Request(METHOD_ETH_ACCOUNTS, params, id);
        System.out.println("Request: " + reqOut.toString());
        return reqOut.toString();
    }

    public static String createRequest(String method) {
        Map<String, Object> params = new HashMap<>();
        Integer id = requestId++;
        JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
        System.out.println("Request: " + reqOut.toString());
        return reqOut.toString();
    }

}
