package com.example.vengersmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button login;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.loginBut);


        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                firebaseAuth.signInWithEmailAndPassword((email.getText().toString()),
                        password.getText().toString())
                        .addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(LoginActivity.this,
                                            SplashActivity.class));
                                }else{
                                    Toast.makeText(LoginActivity.this,
                                            task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }));
            }
        });
    }
    public void goToRegister(View view){
        Intent intent = null;
        if(view.getId()== R.id.goToRegisterBut){
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
