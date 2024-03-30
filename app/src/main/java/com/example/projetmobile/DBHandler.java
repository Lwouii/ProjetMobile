package com.example.projetmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dog_database";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.DogEntry.TABLE_NAME + " (" +
                    DBContract.DogEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.DogEntry.COLUMN_NAME + " TEXT," +
                    DBContract.DogEntry.COLUMN_IMAGE_URL + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.DogEntry.TABLE_NAME;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean addDog(String name, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.DogEntry.COLUMN_NAME, name);
        values.put(DBContract.DogEntry.COLUMN_IMAGE_URL, imageUrl);
        long result = db.insert(DBContract.DogEntry.TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getAllDogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(DBContract.DogEntry.TABLE_NAME, null, null, null, null, null, null);
    }
}

