package com.example.vengersmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

public class AddArtifact extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private int position;
    private EditText etArtName;
    private Artifact a;
    private String name = " ";


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact);
        position = getIntent().getExtras().getInt("position");
//        a = (Artifact)getIntent().getSerializableExtra("artifact");
//        artName = findViewById(R.id.etArtifactName);
//        name = artName.getText().toString();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    public void back(View view){
        etArtName = findViewById(R.id.etArtifactName);
        name = etArtName.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
