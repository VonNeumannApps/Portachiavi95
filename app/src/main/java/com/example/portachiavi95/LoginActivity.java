package com.example.portachiavi95;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText pwdEditText;

    void checkForMasterPassword(){

        String userInsertedPwd = pwdEditText.getText().toString();

        if (userInsertedPwd.length() >= Utilities.PWD_MIN_LENGTH){

            // controllare se esiste master pwd , se non esiste salvarla e fare login
            // se esiste, fare direttamente login
            if(!Utilities.isMasterPasswordSet(this)) {

                // devo salvarla
                Utilities.saveMasterPassword(this, userInsertedPwd);

                Toast.makeText (this,R.string.SAVED_MESSAGE,Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
                finish();
            }
            else {

                if(Utilities.isMasterPasswordCorrect(this, userInsertedPwd)) {

                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    // msg credenziali sbagliate, resto su questa activity
                }
            }


            finish();// ritorna alla activity precedente

        } else {

            Toast.makeText(this, R.string.PWD_WRONG_LENGTH_MESSAGE, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pwdEditText = findViewById(R.id.pwdEditText);


        Button loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForMasterPassword();
            }
        });
    }
}
