package com.example.portachiavi95;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText pwdEditText;

    void saveMasterPassword(){

        if (pwdEditText.getText().toString().length() >= Utilities.PWD_MIN_LENGTH){

            SharedPreferences userData = getSharedPreferences(getString(R.string.USER_DATA), MODE_PRIVATE);

            userData.edit().putString("masterPassword", pwdEditText.getText().toString()).commit();

            Toast.makeText (this,R.string.SAVED_MESSAGE,Toast.LENGTH_SHORT).show();

            finish();
        } else  Toast.makeText (this,R.string.PWD_WRONG_LENGTH_MESSAGE,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
