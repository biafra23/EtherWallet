package com.jaeckel.etherwallet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.ethereum.go_ethereum.cmd.Geth;
import com.jaeckel.geth.EthereumJsonRpc.Callback;
import com.jaeckel.geth.json.EthSyncingResponse;
import com.jaeckel.geth.json.NetPeerCountResponse;
import com.novoda.notils.logger.simple.Log;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //    private GethConnector gethConnector;
    private GethService gethService;
    private TextView currentBlock;
    private TextView netPeerCount;
    private TextView helloWorld;

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            Log.d("onServiceConnected()");
            GethService.MyBinder b = (GethService.MyBinder) binder;
            gethService = b.getService();

            ethSyncingObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<EthSyncingResponse>() {
                        @Override
                        public void onCompleted() {
                            Log.d("onCompleted()");
                            helloWorld.setText("DONE");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(e, "onError(): ");
                            currentBlock.setText("ERROR: " + e.getMessage());
                        }

                        @Override
                        public void onNext(EthSyncingResponse ethSyncingResponse) {
                            Log.d("OnNext(): " + ethSyncingResponse);

                            if (ethSyncingResponse.result != null) {
                                currentBlock.setText("Current Block: " + ethSyncingResponse.result.currentBlock);
                            } else {
                                helloWorld.setText("Not syncing");
                            }
                        }
                    });

            netPeerCountObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<NetPeerCountResponse>() {
                        @Override
                        public void onCompleted() {
                            Log.d("onCompleted()");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(e, "onError(): ");
                            netPeerCount.setText("ERROR: " + e.getMessage());
                        }

                        @Override
                        public void onNext(NetPeerCountResponse netPeerCountResponse) {
                            Log.d("onNext(): ");
                            netPeerCount.setText("NetPeerCount: " + netPeerCountResponse.result);
                        }
                    });

        }

        public void onServiceDisconnected(ComponentName className) {
            gethService = null;
        }
    };

    Observable<EthSyncingResponse> ethSyncingObservable = Observable.create(
            new Observable.OnSubscribe<EthSyncingResponse>() {
                @Override
                public void call(final Subscriber<? super EthSyncingResponse> sub) {

                    Callback<EthSyncingResponse> ethSyncingCallback = new Callback<EthSyncingResponse>() {
                        @Override
                        public void onResult(EthSyncingResponse ethSyncingResult) {
                            Log.d("onResult(): " + ethSyncingResult);
                            sub.onNext(ethSyncingResult);
//                            sub.onCompleted();
                        }

                        @Override
                        public void onError(JSONRPC2Error error) {
                            Log.d("onError(): " + error);
                        }
                    };
                    while (true) {
                        gethService.ethSyncing(ethSyncingCallback);
                        SystemClock.sleep(2000);
                    }
                }
            }
    );

    Observable<NetPeerCountResponse> netPeerCountObservable = Observable.create(
            new Observable.OnSubscribe<NetPeerCountResponse>() {
                @Override
                public void call(final Subscriber<? super NetPeerCountResponse> sub) {

                    Callback<NetPeerCountResponse> netPeerCountcallback = new Callback<NetPeerCountResponse>() {
                        @Override
                        public void onResult(NetPeerCountResponse netPeerCount) {
                            Log.d("onResult(): " + netPeerCount);
                            sub.onNext(netPeerCount);
                        }

                        @Override
                        public void onError(JSONRPC2Error error) {
                            Log.d("onError(): " + error);
                        }
                    };

                    while (true) {
                        gethService.netPeerCount(netPeerCountcallback);
                        SystemClock.sleep(2000);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getChainDataDir();
        setContentView(R.layout.activity_main);

        currentBlock = (TextView) findViewById(R.id.current_block);
        netPeerCount = (TextView) findViewById(R.id.net_peer_count);
        helloWorld = (TextView) findViewById(R.id.hello_world);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick()");


//                gethService.ethAccounts(new EthereumJsonRpc.Callback<EthAccountsResult>() {
//                    @Override
//                    public void onResult(EthAccountsResult ethAccountsResult) {
//                        Log.d("onResult(): " + ethAccountsResult);
//                    }
//
//                    @Override
//                    public void onError(JSONRPC2Error error) {
//                        Log.d("onError(): " + error);
//
//                    }
//                });

                //TODO: create account
                //Geth.doAccountNew(getChainDataDir(), "password");
                //Geth.run("--datadir=" + getChainDataDir() + " account list");
                //Geth.run("account list");

                Snackbar.make(view, "Action...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @NonNull
    private String getChainDataDir() {
        Log.d("absolutePath: " + getFilesDir().getAbsolutePath());
        return getFilesDir().getAbsolutePath();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_create_account) {
            Geth.doAccountNew(getChainDataDir(), "password");
            Snackbar.make(null, "Create Account...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume()");
        Intent intent = new Intent(this, GethService.class);
        bindService(intent, mConnection,
                    Context.BIND_AUTO_CREATE
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause()");

        unbindService(mConnection);
    }
}
