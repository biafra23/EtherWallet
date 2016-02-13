package com.example;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GethConnector {

  private static Integer requestId = 1;

  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  public static  void simpleCall() throws IOException {
    OkHttpClient okHttpClient = new OkHttpClient();

    Response response = okHttpClient.newCall(new Request.Builder()
        .url("http://localhost:8545/")
        //.post(RequestBody.create(JSON, createEthSyncingRequest()))
        .post(RequestBody.create(JSON, createNetPeerCuntRequest()))
        .build()).execute();

    System.out.println("Response: " + response.body().string());
  }

  public static String createNetPeerCuntRequest() {
    // The remote method to call
    String method = "net_peerCount";

    // The required named parameters to pass
    Map<String, Object> params = new HashMap<>();

    // The mandatory request ID
    Integer id = requestId++;

    // Create a new JSON-RPC 2.0 request
    JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);

    // Serialise the request to a JSON-encoded string
    System.out.println("Request: " + reqOut.toString());
    return reqOut.toString();
  }

  public static String createEthSyncingRequest() {
    // The remote method to call
    String method = "eth_syncing";

    // The required named parameters to pass
    Map<String, Object> params = new HashMap<>();

    // The mandatory request ID
    Integer id = requestId++;

    // Create a new JSON-RPC 2.0 request
    JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);

    // Serialise the request to a JSON-encoded string
    System.out.println("Request: " + reqOut.toString());
    return reqOut.toString();
  }
}
