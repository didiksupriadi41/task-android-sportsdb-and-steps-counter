package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView recycler;
    private ArrayList<SoccerMatch> list = new ArrayList<>();
    private SQLiteDatabase mydatabase;

    private Intent intent;
    TextView stepCountTV;
    String countedStep;
    CardViewAdapter cardViewAdapter;

    public static final int LEAGUE_ID = 4328;
    public static final String LEAGUE_NEXT_EVENT_API="https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php";
    public static final String LEAGUE_PAST_EVENT_API="https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        cardViewAdapter = new CardViewAdapter(this);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        String url = String.format("%s?id=%d", LEAGUE_PAST_EVENT_API, LEAGUE_ID);
        jsonPastEventRequest(url);

        stepCountTV = findViewById(R.id.stepInfo);
        intent = new Intent(this, StepCounterService.class);
        Log.d("SENSORAVAILABLE", String.valueOf(isServiceRunning(StepCounterService.class)));
        if(!isServiceRunning(StepCounterService.class)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }
        }
        registerReceiver(broadcastReceiver, new IntentFilter("com.example.bolasepak.mybroadcast"));
        mydatabase = openOrCreateDatabase("Team",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS SubscribedTeam(Team_ID VARCHAR);");
        mydatabase.execSQL("INSERT INTO SubscribedTeam VALUES(133602);");

        showAllSubcription();
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



    public void jsonPastEventRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray pastEvents = response.getJSONArray("events");
                            for (int i = 0; i < pastEvents.length(); i++) {
                                SoccerMatch match = new SoccerMatch();
                                match.setName1( (String) ( (JSONObject) pastEvents.get(i) ).get("strHomeTeam"));
                                match.setName2( (String) ( (JSONObject) pastEvents.get(i) ).get("strAwayTeam"));
                                match.setScore1( (String) ( (JSONObject) pastEvents.get(i) ).get("intHomeScore"));
                                match.setScore2( (String) ( (JSONObject) pastEvents.get(i) ).get("intAwayScore"));
                                jsonLogo1(match, (String) ( (JSONObject) pastEvents.get(i) ).get("idHomeTeam"));
                                jsonLogo2(match, (String) ( (JSONObject) pastEvents.get(i) ).get("idAwayTeam"));
                                list.add(match);
                            }
                            cardViewAdapter.setListMatch(list);
                            recycler.setAdapter(cardViewAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
                );
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
    public void jsonLogo1(final SoccerMatch match, String id) {
        String url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + id;
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray pastEvents = response.getJSONArray("teams");
                            for (int i = 0; i < pastEvents.length(); i++) {
                                match.setImg1((String) ( (JSONObject) pastEvents.get(i) ).get("strTeamBadge"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
                );
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
    public void jsonLogo2(final SoccerMatch match, String id) {
        String url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + id;
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray pastEvents = response.getJSONArray("teams");
                            for (int i = 0; i < pastEvents.length(); i++) {
                                match.setImg2((String) ( (JSONObject) pastEvents.get(i) ).get("strTeamBadge"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
                );
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void fetchData(String url){
        final ArrayList<String> list = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("JSON", "contoh");
                            JSONArray pastEvents = response.getJSONArray("events");
                            for (int i = 0; i < pastEvents.length(); i++) {
                                String event = ( (String) ( (JSONObject) pastEvents.get(i) ).get("strEvent"));
                                list.add(event);
                            }
                            String s = "New upcoming match! ";
                            for(String event : list){
                                s = s.concat(event);
                                s = s.concat("! ");
                            }
                            System.out.println(s);
                            showNotification(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
                );
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void showAllSubcription(){

        Cursor resultSet = mydatabase.rawQuery("SELECT * FROM SubscribedTeam ", null);
        ArrayList<String> list = new ArrayList<>();

        if (resultSet.getCount() > 0) {
            resultSet.moveToFirst();
            int counter = 1;
            while (!resultSet.isAfterLast()) {
                String idTeam = resultSet.getString(0);
                list.add(idTeam);
                resultSet.moveToNext();
                counter++;
            }
        }

        for(String idTeam : list){
            String url = String.format("%s?id=%s", "https://www.thesportsdb.com/api/v1/json/1/eventsnext.php", idTeam);
            fetchData(url);
        }

    }

    public void createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Subscription", "Subcription Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
    //createnotif
    public void showNotification(String content) {

        createChannel();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"Subscription");
        notificationBuilder.setContentTitle("UPCOMING MATCH");
        notificationBuilder.setContentText(content);
        notificationBuilder.setSmallIcon(R.drawable.notif);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(3, notificationBuilder.build());

    }
}
