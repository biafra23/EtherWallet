package com.jaeckel.geth;

public class Main {

  public static void main(String[] args) throws Exception {
    System.out.println("main()");

    GethConnector.ethSyncing();
    GethConnector.netPeerCount();
  }
}
