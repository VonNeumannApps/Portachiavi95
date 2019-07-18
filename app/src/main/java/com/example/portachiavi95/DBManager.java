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

    // NB onCreate è chiamata solo al primo accesso
    // anche se creo più istanza di DBManager
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // prima funzione, eseguita solo quando il db non esiste

        String query = "CREATE TABLE accounts " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descrizione TEXT, " +
                "username TEXT, " +
                "password TEXT, " +
                "mail TEXT) ";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // funzionalità che non useremo
    }

    public void insertNewAccount(Bundle account) {

        try(SQLiteDatabase db = getWritableDatabase()) {

            // tipo di dati "Bundle", sono chiave-valore
            // lo convertiamo in "ContentValues", che è il tipo di oggetto che si aspetta la db insert

            ContentValues contentValues = new ContentValues();
            contentValues.put("descrizione", account.getString("descrizione"));
            contentValues.put("username", account.getString("username"));
            contentValues.put("password", account.getString("password"));
            contentValues.put(Utilities.MAIL_COL, account.getString(Utilities.MAIL_COL));

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
            contentValues.put(Utilities.MAIL_COL, account.getString(Utilities.MAIL_COL));

            String account_id = String.valueOf(account.getInt("id"));

            String[] args = new String[]{account_id};

            db.update("accounts", contentValues, "id=?", args);

        }
    }

    public ArrayList<Bundle> getAccounts() {

        ArrayList<Bundle> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";

        try (SQLiteDatabase db = getReadableDatabase()) { //TODO refactor db e cur in one single try statement
            try(Cursor cur = db.rawQuery(query, null)) {

                cur.moveToFirst();

                while(!cur.isAfterLast()) {

                    Bundle account = new Bundle();

                    putIntFromCursorIntoBundle(cur, account, "id");
                    putStringFromCursorIntoBundle(cur, account, "descrizione");
                    putStringFromCursorIntoBundle(cur, account, "username");
                    putStringFromCursorIntoBundle(cur, account, "password");
                    putStringFromCursorIntoBundle(cur, account, Utilities.MAIL_COL);

                    account.putBoolean("selected", false);

                    accounts.add(account);
                    cur.moveToNext();
                }
            }

        }

        return accounts;
    }

    public static String getDeletionQuerySelectedAccounts(ArrayList<Bundle> accounts) {

        String query = "DELETE FROM accounts WHERE id IN (";

        boolean isFirstIdToDelete = true;

        for(Bundle account : accounts) {

            if(account.getBoolean("selected"))
            {
                int idToAdd = account.getInt("id");

                if(isFirstIdToDelete) {

                    query = query + idToAdd;
                    isFirstIdToDelete = false; // abbiamo appena aggiunto il primo
                }
                else {
                    query = query + ", " + idToAdd;
                }
            }
        }

        query = query + ")";

        return query;
    }

    public void deleteSelectedAccounts(ArrayList<Bundle> accounts) {

        String query = getDeletionQuerySelectedAccounts(accounts);

        try(SQLiteDatabase db = getWritableDatabase()) {

            db.execSQL(query);
        }

        // TODO we need unit tests
    }

    static private void putStringFromCursorIntoBundle(Cursor cur, Bundle bundle, String columnName){

        int columnIndex = cur.getColumnIndex(columnName); // indice della colonna con nome columName
        String columnValue = cur.getString(columnIndex);
        bundle.putString(columnName, columnValue);
    }

    static private void putIntFromCursorIntoBundle(Cursor cur, Bundle bundle, String columnName){

        int columnIndex = cur.getColumnIndex(columnName); // indice della colonna con nome columName
        int columnValue = cur.getInt(columnIndex);
        bundle.putInt(columnName, columnValue);
    }
}
