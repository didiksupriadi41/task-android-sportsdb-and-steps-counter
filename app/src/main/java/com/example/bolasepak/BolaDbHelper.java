package com.example.bolasepak;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BolaDbHelper extends SQLiteOpenHelper {

    public static final String SQL_CREATE_PAST =
            "CREATE TABLE " + BolaContract.PastEntry.TABLE_NAME + " (" +
                    BolaContract.PastEntry.COLUMN_NAME_EVENT_ID + " INTEGER PRIMARY KEY," +
                    BolaContract.PastEntry.COLUMN_NAME_HOME_ID + " INTEGER," +
                    BolaContract.PastEntry.COLUMN_NAME_AWAY_ID + " INTEGER," +
                    BolaContract.PastEntry.COLUMN_NAME_EVENT_DATE + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_HOME_NAME + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_AWAY_NAME + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_HOME_NAME + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_AWAY_NAME + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_HOME_BADGE + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_AWAY_BADGE + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_HOME_SCORE + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_AWAY_SCORE + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_HOME_SCORE + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_AWAY_SCORE + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_HOME_SCORE + " TEXT," +
                    BolaContract.PastEntry.COLUMN_NAME_AWAY_SCORE + " TEXT);";

    public static final String SQL_CREATE_NEXT =
            "CREATE TABLE " + BolaContract.NextEntry.TABLE_NAME + " (" +
                    BolaContract.NextEntry.COLUMN_NAME_EVENT_ID + " INTEGER PRIMARY KEY," +
                    BolaContract.NextEntry.COLUMN_NAME_HOME_ID + " INTEGER," +
                    BolaContract.NextEntry.COLUMN_NAME_AWAY_ID + " INTEGER," +
                    BolaContract.NextEntry.COLUMN_NAME_EVENT_DATE + " TEXT," +
                    BolaContract.NextEntry.COLUMN_NAME_HOME_NAME + " TEXT," +
                    BolaContract.NextEntry.COLUMN_NAME_AWAY_NAME + " TEXT," +
                    BolaContract.NextEntry.COLUMN_NAME_HOME_NAME + " TEXT," +
                    BolaContract.NextEntry.COLUMN_NAME_AWAY_NAME + " TEXT," +
                    BolaContract.NextEntry.COLUMN_NAME_HOME_BADGE + " TEXT," +
                    BolaContract.NextEntry.COLUMN_NAME_AWAY_BADGE + " TEXT);";

    public static final String SQL_DELETE_PAST =
            "DROP TABLE IF EXISTS " + BolaContract.PastEntry.TABLE_NAME;

    public static final String SQL_DELETE_NEXT =
            "DROP TABLE IF EXISTS " + BolaContract.NextEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES = SQL_CREATE_PAST + SQL_CREATE_NEXT;

    private static final String SQL_DELETE_ENTRIES = SQL_DELETE_PAST + SQL_DELETE_NEXT;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Bola.db";

    public BolaDbHelper(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BolaDbHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
