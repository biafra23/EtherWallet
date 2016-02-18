package com.jaeckel.etherwallet;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.ethereum.go_ethereum.cmd.Geth;
import com.jaeckel.geth.EthereumJsonRpc;
import com.jaeckel.geth.GethConnector;
import com.jaeckel.geth.json.EthAccountsResult;
import com.jaeckel.geth.json.EthSyncingResponse;
import com.jaeckel.geth.json.NetPeerCountResponse;
import com.novoda.notils.logger.simple.Log;

import java.io.IOException;

public class GethService extends Service implements EthereumJsonRpc {

    private final IBinder mBinder = new MyBinder();
    private GethConnector gethConnector;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("onBind()");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate()");
        gethConnector = new GethConnector();
        initGeth();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    private void initGeth() {
        Log.d("initGeth()...");

        new Thread(new Runnable() {
            public void run() {

                Log.d("absolutePath: " + getChainDataDir()); //data/data/org.ethereum.droidwallet/files
                int foo = Geth.run("--ipcdisable --rpc --rpccorsdomain=* --fast --datadir=" + getChainDataDir());
                //Never reached
            }
        }).start();

        SystemClock.sleep(1000);
        Log.d("Geth.doUnlockAccount()... right password");

        Geth.doUnlockAccount(getChainDataDir(), "0x5d62714ddded8425414d9665cb63a3a1ebf9f860", "password", "1000ms");
        Log.d("Geth.doUnlockAccount()... wrong password");
        Geth.doUnlockAccount(getChainDataDir(), "0x5d62714ddded8425414d9665cb63a3a1ebf9f860", "wrong", "1000ms");
        Log.d("...done.");

    }

    @NonNull
    private String getChainDataDir() {
        Log.d("absolutePath: " + getFilesDir().getAbsolutePath());
        return getFilesDir().getAbsolutePath();
    }

    @Override
    public void netPeerCount(final Callback<NetPeerCountResponse> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gethConnector.netPeerCount(callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void ethSyncing(final Callback<EthSyncingResponse> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gethConnector.ethSyncing(callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void ethAccounts(final Callback<EthAccountsResult> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gethConnector.ethAccounts(callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public class MyBinder extends Binder {
        GethService getService() {
            return GethService.this;
        }
    }
}
