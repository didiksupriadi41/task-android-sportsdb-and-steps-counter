package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

public class EventDetail extends AppCompatActivity {
    private String event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Intent intent = getIntent();
        event_id = intent.getStringExtra("EVENT_ID");
        Log.d("RVIEW", event_id);
        loadData(event_id);
    }

    private void loadData(String event_id) {
        TextView name1 = findViewById(R.id.name1);
        TextView homeShots = findViewById(R.id.homeShots);
        TextView score1 = findViewById(R.id.score1);
        TextView name2 = findViewById(R.id.name2);
        TextView awayShots = findViewById(R.id.awayShots);
        TextView score2 = findViewById(R.id.score2);
        String temp = "TEST";
        name1.setText(temp);
        homeShots.setText("12");
        score1.setText("12");
        name2.setText(temp);
        awayShots.setText("12");
        score2.setText("12");
        Picasso.get()
                .load("https://www.thesportsdb.com/images/media/team/badge/a1af2i1557005128.png")
                .into((ImageView) findViewById(R.id.img1));
        Picasso.get()
                .load("https://www.thesportsdb.com/images/media/team/badge/a1af2i1557005128.png")
                .into((ImageView) findViewById(R.id.img2));
        ListView homeGoals = findViewById(R.id.homeGoals);
        ListView awayGoals = findViewById(R.id.awayGoals);
        ArrayAdapter<String> adapter1;
        ArrayAdapter<String> adapter2;
        ArrayList<String> goals1 = new ArrayList<String>();
        ArrayList<String> goals2 = new ArrayList<String>();
        goals1.add("VINSEN '01");
        goals1.add("VINSEN '02");
        goals2.add("VINSEN '03");
        goals2.add("VINSEN '04");
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goals1);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goals2);
        homeGoals.setAdapter(adapter1);
        awayGoals.setAdapter(adapter2);
    }

    public void openTeamDetail(View view) {
        Intent intent = new Intent(EventDetail.this, TeamDetail.class);
        startActivity(intent);
    }
}
