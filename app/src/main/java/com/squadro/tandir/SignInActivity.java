package com.squadro.tandir;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        editTextName.setHint("Enter User Name...");
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword.setHint("Enter password...");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }
}
