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
                    Toast.makeText (this, R.string.WRONG_PASSWORD_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }


        } else {

            showLongToast(R.string.PWD_WRONG_LENGTH_MESSAGE);
        }
    }

    private void showShortToast(int textStringId) {

        String stringToShow = getString(textStringId);
        Toast.makeText(this, stringToShow, Toast.LENGTH_SHORT).show();
    }

    private void showLongToast(int textStringId) {

        String stringToShow = getString(textStringId);
        Toast.makeText(this, stringToShow, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // "disabilitiamo" il pulsante indietro, se lo si preme non fa niente (rimuovendo la chiamata a super)
        // in modo che l'utente non torni alla main activity senza loggarsi
    }
}
