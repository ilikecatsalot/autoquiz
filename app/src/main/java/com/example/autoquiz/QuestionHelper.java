package com.example.autoquiz;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
public class QuestionHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1;

    public QuestionHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table for distractors
        db.execSQL("CREATE TABLE distractors (entity TEXT, distractor TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS distractors");
        onCreate(db);
    }

    public List<String> getDistractors(String entity) {
        List<String> distractors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT distractor FROM distractors WHERE entity = ?", new String[]{entity});

        if (cursor.moveToFirst()) {
            do {
                distractors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return distractors;
    }
}
