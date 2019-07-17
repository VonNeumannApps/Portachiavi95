package com.example.portachiavi95;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    /*
        Usato per gestire i database.
        Lo useremo per inserire gli account.

     */

    public static final String DATABASE_NAME = "db";
    public static final int DATABASE_VERSION = 1;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // prima funzione, eseguita solo quando il db non esiste
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // funzionalità che non useremo
    }
}
