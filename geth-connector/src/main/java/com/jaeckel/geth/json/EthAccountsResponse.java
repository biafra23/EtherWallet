package com.jaeckel.geth.json;

import java.util.List;

public class EthAccountsResponse {

    List<String> result;

    @Override
    public String toString() {
        return "EthAccountsResponse{" +
                "id='" + id + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", result=" + result +
                '}';
    }
}
