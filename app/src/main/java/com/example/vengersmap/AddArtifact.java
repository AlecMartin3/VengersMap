package com.example.vengersmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class AddArtifact extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private int position;
    private EditText etArtName;
    private String name = " ";
    private String park;
    private ArrayList<locations> artLocations;
    public SupportMapFragment mapFragment;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact);

        position = getIntent().getExtras().getInt("position");
        park = getIntent().getExtras().getString("park");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    public void createLocations(){
        if(park.equals("Strathchona Park")){
            locations strath1 = new locations();
            strath1.setName("Point 1");
            strath1.setX(49.274795);
            strath1.setY(-123.086702);

            locations strath2 = new locations();
            strath2.setName("Point 2");
            strath2.setX(49.276052);
            strath2.setY(-123.087088);

            artLocations.add(strath1);
            artLocations.add(strath2);
        }
        if(park.equals("Jericho Beach Park")){

        }
        if(park.equals("Musqueam Park")){

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(park.equals("Strathcona Park")){
            System.out.println(park);
            LatLng parkLoc = new LatLng(49.274959, -123.085946);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkLoc, 16));
        }

        if(park.equals("Jericho Beach Park")){
            System.out.println(park);
            LatLng parkLoc = new LatLng( 49.271520, -123.197868);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkLoc, 15));
        }

        if(park.equals("Musqueam Park")){
            System.out.println(park);
            LatLng parkLoc = new LatLng(49.229412, -123.190269);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkLoc, 16));
        }

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
