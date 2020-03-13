package com.example.bolasepak;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

public class TeamDetail extends AppCompatActivity {
    private SQLiteDatabase mydatabase;
    private String team_id;
    private PagerAdapter pagerAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        this.team_id = "12345";
        loadData();

        Intent intent = getIntent();
//        String id = intent.getStringExtra("TEAM_ID");

        mydatabase = openOrCreateDatabase("Team",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS SubscribedTeam(Team_ID VARCHAR);");
        updateButton();

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(new com.example.bolasepak.PagerAdapter(getSupportFragmentManager(), 1));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mPager);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void loadData() {
        Picasso.get()
                .load("https://www.thesportsdb.com/images/media/team/badge/a1af2i1557005128.png")
                .into((ImageView) findViewById(R.id.teamLogo));
        TextView name = (TextView) findViewById(R.id.teamName);
        name.setText("Arsenal");

    }

    public void editSubscribedTeam(View view) {
        if (!isSubscribed()) {
            mydatabase.execSQL("INSERT INTO SubscribedTeam VALUES(" + this.team_id + ");");
            Cursor resultSet = mydatabase.rawQuery("Select * from SubscribedTeam", null);
            resultSet.moveToFirst();
            Integer counter = 1;
            while (!resultSet.isAfterLast()) {
                String username = resultSet.getString(0);
                resultSet.moveToNext();
                counter++;
            }
        } else {
            mydatabase.execSQL("DELETE FROM SubscribedTeam WHERE Team_ID = " + this.team_id);
            Cursor resultSet = mydatabase.rawQuery("Select * from SubscribedTeam", null);
            if (resultSet.getCount() > 0) {
                resultSet.moveToFirst();
                int counter = 1;
                while (!resultSet.isAfterLast()) {
                    String username = resultSet.getString(0);
                    resultSet.moveToNext();
                    counter++;
                }
            }
        }
        updateButton();
    }
    public boolean isSubscribed() {
        Cursor resultSet = mydatabase.rawQuery("SELECT * FROM SubscribedTeam WHERE Team_ID = ?", new String[] {this.team_id});
        return resultSet.getCount() != 0;
    }
    public void updateButton() {
        Button btn = findViewById(R.id.button);
        if (isSubscribed()) {
            btn.setBackgroundColor(ContextCompat.getColor(TeamDetail.this,R.color.colorGrey));
            btn.setText(R.string.subscribed);
        } else {
            btn.setBackgroundColor(ContextCompat.getColor(TeamDetail.this,R.color.colorPrimary));
            btn.setText(R.string.subscribe);
        }
    }
}
