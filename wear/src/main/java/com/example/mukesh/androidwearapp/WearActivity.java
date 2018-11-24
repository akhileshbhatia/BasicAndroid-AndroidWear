package com.example.mukesh.androidwearapp;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.nio.charset.StandardCharsets;

public class WearActivity extends WearableActivity  {

    private TextView textViewMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        textViewMsg = findViewById(R.id.textViewMsg);
        // Enables Always-on
        setAmbientEnabled();


        Wearable.getMessageClient(this).addListener(new MessageClient.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived( MessageEvent messageEvent) {
                if(messageEvent.getPath().contentEquals("/messageToWearable")){
                    System.out.println("Message received");
                    textViewMsg.setText(new String(messageEvent.getData(), StandardCharsets.UTF_8));
                }
            }
        });

    }



}
