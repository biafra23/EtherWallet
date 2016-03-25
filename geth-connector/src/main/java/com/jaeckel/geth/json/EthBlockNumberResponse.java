package com.jaeckel.geth.json;

public class EthBlockNumberResponse {

    @Hex
    public long result;

    @Override
    public String toString() {
        return "EthBlockNumberResponse{" +
                "result=" + result +
                '}';
    }
}
