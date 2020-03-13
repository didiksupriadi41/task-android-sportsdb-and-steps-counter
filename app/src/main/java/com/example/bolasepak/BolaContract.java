package com.example.bolasepak;

import android.provider.BaseColumns;

public final class BolaContract {
    private BolaContract() {
    }

    public static class PastEntry implements BaseColumns {
        public static final String TABLE_NAME = "past";
        public static final String COLUMN_NAME_EVENT_ID = "eventId";
        public static final String COLUMN_NAME_HOME_ID = "homeId";
        public static final String COLUMN_NAME_AWAY_ID = "awayId";
        public static final String COLUMN_NAME_EVENT_DATE = "eventDate";
        public static final String COLUMN_NAME_HOME_NAME = "homeName";
        public static final String COLUMN_NAME_AWAY_NAME = "awayName";
        public static final String COLUMN_NAME_HOME_BADGE = "homeBadge";
        public static final String COLUMN_NAME_AWAY_BADGE = "awayBadge";
        public static final String COLUMN_NAME_HOME_SCORE = "homeScore";
        public static final String COLUMN_NAME_AWAY_SCORE = "awayScore";
        public static final String COLUMN_NAME_HOME_GOAL = "homeGoal";
        public static final String COLUMN_NAME_AWAY_GOAL = "awayGoal";
        public static final String COLUMN_NAME_HOME_GOAL_DETAIL = "homeGoalDetail";
        public static final String COLUMN_NAME_AWAY_GOAL_DETAIL = "awayGoalDetail";
    }

    public static class NextEntry implements BaseColumns {
        public static final String TABLE_NAME = "next";
        public static final String COLUMN_NAME_EVENT_ID = "eventId";
        public static final String COLUMN_NAME_HOME_ID = "homeId";
        public static final String COLUMN_NAME_AWAY_ID = "awayId";
        public static final String COLUMN_NAME_EVENT_DATE = "eventDate";
        public static final String COLUMN_NAME_HOME_NAME = "homeName";
        public static final String COLUMN_NAME_AWAY_NAME = "awayName";
        public static final String COLUMN_NAME_HOME_BADGE = "homeBadge";
        public static final String COLUMN_NAME_AWAY_BADGE = "awayBadge";
    }
}
