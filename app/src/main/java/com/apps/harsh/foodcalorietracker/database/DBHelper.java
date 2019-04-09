package com.apps.harsh.foodcalorietracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apps.harsh.foodcalorietracker.model.InfoModel;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME ="logs.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, context.getDatabasePath(DATABASE_NAME).toString(),null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE nutrientsLog(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date TEXT, time TEXT, fat FLOAT, proteins FLOAT, calories FLOAT, carbohydrates FLOAT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS nutrientsLog");
        onCreate(sqLiteDatabase);
    }

    public void addDetails(String name, String date, String time, float fat, float proteins, float calories, float carbs) {
        ContentValues values = new ContentValues(7);
        values.put("name", name);
        values.put("date", date);
        values.put("time", time);
        values.put("fat", fat);
        values.put("proteins", proteins);
        values.put("calories", calories);
        values.put("carbohydrates", carbs);
        getWritableDatabase().insert("nutrientsLog", "date", values);
    }

    public List<InfoModel> getAllLogs() {
        List<InfoModel> logs = new ArrayList<>();
        String selectQuery = "SELECT * FROM nutrientsLog ORDER BY date DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                InfoModel model = new InfoModel();
                model.setName(cursor.getString(cursor.getColumnIndex("name")));
                model.setDate(cursor.getString(cursor.getColumnIndex("date")));
                model.setTime(cursor.getString(cursor.getColumnIndex("time")));
                model.setFat(String.valueOf(cursor.getFloat(cursor.getColumnIndex("fat"))));
                model.setProteins(String.valueOf(cursor.getFloat(cursor.getColumnIndex("proteins"))));
                model.setCalories(String.valueOf(cursor.getFloat(cursor.getColumnIndex("calories"))));
                model.setCarbohydrates(String.valueOf(cursor.getFloat(cursor.getColumnIndex("carbohydrates"))));
                logs.add(model);
            } while (cursor.moveToNext());
        }
        db.close();
        return logs;
    }

    public String getDailyCal(String date) {
        float total = 0;
        String query = "SELECT calories FROM nutrientsLog WHERE date ='"+date+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        if(cursor.moveToFirst()) {
            for (int i = 0; i < count; i++) {
                total += cursor.getFloat(cursor.getColumnIndex("calories"));
                cursor.moveToNext();
            }
        }
        return String.valueOf(total);
    }
}
