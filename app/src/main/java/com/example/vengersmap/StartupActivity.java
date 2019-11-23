package com.example.vengersmap;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * The first activity that is started if a user is not logged in. If a user were to be logged
 * in then the stayLoggedIn class will handle that and this activity will not be used on startup.
 * However, if no user is logged in then this will be the first activity displayed.
 *
 * This activity is used to link to the register and log in activities.
 */
public class StartupActivity extends AppCompatActivity {
    public Button login;
    public Button register;
    FirebaseAuth firebaseAuth;
    AnimatedVectorDrawable d;
    ImageView avd;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_startup);
            firebaseAuth = FirebaseAuth.getInstance();
            d = (AnimatedVectorDrawable) getDrawable(R.drawable.logo);
            avd = (ImageView) findViewById(R.id.animation);
            avd.setImageDrawable(d);
        }

    /**
     * Sends the user to the login or register activity based on the button that is clicked.
     * @param view
     */
    public void LoginRegister(View view){
        Intent intent = null;
            if(view.getId()== R.id.launchLogin){
                intent = new Intent(StartupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            if(view.getId()== R.id.launchRegister){
                intent = new Intent(StartupActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        }

    /**
     * Starts the animation of the N in Vengers.
     */
    @Override
    protected void onStart() {
        super.onStart();
        d.start();
    }

}
