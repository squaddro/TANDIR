package com.squadro.tandir;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

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

    private TextView recipeIds = null;

    private Recipe[] recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

    //    final EditText editTextRecipeName = (EditText) findViewById(R.id.editTextRecipeName);
    //    editTextRecipeName.setHint("Enter Recipe Name...");
    //    final EditText editTextRecipeDesc = (EditText) findViewById(R.id.editTextRecipeDesc);
    //    editTextRecipeDesc.setHint("Enter Recipe Description...");
    //      recipeIds = findViewById(R.id.textViewResult);

        user_name = SignInActivity.user_name;
        getRecipeIds();

/*
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
                */
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

        Call<JsonObject> call = rest.getRecipeIds(user_name);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Gson gson= new Gson();

                JsonObject resultList = response.body();
                JsonElement element = resultList.get("recipes");
                JsonArray array = element.getAsJsonArray();

                recipes = new Recipe[array.size()];

                for(int i=0;i<array.size();i++){
                    Recipe obj = gson.fromJson(array.get(i).toString(),Recipe.class);
                    recipes[i] = obj;
                }
                makeList();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    public void makeList(){

        final String names[] = new String[recipes.length];
        for(int i=0;i<recipes.length;i++){
            names[i]=recipes[i].getRecipe_name();
        }

        ListView listView = (ListView)findViewById(R.id.listView1);
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, names);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                AlertDialog.Builder desc =
                        new AlertDialog.Builder(RecipePageActivity.this);

                desc.setMessage(recipes[position].getRecipe_desc())
                        .setCancelable(true);

                desc.create().show();
            }
        });
    }

}
