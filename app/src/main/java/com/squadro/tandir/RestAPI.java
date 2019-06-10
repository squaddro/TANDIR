package com.squadro.tandir;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface RestAPI {
    @Headers({
            "Accept: application/json"
    })
    @POST("/signup")
    @FormUrlEncoded
    Call<SignInUp> saveSign(@Field("user_name") String user_name,
                        @Field("password") String password);
}
