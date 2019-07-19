package com.example.portachiavi95;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AccountDetailActivity extends AppCompatActivity {

    DBManager dbManager;
    Bundle account;

    EditText descET;
    EditText userET;
    EditText passET;
    EditText mailET;

    boolean isPassVisible = false;
    ImageView togglePwdIV;

    // carico i valori dell'account nell'EditText
    void loadAccountData() {

        if (!account.isEmpty()) {

            this.descET.setText(account.getString("descrizione"));
            this.userET.setText(account.getString("username"));
            this.passET.setText(account.getString("password"));
            this.mailET.setText(account.getString(Utilities.MAIL_COL));
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

        TextView titleTV = findViewById(R.id.titleTextView);

        // recupera l'intent con cui sono arrivato a questa activity
        // e ottiene gli extras passati all'intent
        this.account = getIntent().getExtras();

        Boolean nuovoAccount = account.isEmpty();
        if(nuovoAccount) {

            titleTV.setText(getString(R.string.NUOVO_ACCOUNT));
        }
        else {
            titleTV.setText(getString(R.string.EDIT_ACCOUNT, account.getString("descrizione")));
        }

        this.dbManager = new DBManager(this, DBManager.DATABASE_NAME, null, DBManager.DATABASE_VERSION);

        Button saveBtn = findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO controllare input utente

                // verificare sela chiamata all'inflater getsystemservice funziona (cambiata per compatibilità API < 23)
                saveAccountData();
            }
        });

        loadAccountData();

        togglePwdIV = findViewById(R.id.togglePasswordImageView);
        togglePwdIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                togglePasswordVisibility();
            }
        });
    }

    void saveAccountData() {

        // controllare se id esiste già, se no si crea. Usere db manager update o insert new account
        Boolean isNewAccount = account.isEmpty();

        account.putString("descrizione",  descET.getText().toString());
        account.putString("username",  userET.getText().toString());
        account.putString("password",  passET.getText().toString());
        account.putString(Utilities.MAIL_COL,  mailET.getText().toString());

        if(isNewAccount) {

            dbManager.insertNewAccount(account);
        }
        else {
            dbManager.updateAccount(account);
        }

        Toast.makeText(this, R.string.SAVED_MESSAGE, Toast.LENGTH_SHORT);

        setResult(RESULT_OK);

        finish();
    }

    void togglePasswordVisibility() {

        isPassVisible = Utilities.togglePasswordVisibility(isPassVisible, passET, togglePwdIV);
    }
}
