package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
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

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private RecyclerView recycler;
    private ArrayList<SoccerMatch> list = new ArrayList<>();

    TextView steps;
    SensorManager sensorManager;
    Sensor stepCounter;
    boolean running = false;

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

        steps = findViewById(R.id.stepInfo);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

//        if(ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
//            //ask for permission
//            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PHYISCAL_ACTIVITY);
//        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        running = true;
//        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCounter!=null){
            sensorManager.registerListener(this,stepCounter,SensorManager.SENSOR_DELAY_FASTEST);
            Toast.makeText(this, "Sensor found!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        running = false;
//        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(this,stepCounter);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("STATUS BOI", String.valueOf(running));
        if(running){
            Log.d("SENSORRRS", String.valueOf(event.values[0]));
            steps.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
