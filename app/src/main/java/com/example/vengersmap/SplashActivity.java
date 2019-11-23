package com.example.vengersmap;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Splash page the user will see after login. Has three buttons. One for profile, one for creating a
 * hunt and one for joining a hunt.
 */
public class SplashActivity extends AppCompatActivity {
    AnimatedVectorDrawable d;
    ImageView avd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        d = (AnimatedVectorDrawable) getDrawable(R.drawable.logo);
        avd = (ImageView) findViewById(R.id.animation2);
        avd.setImageDrawable(d);
    }

    /**
     * Button function that takes user to desired activity.
     * @param view
     */
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
    @Override
    protected void onStart() {
        super.onStart();
        d.start();
    }
}
