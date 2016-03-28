package com.jaeckel.geth;

import com.jaeckel.geth.json.EthAccountsResponse;
import com.jaeckel.geth.json.PersonalListAccountsResponse;
import com.jaeckel.geth.json.PersonalNewAccountResponse;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;

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

        gethConnector.ethSyncing();

        gethConnector.ethAccounts(new EthereumJsonRpc.Callback<EthAccountsResponse>() {
            @Override
            public void onResult(EthAccountsResponse ethAccountsResponse) {
                System.out.println("ethAccountsResult: " + ethAccountsResponse);
            }

            @Override
            public void onError(JSONRPC2Error error) {
                System.out.println("error: " + error);
            }
        });

        gethConnector.personalListAccounts(new EthereumJsonRpc.Callback<PersonalListAccountsResponse>() {
            @Override
            public void onResult(PersonalListAccountsResponse personalListAccountsResponse) {
                System.out.println("personalListAccountsResponse: " + personalListAccountsResponse);
            }

            @Override
            public void onError(JSONRPC2Error error) {
                System.out.println("error: " + error);
            }
        });
        gethConnector.personalNewAccount("", new EthereumJsonRpc.Callback<PersonalNewAccountResponse>() {
            @Override
            public void onResult(PersonalNewAccountResponse personalNewAccountResponse) {
                System.out.println("personalNewAccountResponse: " + personalNewAccountResponse);
            }

            @Override
            public void onError(JSONRPC2Error error) {
                System.out.println("error: " + error);
            }
        });
    }
}
