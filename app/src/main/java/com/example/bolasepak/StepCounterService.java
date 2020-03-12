package com.example.bolasepak;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;


public class StepCounterService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    Sensor stepCounterSensor;

    int currentStep;
    int stepCounter;
    int newStepCounter;
    boolean serviceStopped;
    Intent intent;
    NotificationManager notificationManager;
    private final Handler handler = new Handler();
    int counter;

    @Override
    public void onCreate(){
        super.onCreate();
        intent = new Intent("com.example.bolasepak.mybroadcast");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        createChannel();
        showNotification();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepCounterSensor, 0);

        //currentStepCount = 0;
        currentStep = 0;
        stepCounter = 0;
        newStepCounter = 0;

        serviceStopped = false;
        handler.removeCallbacks(updateBroadcastData);
        handler.post(updateBroadcastData);

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int countSteps = (int) event.values[0];
        if (stepCounter == 0) { // If the stepCounter is in its initial value, then...
            stepCounter = (int) event.values[0]; // Assign the StepCounter Sensor event value to it.
        }
        newStepCounter = countSteps - stepCounter;
        Log.d("TESTSENSOR", String.valueOf(newStepCounter));
        broadcastSensorValue();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastSensorValue();
        dismissNotification();
    }

    private void createChannel(){
        //create channel
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("BOLASEPAK", "Bola Sepak", NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void showNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"BOLASEPAK");
        notificationBuilder.setContentTitle("Bola Sepak");
        notificationBuilder.setContentText("Step Counter is running in the background.");
        notificationBuilder.setSmallIcon(R.drawable.notif);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);

        startForeground(1, notificationBuilder.build());
    }

    private void dismissNotification() {
        notificationManager.cancel(1);
    }

    private Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (serviceStopped == false) { // Only allow the repeating timer while service is running (once service is stopped the flag state will change and the code inside the conditional statement here will not execute).
                // Call the method that broadcasts the data to the Activity..
                broadcastSensorValue();
                // Call "handler.postDelayed" again, after a specified delay.
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void broadcastSensorValue() {
        // add step counter to intent.
        intent.putExtra("Counted_Step_Int", newStepCounter);
        intent.putExtra("Counted_Step", String.valueOf(newStepCounter));
        sendBroadcast(intent);
    }
}
