package com.jaeckel.geth.json;

public class GetBalanceParams {
    public String address;
    public String blockParam;

    @Override
    public String toString() {
        return "GetBalanceParams{" +
                "address='" + address + '\'' +
                ", blockParam='" + blockParam + '\'' +
                '}';
    }

    private GetBalanceParams(String address, String blockParam) {
        this.address = address;
        this.blockParam = blockParam;
    }

    public static GetBalanceParams create(String address, String blockParam) {
        return new GetBalanceParams(address, blockParam);
    }
}
