package com.jaeckel.geth;

import com.jaeckel.geth.json.EthAccountsResponse;
import com.jaeckel.geth.json.EthBlockNumberResponse;
import com.jaeckel.geth.json.EthSyncingResponse;
import com.jaeckel.geth.json.HexAdapter;
import com.jaeckel.geth.json.NetPeerCountResponse;
import com.jaeckel.geth.json.PersonalListAccountsResponse;
import com.jaeckel.geth.json.PersonalNewAccountResponse;
import com.jaeckel.geth.json.PersonalUnlockAccountResponse;
import com.jaeckel.geth.json.SendTransactionresult;
import com.segment.jsonrpc.JsonRPCConverterFactory;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import rx.Observer;
import rx.schedulers.Schedulers;

public class GethConnector implements EthereumJsonRpc {

    private static final String METHOD_NET_PEER_COUNT = "net_peerCount";
    private static final String METHOD_ETH_BLOCK_NUMBER = "eth_blockNumber";
    private static final String METHOD_ETH_GET_BALANCE = "eth_getBalance";
    private static final String METHOD_ETH_SYNCING = "eth_syncing";
    private static final String JSON_RPC_ENDPOINT = "http://localhost:8545/";
    private static final String METHOD_ETH_ACCOUNTS = "eth_accounts";
    private static Integer requestId = 1;
    private Moshi moshi = MoshiFactory.createMoshi();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient httpClient;
    private final Retrofit retrofit;

    public GethConnector() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(JSON_RPC_ENDPOINT)
                .addConverterFactory(JsonRPCConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(MoshiFactory.createMoshi()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public void netPeerCount(Callback<NetPeerCountResponse> callback) throws IOException {

        Response response = httpClient.newCall(new Request.Builder().url(JSON_RPC_ENDPOINT)
                                                       .post(RequestBody.create(JSON, createRequest(METHOD_NET_PEER_COUNT)))
                                                       .build()).execute();
        JsonAdapter<NetPeerCountResponse> jsonAdapter = moshi.adapter(NetPeerCountResponse.class);
        NetPeerCountResponse netPeerCountResponse = jsonAdapter.fromJson(response.body().source());
        callback.onResult(netPeerCountResponse);

//        try {
//            personalListAccounts(new Callback<EthAccountsResponse>() {
//                @Override
//                public void onResult(EthAccountsResponse ethAccountsResponse) {
//
//                }
//
//                @Override
//                public void onError(JSONRPC2Error error) {
//
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void ethGetBalance(String address, String blockParameter, final Callback<BigInteger> callback) throws IOException {

        GethService gethService = retrofit.create(GethService.class);
        List<String> params = new ArrayList<>();
        params.add(address);
        params.add(blockParameter);
        gethService.getBalance(params)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("-------------> onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("-------------> onError(): " + e.getMessage());
                        e.printStackTrace();
                        callback.onError(new JSONRPC2Error(0, e.getMessage()));
                    }

                    @Override
                    public void onNext(String ethGetBalanceResponse) {
                        System.out.println("-------------> ethGetBalanceResponse: " + ethGetBalanceResponse);
                        callback.onResult(new BigInteger(ethGetBalanceResponse.substring(2), 16));

                    }
                });
//        Response response = httpClient.newCall(new Request.Builder().url(JSON_RPC_ENDPOINT)
//                                                       .post(RequestBody.create(JSON, createBalanceRequest(address, blockParameter, METHOD_ETH_GET_BALANCE)))
//                                                       .build()).execute();
//        JsonAdapter<EthGetBalanceResponse> jsonAdapter = moshi.adapter(EthGetBalanceResponse.class);
//        EthGetBalanceResponse ethGetBalanceResponse = jsonAdapter.fromJson(response.body().source());
//        callback.onResult(ethGetBalanceResponse);
    }

    public void ethSyncing(Callback<EthSyncingResponse> callback) throws IOException {

        Response response = httpClient.newCall(
                new Request.Builder().url(JSON_RPC_ENDPOINT)
                        .post(RequestBody.create(JSON, createRequest(METHOD_ETH_SYNCING)))
                        .build())
                .execute();
        JsonAdapter<EthSyncingResponse> jsonAdapter = moshi.adapter(EthSyncingResponse.class);
        EthSyncingResponse ethSyncingResponse = jsonAdapter.fromJson(response.body().source());
        callback.onResult(ethSyncingResponse);
    }

    public void ethBlockNumber(Callback<EthBlockNumberResponse> callback) throws IOException {

        Response response = httpClient.newCall(new Request.Builder().url(JSON_RPC_ENDPOINT)
                                                       .post(RequestBody.create(JSON, createRequest(METHOD_ETH_BLOCK_NUMBER)))
                                                       .build()).execute();
        JsonAdapter<EthBlockNumberResponse> jsonAdapter = moshi.adapter(EthBlockNumberResponse.class);
        EthBlockNumberResponse ethBlockNumberResponse = jsonAdapter.fromJson(response.body().source());
        callback.onResult(ethBlockNumberResponse);
    }

    public void ethAccounts(Callback<EthAccountsResponse> callback) throws IOException {

        Response response = httpClient.newCall(new Request.Builder().url(JSON_RPC_ENDPOINT)
                                                       .post(RequestBody.create(JSON, createRequest(METHOD_ETH_ACCOUNTS)))
                                                       .build()).execute();

        JsonAdapter<EthAccountsResponse> jsonAdapter = moshi.adapter(EthAccountsResponse.class);
        EthAccountsResponse ethAccountsResponse = jsonAdapter.fromJson(response.body().source());

        callback.onResult(ethAccountsResponse);
    }

    @Override
    public void personalNewAccount(String password, Callback<PersonalNewAccountResponse> callback) throws IOException {

        Response response = httpClient.newCall(new Request.Builder().url(JSON_RPC_ENDPOINT)
                                                       .post(RequestBody.create(JSON, createNewAccountRequest("personal_newAccount", password)))
                                                       .build()).execute();

        JsonAdapter<PersonalNewAccountResponse> jsonAdapter = moshi.adapter(PersonalNewAccountResponse.class);
        PersonalNewAccountResponse ethAccountsResponse = jsonAdapter.fromJson(response.body().source());

        callback.onResult(ethAccountsResponse);
    }

    @Override
    public void personalListAccounts(Callback<PersonalListAccountsResponse> callback) throws IOException {

        Response response = httpClient.newCall(new Request.Builder().url(JSON_RPC_ENDPOINT)
                                                       .post(RequestBody.create(
                                                               JSON,
                                                               createListAccountRequest("personal_listAccounts")
                                                             )
                                                       )
                                                       .build()
        ).execute();

        JsonAdapter<PersonalListAccountsResponse> jsonAdapter = moshi.adapter(PersonalListAccountsResponse.class);
//        PersonalListAccountsResponse ethAccountsResponse = jsonAdapter.fromJson(response.body().source());
        String string = response.body().string();
        System.out.println("personal_listAccounts:  " + string);
        PersonalListAccountsResponse ethAccountsResponse = jsonAdapter.fromJson(string);

        callback.onResult(ethAccountsResponse);
    }

    @Override
    public void personalUnlockAccount(String address, String password, int timeInSeconds, Callback<PersonalUnlockAccountResponse> callback) throws IOException {

        Response response = httpClient.newCall(new Request.Builder().url(JSON_RPC_ENDPOINT)
                                                       .post(RequestBody.create(
                                                               JSON,
                                                               createUnlockAccountRequest(
                                                                       "personal_unlockAccount",
                                                                       address,
                                                                       password,
                                                                       timeInSeconds
                                                               )
                                                             )
                                                       ).build()
        ).execute();

        JsonAdapter<PersonalUnlockAccountResponse> jsonAdapter = moshi.adapter(PersonalUnlockAccountResponse.class);
        PersonalUnlockAccountResponse ethAccountsResponse = jsonAdapter.fromJson(response.body().source());

        callback.onResult(ethAccountsResponse);
    }
//    public long ethGetBalance(String account) {
//        JsonRpcRequest jsonRpcRequest = new JsonRpcRequest(Collections.singletonList(account));
//        Response response = httpClient.newCall(
//                new Request.Builder().url(JSON_RPC_ENDPOINT)
//                        .post(RequestBody.create(JSON, createGetBalanceRequest(account)))
//                        .build()
//        ).execute();
//
//
//    }
//
//    private String createGetBalanceRequest(String account) {
//        return
//    }

    public static String sendTransaction(String from, String to, long wei, Callback<SendTransactionresult> callback) throws IOException {

        Response response = httpClient.newCall(
                new Request.Builder().url(JSON_RPC_ENDPOINT)
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

    public static String createListAccountRequest(String method) {
        List<Object> params = new ArrayList<>();
        Integer id = requestId++;
        JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
        System.out.println("Request: " + reqOut.toString());
        return reqOut.toString();
    }

    public static String createNewAccountRequest(String method, String password) {
        List<Object> params = new ArrayList<>();
        params.add(password);

        Integer id = requestId++;
        JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
        System.out.println("Request: " + reqOut.toString());
        return reqOut.toString();
    }

    public static String createUnlockAccountRequest(String method, String address, String password, int timeInSeconds) {
        List<Object> params = new ArrayList<>();
        params.add(address);
        params.add(password);
        params.add(timeInSeconds);

        Integer id = requestId++;
        JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
        System.out.println("Request: " + reqOut.toString());
        return reqOut.toString();
    }

    public static String createBalanceRequest(String address, String blockParameter, String method) {
        List<Object> params = new ArrayList<>();
        params.add(address);
        params.add(blockParameter);
        Integer id = requestId++;
        JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
        System.out.println("Request: " + reqOut.toString());
        return reqOut.toString();
    }
}
