package com.jaeckel.geth;

import com.jaeckel.geth.json.EthSyncingResult;
import com.jaeckel.geth.json.HexAdapter;
import com.segment.jsonrpc.JsonRPCConverterFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GethConnector implements EthereumJsonRpc {

    private static final String JSON_RPC_ENDPOINT = "http://localhost:8545/";

    private GethService gethService;

    public GethConnector() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(JSON_RPC_ENDPOINT)
                .addConverterFactory(JsonRPCConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(MoshiFactory.createMoshi()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        gethService = retrofit.create(GethService.class);

    }

    public Observable<Long> netPeerCount() {
        return Observable.interval(5, TimeUnit.SECONDS)
                .retry() // continue on any error
                .flatMap(new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return gethService.netPeerCount("").map(convertHexStringToLong());
                    }

                    private Func1<String, Long> convertHexStringToLong() {
                        return new Func1<String, Long>() {
                            @Override
                            public Long call(String s) {
                                return HexAdapter.fromJson(s);
                            }
                        };
                    }
                });
    }

    public Observable<Long> ethBlockNumber() {
        return Observable.interval(5, TimeUnit.SECONDS)
                .retry() // continue on any error
                .flatMap(new Func1<Long, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Long aLong) {
                        return gethService.ethBlockNumber("").map(convertHexStringToLong());
                    }

                    private Func1<String, Long> convertHexStringToLong() {
                        return new Func1<String, Long>() {
                            @Override
                            public Long call(String s) {
                                return HexAdapter.fromJson(s);
                            }
                        };
                    }
                });
    }

    public Observable<BigInteger> ethGetBalance(String address, String blockParameter) {

        List<String> params = new ArrayList<>();
        params.add(address);
        params.add(blockParameter);
        return gethService.getBalance(params)
                .map(hexStringToBigInteger())
                .subscribeOn(Schedulers.io());
    }

    private Func1<String, BigInteger> hexStringToBigInteger() {
        return new Func1<String, BigInteger>() {
            @Override
            public BigInteger call(String balanceHexString) {
                return new BigInteger(balanceHexString.substring(2), 16);
            }
        };
    }

    public Observable<EthSyncingResult> ethSyncing() {
        return Observable.interval(5, TimeUnit.SECONDS).flatMap(new Func1<Long, Observable<EthSyncingResult>>() {
            @Override
            public Observable<EthSyncingResult> call(Long aLong) {
                return gethService.ethSyncing("");
            }
        });
    }

    public Observable<List<String>> ethAccounts() {
        return gethService.ethAccounts("");
    }

    @Override
    public Observable<String> personalNewAccount(String password) {

        return gethService.personalNewAccount(password);
    }

    @Override
    public Observable<List<String>> personalListAccounts() {
        return gethService.personalListAccounts("");
    }

    @Override
    public Observable<Void> personalUnlockAccount(String address, String password, int timeInSeconds) {
        List<Object> params = new ArrayList<>();
        params.add(address);
        params.add(password);
        params.add(timeInSeconds);
        return gethService.personalUnlockAccount(params);
    }
}
