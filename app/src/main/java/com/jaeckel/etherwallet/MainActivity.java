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
import java.util.Locale;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static java.lang.String.format;

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
                            ethSyncing.setText(R.string.msg_syncing_done);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "onError(): ");
                            highestBlock.setText(format("ERROR: %s", e.getMessage()));
                        }

                        @Override
                        public void onNext(EthSyncingResult result) {
                            Timber.d(format("OnNext(): %s", result));

                            if (result != null) {
                                highestBlock.setText(format(Locale.ENGLISH, "Highest Block: %d", result.highestBlock));
                                ethSyncing.setText(R.string.msg_syncing_true);
                            } else {
                                ethSyncing.setText(R.string.msg_syncing_false);
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
                        public void onNext(Long result) {
                            currentBlock.setText(format(Locale.ENGLISH, "Current Block: %d", result));
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
                            netPeerCount.setText(format(Locale.ENGLISH, "ERROR: %s", e.getMessage()));
                        }

                        @Override
                        public void onNext(Long result) {
                            Timber.d("onNext(): ");
                            netPeerCount.setText(format(Locale.ENGLISH, "NetPeerCount: %d", result));
                        }
                    });

        }

        public void onServiceDisconnected(ComponentName className) {
            gethService = null;
        }
    };

//    Observable<EthSyncingResponse> ethSyncingObservable = Observable.create(
//            new Observable.OnSubscribe<EthSyncingResponse>() {
//                @Override
//                public void call(final Subscriber<? super EthSyncingResponse> sub) {
//
//                    Callback<EthSyncingResponse> ethSyncingCallback = new Callback<EthSyncingResponse>() {
//                        @Override
//                        public void onResult(EthSyncingResponse ethSyncingResult) {
//                            Timber.d("onResult(): %s", ethSyncingResult);
//                            sub.onNext(ethSyncingResult);
////                            sub.onCompleted();
//                        }
//
//                        @Override
//                        public void onError(JSONRPC2Error error) {
//                            Timber.d("onError(): %s", error.getMessage());
//                        }
//                    };
//                    while (!sub.isUnsubscribed()) {
//                        gethService.ethSyncing(ethSyncingCallback);
//                        SystemClock.sleep(10000);
//                    }
//                }
//            }
//    );

//    Observable<EthBlockNumberResponse> ethBlockNumberObservable = Observable.create(
//            new Observable.OnSubscribe<EthBlockNumberResponse>() {
//                @Override
//                public void call(final Subscriber<? super EthBlockNumberResponse> sub) {
//
//                    Callback<EthBlockNumberResponse> ethBlockNumberCallback = new Callback<EthBlockNumberResponse>() {
//                        @Override
//                        public void onResult(EthBlockNumberResponse ethSyncingResult) {
//                            Timber.d("onResult(): %s", ethSyncingResult);
//                            sub.onNext(ethSyncingResult);
////                            sub.onCompleted();
//                        }
//
//                        @Override
//                        public void onError(JSONRPC2Error error) {
//                            Timber.d("onError(): %s", error.getMessage());
//                        }
//                    };
//                    while (!sub.isUnsubscribed()) {
//                        gethService.ethBlockNumber(ethBlockNumberCallback);
//                        SystemClock.sleep(2000);
//                    }
//                }
//            }
//    );

//    Observable<NetPeerCountResponse> netPeerCountObservable = Observable.create(
//            new Observable.OnSubscribe<NetPeerCountResponse>() {
//                @Override
//                public void call(final Subscriber<? super NetPeerCountResponse> sub) {
//
//                    Callback<NetPeerCountResponse> netPeerCountcallback = new Callback<NetPeerCountResponse>() {
//                        @Override
//                        public void onResult(NetPeerCountResponse netPeerCount) {
//                            Timber.d("onResult(): %s", netPeerCount);
//                            sub.onNext(netPeerCount);
//                        }
//
//                        @Override
//                        public void onError(JSONRPC2Error error) {
//                            Timber.d("onError(): %s", error.getMessage());
//                        }
//                    };
//
//                    while (!sub.isUnsubscribed()) {
//                        gethService.netPeerCount(netPeerCountcallback);
//                        SystemClock.sleep(2000);
//                    }
//                }
//            }
//    );

//    Observable<EthAccountsResponse> ethAccountsObservable = Observable.create(
//            new Observable.OnSubscribe<EthAccountsResponse>() {
//                @Override
//                public void call(final Subscriber<? super EthAccountsResponse> sub) {
//
//                    Callback<EthAccountsResponse> ethAccountsCallback = new Callback<EthAccountsResponse>() {
//                        @Override
//                        public void onResult(EthAccountsResponse ethAccountsResponse) {
//                            Timber.d("onResult(): %s", ethAccountsResponse);
//                            sub.onNext(ethAccountsResponse);
//                            sub.onCompleted();
//                        }
//
//                        @Override
//                        public void onError(JSONRPC2Error error) {
//                            Timber.d("onError(): %s", error.getMessage());
//                        }
//                    };
//
//                    gethService.ethAccounts(ethAccountsCallback);
//                }
//            }
//    );

//    Observable<PersonalListAccountsResponse> personalListAccountsObservable = Observable.create(
//            new Observable.OnSubscribe<PersonalListAccountsResponse>() {
//                @Override
//                public void call(final Subscriber<? super PersonalListAccountsResponse> sub) {
//
//                    Callback<PersonalListAccountsResponse> ethAccountsCallback = new Callback<PersonalListAccountsResponse>() {
//                        @Override
//                        public void onResult(PersonalListAccountsResponse ethAccountsResponse) {
//                            Timber.d("onResult(): %s", ethAccountsResponse);
//                            sub.onNext(ethAccountsResponse);
//                            sub.onCompleted();
//
////                            ethGetBalanceRespnseObservable.subscribeOn(Schedulers.io())
////                                    .observeOn(AndroidSchedulers.mainThread())
////                                    .subscribe();
//                        }
//
//                        @Override
//                        public void onError(JSONRPC2Error error) {
//                            Timber.d("onError(): %s", error.getMessage());
//                        }
//                    };
//
//                    gethService.personalListAccounts(ethAccountsCallback);
//
//                }
//            }
//    );

//    Observable<EthGetBalanceResponse> ethGetBalanceRespnseObservable = Observable.create(
//            new Observable.OnSubscribe<EthGetBalanceResponse>() {
//                @Override
//                public void call(final Subscriber<? super EthGetBalanceResponse> sub) {
//
//                    Callback<EthGetBalanceResponse> ethAccountsCallback = new Callback<EthGetBalanceResponse>() {
//                        @Override
//                        public void onResult(EthGetBalanceResponse ethAccountsResponse) {
//                            Timber.d("onResult(): %s", ethAccountsResponse);
//                            sub.onNext(ethAccountsResponse);
//                            sub.onCompleted();
//                        }
//
//                        @Override
//                        public void onError(JSONRPC2Error error) {
//                            Timber.d(error, "onError()");
//                        }
//                    };
//
//                    gethService.ethGetBalance("fc175d4ebc50742899821ec95275f56d33dd5cd2", "latest", ethAccountsCallback);
//                }
//            }
//    );

//    Observable<PersonalNewAccountResponse> personalNewAccountObservable = Observable.create(
//            new Observable.OnSubscribe<PersonalNewAccountResponse>() {
//                @Override
//                public void call(final Subscriber<? super PersonalNewAccountResponse> sub) {
//
//                    Callback<PersonalNewAccountResponse> personalNewAccount = new Callback<PersonalNewAccountResponse>() {
//                        @Override
//                        public void onResult(PersonalNewAccountResponse ethAccountsResponse) {
//                            Timber.d("onResult(): %s", ethAccountsResponse);
//                            sub.onNext(ethAccountsResponse);
//                            sub.onCompleted();
//                        }
//
//                        @Override
//                        public void onError(JSONRPC2Error error) {
//                            Timber.d(error, "onError()");
//                        }
//                    };
//
//                    gethService.personalNewAccount("", personalNewAccount);
//                }
//            }
//    );

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
                                    ethBalance.setText(format("ERROR: %s", e.getMessage()));
                                }

                                @Override
                                public void onNext(List<String> result) {
                                    Timber.d("onNext(): ethAccountsResponse: %s", result);
                                    ethBalance.setText(format("Balances: %s", result));
                                }
                            });

                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

        }
    }

    @NonNull
    private String getChainDataDir() {
        Timber.d("absolutePath: %s", getFilesDir().getAbsolutePath());
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
                        ethBalance.setText(format("ERROR: " + e.getMessage()));
                    }

                    @Override
                    public void onNext(String response) {
                        Timber.d("onNext(): response: %s", response);
                        ethBalance.setText(format("Balances: %s", response));
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
                        ethBalance.setText(format("Balance: %s", EtherFormatter.formatWeiAsEther(sum)));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError(): ");
                        ethBalance.setText(format("ERROR: %s", e.getMessage()));
                    }

                    @Override
                    public void onNext(BigInteger bigInteger) {
                        Timber.d("onNext(): response: %s", bigInteger);
                        sum = sum.add(bigInteger);
                        ethBalance.setText(format("Balance: %s", EtherFormatter.formatWeiAsEther(sum)));
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
