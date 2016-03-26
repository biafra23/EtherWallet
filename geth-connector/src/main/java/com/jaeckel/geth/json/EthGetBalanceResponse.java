package com.jaeckel.geth.json;

public class EthGetBalanceResponse {
    @Hex
    public long result;

    @Override
    public String toString() {
        return "EthGetBalanceResponse{" +
                "result=" + result +
                '}';
    }
}
