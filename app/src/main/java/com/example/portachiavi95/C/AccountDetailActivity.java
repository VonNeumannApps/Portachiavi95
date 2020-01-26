package com.example.portachiavi95.C;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.portachiavi95.DBManager;
import com.example.portachiavi95.R;
import com.example.portachiavi95.Utils;

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

            this.descET.setText(account.getString(DBManager.DESCRIPTION_COL));
            this.userET.setText(account.getString(DBManager.USERNAME_COL));
            this.passET.setText(account.getString(DBManager.PASSWORD_COL));
            this.mailET.setText(account.getString(DBManager.MAIL_COL));
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
            titleTV.setText(getString(R.string.EDIT_ACCOUNT,
                    account.getString(DBManager.DESCRIPTION_COL)));
        }

        this.dbManager = new DBManager(this, DBManager.DATABASE_NAME,
                null, DBManager.DATABASE_VERSION);

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

        account.putString(DBManager.DESCRIPTION_COL, descET.getText().toString());
        account.putString(DBManager.USERNAME_COL, userET.getText().toString());
        account.putString(DBManager.PASSWORD_COL, passET.getText().toString());
        account.putString(DBManager.MAIL_COL, mailET.getText().toString());

        if(isNewAccount) {

            dbManager.insertNewAccount(account);
        }
        else {
            dbManager.updateAccount(account);
        }

        Utils.showShortToast(this, R.string.SAVED_MESSAGE);

        setResult(RESULT_OK);

        finish();
    }

    void togglePasswordVisibility() {

        isPassVisible = Utils.togglePasswordVisibility(isPassVisible, passET, togglePwdIV);
    }
}
