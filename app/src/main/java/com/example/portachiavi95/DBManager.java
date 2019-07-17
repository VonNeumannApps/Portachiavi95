package com.example.portachiavi95;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

        String query = "CREATE TABLE accounts (id INTEGER PRIMARY KEY AUTOINCREMENT, descrizione TEXT, username TEXT, password TEXT, mail TEXT) ";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // funzionalità che non useremo
    }

    // TODO funzioni che servono: lettura account, inserimento

    public void insertNewAccount(SQLiteDatabase sqLiteDatabase, Bundle account) {

        // TODO: recuperare db, aprirlo in scrittura, salvare i dati nella tabella
        try(SQLiteDatabase db = getWritableDatabase()) {
            // tipo di dati "Bundle", sono chiave-valore
            // db insert si aspetta oggetto di tipo ContentValues
            ContentValues contentValues = new ContentValues();
            contentValues.put("descrizione", account.getString("descrizione"));
            contentValues.put("username", account.getString("username"));
            contentValues.put("password", account.getString("password"));
            contentValues.put("mail", account.getString("mail"));

            db.insert("accounts", null, contentValues);
        }

    }

    public void updateAccount(Bundle account) {

        try(SQLiteDatabase db = getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();

            contentValues.put("id", account.getInt("id"));
            contentValues.put("descrizione", account.getString("descrizione"));
            contentValues.put("username", account.getString("username"));
            contentValues.put("password", account.getString("password"));
            contentValues.put("mail", account.getString("mail"));

            String account_id = String.valueOf(account.getInt("id"));

            String[] args = new String[]{account_id};

            db.update("accounts", contentValues, "id=?", args);

        }
    }

    public ArrayList<Bundle> getAccounts() {

        ArrayList<Bundle> accounts = new ArrayList<>();

        try (SQLiteDatabase db = getReadableDatabase()) {

            String query = "SELECT * FROM accounts";

            // Cursor also implements the Autocloseable interface, so it can be used in the try-with-resources statemenT
            try(Cursor cur = db.rawQuery(query, null)) { //TODO refactor db e cur in one single try statement
                cur.moveToFirst();

                while(!cur.isAfterLast()) {

                    Bundle account = new Bundle();

                    {   int columnIdIndexID = cur.getColumnIndex("id"); // indice della colonna id
                        int columnValueID = cur.getInt(columnIdIndexID);
                        account.putInt("id", columnValueID);
                    }

                    {   String columnName = "username";
                        int columnIndexDESCRIZIONE = cur.getColumnIndex(columnName); // indice della colonna descrizione
                        String columnValueDESCRIZIONE = cur.getString(columnIndexDESCRIZIONE);
                        account.putString(columnName, columnValueDESCRIZIONE);
                    }

                    {   String columnName = "username";
                        int columnIndexUSERNAME = cur.getColumnIndex(columnName); // indice della colonna username
                        String columnValueUSERNAME = cur.getString(columnIndexUSERNAME);
                        account.putString(columnName, columnValueUSERNAME);
                    }

                    {   String columnName = "password";
                        int columnIndexPASSWORD = cur.getColumnIndex(columnName); // indice della colonna password
                        String columnValuePASSWORD = cur.getString(columnIndexPASSWORD);
                        account.putString(columnName, columnValuePASSWORD);
                    }

                    // TODO unfinished
                }
            }

            db.execSQL(query);
        }

        return accounts;
    }
}
