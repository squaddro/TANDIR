package com.squadro.tandir;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private String recipe_date = null;
    static int takeFlags=0;
    private RestAPI rest;
    private RetrofitCreate rc;
    private Retrofit retrofit;
    protected static ArrayList<String> photoIds = null;

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
        final Button addRecipe = (Button) findViewById(R.id.addRecipe);
        pickPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecipe.setEnabled(false);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);


                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
            }
        });



        addRecipe.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                            recipe_name = editTextRecipeName.getText().toString();
                            recipe_desc = editTextRecipeDesc.getText().toString();
                            tag = (editTextTag.getText().toString());
                            Date date = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            recipe_date = formatter.format(date);

                            addRecipe();
                            finish();
                            startActivity(getIntent());
                        startActivity(new Intent(AddRecipeActivity.this, RecipePageActivity.class));
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)  {

        uriList = new ArrayList<String>();

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_MULTIPLE) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    Uri imageUri = null;

                    for(int i = 0; i < count; i++) {
                        imageUri = data.getClipData().getItemAt(i).getUri();


                        Bitmap bitmap = null;
                        try{
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        }catch(Exception e){
                            e.printStackTrace();
                        }


                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();
                        String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        uriList.add(encoded);


                    }

                }

            }
        }
        addPhotos();
    }

    public void addPhotos(){
        photoIds = new ArrayList<>();
        rc = new RetrofitCreate();
        retrofit = rc.createRetrofit();

        rest = retrofit.create(RestAPI.class);

        final AtomicInteger counter = new AtomicInteger();

        for(String s : uriList){

            JsonObject obj = new JsonObject();

            obj.addProperty("payload",s);

            Call call = rest.uploadPhoto(obj);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    String id = response.body().get("message").getAsString();
                    photoIds.add(id);
                    if(counter.incrementAndGet()>= uriList.size()){
                        Button button = (Button)findViewById(R.id.addRecipe);
                        button.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

        }

    }
    private void addRecipe(){


        rc = new RetrofitCreate();
        retrofit = rc.createRetrofit();

        rest = retrofit.create(RestAPI.class);
        JsonObject obj = new JsonObject();

        JsonArray uriArray = new JsonArray();
        for(String s : photoIds) {
            uriArray.add(s);
        }

        obj.addProperty("recipe_name",recipe_name);
        obj.addProperty("recipe_desc",recipe_desc);
        obj.addProperty("user_id",user_id);
        obj.addProperty("recipe_id",recipe_id);
        obj.addProperty("tag",tag);
        obj.add("uris",uriArray);
        obj.addProperty("recipe_date",recipe_date);

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
}

