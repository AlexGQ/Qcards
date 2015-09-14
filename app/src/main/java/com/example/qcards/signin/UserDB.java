package com.example.qcards.signin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE PB_COM_USERS (ID_USER INTEGER, NAME TEXT, LAST_NAME TEXT, EMAIL TEXT, PASSWORD TEXT)";

    public UserDB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Table PB_COM_USERS
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        // Drop last table
        db.execSQL("DROP TABLE IF EXISTS PB_COM_USERS");

        // New table
        db.execSQL(sqlCreate);
    }
}