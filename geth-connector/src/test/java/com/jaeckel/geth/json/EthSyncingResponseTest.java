package com.jaeckel.geth.json;

import com.jaeckel.geth.MoshiFactory;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import okio.BufferedSource;
import okio.Okio;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EthSyncingResponseTest {

    Moshi moshi;

    @Before
    public void setUp() {
        moshi = MoshiFactory.createMoshi();
    }

    @Test
    public void testEthSyncingResponse() throws IOException {
        BufferedSource json = createJsonBuffer("json/eth_syncing_response.json");
        JsonAdapter<EthSyncingResponse> jsonAdapter = moshi.adapter(EthSyncingResponse.class);

        EthSyncingResponse ethSyncingResponse = jsonAdapter.fromJson(json);

        assertNotNull(ethSyncingResponse.result);
        EthSyncingResult response = ethSyncingResponse.result;
        assertThat(response.currentBlock, is(898549L));
        assertThat(response.highestBlock, is(1005156L));
        assertThat(response.knownStates, is(10L));
        assertThat(response.pulledStates, is(11L));
        assertThat(response.startingBlock, is(549562L));
    }

    @Test
    public void testEthSyncingResponseFalse() throws IOException {
        BufferedSource json = createJsonBuffer("json/eth_syncing_response_false.json");
        JsonAdapter<EthSyncingResponse> jsonAdapter = moshi.adapter(EthSyncingResponse.class);

        EthSyncingResponse ethSyncingResponse = jsonAdapter.fromJson(json);

        assertNull(ethSyncingResponse.result);

    }

    private BufferedSource createJsonBuffer(String file) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        return Okio.buffer(Okio.source(is));
    }
}
