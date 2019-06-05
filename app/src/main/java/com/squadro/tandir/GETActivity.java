package com.squadro.tandir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GETActivity extends AppCompatActivity {


    private Retrofit retrofit = null;
    private RestAPI rest = null;
    private Call<UserData> call = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        createRetrofit();


    }

    public void createRetrofit() {

        final TextView textView = (TextView) findViewById(R.id.textViewGet);

        retrofit = new Retrofit.Builder()
                // .baseUrl("http://jsonplaceholder.typicode.com/")
                .baseUrl("https://tandir.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        rest = retrofit.create(RestAPI.class);


        call = rest.getJoke();

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {

                if (!response.isSuccessful()) {
                    textView.setText("Code is : " + response.code());
                    return;
                } else {
                    UserData data = response.body();

                    String s = "ID : " + data.getId() + "\n" +
                            "content : " + data.getContent() + "\n" +
                            "joke id : " + data.getJoke().getId() + "\n" +
                            "joke : " + data.getJoke().getJoke() + "\n" +
                            "status : " + data.getJoke().getStatus() + "\n\n";
                    textView.append(s);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
}
