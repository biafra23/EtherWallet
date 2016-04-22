package com.jaeckel.etherwallet;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.ethereum.go_ethereum.cmd.Geth;
import com.jaeckel.geth.EthereumJsonRpc;
import com.jaeckel.geth.GethConnector;
import com.jaeckel.geth.json.EthAccountsResponse;
import com.jaeckel.geth.json.EthBlockNumberResponse;
import com.jaeckel.geth.json.EthGetBalanceResponse;
import com.jaeckel.geth.json.EthSyncingResponse;
import com.jaeckel.geth.json.NetPeerCountResponse;
import com.jaeckel.geth.json.PersonalListAccountsResponse;
import com.jaeckel.geth.json.PersonalNewAccountResponse;
import com.jaeckel.geth.json.PersonalUnlockAccountResponse;

import java.io.IOException;

import timber.log.Timber;

public class GethService extends Service implements EthereumJsonRpc {

    private final IBinder mBinder = new MyBinder();
    private GethConnector gethConnector;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.d("onBind()");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate()");
        gethConnector = new GethConnector();

        initGeth();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    private void initGeth() {
        Timber.d("initGeth()...");

        new Thread(new Runnable() {
            public void run() {
                Timber.d("absolutePath: %s", getChainDataDir()); //data/data/org.ethereum.droidwallet/files

                if (BuildConfig.ETH_NETWORK.equals("testnet")) {

                    Geth.run("--testnet " +
//                                     " --bootnodes enode://f6526c15fed808a8079debdf2284234bf511907bdf10bd8a80a99a8f635445f015d53e6f50db6deca82c8754da8d0e72188de2b31886f9fa5f5df0fc3ca5157e@[::]:30304 " +
                                     "--ipcdisable --rpcaddr 127.0.0.1 --rpcapi eth,net,personal --rpc --rpccorsdomain=* " +
                                     "--fast --datadir=" + getChainDataDir());
                } else {
                    Geth.run("--ipcdisable --rpcaddr 127.0.0.1 --rpcapi eth,net,personal --rpc --rpccorsdomain=* --fast --datadir=" + getChainDataDir());
                    //Never reached

                    throw new RuntimeException("geth crashed");
                }
            }
        }).start();

//        SystemClock.sleep(1000);
//        Log.d("Geth.doUnlockAccount()... right password");

//        Geth.doUnlockAccount(getChainDataDir(), "0x5d62714ddded8425414d9665cb63a3a1ebf9f860", "password", "1000ms");
//        Log.d("Geth.doUnlockAccount()... wrong password");
//        Geth.doUnlockAccount(getChainDataDir(), "0x5d62714ddded8425414d9665cb63a3a1ebf9f860", "wrong", "1000ms");
//        Log.d("...done.");

    }

    @NonNull
    private String getChainDataDir() {
        Timber.d("-> absolutePath: %s", getFilesDir().getAbsolutePath());
        return getFilesDir().getAbsolutePath();
    }

    @Override
    public void netPeerCount(final Callback<NetPeerCountResponse> callback) {
        try {
            gethConnector.netPeerCount(callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ethSyncing(final Callback<EthSyncingResponse> callback) {
        try {
            gethConnector.ethSyncing(callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ethBlockNumber(final Callback<EthBlockNumberResponse> callback) {
        try {
            gethConnector.ethBlockNumber(callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void ethAccounts(final Callback<EthAccountsResponse> callback) {
        try {
            gethConnector.ethAccounts(callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void personalListAccounts(Callback<PersonalListAccountsResponse> callback) {
        try {
            gethConnector.personalListAccounts(callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void personalUnlockAccount(String address, String password, int timeInSeconds, Callback<PersonalUnlockAccountResponse> callback) throws IOException {
        try {
            gethConnector.personalUnlockAccount(address, password, timeInSeconds, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ethGetBalance(String address, String blockParameter, Callback<EthGetBalanceResponse> ethGetBalanceCallback) {
        try {
            //Block number in Hex or "latest", "earliest" or "pending"
            // See https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter
            gethConnector.ethGetBalance(address, blockParameter, ethGetBalanceCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void personalNewAccount(String password, final Callback<PersonalNewAccountResponse> callback) {
        try {
            gethConnector.personalNewAccount(password, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MyBinder extends Binder {
        GethService getService() {
            return GethService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Timber.d("onUnbind()");
        //TODO: Kill Geth
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy()");
        super.onDestroy();
    }
}
