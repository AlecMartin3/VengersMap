package com.example.vengersmap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ArtifactListActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private DatabaseReference databaseArtifact;
    private ListView lvArtifact;
    private ArrayList<Artifact> ArtifactList;
    public SupportMapFragment mapFragment;
    private String id;
    private GoogleMap mMap;

    private LocationManager lm;
    private static final int MIN_TIME = 100;
    private static final int MIN_DISTANCE = 1;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_list);
        id = getIntent().getExtras().getString("StringID");
        databaseArtifact = FirebaseDatabase.getInstance().getReference("hunts").child(id);
        lvArtifact = findViewById(R.id.lvArtifacts);
        ArtifactList = new ArrayList<Artifact>();


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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**
         * Create a location manager and find last location reported.
         */

        /**
         * If permission not granted to user, deny access.
         * Ask for permission if denied.
         */
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
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
//        recenterLocation();

        /**
         * Requests the current location periodically
         */
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

//    public void recenterLocation() {
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            System.out.println("PERMISSION DENIED");
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
//            System.out.println("REQUESTING PERMISSION");
//
//            return;
//        }
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        mMap.animateCamera((CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15)));
//
//    }

    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        mMap.animateCamera((CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18)));
//        lm.removeUpdates(this);
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

//    /**
//     * Location listener to update location and move camera to that location
//     */
//    private final LocationListener locationListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//
//            double longitude, latitude;
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
//
//        }
//
//        @Override
//        public void onStatusChanged(String s, int i, Bundle bundle) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String s) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String s) {
//
//        }
//    };




}
