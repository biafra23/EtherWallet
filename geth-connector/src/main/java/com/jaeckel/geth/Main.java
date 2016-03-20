package com.jaeckel.geth;

import com.jaeckel.geth.json.EthAccountsResponse;
import com.jaeckel.geth.json.EthSyncingResponse;
import com.jaeckel.geth.json.NetPeerCountResponse;
import com.jaeckel.geth.json.PersonalListAccountsResponse;
import com.jaeckel.geth.json.PersonalNewAccountResponse;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("main()");

        GethConnector gethConnector = new GethConnector();
        gethConnector.netPeerCount(new EthereumJsonRpc.Callback<NetPeerCountResponse>() {
            @Override
            public void onResult(NetPeerCountResponse netPeerCountResponse) {
                System.out.println("netPeerCountResponse: " + netPeerCountResponse);
            }

            @Override
            public void onError(JSONRPC2Error error) {
                System.out.println("error: " + error);
            }
        });

        gethConnector.ethSyncing(new EthereumJsonRpc.Callback<EthSyncingResponse>() {
            @Override
            public void onResult(EthSyncingResponse ethSyncingResponse) {
                System.out.println("EthSyncingResult: " + ethSyncingResponse);
            }

            @Override
            public void onError(JSONRPC2Error error) {
                System.out.println("error: " + error);
            }
        });

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
