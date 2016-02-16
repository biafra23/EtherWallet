package com.jaeckel.geth.json;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class HexAdapter {
    @ToJson
    String toJson(@Hex long number) {
        return String.format("0x%x", number);
    }

    @Hex
    @FromJson
    long fromJson(String hexString) {
        return Long.parseLong(hexString.substring(2), 16);
    }
}
