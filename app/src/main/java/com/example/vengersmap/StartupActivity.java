package com.example.vengersmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity {
    public Button login;
    public Button register;
    FirebaseAuth firebaseAuth;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_startup);
            firebaseAuth = FirebaseAuth.getInstance();
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
            if(view.getId()== R.id.launchMap){
                intent = new Intent(StartupActivity.this, CreateAHuntActivity.class);
                startActivity(intent);
            }

        }

}
