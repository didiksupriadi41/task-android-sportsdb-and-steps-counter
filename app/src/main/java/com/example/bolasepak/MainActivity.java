package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.res.Configuration;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ArrayList<SoccerMatch> tempList = new ArrayList<>();
    private SQLiteDatabase mydatabase;
    private Response.Listener<JSONObject> noice;

    private Intent intent;
    TextView stepCountTV;
    String countedStep;
    CardViewAdapter cardViewAdapter;
    SQLiteDatabase db;

    public static final int LEAGUE_ID = 4328;
    public static final String LEAGUE_NEXT_EVENT_API = "https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php";
    public static final String LEAGUE_PAST_EVENT_API = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        int orientation = getResources().getConfiguration().orientation; //check whether is it portrait or landscape
        int col = 1;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            col = 2;
        }

        recycler.setLayoutManager(new GridLayoutManager(this, col));
        cardViewAdapter = new CardViewAdapter(this);
        registerReceiver(broadcastReceiver, new IntentFilter("com.example.bolasepak.mybroadcast"));
        // Initialize SQLite data storage.
        BolaDbHelper dbHelper = new BolaDbHelper(this.getApplicationContext());
        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase();

        // Initialize request queue.
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        String url = String.format("%s?id=%d", LEAGUE_PAST_EVENT_API, LEAGUE_ID);
        String nextUrl = String.format("%s?id=%d", LEAGUE_NEXT_EVENT_API, LEAGUE_ID);
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo.isConnected()){
          jsonPastEventRequest(url);
          jsonNextEventRequest(nextUrl);
        } else {
            cardViewAdapter.setListMatch(getData());
            recycler.setAdapter(cardViewAdapter);
        }



        SearchView searchBar = (SearchView) findViewById(R.id.searchBar); // inititate a search view

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTeam(newText);
                return false;
            }
        });

        stepCountTV = findViewById(R.id.stepInfo);
        intent = new Intent(this, StepCounterService.class);
        Log.d("SENSORAVAILABLE", String.valueOf(isServiceRunning(StepCounterService.class)));
        if (!isServiceRunning(StepCounterService.class)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }
        }
//        registerReceiver(broadcastReceiver, new IntentFilter("com.example.bolasepak.mybroadcast"));
        mydatabase = openOrCreateDatabase("Team",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS SubscribedTeam(Team_ID VARCHAR);");
        mydatabase.execSQL("INSERT INTO SubscribedTeam VALUES(133602);");
        showAllSubcription();

        Cursor cursor = db.query(
                BolaContract.PastEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BolaContract.PastEntry.COLUMN_NAME_EVENT_ID));
            String itemHomeName = cursor.getString(cursor.getColumnIndexOrThrow(BolaContract.PastEntry.COLUMN_NAME_HOME_NAME));
            String itemAwayName = cursor.getString(cursor.getColumnIndexOrThrow(BolaContract.PastEntry.COLUMN_NAME_AWAY_NAME));
            String itemStadium = cursor.getString(cursor.getColumnIndexOrThrow(BolaContract.PastEntry.COLUMN_NAME_HOME_BADGE));
            System.out.println(String.format("%s %s %s", itemAwayName, itemHomeName, itemStadium));
        }
        cursor.close();

    }

    private ArrayList<SoccerMatch> getData(){
        mydatabase = openOrCreateDatabase("Bola.db",MODE_PRIVATE,null); //need to change database name
        Cursor resultSet = mydatabase.rawQuery("SELECT * FROM past",null); //need to change query

        resultSet.moveToFirst();
        SoccerMatch match = null;
        ArrayList<SoccerMatch> list = new ArrayList<>();
        while (!resultSet.isAfterLast()) {
            match = new SoccerMatch();
            match.setName1(resultSet.getString(4));
            match.setName2(resultSet.getString(5));
            match.setDateMatch(resultSet.getString(3));
            match.setScore1(resultSet.getString(8));
            match.setScore2(resultSet.getString(9));
            Log.d("name", match.getName1());
            Log.d("name2", match.getName2());
            match.setImg1(resultSet.getString(6));
            match.setImg2(resultSet.getString(7));
            match.setEventID(resultSet.getString(0));

            list.add(match);
            resultSet.moveToNext();
        }
        return list;
    }

    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
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

    protected void searchTeam(String teamName) {
        Log.d("SEARCH", teamName);
        SQLiteDatabase dbSearch = openOrCreateDatabase("Bola.db",MODE_PRIVATE,null);
        String sqlStatement = "SELECT * FROM past WHERE homeName LIKE '" + teamName + "%' OR awayName LIKE '" + teamName + "%'";
        Cursor resultSet = dbSearch.rawQuery(sqlStatement, null);
        Log.d("TEST", String.valueOf(resultSet.getCount()));

        tempList.clear();
        if (resultSet.getCount() > 0) {
            resultSet.moveToFirst();
            while (!resultSet.isAfterLast()) {
                for (int i = 0 ; i < list.size() ; i++) {
                    if (list.get(i).getName1().equals(resultSet.getString(4)) &&
                            list.get(i).getName2().equals(resultSet.getString(5))) {
                        Log.d("TEST", "SAME");
                        tempList.add(list.get(i));
                    }
                }
                resultSet.moveToNext();
            }
        }
        Log.d("TEST", String.valueOf(tempList.size()));
        cardViewAdapter.setListMatch(tempList);
        recycler.setAdapter(cardViewAdapter);
    }

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
                            for (int i = 1; i < pastEvents.length(); i++) {
                                ContentValues values = new ContentValues();
                                SoccerMatch match = new SoccerMatch();
                                Long id = Long.parseLong((String) ((JSONObject) pastEvents.get(i)).get("idEvent"));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_EVENT_ID,
                                        Long.parseLong((String) ((JSONObject) pastEvents.get(i)).get("idEvent")));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_HOME_ID,
                                        Long.parseLong((String) ((JSONObject) pastEvents.get(i)).get("idHomeTeam")));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_AWAY_ID,
                                        Long.parseLong((String) ((JSONObject) pastEvents.get(i)).get("idAwayTeam")));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_EVENT_DATE,
                                        (String) ((JSONObject) pastEvents.get(i)).get("dateEvent"));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_HOME_NAME,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strHomeTeam"));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_AWAY_NAME,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strAwayTeam"));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_HOME_SCORE,
                                        (String) ((JSONObject) pastEvents.get(i)).get("intHomeScore"));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_AWAY_SCORE,
                                        (String) ((JSONObject) pastEvents.get(i)).get("intAwayScore"));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_HOME_GOAL,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strHomeGoalDetails"));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_AWAY_GOAL,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strAwayGoalDetails"));
                                match.setName1((String) ((JSONObject) pastEvents.get(i)).get("strHomeTeam"));
                                match.setName2((String) ((JSONObject) pastEvents.get(i)).get("strAwayTeam"));
                                match.setDateMatch((String) ((JSONObject) pastEvents.get(i)).get("dateEvent"));
                                match.setScore1((String) ((JSONObject) pastEvents.get(i)).get("intHomeScore"));
                                match.setScore2((String) ((JSONObject) pastEvents.get(i)).get("intAwayScore"));
                                jsonLogo1(values, match, (String) ((JSONObject) pastEvents.get(i)).get("idHomeTeam"));
                                jsonLogo2(values, match, (String) ((JSONObject) pastEvents.get(i)).get("idAwayTeam"));
                                match.setEventID((String) ( (JSONObject) pastEvents.get(i) ).get("idEvent"));
                                list.add(match);
                                long newRowId = db.insertWithOnConflict(BolaContract.PastEntry.TABLE_NAME,
                                        null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            }

                            cardViewAdapter.setListMatch(list);
                            Log.e("TEST", list.get(0).getName1());
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

    public void jsonLogo1(final ContentValues values, final SoccerMatch match, String id) {
        String url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + id;
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray pastEvents = response.getJSONArray("teams");
                            for (int i = 0; i < pastEvents.length(); i++) {
                                match.setImg1((String) ((JSONObject) pastEvents.get(i)).get("strTeamBadge"));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_HOME_BADGE,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strTeamBadge"));
                                values.put(BolaContract.PastEntry.COLUMN_NAME_STADIUM,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strStadiumLocation"));
                                db.execSQL("UPDATE " + BolaContract.PastEntry.TABLE_NAME
                                        + " SET " + BolaContract.PastEntry.COLUMN_NAME_HOME_BADGE + " = "
                                        + "'" + (String) ((JSONObject) pastEvents.get(i)).get("strTeamBadge") + "'"
                                        + " , " + BolaContract.PastEntry.COLUMN_NAME_STADIUM + " = "
                                        + "'" + (String) ((JSONObject) pastEvents.get(i)).get("strStadiumLocation") + "'"
                                );
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

    public void jsonLogo2(final ContentValues values, final SoccerMatch match, String id) {
        String url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + id;
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray pastEvents = response.getJSONArray("teams");
                            for (int i = 0; i < pastEvents.length(); i++) {
                                values.put(BolaContract.PastEntry.COLUMN_NAME_AWAY_BADGE,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strTeamBadge"));
                                match.setImg2((String) ((JSONObject) pastEvents.get(i)).get("strTeamBadge"));
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

    public void fetchData(String url) {
        final ArrayList<String> list = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("JSON", "contoh");
                            JSONArray pastEvents = response.getJSONArray("events");
                            for (int i = 0; i < pastEvents.length(); i++) {
                                String event = ((String) ((JSONObject) pastEvents.get(i)).get("strEvent"));
                                list.add(event);
                            }
                            String s = "New upcoming match! ";
                            for (String event : list) {
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

    public void showAllSubcription() {

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

        for (String idTeam : list) {
            String url = String.format("%s?id=%s", "https://www.thesportsdb.com/api/v1/json/1/eventsnext.php", idTeam);
            fetchData(url);
        }

    }

    public void createChannel() {
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Subscription");
        notificationBuilder.setContentTitle("UPCOMING MATCH");
        notificationBuilder.setContentText(content);
        notificationBuilder.setSmallIcon(R.drawable.notif);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(3, notificationBuilder.build());

    }

    public void jsonNextEventRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray nextEvents = response.getJSONArray("events");
                            for (int i = 0; i < nextEvents.length(); i++) {
                                ContentValues values = new ContentValues();
                                values.put(BolaContract.NextEntry.COLUMN_NAME_EVENT_ID,
                                        Long.parseLong((String) ((JSONObject) nextEvents.get(i)).get("idEvent")));
                                values.put(BolaContract.NextEntry.COLUMN_NAME_HOME_ID,
                                        Long.parseLong((String) ((JSONObject) nextEvents.get(i)).get("idHomeTeam")));
                                values.put(BolaContract.NextEntry.COLUMN_NAME_AWAY_ID,
                                        Long.parseLong((String) ((JSONObject) nextEvents.get(i)).get("idAwayTeam")));
                                values.put(BolaContract.NextEntry.COLUMN_NAME_EVENT_DATE,
                                        (String) ((JSONObject) nextEvents.get(i)).get("dateEvent"));
                                values.put(BolaContract.NextEntry.COLUMN_NAME_HOME_NAME,
                                        (String) ((JSONObject) nextEvents.get(i)).get("strHomeTeam"));
                                values.put(BolaContract.NextEntry.COLUMN_NAME_AWAY_NAME,
                                        (String) ((JSONObject) nextEvents.get(i)).get("strAwayTeam"));
                                jsonNextLogo1(values, (String) ((JSONObject) nextEvents.get(i)).get("idHomeTeam"));
                                jsonNextLogo2(values, (String) ((JSONObject) nextEvents.get(i)).get("idAwayTeam"));
                                long newRowId = db.insertWithOnConflict(BolaContract.NextEntry.TABLE_NAME,
                                        null, values, SQLiteDatabase.CONFLICT_REPLACE);
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
    public void jsonNextLogo1(final ContentValues values, String id) {
        String url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + id;
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray pastEvents = response.getJSONArray("teams");
                            for (int i = 0; i < pastEvents.length(); i++) {
                                values.put(BolaContract.NextEntry.COLUMN_NAME_HOME_BADGE,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strTeamBadge"));
                                values.put(BolaContract.NextEntry.COLUMN_NAME_STADIUM,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strStadiumLocation"));
                                db.execSQL("UPDATE " + BolaContract.NextEntry.TABLE_NAME
                                        + " SET " + BolaContract.NextEntry.COLUMN_NAME_HOME_BADGE + " = "
                                        + "'" + (String) ((JSONObject) pastEvents.get(i)).get("strTeamBadge") + "'"
                                        + " , " + BolaContract.NextEntry.COLUMN_NAME_STADIUM + " = "
                                        + "'" + (String) ((JSONObject) pastEvents.get(i)).get("strStadiumLocation") + "'"
                                );
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
    public void jsonNextLogo2(final ContentValues values, String id) {
        String url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=" + id;
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray pastEvents = response.getJSONArray("teams");
                            for (int i = 0; i < pastEvents.length(); i++) {
                                values.put(BolaContract.NextEntry.COLUMN_NAME_HOME_BADGE,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strTeamBadge"));
                                values.put(BolaContract.NextEntry.COLUMN_NAME_STADIUM,
                                        (String) ((JSONObject) pastEvents.get(i)).get("strStadiumLocation"));
                                db.execSQL("UPDATE " + BolaContract.NextEntry.TABLE_NAME
                                        + " SET " + BolaContract.NextEntry.COLUMN_NAME_HOME_BADGE + " = "
                                        + "'" + (String) ((JSONObject) pastEvents.get(i)).get("strTeamBadge") + "'"
                                        + " , " + BolaContract.NextEntry.COLUMN_NAME_STADIUM + " = "
                                        + "'" + (String) ((JSONObject) pastEvents.get(i)).get("strStadiumLocation") + "'"
                                );
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
}
