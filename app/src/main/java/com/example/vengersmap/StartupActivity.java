package com.example.vengersmap;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        d.start();
    }

}
