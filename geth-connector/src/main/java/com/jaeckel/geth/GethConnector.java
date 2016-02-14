package com.jaeckel.geth;

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

  private static final String METHOD_NET_PEER_COUNT = "net_peerCount";
  private static final String METHOD_ETH_SYNCING = "eth_syncing";
  private static final String JSONRPC_ENDPOINT = "http://localhost:8545/";
  private static final String METHOD_ETH_ACCOUNTS = "eth_accounts";
  private static Integer requestId = 1;

  public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private static OkHttpClient httpClient = new OkHttpClient();

  public static String netPeerCount() throws IOException {

    Response response = httpClient.newCall(new Request.Builder().url(JSONRPC_ENDPOINT)
        .post(RequestBody.create(JSON, createRequest(METHOD_NET_PEER_COUNT)))
        .build())
        .execute();

    String result = response.body()
        .string();
    System.out.println("Response: " + result);

    return result;
  }

  public static String ethSyncing() throws IOException {

    Response response = httpClient.newCall(new Request.Builder().url(JSONRPC_ENDPOINT)
        .post(RequestBody.create(JSON, createRequest(METHOD_ETH_SYNCING)))
        .build())
        .execute();

    String result = response.body()
        .string();
    System.out.println("Response: " + result);

    return result;
  }

  public static String ethAccounts() throws IOException {

    Response response = httpClient.newCall(new Request.Builder().url(JSONRPC_ENDPOINT)
        .post(RequestBody.create(JSON, createRequest(METHOD_ETH_ACCOUNTS)))
        .build())
        .execute();

    String result = response.body()
        .string();
    System.out.println("Response: " + result);

    return result;
  }

  public static String sendTransaction(String from, String to, long wei) throws IOException {

    Response response = httpClient.newCall(new Request.Builder().url(JSONRPC_ENDPOINT)
        .post(RequestBody.create(JSON, createTxRequest(from, to, wei)))
        .build())
        .execute();

    String result = response.body()
        .string();
    System.out.println("Response: " + result);

    return result;

  }

  public static String createTxRequest(String from, String to, long wei) {
    Map<String, Object> params = new HashMap<>();

    Integer id = requestId++;
    JSONRPC2Request reqOut = new JSONRPC2Request(METHOD_ETH_ACCOUNTS, params, id);
    System.out.println("Request: " + reqOut.toString());
    return reqOut.toString();
  }

  public static String createRequest(String method) {
    Map<String, Object> params = new HashMap<>();
    Integer id = requestId++;
    JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
    System.out.println("Request: " + reqOut.toString());
    return reqOut.toString();
  }
}
