package com.example.portachiavi95;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

        accountsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle account = accounts.get(i);
                openAccountDetailActivity(account);
            }
        });

        ImageView addAccountBtn = findViewById(R.id.addAccountButton);

        addAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAccountDetailActivity(new Bundle());// problema memory leak: se poi si annulla la creazione dell'account
                // andrebbe creato solo facendo "salva"
            }
        });

        // TODO: layout account detail activity è fatto, va aggiunta logica per passare a quella activity
    }

    void openAccountDetailActivity(Bundle account) {

        Intent intent = new Intent(MainActivity.this, AccountDetailActivity.class);

        // serve per dire all'altra activity che riceve dei dati,
        // in questo caso il bundle dell'account
        intent.putExtras(account);

        // result: ADD_OR_EDIT_CODE, per dire che ho aggiunto o modificato un elemento
        startActivityForResult(intent, Utilities.ADD_OR_EDIT_CODE);

    }

    // per sapere quando torna indietro dall'altra activity (dalla AccountDetail activity)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {

            case Utilities.ADD_OR_EDIT_CODE: {

                if(resultCode == RESULT_OK) {

                    loadAccounts();
                    // TODO un pò inefficiente ricaricare tutti gli account dal db ogni volta
                    // che se ne aggiunge/modifica uno
                    // si potrebbe modificare solo quello, su accounts e sul db
                }
                break;
            }
            default: {

                super.onActivityResult(requestCode, resultCode, data);
            }
        }


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
                mailTV.setText(account.getString(Utilities.MAIL_COL));

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
