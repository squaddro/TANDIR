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
import retrofit2.http.Path;


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

    @POST("addrecipe")
    Call<JsonObject> addRecipe(@Body JsonObject body);

    @GET("user/{user_name}")
    Call<JsonObject> getRecipeIds(@Path("user_name") String userName );

    @GET("recipe/{recipe_id}")
    Call<JsonObject> getRecipe(@Path("recipe_id") int recipeId);

    @POST("deleterecipe")
    Call<JsonObject> deleteRecipe(@Body JsonObject body);

    @POST("updaterecipe")
    Call<JsonObject> updateRecipe(@Body JsonObject body);
}
