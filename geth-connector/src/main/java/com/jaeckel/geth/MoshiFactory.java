package com.jaeckel.geth;

import com.jaeckel.geth.json.EthSyncingResultAdapter;
import com.jaeckel.geth.json.FalseToNullFactory;
import com.jaeckel.geth.json.HexAdapter;
import com.squareup.moshi.Moshi;

public class MoshiFactory {

    public static Moshi createMoshi() {
        return new Moshi.Builder()
                .add(new HexAdapter())
                .add(new FalseToNullFactory())
                .add(new EthSyncingResultAdapter())
//            .add(new AccountListAdapter())
                .build();
    }
}
