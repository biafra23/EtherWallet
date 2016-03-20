package com.jaeckel.geth.json;

import com.squareup.moshi.Json;

import java.util.List;

public class PersonalListAccountsResponse {

    @Json(name = "result")
    private List<String> accounts;
    private PersonalListAccountsError error;

    @Override
    public String toString() {
        return "PersonalListAccountsResponse{" +
                "result=" + accounts +
                ", error=" + error +
                '}';
    }
}
