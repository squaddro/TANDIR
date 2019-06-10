package com.squadro.tandir;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCreate {


    public Retrofit createRetrofit() {

         Retrofit retrofit = null;

        retrofit = new Retrofit.Builder()
                // .baseUrl("http://jsonplaceholder.typicode.com/")
                .baseUrl("https://tandir.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
