package com.example.portachiavi95;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountDetailActivity extends AppCompatActivity {

    DBManager dbManager;
    Bundle account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        // recupera l'intent con cui sono arrivato a questa activity
        // e ottiene gli extras passati all'intent
        this.account = getIntent().getExtras();

        this.dbManager = new DBManager(this, DBManager.DATABASE_NAME, null, DBManager.DATABASE_VERSION);

        Button saveBtn = findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO salvare/controllare input utente
            }
        });
    }
}
