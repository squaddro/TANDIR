package com.squadro.tandir;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

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

        final EditText editTextName = (EditText) findViewById(R.id.editTextName);
        editTextName.setHint("Enter User Name...");
        final EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword.setHint("Enter password...");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button button = (Button)findViewById(R.id.SignUpButton);

        button.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        user_name = editTextName.getText().toString();
                        password = editTextPassword.getText().toString();
                        sendUserData();
                    }
                });
    }

    public void sendUserData(){

        RetrofitCreate rc = new RetrofitCreate();
        Retrofit retrofit = rc.createRetrofit();

        RestAPI rest = retrofit.create(RestAPI.class);

        JsonObject obj = new JsonObject();
        obj.addProperty("user_name",user_name);
        obj.addProperty("password",password);
        Call<JsonObject> call = rest.saveSign(obj);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Status status = new Status(response.body().get("status").getAsInt(),
                        response.body().get("message").toString());
                Toast.makeText(getBaseContext(),status.toString(),Toast.LENGTH_LONG).show();
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Error!",Toast.LENGTH_LONG).show();
            }
        });
    }
}
