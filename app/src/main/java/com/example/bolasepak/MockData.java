package com.example.bolasepak;

import java.util.ArrayList;
import android.util.Log;

public class MockData {
    public static String[][] data = new String[][]{
            {"Leicester", "Aston Villa", "9 March 2020", "1", "0", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png"},
            {"Aston Villa", "Leicester", "8 March 2020", "2", "1", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png"},
            {"Leicester", "Aston Villa", "9 March 2020", "3", "0", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png"},
            {"Aston Villa", "Leicester", "8 March 2020", "4", "1", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png"},
            {"Leicester", "Aston Villa", "9 March 2020", "5", "0", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png"},
            {"Aston Villa", "Leicester", "8 March 2020", "6", "1", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png"},
            {"Leicester", "Aston Villa", "9 March 2020", "7", "0", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png"},
            {"Aston Villa", "Leicester", "8 March 2020", "8", "1", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png"},
            {"Leicester", "Aston Villa", "9 March 2020", "9", "0", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png"},
            {"Aston Villa", "Leicester", "8 March 2020", "10", "1", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png"},
            {"Leicester", "Aston Villa", "9 March 2020", "1", "0", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png"},
            {"Aston Villa", "Leicester", "8 March 2020", "2", "1", "https://www.thesportsdb.com/images/media/team/badge/aofmzk1565427581.png", "https://www.thesportsdb.com/images/media/team/badge/xtxwtu1448813356.png"}
    };

    public static ArrayList<SoccerMatch> getListData(){
        SoccerMatch match = null;
        ArrayList<SoccerMatch> list = new ArrayList<>();
        for (String[] aData : data) {
            match = new SoccerMatch();
            match.setName1(aData[0]);
            match.setName2(aData[1]);
            match.setDateMatch(aData[2]);
            match.setScore1(aData[3]);
            match.setScore2(aData[4]);
            Log.d("name", match.getName1());
            Log.d("name2", match.getName2());
            match.setImg1(aData[5]);
            match.setImg2(aData[6]);

            list.add(match);
        }

        return list;
    }
}
