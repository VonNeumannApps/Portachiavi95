package com.example.portachiavi95;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Bundle> accounts = new ArrayList<>();
    BaseAdapter baseAdapter;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this, DBManager.DATABASE_NAME, null, DBManager.DATABASE_VERSION);

        // salviamo la psw nelle shared preferences
        // NB. quando si disinstalla la app il file viene eliminato
        if(!isLogged()){

            //mi devo loggare
            openLoginActivity();
        }

        initializeAdapter();
        loadAccounts();
        ListView accountsLV = findViewById(R.id.accountListView);
        accountsLV.setAdapter(baseAdapter);

        // TODO: layout account detail activity è fatto, va aggiunta logica per passare a quella activity
    }

    void initializeAdapter() {

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return accounts.size();
            }

            @Override
            public Bundle getItem(int i) {
                return accounts.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                if(view == null){
                    // dobbiamo creare la view
                    // si usa il layout inflater
                    // e si carica dentro la view

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    // NB: only  API >= 23
                    // getSystemServiceName(LAYOUT_INFLATER_SERVICE);

                    view = inflater.inflate(R.layout.account_item_layout, viewGroup, false);
                }

                // recuperare elemento a questa postizione
                Bundle account = getItem(i);

                // recupero testi
                TextView descTV = view.findViewById(R.id.descriptionTextView);
                TextView mailTV = view.findViewById(R.id.mailTextView);
                descTV.setText(account.getString("descrizione"));
                mailTV.setText(account.getString("mail"));

                ImageView checkIV = view.findViewById(R.id.checkImageView);

                // controllo se elemento è selezionato
                if(account.getBoolean("selected")) {

                    checkIV.setVisibility(View.VISIBLE);

                } else {

                    checkIV.setVisibility(View.GONE);
                }

                return view;
            }
        };
    }

    void loadAccounts() {

        this.accounts.clear();
        this.accounts.addAll(dbManager.getAccounts());

        this.baseAdapter.notifyDataSetChanged();
    }

    void openLoginActivity(){

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        startActivity(intent);
    }

    boolean isLogged() {

        boolean logged = false;

        SharedPreferences userData = getSharedPreferences(getString(R.string.USER_DATA), MODE_PRIVATE);

        String pass = userData.getString("masterPassword", "");

        logged = !pass.equals("");

        return logged;

    }
}
