package com.squadro.tandir;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AddRecipeActivity extends AppCompatActivity {

    int PICK_IMAGE_MULTIPLE = 1;

    private LayoutInflater layoutInflater;

    private String recipe_id=null;
    private String recipe_name;
    private String recipe_desc;
    private String user_id=null;
    private String user_name=null;
    private String  tag = null;
    static int takeFlags=0;
    private RestAPI rest;
    private RetrofitCreate rc;
    private Retrofit retrofit;

    private ArrayList<String> uriList = null;

    private Recipe[] recipes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        layoutInflater = LayoutInflater.from(this);

        final EditText editTextRecipeName = (EditText) findViewById(R.id.titleText);
        editTextRecipeName.setHint("Enter Recipe Name...");
        final EditText editTextRecipeDesc = (EditText) findViewById(R.id.descText);
        editTextRecipeDesc.setHint("Enter Recipe Description...");
        final EditText editTextTag = (EditText) findViewById(R.id.tagText);
        editTextTag.setHint("Enter Tag...");


        Button pickPhotos = (Button)findViewById(R.id.pickPhotos);

        pickPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);


                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        Button addRecipe = (Button) findViewById(R.id.addRecipe);

        addRecipe.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                            recipe_name = editTextRecipeName.getText().toString();
                            recipe_desc = editTextRecipeDesc.getText().toString();
                            tag = (editTextTag.getText().toString());
                            addRecipe();
                            finish();
                            startActivity(getIntent());
                        startActivity(new Intent(AddRecipeActivity.this, RecipePageActivity.class));
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        uriList = new ArrayList<String>();

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_MULTIPLE) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    Uri imageUri = null;

                    for(int i = 0; i < count; i++) {
                        imageUri = data.getClipData().getItemAt(i).getUri();



                        uriList.add(imageUri.toString());
                    }

                }

            }
        }
    }

    private void addRecipe(){

        rc = new RetrofitCreate();
        retrofit = rc.createRetrofit();

        rest = retrofit.create(RestAPI.class);
        JsonObject obj = new JsonObject();

        JsonArray uriArray = new JsonArray();

        for(String uri : uriList)
            uriArray.add(uri);

        if(uriList.size()==0){
            String nullString =null;
            obj.addProperty("uris",nullString);
        }

        else{
            obj.add("uris", uriArray);
        }

        obj.addProperty("recipe_name",recipe_name);
        obj.addProperty("recipe_desc",recipe_desc);
        obj.addProperty("user_id",user_id);
        obj.addProperty("recipe_id",recipe_id);
        obj.addProperty("tag",tag);


        Call<JsonObject> call = rest.addRecipe(obj);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Status status = new Status(response.body().get("status").getAsInt(),
                        response.body().get("message").toString());

                Toast.makeText(getBaseContext(),status.getMessage(),Toast.LENGTH_LONG).show();



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
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, names){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                // Set the typeface/font for the current item


                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#000000"));

                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.BOLD);


                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);

                ViewGroup.LayoutParams layoutparams = item.getLayoutParams();
                layoutparams.height = 170;

                item.setLayoutParams(layoutparams);


                // return the view
                return item;
            }
        };
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {


                AlertDialog.Builder desc =
                        new AlertDialog.Builder(AddRecipeActivity.this);

                desc.setMessage(recipes[position].getRecipe_desc())
                        .setCancelable(true);

                desc.setNeutralButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                final String recipeId = recipes[position].getRecipe_id();
                                deleteRecipe(recipeId);
                                finish();
                                startActivity(getIntent());

                                dialog.cancel();
                            }
                        });

                desc.setNegativeButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                recipe_id = recipes[position].getRecipe_id();
                                recipe_name = recipes[position].getRecipe_name();
                                recipe_desc = recipes[position].getRecipe_desc();
                                tag = (recipes[position].getTag());

                                AlertDialog.Builder updateAlert =
                                        new AlertDialog.Builder(AddRecipeActivity.this);



                                final EditText inputName = new EditText(AddRecipeActivity.this);
                                final EditText inputDesc = new EditText(AddRecipeActivity.this);
                                final EditText inputTag = new EditText(AddRecipeActivity.this);


                                inputName.setText(recipe_name);
                                inputDesc.setText(recipe_desc);
                                inputTag.setText(tag);

                                Context context = updateAlert.getContext();
                                LinearLayout layout = new LinearLayout(context);

                                layout.setOrientation(LinearLayout.VERTICAL);



                                inputName.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE
                                        | InputType.TYPE_TEXT_VARIATION_NORMAL);
                                layout.addView(inputName);

                                inputDesc.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE
                                        |InputType.TYPE_TEXT_VARIATION_NORMAL);
                                layout.addView(inputDesc);

                                inputTag.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE
                                        |InputType.TYPE_TEXT_VARIATION_NORMAL);
                                layout.addView(inputTag);

                                updateAlert.setView(layout);

                                updateAlert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String updatedName = inputName.getText().toString();
                                        String updatedDesc = inputDesc.getText().toString();
                                        String updatedTag = inputTag.getText().toString();

                                        if(updatedName.equals(recipe_name)){
                                            updatedName = null;
                                        }
                                        if(updatedDesc.equals(recipe_desc)){
                                            updatedDesc = null;
                                        }

                                        if(updatedTag.equals(tag)){
                                            updatedTag = null;
                                        }

                                        updateRecipe(recipes[position].getRecipe_id(), updatedName, updatedDesc, updatedTag);
                                        finish();
                                        startActivity(getIntent());

                                    }
                                });

                                updateAlert.create().show();
                                //  ((Dialog)dialog).getWindow().setLayout(600,800);
                                //dialog.cancel();
                            }
                        });
                desc.create().show();
            }
        });
    }

    private void updateRecipe(String recipe_id, String updatedName, String updatedDesc, String updatedTag) {

        rc = new RetrofitCreate();
        retrofit = rc.createRetrofit();

        rest = retrofit.create(RestAPI.class);
        JsonObject obj = new JsonObject();
        obj.addProperty("recipe_name",updatedName);
        obj.addProperty("recipe_desc",updatedDesc);
        obj.addProperty("recipe_id",recipe_id);
        obj.addProperty("tag", updatedTag);

        Call<JsonObject> call = rest.updateRecipe(obj);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Status status = new Status(response.body().get("status").getAsInt(),
                        response.body().get("message").toString());

                Toast.makeText(getBaseContext(),status.getMessage(),Toast.LENGTH_LONG).show();

                //TODO
                //if status is blabla

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Error!",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void deleteRecipe(String recipeId){

        recipe_desc = null;
        recipe_name = null;
        user_id = null;
        tag = null;

        rc = new RetrofitCreate();
        retrofit = rc.createRetrofit();

        rest = retrofit.create(RestAPI.class);
        JsonObject obj = new JsonObject();
        obj.addProperty("recipe_name",recipe_name);
        obj.addProperty("recipe_desc",recipe_desc);
        obj.addProperty("user_id",user_id);
        obj.addProperty("recipe_id",recipeId);
        obj.addProperty("tag", tag);

        Call<JsonObject> call = rest.deleteRecipe(obj);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Status status = new Status(response.body().get("status").getAsInt(),
                        response.body().get("message").toString());

                Toast.makeText(getBaseContext(),status.getMessage(),Toast.LENGTH_LONG).show();

                //TODO
                //if status is blabla
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Error!",Toast.LENGTH_LONG).show();
            }
        });



    }



}

