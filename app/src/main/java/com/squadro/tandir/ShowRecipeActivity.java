package com.squadro.tandir;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowRecipeActivity extends AppCompatActivity {

    private RestAPI rest;
    private RetrofitCreate rc;
    private Retrofit retrofit;
    private String recipe_id=null;
    private String recipe_name;
    private String recipe_desc;
    private String user_id=null;
    private String user_name=null;
    private String  tag = null;
    private LayoutInflater layoutInflater;
    private Recipe[] recipes;
    private String srcBaseUrl = "https://tandir.herokuapp.com/image/";
    private static ArrayList<String> photoIds = AddRecipeActivity.photoIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);

        this.getIntent().setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        user_name = SignInActivity.user_name;
        layoutInflater = LayoutInflater.from(this);


                getRecipe();


    }


    private void getRecipe() {



            rc = new RetrofitCreate();
            retrofit = rc.createRetrofit();

            rest = retrofit.create(RestAPI.class);

            Call<JsonObject> call = rest.getRecipeIds(user_name);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {



                            Gson gson = new Gson();

                            JsonObject resultList = response.body();
                            JsonElement element = resultList.get("recipes");
                            JsonArray array = element.getAsJsonArray();

                            recipes = new Recipe[array.size()];

                            for (int i = 0; i < array.size(); i++) {
                                Recipe obj = gson.fromJson(array.get(i).toString(), Recipe.class);
                                recipes[i] = obj;
                            }

                            final EditText editTextRecipeName = (EditText) findViewById(R.id.titleText);
                            editTextRecipeName.setText(recipes[RecipePageActivity.recipeNumber].getRecipe_name());

                            editTextRecipeName.setFocusableInTouchMode(false);
                            editTextRecipeName.clearFocus();
                            final EditText editTextRecipeDesc = (EditText) findViewById(R.id.descText);
                            editTextRecipeDesc.setText(recipes[RecipePageActivity.recipeNumber].getRecipe_desc());

                            editTextRecipeDesc.setFocusableInTouchMode(false);
                            editTextRecipeDesc.clearFocus();
                            final EditText editTextTag = (EditText) findViewById(R.id.tagText);
                            editTextTag.setText(recipes[RecipePageActivity.recipeNumber].getTag());

                            editTextTag.setFocusableInTouchMode(false);
                            editTextTag.clearFocus();

                            String[] uriArray = recipes[RecipePageActivity.recipeNumber].getURIs();

                            for (int i = 0; i < uriArray.length; i++) {

                                rc = new RetrofitCreate();
                                retrofit = rc.createRetrofit();

                                rest = retrofit.create(RestAPI.class);

                                Call<JsonObject> call2 = rest.getImage(uriArray[i]);

                               call2.enqueue(new Callback<JsonObject>() {
                                   @Override
                                   public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                      String base64 =  response.body().get("payload").getAsString();

                                       byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
                                       Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                       LinearLayout linearLayout = (LinearLayout) findViewById(R.id.gallery2);
                                       View view = layoutInflater.inflate(R.layout.gallery_item,
                                               linearLayout, false);


                                       ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                                       imageView.setImageBitmap(decodedByte);
                                       linearLayout.addView(view);
                                   }

                                   @Override
                                   public void onFailure(Call<JsonObject> call, Throwable t) {

                                   }
                               });





                            }
                        }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
                    });


        }

}
