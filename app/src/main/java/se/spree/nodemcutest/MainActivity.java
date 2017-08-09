package se.spree.nodemcutest;

import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private  Server server;

    Button btOn, btOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = new Server();
        server.start();

        btOn = (Button) findViewById(R.id.btOn);
        btOff = (Button) findViewById(R.id.btOff);

        btOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server.sendMessage("LED_ON");
            }
        });

        btOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server.sendMessage("LED_OFF");
            }
        });

    }
}
