package com.jaeckel.geth;

import com.jaeckel.geth.json.EthSyncingResult;

import java.util.List;

import rx.Observer;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("main()");

        GethConnector gethConnector = new GethConnector();
        gethConnector.netPeerCount()
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error: " + e.getMessage());

                    }

                    @Override
                    public void onNext(Long netPeerCount) {
                        System.out.println("netPeerCount: " + netPeerCount);

                    }
                });

        gethConnector.ethSyncing().subscribe(new Observer<EthSyncingResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error: " + e.getMessage());
            }

            @Override
            public void onNext(EthSyncingResult ethSyncingResult) {
                System.out.println("ethSyncingResult: " + ethSyncingResult);

            }
        });
        gethConnector.ethAccounts().subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error: " + e.getMessage());
            }

            @Override
            public void onNext(List<String> accounts) {
                for (String account : accounts) {
                    System.out.println("Account: " + account);
                }
            }
        });
        gethConnector.personalListAccounts().subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error: " + e.getMessage());
            }

            @Override
            public void onNext(List<String> accounts) {
                for (String account : accounts) {
                    System.out.println("Account: " + account);
                }
            }
        });
        gethConnector.personalNewAccount("").subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error: " + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                System.out.println("s: " + s);

            }
        });
    }
}
