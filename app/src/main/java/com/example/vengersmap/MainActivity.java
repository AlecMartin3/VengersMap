package com.example.vengersmap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity  extends AppCompatActivity {
    public Button mapBut;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mapBut = (Button) findViewById(R.id.mapBut);
            mapBut.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    setContentView(R.layout.activity_maps);
                }
            });
        }

}
