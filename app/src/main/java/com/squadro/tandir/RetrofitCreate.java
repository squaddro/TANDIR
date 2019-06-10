package com.squadro.tandir;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitCreate {


    public Retrofit createRetrofit() {

         Retrofit retrofit = null;

        retrofit = new Retrofit.Builder()
                // .baseUrl("http://jsonplaceholder.typicode.com/")
                .baseUrl("https://tandir.herokuapp.com/")
           //     .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
