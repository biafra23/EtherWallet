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
import com.jaeckel.geth.json.EthSyncingResult;
import com.novoda.notils.logger.simple.Log;

import java.math.BigInteger;
import java.util.List;

import rx.Observable;

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

                if (BuildConfig.ETH_NETWORK.equals("testnet")) {

                    Geth.run("--testnet " +
//                                     " --bootnodes enode://f6526c15fed808a8079debdf2284234bf511907bdf10bd8a80a99a8f635445f015d53e6f50db6deca82c8754da8d0e72188de2b31886f9fa5f5df0fc3ca5157e@[::]:30304 " +
                                     "--ipcdisable --rpcaddr 127.0.0.1 --rpcapi eth,net,personal --rpc --rpccorsdomain=* " +
                                     "--fast --datadir=" + getChainDataDir());
                    throw new RuntimeException("geth (testnet) crashed");

                } else {
                    Geth.run("--ipcdisable --rpcaddr 127.0.0.1 --rpcapi eth,net,personal --rpc --rpccorsdomain=* --fast --datadir=" + getChainDataDir());
                    //Never reached

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
        Log.d("-> absolutePath: " + getFilesDir().getAbsolutePath());
        return getFilesDir().getAbsolutePath();
    }

    @Override
    public Observable<Long> netPeerCount() {
        return gethConnector.netPeerCount();
    }

    @Override
    public Observable<EthSyncingResult> ethSyncing() {
        return gethConnector.ethSyncing();
    }

    @Override
    public Observable<Long> ethBlockNumber() {
        return gethConnector.ethBlockNumber();
    }

    @Override
    public Observable<List<String>> ethAccounts() {
        return gethConnector.ethAccounts();
    }

    @Override
    public Observable<List<String>> personalListAccounts() {
        return gethConnector.personalListAccounts();
    }

    @Override
    public Observable<Void> personalUnlockAccount(String address, String password, int timeInSeconds) {
        return gethConnector.personalUnlockAccount(address, password, timeInSeconds);
    }

    @Override
    public Observable<BigInteger> ethGetBalance(String address, String blockParameter) {
        //Block number in Hex or "latest", "earliest" or "pending"
        // See https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter
        return gethConnector.ethGetBalance(address, blockParameter);
    }

    @Override
    public Observable<String> personalNewAccount(String password) {
        return gethConnector.personalNewAccount(password);
    }

    public class MyBinder extends Binder {
        GethService getService() {
            return GethService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("onUnbind()");
        //TODO: Kill Geth
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("onDestroy()");
        super.onDestroy();
    }
}
