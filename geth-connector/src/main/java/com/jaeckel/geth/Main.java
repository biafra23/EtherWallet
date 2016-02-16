package com.jaeckel.geth;

import com.jaeckel.geth.json.EthAccountsResult;
import com.jaeckel.geth.json.EthSyncingResult;
import com.jaeckel.geth.json.NetPeerCountResponse;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("main()");

        GethConnector gethConnector = new GethConnector();
        gethConnector.netPeerCount(new GethConnector.Callback<NetPeerCountResponse>() {
            @Override
            public void setResult(NetPeerCountResponse netPeerCountResponse) {
                System.out.println("netPeerCountResponse: " + netPeerCountResponse);
            }

            @Override
            public void setError(JSONRPC2Error error) {
                System.out.println("error: " + error);
            }
        });

        gethConnector.ethSyncing(new GethConnector.Callback<EthSyncingResult>() {
            @Override
            public void setResult(EthSyncingResult ethSyncingResult) {
                System.out.println("EthSyncingResult: " + ethSyncingResult);
            }

            @Override
            public void setError(JSONRPC2Error error) {
                System.out.println("error: " + error);
            }
        });

        gethConnector.ethAccounts(new GethConnector.Callback<EthAccountsResult>() {
            @Override
            public void setResult(EthAccountsResult ethAccountsResult) {
                System.out.println("ethAccountsResult: " + ethAccountsResult);
            }

            @Override
            public void setError(JSONRPC2Error error) {
                System.out.println("error: " + error);
            }
        });
    }
}
