package com.squadro.tandir;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;


public interface RestAPI {
    @Headers({
            "Accept: application/json"
    })
    @GET("greeting")
    Call<UserData> getJoke();
}
