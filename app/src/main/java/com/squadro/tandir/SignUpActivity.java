package com.squadro.tandir;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SignUpActivity extends AppCompatActivity {

    private String user_name=null;
    private String password=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        editTextName.setHint("Enter User Name...");
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword.setHint("Enter password...");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void sendUserData(){

        RetrofitCreate rc = new RetrofitCreate();
        Retrofit retrofit = rc.createRetrofit();

        RestAPI rest = retrofit.create(RestAPI.class);

        Call<SignInUp> call = rest.saveSign(user_name,password);

        call.enqueue(new Callback<SignInUp>() {
            @Override
            public void onResponse(Call<SignInUp> call, Response<SignInUp> response) {

            }

            @Override
            public void onFailure(Call<SignInUp> call, Throwable t) {

            }
        });


    }

}
