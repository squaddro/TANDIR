package com.squadro.tandir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClick(View view) {
        //Toast.makeText(this,"HELLOOOO",Toast.LENGTH_LONG).show();
        if (view.getId() == R.id.button_signin) {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        }else if (view.getId() == R.id.button_signup) {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        }
    }
}
