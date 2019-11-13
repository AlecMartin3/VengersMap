package com.example.vengersmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtifactListActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference databaseArtifact;
    private ListView lvArtifact;
    private ArrayList<Artifact> ArtifactList;
    public SupportMapFragment mapFragment;
    private String id;
    private GoogleMap mMap;
    private LatLng cLoc;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_list);
        id = getIntent().getExtras().getString("StringID");
        databaseArtifact = FirebaseDatabase.getInstance().getReference("hunts").child(id);
        lvArtifact= findViewById(R.id.lvArtifacts);
        ArtifactList = new ArrayList<Artifact>();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        System.out.println(location);
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            System.out.println(location);
                        }
                    }
                });
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseArtifact.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArtifactList.clear();
                    for (DataSnapshot CountSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot NameSnapshot : CountSnapshot.getChildren()) {
                            Artifact Artifact = NameSnapshot.getValue(Artifact.class);
                            Artifact.setArtName(NameSnapshot.child("artName").getValue().toString());
                            ArtifactList.add(Artifact);
                        }
                    }

                ArtifactAdapter adapter = new ArtifactAdapter(ArtifactListActivity.this, ArtifactList);
                lvArtifact.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("**********************TEST******************");

    }
}
