package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView recycler;
    private ArrayList<SoccerMatch> list = new ArrayList<>();

    private Intent intent;
    TextView stepCountTV;
    String countedStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        list.addAll(MockData.getListData());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        CardViewAdapter cardViewAdapter = new CardViewAdapter(this);
        cardViewAdapter.setListMatch(list);
        recycler.setAdapter(cardViewAdapter);

        stepCountTV = findViewById(R.id.stepInfo);
        intent = new Intent(this, StepCounterService.class);
        Log.d("SENSORAVAILABLE", String.valueOf(isServiceRunning(StepCounterService.class)));
        if(!isServiceRunning(StepCounterService.class)){
            startForegroundService(intent);
        }
        registerReceiver(broadcastReceiver, new IntentFilter("com.example.bolasepak.mybroadcast"));

    }

    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadcastReceiver", "Received");
            // call updateUI passing in our intent which is holding the data to display.
            updateViews(intent);
        }
    };

    private void updateViews(Intent intent) {
        // retrieve data out of the intent.
        countedStep = intent.getStringExtra("Counted_Step");
        Log.d("COUNTEDSTEPPPPP", String.valueOf(countedStep));
        stepCountTV.setText(String.valueOf(countedStep));
    }

}
