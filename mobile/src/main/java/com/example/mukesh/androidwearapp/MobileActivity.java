package com.example.mukesh.androidwearapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class MobileActivity extends AppCompatActivity {
    private NotificationManager notificationManager;
    private EditText msg;
    private String nodeId;
    private TextView errText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        createNotificationChannel();

        msg = findViewById(R.id.messageEditText);
        errText = findViewById(R.id.textViewErr);


        Button createNotificationBtn = findViewById(R.id.notificationBtn);
        createNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errText.setVisibility(View.GONE);
                String msgText = msg.getText().toString().trim();
                if(msgText.isEmpty()){
                    //Toast.makeText(getApplicationContext(),"Notification name cannot be empty",Toast.LENGTH_LONG);
                    errText.setText("Message cannot be empty");
                    errText.setVisibility(View.VISIBLE);
                }
//                else{
//                    //creating pending intent
//                    Intent intent = new Intent(getApplicationContext(),SampleActivity.class);
//                    PendingIntent viewPendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
//
//                    //creating notification
//                    final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),getString(R.string.channel_id))
//                            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
//                            .setContentTitle(name)
//                            .setContentText("Click here for "+name)
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                            .setContentIntent(viewPendingIntent);
//
//                    //notifying
//                    notificationBuilder.addAction(R.drawable.common_google_signin_btn_icon_dark_normal,"Click here",viewPendingIntent);
//                    notificationManager.notify(001,notificationBuilder.build());
//
//                }
                else{
                    Wearable.getMessageClient(getApplicationContext()).sendMessage(nodeId,"/messageToWearable",msgText.getBytes());
                }
            }
        });

        Task<CapabilityInfo> capabilityInfoTask = Wearable.getCapabilityClient(this)
                                                    .getCapability("wear_app_capability", CapabilityClient.FILTER_REACHABLE);
        capabilityInfoTask.addOnCompleteListener(new OnCompleteListener<CapabilityInfo>() {
            @Override
            public void onComplete(Task<CapabilityInfo> task) {
                if(task.isSuccessful()){
                    CapabilityInfo capabilityInfo = task.getResult();
                    Set<Node> nodes = capabilityInfo.getNodes();
                    if(nodes.isEmpty()){
                        System.out.println("no nodes found");
                    }
                    else{
                        System.out.println("nodes found");
                        nodeId = ((Node)nodes.toArray()[0]).getId();
                    }
                }
                else{
                    System.out.println("phone task unsucessful");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mobile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel (){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            String channel_id = getString(R.string.channel_id);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id,name,importance);
            channel.setDescription(description);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
