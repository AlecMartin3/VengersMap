package com.example.vengersmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    public Button mapBut;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
        }
        public void login(View view){
        Intent intent = null;
        if(view.getId()== R.id.launchMap){
            intent = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        }

}
