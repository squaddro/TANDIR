package com.squadro.tandir;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
