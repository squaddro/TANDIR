package com.squadro.tandir;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;

public class MainActivity extends AppCompatActivity {


    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();


    }
    @Override
    protected void onResume(){
        super.onResume();
        CookieMethods.cleanCookies();

    }




    public void onClick(View view) {

        if (view.getId() == R.id.button_signin) {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        }else if (view.getId() == R.id.button_signup) {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        }
    }
}
