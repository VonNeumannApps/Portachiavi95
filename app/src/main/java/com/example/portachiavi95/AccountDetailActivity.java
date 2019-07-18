package com.example.portachiavi95;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountDetailActivity extends AppCompatActivity {

    DBManager dbManager;
    Bundle account;

    EditText descET;
    EditText userET;
    EditText passET;
    EditText mailET;

    // carico i valori dell'account nell'EditText
    void loadAccountData() {

        if (!account.isEmpty()) {

            this.descET.setText(account.getString("descrizione"));
            this.userET.setText(account.getString("username"));
            this.passET.setText(account.getString("password"));
            this.mailET.setText(account.getString("mail"));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        // inizializzo campi EditText dell'account
        descET = findViewById(R.id.descriptionEditText);
        userET = findViewById(R.id.usernameEditText);
        passET = findViewById(R.id.passwordEditText);
        mailET = findViewById(R.id.emailEditText);

        // recupera l'intent con cui sono arrivato a questa activity
        // e ottiene gli extras passati all'intent
        this.account = getIntent().getExtras();

        this.dbManager = new DBManager(this, DBManager.DATABASE_NAME, null, DBManager.DATABASE_VERSION);

        Button saveBtn = findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO salvare/controllare input utente

                // verificare sela chiamata all'inflater getsystemservice funziona (cambiata per compatibilità API < 23)

            }
        });

        loadAccountData();
    }

    void saveAccountData() {

        // controllare se id esiste già, se no si crea. Usere db manager update o insert new account
        Boolean isNewAccount = account.isEmpty();

        account.putString("descrizione",  descET.getText().toString());
        account.putString("username",  userET.getText().toString());
        account.putString("password",  passET.getText().toString());
        account.putString("email",  mailET.getText().toString());

        if(isNewAccount) {

            dbManager.insertNewAccount(account);
        }
        else {
            dbManager.updateAccount(account);
        }


    }
}
