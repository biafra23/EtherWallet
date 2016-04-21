package com.jaeckel.etherwallet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jaeckel.geth.json.EthSyncingResult;

import java.math.BigInteger;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private GethService gethService;
    private TextView currentBlock;
    private TextView highestBlock;
    private TextView netPeerCount;
    private TextView ethBalance;
    private TextView ethSyncing;
    private TextView ethAccount;

    private Subscription netPeerCountSubscription;
    private Subscription ethBlockNumberSubscription;
    private Subscription ethSyncingSubscription;

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            Timber.d("onServiceConnected()");
            GethService.MyBinder b = (GethService.MyBinder) binder;
            gethService = b.getService();

            ethSyncingSubscription = gethService.ethSyncing().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<EthSyncingResult>() {
                        @Override
                        public void onCompleted() {
                            Timber.d("onCompleted()");
                            ethSyncing.setText("Syncing: DONE");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "onError(): ");
                            highestBlock.setText("ERROR: " + e.getMessage());
                        }

                        @Override
                        public void onNext(EthSyncingResult result) {
                            Timber.d("OnNext(): " + result);

                            if (result != null) {
                                highestBlock.setText("Highest Block: " + result.highestBlock);
                                ethSyncing.setText("Syncing: true");
                            } else {
                                ethSyncing.setText("Syncing: false");
                            }
                        }
                    });

            ethBlockNumberSubscription = gethService.ethBlockNumber().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "Exception");
                        }

                        @Override
                        public void onNext(Long ethBlockNumberResponse) {
                            currentBlock.setText("Current Block: " + ethBlockNumberResponse);
                        }
                    });

            netPeerCountSubscription = gethService.netPeerCount().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onCompleted() {
                            Timber.d("onCompleted()");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "onError(): ");
                            netPeerCount.setText("ERROR: " + e.getMessage());
                        }

                        public void onNext(Long netPeerCountResponse) {
                            Timber.d("----> onNext(): ");
                            netPeerCount.setText("NetPeerCount: " + netPeerCountResponse);
                        }
                    });

        }

        public void onServiceDisconnected(ComponentName className) {
            gethService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getChainDataDir();
        setContentView(R.layout.activity_main);

        currentBlock = (TextView) findViewById(R.id.eth_current_block);
        highestBlock = (TextView) findViewById(R.id.highest_block);
        netPeerCount = (TextView) findViewById(R.id.net_peer_count);
        ethSyncing = (TextView) findViewById(R.id.eth_syncing);
        ethBalance = (TextView) findViewById(R.id.balances);
        ethAccount = (TextView) findViewById(R.id.account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timber.d("onClick()");
                    gethService.ethAccounts().subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<List<String>>() {
                                @Override
                                public void onCompleted() {
                                    Timber.d("onCompleted()");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Timber.e(e, "onError(): ");
                                    ethBalance.setText("ERROR: " + e.getMessage());
                                }

                                @Override
                                public void onNext(List<String> ethAccounts) {
                                    Timber.d("onNext(): ethAccounts: " + ethAccounts);
                                    ethBalance.setText("Balances: " + ethAccounts);
                                }
                            });
                }
            });
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @NonNull
    private String getChainDataDir() {
        Timber.d("absolutePath: " + getFilesDir().getAbsolutePath());
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
        if (id == R.id.action_list_accounts) {
            calculateBalanceForAllAccounts();
            return true;
        }

        if (id == R.id.action_new_account) {
            createAccount();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAccount() {
        gethService.personalNewAccount("")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError(): ");
                        ethBalance.setText("ERROR: " + e.getMessage());
                    }

                    @Override
                    public void onNext(String response) {
                        Timber.d("onNext(): response: " + response);
                        ethBalance.setText("Balances: " + response);
                    }
                });
    }

    private void calculateBalanceForAllAccounts() {
        gethService.personalListAccounts()
                .subscribeOn(Schedulers.io())
                .flatMap(splitList())
                .map(getBalances())
                .concatMap(mergeObservables())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BigInteger>() {
                    BigInteger sum = BigInteger.ZERO;

                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                        ethBalance.setText("Balance: " + EtherFormatter.formatWeiAsEther(sum));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError(): ");
                    }

                    @Override
                    public void onNext(BigInteger bigInteger) {
                        Timber.d("onNext(): response: " + bigInteger);
                        sum = sum.add(bigInteger);
                        ethBalance.setText("Balance: " + EtherFormatter.formatWeiAsEther(sum));
                    }
                });
    }

    @NonNull
    private Func1<Observable<BigInteger>, Observable<BigInteger>> mergeObservables() {
        return new Func1<Observable<BigInteger>, Observable<BigInteger>>() {
            @Override
            public Observable<BigInteger> call(Observable<BigInteger> bigIntegerObservable) {
                return bigIntegerObservable;
            }
        };
    }

    @NonNull
    private Func1<String, Observable<BigInteger>> getBalances() {
        return new Func1<String, Observable<BigInteger>>() {
            @Override
            public Observable<BigInteger> call(String account) {
                return gethService.ethGetBalance(account, "latest");
            }
        };
    }

    @NonNull
    private Func1<List<String>, Observable<String>> splitList() {
        return new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        };
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
        Timber.d("onResume()");
        Intent intent = new Intent(this, GethService.class);
        bindService(intent, mConnection,
                    Context.BIND_AUTO_CREATE
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("onPause()");

        Timber.d("netPeerCountSubscription.unsubscribe();");
        netPeerCountSubscription.unsubscribe();

        Timber.d("ethSyncingSubscription.unsubscribe();");
        ethSyncingSubscription.unsubscribe();
        ethBlockNumberSubscription.unsubscribe();

        unbindService(mConnection);
    }
}
