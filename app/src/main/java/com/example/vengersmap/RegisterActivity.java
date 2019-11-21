package com.example.vengersmap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databasePlayer;
    private List<Artifact> artifactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databasePlayer = database.getReference("players");
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        register = findViewById(R.id.registerBut);
        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter an email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword((email.getText().toString()),
                        password.getText().toString())
                        .addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this,
                                            "Registered Successfully",
                                            Toast.LENGTH_LONG).show();
                                    email.setText("");
                                    password.setText("");
                                }else{
                                    Toast.makeText(RegisterActivity.this,
                                            task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }));
            }
        });
    }
    public void goToLogin(View view){
        Intent intent = null;
        if(view.getId()== R.id.goToLoginBut){
            intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
