package com.example.vengersmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ArtifactListActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private String id;
    private String userID;
    private DatabaseReference databaseArtifact;
    private DatabaseReference databaseUser;
    private ListView lvArtifact;
    private ArrayList<Artifact> artifactList;
    private ArrayList<Artifact> foundArtifacts;
    private int removeIndex = -1;

    public SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FloatingActionButton fabScan;
    private static final double CLOSE_RANGE = 0.00005; // roughly 5m
    private static final double MED_RANGE = 0.00010;  //          10m
    private static final double LONG_RANGE = 0.00020; //          20m


    private LocationManager lm;
    private double deviceLatitude;
    private double deviceLongitude;
    private static final int MIN_TIME = 500;
    private static final int MIN_DISTANCE = 5;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_artifact_list);
        id = getIntent().getExtras().getString("StringID");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseArtifact = FirebaseDatabase.getInstance().getReference("hunts").child(id);
        databaseUser = FirebaseDatabase.getInstance().getReference("players").child(userID);
        lvArtifact = findViewById(R.id.lvArtifacts);
        artifactList = new ArrayList<>();
        foundArtifacts = new ArrayList<>();
        fabScan = findViewById(R.id.fabScan);
        fabScan.setImageResource(R.drawable.loupe);

        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foundArtifacts.clear();
                for (DataSnapshot CountSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot NameSnapshot : CountSnapshot.getChildren()) {
                        Artifact artifact = NameSnapshot.getValue(Artifact.class);
                        artifact.setArtName(NameSnapshot.child("artName").getValue().toString());
                        foundArtifacts.add(artifact);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        databaseArtifact.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                artifactList.clear();
                for (DataSnapshot CountSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot NameSnapshot : CountSnapshot.getChildren()) {
                        Artifact artifact = NameSnapshot.getValue(Artifact.class);
                        artifact.setArtName(NameSnapshot.child("artName").getValue().toString());

                        boolean found = false;
                        for (Artifact foundArtifact : foundArtifacts) {
                            if (artifact.getArtName().equals(foundArtifact.getArtName())) {
                                found = true;
                            }
                        }
                        if (!found) {
                            artifactList.add(artifact);
                        } else {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(artifact.getX(), artifact.getY()))
                                    .title(artifact.getArtName()));
                        }
                    }
                }

                ArtifactAdapter adapter = new ArtifactAdapter(ArtifactListActivity.this, artifactList);
                lvArtifact.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        boolean preload = getIntent().getBooleanExtra("preload", false);
        if (preload) {
            System.out.println("***************PRELOADING");
            Intent i = getIntent();
            i.removeExtra("preload");
            finish();
            startActivity(i);
        }

        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeIndex = -1;
                boolean inRange = false;
                for (Artifact a : artifactList) {

                    if (inRange(a, CLOSE_RANGE)) {
                        inRange = true;
                        System.out.println("found something");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Found something!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(a.getX(), a.getY()))
                                .title(a.getArtName()));

                        /** Adds artifact to users artifact list in database */
                        foundArtifacts.add(a);
                        databaseUser = FirebaseDatabase.getInstance().getReference("players").child(userID).child("Artifacts");
                        databaseUser.setValue(foundArtifacts);

                        /** Removes artifact from list when it's found */
                        removeIndex = artifactList.indexOf(a);

                    } else if (inRange(a, MED_RANGE)) {
                        inRange = true;
                        System.out.println("getting closer");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Getting closer!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (inRange(a, LONG_RANGE)) {
                        inRange = true;
                        System.out.println("something's around here");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Something's around here!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                if (!inRange) {
                    System.out.println("nothing here");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Nothing here",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
                if (removeIndex != -1) {
                    artifactList.remove(removeIndex);
                    ArtifactAdapter adapter = new ArtifactAdapter(ArtifactListActivity.this, artifactList);
                    lvArtifact.setAdapter(adapter);
                }

            }
        });
    }

    private boolean inRange(Artifact a, double levelRange) {

        double artLong = a.getY(); // These values are backwards!! WHY?!!
        double artLat = a.getX();

        System.out.println("Artifact at: " + artLat + " " + artLong);
        System.out.println("Device at: " + deviceLatitude + " " + deviceLongitude);

        if (artLong - levelRange <= deviceLongitude && deviceLongitude <= artLong + levelRange &&
                artLat - levelRange <= deviceLatitude && deviceLatitude <= artLat + levelRange)
            return true;

        return false;
    }

    /**
     * When Map is ready, location permission is acquired and user is located on map.
     * User will be followed while map is active.
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**
         * If permission not granted to user, deny access.
         * Ask for permission if denied.
         */
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("PERMISSION DENIED");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            System.out.println("REQUESTING PERMISSION");

            finish();
            ActivityCompat.recreate(this);
            return;
        }
        mMap.setMyLocationEnabled(true);

        findDeviceLocation();
        mMap.animateCamera((CameraUpdateFactory.newLatLngZoom(new LatLng(deviceLatitude, deviceLongitude), 18)));

        /**
         * Requests the current location periodically
         */
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    private void findDeviceLocation() {
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        deviceLongitude = location.getLongitude();
        deviceLatitude = location.getLatitude();
    }

    @Override
    public void onLocationChanged(Location location) {
        deviceLongitude = location.getLongitude();
        deviceLatitude = location.getLatitude();
        mMap.animateCamera((CameraUpdateFactory.newLatLngZoom(new LatLng(deviceLatitude, deviceLongitude), 18)));
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

}
