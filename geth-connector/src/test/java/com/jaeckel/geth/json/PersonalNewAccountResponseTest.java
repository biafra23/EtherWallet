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

public class PersonalNewAccountResponseTest {

    Moshi moshi;

    @Before
    public void setUp() {
        moshi = MoshiFactory.createMoshi();
    }

    @Test
    public void testPersonalNewAccountResponse() throws IOException {
        BufferedSource json = createJsonBuffer("json/personal_new_account_response.json");
        JsonAdapter<PersonalNewAccountResponse> jsonAdapter = moshi.adapter(PersonalNewAccountResponse.class);

        PersonalNewAccountResponse ethSyncingResponse = jsonAdapter.fromJson(json);

        assertNotNull(ethSyncingResponse.result);
        assertThat(ethSyncingResponse.result, is("0x36605dbfe30b25acd05a21884d19483be54de6fd"));

    }

    private BufferedSource createJsonBuffer(String file) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(file);
        return Okio.buffer(Okio.source(is));
    }
}
