package com.jaeckel.etherwallet;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import org.ethereum.geth.Context;
import org.ethereum.geth.EthereumClient;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Header;
import org.ethereum.geth.NewHeadHandler;
import org.ethereum.geth.Node;
import org.ethereum.geth.NodeConfig;
import org.ethereum.geth.NodeInfo;


public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setTitle("Android In-Process Node");
        final TextView textbox = (TextView) findViewById(R.id.textbox);

        Context ctx = new Context();

        try {
            Node node = Geth.newNode(getFilesDir() + "/.ethereum", new NodeConfig());
            node.start();

            NodeInfo info = node.getNodeInfo();
            textbox.append("My name: " + info.getName() + "\n");
            textbox.append("My address: " + info.getListenerAddress() + "\n");
            textbox.append("My protocols: " + info.getProtocols() + "\n\n");

            EthereumClient ec = node.getEthereumClient();
            textbox.append("Latest block: " + ec.getBlockByNumber(ctx, -1).getNumber() + ", syncing...\n");

            NewHeadHandler handler = new NewHeadHandler() {
                @Override
                public void onError(String error) {
                }

                @Override
                public void onNewHead(final Header header) {
                    Main2Activity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            textbox.append("#" + header.getNumber() + ": " + header.getHash().getHex().substring(0, 10) + "…\n");
                        }
                    });
                }
            };
            ec.subscribeNewHead(ctx, handler, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

