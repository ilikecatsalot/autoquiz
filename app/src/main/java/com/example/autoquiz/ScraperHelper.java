package com.example.autoquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScraperHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "scraper.db";
    private static final String TABLE_NAME = "scraped_data";

    public ScraperHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "site_link TEXT, " +
                "title TEXT, " + // Added column for title
                "content TEXT, " +
                "date DATETIME, " +
                "timestamp TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String siteLink, String title, String content, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("site_link", siteLink);
        values.put("title", title); // Store the title
        values.put("content", content);
        values.put("date", date);
        values.put("timestamp", System.currentTimeMillis());

        db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY date DESC", null);
    }
}
