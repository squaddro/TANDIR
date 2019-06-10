package com.squadro.tandir;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface RestAPI {
    @Headers({
            "Accept: application/json"
    })
    @POST("signup")
    Call<JsonObject> saveSign(@Body JsonObject body);

    @Headers({
            "Accept: application/json"
    })
    @POST("signin")
    Call<JsonObject> getSign(@Body JsonObject body);
}
