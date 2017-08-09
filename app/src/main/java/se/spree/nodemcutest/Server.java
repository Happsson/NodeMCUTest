package se.spree.nodemcutest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

/**
 * Created by hannes on 2017-08-08.
 */

public class Server extends Thread {

    boolean running = false;
    ServerSocket serverSocket;
    Socket client;
    PrintWriter bufferSender;

    String msg = null;

    public void sendMessage(String msg){
        this.msg = msg;
    }


    @Override
    public void run() {
        super.run();
        runServer();
    }

    private void runServer() {
        running = true;

        try {
            Log.d("gah", "Establishing server");

            //create a server socket. A server socket waits for requests to come in over the network.
            serverSocket = new ServerSocket(8988);
            serverSocket.setReuseAddress(true);

            Log.d("gah", "Server established, waiting for client. Server ip: " + getIPAddress(true) +", socketadress: " + serverSocket.getLocalSocketAddress().toString());

            //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
            client = serverSocket.accept();
            Log.d("gah", "Client connected");

            try {

                //sends the message to the client
                bufferSender = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

                //read the message received from client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                //in this while we wait to receive messages from client (it's an infinite loop)
                //this while it's like a listener for messages
                while (running) {

                    if(msg != null){
                        if(bufferSender != null && !bufferSender.checkError()) {
                            Log.d("gah", "Sending: " + msg);
                            bufferSender.println(msg + '\r');
                            bufferSender.flush();
                            msg = null;
                        }else{
                            Log.d("gah", "buffersender == null or buffererror.checkerror gave fault");
                        }
                    }
                }

            } catch (Exception e) {
                Log.d("gah", "error");
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.d("gah", "error2");
            e.printStackTrace();
        }
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
}
