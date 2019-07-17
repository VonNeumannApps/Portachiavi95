package com.example.portachiavi95;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // salviamo la psw nelle shared preferences
        // NB. quando si disinstalla la app il file viene eliminato
        if(!isLogged()){

            //mi devo loggare
            openLoginActivity();
        }

        // TODO: layout account detail activity è fatto, va aggiunta logica per passare a quella activity
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
