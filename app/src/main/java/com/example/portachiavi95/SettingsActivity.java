package com.example.portachiavi95;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    EditText pwdET;
    ImageView togglePwdIV;
    Switch accessSW;

    boolean isPassVisible = false;

    void togglePasswordVisibility() {

        isPassVisible = Utilities.togglePasswordVisibility(isPassVisible, pwdET, togglePwdIV);
    }


    void saveSettings() {
        // TODO salvare, prima fare dei controlli: pwd > 8 caratteri

        if(pwdET.getText().toString().length() >= Utilities.PWD_MIN_LENGTH) {

            Utilities.saveMasterPassword(this, pwdET.getText().toString());
            Utilities.saveAccess(this, accessSW.isChecked());

            Utilities.showLongToast(this, R.string.SETTINGS_SAVED_MESSAGE);

            finish();
        }
        else {
            Utilities.showLongToast(this, R.string.PWD_WRONG_LENGTH_MESSAGE);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pwdET = findViewById(R.id.pwdEditText);
        togglePwdIV = findViewById(R.id.togglePasswordImageView);
        accessSW = findViewById(R.id.toggleAccessSwitch);

        accessSW.setChecked(Utilities.isAccessKept(this));

        pwdET.setText(Utilities.getMasterPassword(this));

        togglePwdIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                togglePasswordVisibility();
            }
        });

        Button saveBtn = findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });
    }
}
