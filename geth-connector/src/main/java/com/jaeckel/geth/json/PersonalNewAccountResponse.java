package com.jaeckel.geth.json;

public class PersonalNewAccountResponse {
// {"jsonrpc":"2.0","id":5,"result":"0x36605dbfe30b25acd05a21884d19483be54de6fd"}

    public String result;

    @Override
    public String toString() {
        return "PersonalNewAccountResponse{" +
                "result='" + result + '\'' +
                '}';
    }
}
