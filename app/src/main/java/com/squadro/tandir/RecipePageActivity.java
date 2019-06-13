package com.squadro.tandir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecipePageActivity extends AppCompatActivity {

    private String recipe_id=null;
    private String recipe_name;
    private String recipe_desc;
    private String user_id=null;
    private String user_name=null;

    private RestAPI rest;
    private RetrofitCreate rc;
    private Retrofit retrofit;

    private TextView recipeIds = findViewById(R.id.textViewResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        final EditText editTextRecipeName = (EditText) findViewById(R.id.editTextRecipeName);
        editTextRecipeName.setHint("Enter Recipe Name...");
        final EditText editTextRecipeDesc = (EditText) findViewById(R.id.editTextRecipeDesc);
        editTextRecipeDesc.setHint("Enter Recipe Description...");

        getRecipeIds();

        Button button = (Button)findViewById(R.id.RecipeAddButton);

        button.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        recipe_name = editTextRecipeName.getText().toString();
                        recipe_desc = editTextRecipeDesc.getText().toString();
                        addRecipe();
                    }
                });
    }


    private void addRecipe(){

        rc = new RetrofitCreate();
        retrofit = rc.createRetrofit();

        rest = retrofit.create(RestAPI.class);

        JsonObject obj = new JsonObject();
        obj.addProperty("recipe_name",recipe_name);
        obj.addProperty("recipe_desc",recipe_desc);
        obj.addProperty("user_id",user_id);
        obj.addProperty("recipe_id",recipe_id);

        Call<JsonObject> call = rest.addRecipe(obj);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Status status = new Status(response.body().get("status").getAsInt(),
                        response.body().get("message").toString());

                Toast.makeText(getBaseContext(),status.toString(),Toast.LENGTH_LONG).show();

                //TODO
                //if status is blabla
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Error!",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getRecipeIds(){

        rc = new RetrofitCreate();
        retrofit = rc.createRetrofit();

        rest = retrofit.create(RestAPI.class);

        JsonObject obj = new JsonObject();
        obj.addProperty("user_name",user_name);

        Call<List<JsonObject>> call = rest.getRecipeIds(user_name);

        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                
                List<JsonObject> resultList = response.body();
                System.out.println(resultList.size());

                for(int i=0;i<resultList.size();i++){

                    JsonObject j = resultList.get(i);
                    System.out.println(j.getAsInt());
                }

                for(JsonObject j : resultList){

                    String content = "";
                    content += "recipe_id= " + j.getAsInt();
                    recipeIds.append(content);
                }


            //    Toast.makeText(getBaseContext(),status.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });



    }

}
