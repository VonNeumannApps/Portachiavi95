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

    public static final String SELECTED_FIELD_NAME = "selected";
    public static final String MAIL_COL = "mail";
    public static final String PASSWORD_COL = "password";
    public static final String USERNAME_COL = "username";
    public static final String ACCOUNTS_TABLE_NAME = "accounts";
    public static final String DESCRIPTION_COL = "descrizione";
    public static final String ID_COL = "id";

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
            setAccountMainFieldsToContentValues(contentValues, account);

            db.insert(ACCOUNTS_TABLE_NAME, null, contentValues);
        }
    }

    public static void setAccountMainFieldsToContentValues(
            ContentValues contentValues, Bundle account) {
        contentValues.put(DESCRIPTION_COL, account.getString(DESCRIPTION_COL));
        contentValues.put(USERNAME_COL, account.getString(USERNAME_COL));
        contentValues.put(PASSWORD_COL, account.getString(PASSWORD_COL));
        contentValues.put(MAIL_COL, account.getString(MAIL_COL));
    }

    public void updateAccount(Bundle account) {

        try(SQLiteDatabase db = getWritableDatabase()) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(ID_COL, account.getInt(ID_COL));
            setAccountMainFieldsToContentValues(contentValues, account);

            String account_id = String.valueOf(account.getInt(ID_COL));

            String[] args = new String[]{account_id};

            db.update(ACCOUNTS_TABLE_NAME, contentValues, ID_COL + "=?", args);
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

                    putIntFromCursorIntoBundle(cur, account, ID_COL);
                    putStringFromCursorIntoBundle(cur, account, DESCRIPTION_COL);
                    putStringFromCursorIntoBundle(cur, account, USERNAME_COL);
                    putStringFromCursorIntoBundle(cur, account, PASSWORD_COL);
                    putStringFromCursorIntoBundle(cur, account, MAIL_COL);

                    account.putBoolean(SELECTED_FIELD_NAME, false);

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

            if(account.getBoolean(SELECTED_FIELD_NAME))
            {
                int idToAdd = account.getInt(ID_COL);

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
