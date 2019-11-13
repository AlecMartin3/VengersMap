package com.example.vengersmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.io.Console;
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

    private LocationManager locationManager;
    private static final float MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_list);
        id = getIntent().getExtras().getString("StringID");
        databaseArtifact = FirebaseDatabase.getInstance().getReference("hunts").child(id);
        lvArtifact = findViewById(R.id.lvArtifacts);
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("**********************TEST******************");


        /**
         * Create a location manager and find last location reported.
         */
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        /**
         * If permission not granted to user, deny access.
         * Consider asking for permission if denied.
         */
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            System.out.println("PERMISSION DENIED");
            return;
        }

        /**
         * Get last known position and move camera here.
         * Use location manager to request location updates.
         * Please move the marked line's location to where it is needed.
         */
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

            // Not sure where to put this line here to check for updates; please relocate
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        }
    }

    /**
     * Location listener to update location and move camera to that location
     */
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            double longitude, latitude;
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };




}
