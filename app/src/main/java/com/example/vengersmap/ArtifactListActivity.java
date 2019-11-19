package com.example.vengersmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
    private ArrayList<Artifact> ArtifactList;

    public SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FloatingActionButton fabScan;
    private static final double CLOSE_RANGE = 0.00005; // roughly 5m
    private static final double MED_RANGE = 0.00010;  //         10m
    private static final double LONG_RANGE = 0.00020; //         20m


    private LocationManager lm;
    private static final int MIN_TIME = 500;
    private static final int MIN_DISTANCE = 5;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_list);
        id = getIntent().getExtras().getString("StringID");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseArtifact = FirebaseDatabase.getInstance().getReference("hunts").child(id);
        lvArtifact = findViewById(R.id.lvArtifacts);
        ArtifactList = new ArrayList<Artifact>();
        fabScan = findViewById(R.id.fabScan);
        fabScan.setImageResource(R.drawable.loupe);

        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

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

        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Artifact a: ArtifactList) {

                    if (inRange(a, CLOSE_RANGE)) {
                        System.out.println("found something");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Found something!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(a.getX(), a.getY()))
                                        .title(a.getArtName()));

                        /** Adds artifact to users artifact list in database */
                        String toCollect = a.getArtName();
                        databaseUser = FirebaseDatabase.getInstance().getReference("players").child(userID).child("Artifacts");
                        databaseUser.setValue(toCollect);


                        /** Removes artifact from list when it's found */
                        ArtifactList.remove(a);
                        ArtifactAdapter adapter = new ArtifactAdapter(ArtifactListActivity.this, ArtifactList);
                        lvArtifact.setAdapter(adapter);


                    } else if (inRange(a, MED_RANGE)) {
                        System.out.println("getting closer");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Getting closer!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (inRange(a, LONG_RANGE)) {
                        System.out.println("something's around here");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Something's around here!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        System.out.println("nothing here");
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Nothing here",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }

                }
            }
        });


    }

    private boolean inRange(Artifact a, double levelRange) {
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        double artLong = a.getY();
        double artLat = a.getX();

        System.out.println("Artifact at: " + artLong + " " + artLat);
        System.out.println("Device at: " + longitude + " " + latitude);

        if (artLong - levelRange <= longitude && longitude <= artLong + levelRange &&
            artLat - levelRange <= latitude && latitude <= artLat + levelRange)
            return true;

        return false;
    }

    /**
     * When Map is ready, location permission is acquired and user is located on map.
     * User will be followed while map is active.
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

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        mMap.animateCamera((CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18)));

        /**
         * Requests the current location periodically
         */
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        mMap.animateCamera((CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18)));
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
