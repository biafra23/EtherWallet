package com.jaeckel.geth.json;

import com.squareup.moshi.Json;

import java.util.List;

public class PersonalListAccountsResponse {

    @Json(name = "result")
    public List<String> accounts;
    public PersonalListAccountsError error;

    @Override
    public String toString() {
        return "PersonalListAccountsResponse{" +
                "result=" + accounts +
                ", error=" + error +
                '}';
    }
}
