package com.example.projetmobile;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projetmobile.DBContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pet_database";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_DOG_ENTRIES =
            "CREATE TABLE " + DBContract.DogEntry.TABLE_NAME + " (" +
                    DBContract.DogEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.DogEntry.COLUMN_NAME + " TEXT," +
                    DBContract.DogEntry.COLUMN_IMAGE_URL + " TEXT)";

    private static final String SQL_CREATE_CAT_ENTRIES =
            "CREATE TABLE " + DBContract.CatEntry.TABLE_NAME + " (" +
                    DBContract.CatEntry._ID + " INTEGER PRIMARY KEY," +
                    DBContract.CatEntry.COLUMN_NAME + " TEXT," +
                    DBContract.CatEntry.COLUMN_IMAGE_URL + " TEXT)";

    private static final String SQL_DELETE_DOG_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.DogEntry.TABLE_NAME;

    private static final String SQL_DELETE_CAT_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.CatEntry.TABLE_NAME;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DOG_ENTRIES);
        db.execSQL(SQL_CREATE_CAT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DOG_ENTRIES);
        db.execSQL(SQL_DELETE_CAT_ENTRIES);
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

    public boolean addCat(String name, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.CatEntry.COLUMN_NAME, name);
        values.put(DBContract.CatEntry.COLUMN_IMAGE_URL, imageUrl);
        long result = db.insert(DBContract.CatEntry.TABLE_NAME, null, values);
        return result != -1;
    }

    public List<Pet> getAllDogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.DogEntry.TABLE_NAME, null, null, null, null, null, null);
        return cursorToDogList(cursor);
    }

    public List<Pet> getAllCats() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.CatEntry.TABLE_NAME, null, null, null, null, null, null);
        return cursorToCatList(cursor);
    }

    public void deleteDB(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @SuppressLint("Range")
    public List<Pet> cursorToDogList(Cursor cursor) {
        List<Pet> pets = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Pet pet = new Pet();
                pet.setNom(cursor.getString(cursor.getColumnIndex(DBContract.DogEntry.COLUMN_NAME)));
                pet.setUrl(cursor.getString(cursor.getColumnIndex(DBContract.DogEntry.COLUMN_IMAGE_URL)));
                pets.add(pet);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return pets;
    }

    @SuppressLint("Range")
    public List<Pet> cursorToCatList(Cursor cursor) {
        List<Pet> pets = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Pet pet = new Pet();
                pet.setNom(cursor.getString(cursor.getColumnIndex(DBContract.CatEntry.COLUMN_NAME)));
                pet.setUrl(cursor.getString(cursor.getColumnIndex(DBContract.CatEntry.COLUMN_IMAGE_URL)));
                pets.add(pet);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return pets;
    }
}



