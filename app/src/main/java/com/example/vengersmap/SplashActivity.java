package com.example.vengersmap;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void ProfileCreateJoin(View view){
        Intent intent = null;
        if(view.getId()== R.id.launchProfile){
            intent = new Intent(SplashActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        if(view.getId()== R.id.launchCreateAHunt){
            intent = new Intent(SplashActivity.this, CreateAHuntActivity.class);
            startActivity(intent);
        }
        if(view.getId()== R.id.launchJoinAHunt){
            intent = new Intent(SplashActivity.this, LobbyListActivity.class);
            startActivity(intent);
        }
    }
}