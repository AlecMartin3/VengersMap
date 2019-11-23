package com.example.vengersmap;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A class that checks if a user is logged in. If they are then it sends them to the splash page,
 * else it sends them to the startup page.
 */
public class StayLoggedIn extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();

        if(firebaseuser != null){
            Intent intent = new Intent(StayLoggedIn.this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
