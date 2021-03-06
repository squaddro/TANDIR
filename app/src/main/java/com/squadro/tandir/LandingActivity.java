package com.squadro.tandir;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {

    private Button myRecipesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        myRecipesButton = (Button) findViewById(R.id.MyRecipesButton);
        myRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.MyRecipesButton) {
                    openRecipePageActivity();
                }else if (v.getId() == R.id.profileButton) {
                    // startActivity(new Intent(this, .class));
                }
            }
        });

        Button signOut = (Button) findViewById(R.id.signOutButton);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, MainActivity.class));
            }
        });

        Button myProfile = (Button) findViewById(R.id.profileButton);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, ProfilePageActivity.class));
            }
        });
    }




    public void openRecipePageActivity(){
        Intent intent = new Intent(this, RecipePageActivity.class);
        startActivity(intent);

    }

    public void openProfilePageActivity(){
       // Intent intent = new Intent(this, ProfilePageActivity.class);
      //  startActivity(intent);
    }

}
