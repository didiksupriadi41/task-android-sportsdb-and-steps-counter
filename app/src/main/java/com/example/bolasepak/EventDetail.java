package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EventDetail extends AppCompatActivity {
    private String event_id;
    private SQLiteDatabase mydatabase;
    private String home_id, away_id;
    private String dayCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        Intent intent = getIntent();

        event_id = intent.getStringExtra("EVENT_ID");
        Log.d("RVIEW", event_id);
        mydatabase = openOrCreateDatabase("Bola.db",MODE_PRIVATE,null); //need to change database name

        loadData(event_id);
    }

    private void loadData(String event_id) {
        Cursor resultSet = mydatabase.rawQuery("SELECT * FROM past WHERE eventID = ?", new String[] {event_id}); //need to change query

        TextView name1 = findViewById(R.id.name1);
        TextView score1 = findViewById(R.id.score1);
        TextView name2 = findViewById(R.id.name2);
        TextView score2 = findViewById(R.id.score2);
        TextView date = findViewById(R.id.dateMatch);

        resultSet.moveToFirst();
        name1.setText(resultSet.getString(4));
        score1.setText(resultSet.getString(8));
        name2.setText(resultSet.getString(5));
        score2.setText(resultSet.getString(9));
        date.setText(resultSet.getString(3));

        home_id = resultSet.getString(1);
        away_id = resultSet.getString(2);
        Picasso.get()
                .load(resultSet.getString(6))
                .into((ImageView) findViewById(R.id.img1));
        Picasso.get()
                .load(resultSet.getString(7))
                .into((ImageView) findViewById(R.id.img2));

        ListView homeGoals = findViewById(R.id.homeGoals);
        ListView awayGoals = findViewById(R.id.awayGoals);

        ArrayAdapter<String> adapter1;
        ArrayAdapter<String> adapter2;
        ArrayList<String> goals1 = new ArrayList<String>();
        ArrayList<String> goals2 = new ArrayList<String>();

        String home = resultSet.getString(10);
        String delims = "[;]";
        String[] tokens1 = home.split(delims);
        String away = resultSet.getString(11);
        String[] tokens2 = away.split(delims);

        for(String goal: tokens1){
            goals1.add(goal);
        }

        for(String goal: tokens2){
            goals2.add(goal);
        }

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goals1);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goals2);
        homeGoals.setAdapter(adapter1);
        awayGoals.setAdapter(adapter2);
        String stadiumName = "Emirates Stadium";
        dayCount = "10";
        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(stadiumName,
                getApplicationContext(), new GeocoderHandler());

    }

    public void openHomeDetail(View view) {
        Intent intent = new Intent(EventDetail.this, TeamDetail.class);
        intent.putExtra("TEAM_ID", this.home_id);
        startActivity(intent);
    }

    public void openAwayDetail(View view) {
        Intent intent = new Intent(EventDetail.this, TeamDetail.class);
        intent.putExtra("TEAM_ID", this.away_id);
        startActivity(intent);
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String latitude;
            String longitude;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    latitude = bundle.getString("latitude");
                    longitude = bundle.getString("longitude");
                    break;
                default:
                    latitude = null;
                    longitude = null;
            }
            Log.d("GEO", latitude);
            Log.d("GEO", longitude);
            String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?";
            String url = String.format("%slat=%s&lon=%s&appid=%s", BASE_URL, latitude, longitude, getString(R.string.api_key));
            jsonWeather(url);

        }
    }

    public void jsonWeather(String url) {
        Log.d("TEST", url);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(12);
                            Log.d("TEST", "TEST");
                            JSONArray listData = response.getJSONArray("list");
                            int i = 0;
                            Log.d("TEST", (String) ((JSONObject)((JSONObject) listData.get(i)).get("main")).get("temp"));
//                            Log.d("TEST", listData.getJSONObject(i).getString("pressure"));
//                            for (int i = 0; i < pastEvents.length(); i++) {
//                                SoccerMatch match = new SoccerMatch();
//                                match.setName1( (String) ( (JSONObject) pastEvents.get(i) ).get("strHomeTeam"));
//                                list.add(match);
//                            }
//                            cardViewAdapter.setListMatch(list);
//                            Log.e("TEST", list.get(0).getName1());
//                            recycler.setAdapter(cardViewAdapter);
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
