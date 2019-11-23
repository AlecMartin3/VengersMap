package com.example.vengersmap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Customization for each artifact that is added to a hunt. Allows the user to set a name and location
 * within their selected park.
 */
public class AddArtifact extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private int position;
    private EditText etArtName;
    private String name = " ";
    private String park;
    private ArrayList<Location> artLocation;
    private Spinner spinnerPoint;
    private double x;
    private double y;
    private Marker myMarker;
    public SupportMapFragment mapFragment;


    /**
     * Gets park and position of the artifact in the list from the previous activity.
     * Creates locations based on the park and loads the map
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact);

        position = getIntent().getExtras().getInt("position");
        park = getIntent().getExtras().getString("park");
        artLocation = new ArrayList<Location>();
        createLocation();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    /**
     * Creates locations for points depending on which park was selected
     */
    public void createLocation(){
        if(park.equals("Strathcona Park")){
            Location strath1 = new Location();
            strath1.setName("Point 1");
            strath1.setX(49.274795);
            strath1.setY(-123.086702);

            Location strath2 = new Location();
            strath2.setName("Point 2");
            strath2.setX(49.276052);
            strath2.setY(-123.087088);

            Location strath3 = new Location();
            strath3.setName("Point 3");
            strath3.setX(49.276078);
            strath3.setY(-123.085570);

            Location strath4 = new Location();
            strath4.setName("Point 4");
            strath4.setX(49.275772);
            strath4.setY(-132.085506);

            Location strath5 = new Location();
            strath5.setName("Point 5");
            strath5.setX(49.275561);
            strath5.setY(-123.085490);

            Location strath6 = new Location();
            strath6.setName("Point 6");
            strath6.setX(49.275278);
            strath6.setY(-123.085662);

            Location strath7 = new Location();
            strath7.setName("Point 7");
            strath7.setX(49.274820);
            strath7.setY(-123.085796);

            Location strath8 = new Location();
            strath8.setName("Point 8");
            strath8.setX(49.274542);
            strath8.setY(-123.085285);

            Location strath9 = new Location();
            strath9.setName("Point 9");
            strath9.setX(49.275333);
            strath9.setY(-123.085105);

            Location strath10 = new Location();
            strath10.setName("Point 10");
            strath10.setX(49.275549);
            strath10.setY(-123.083941);

            artLocation.add(strath1);
            artLocation.add(strath2);
            artLocation.add(strath3);
            artLocation.add(strath4);
            artLocation.add(strath5);
            artLocation.add(strath6);
            artLocation.add(strath7);
            artLocation.add(strath8);
            artLocation.add(strath9);
            artLocation.add(strath10);
        }
        if(park.equals("Jericho Beach Park")){

            Location jericho1 = new Location();
            jericho1.setName("Point 1");
            jericho1.setX(49.274828);
            jericho1.setY(-123.201832);
            artLocation.add(jericho1);

            Location jericho2 = new Location();
            jericho2.setName("Point 2");
            jericho2.setX(49.276294);
            jericho2.setY(-123.202155);
            artLocation.add(jericho2);

            Location jericho3 = new Location();
            jericho3.setName("Point 3");
            jericho3.setX(49.273879);
            jericho3.setY(-123.202341);
            artLocation.add(jericho3);

            Location jericho4 = new Location();
            jericho4.setName("Point 4");
            jericho4.setX(49.273739);
            jericho4.setY(-123.200058);
            artLocation.add(jericho4);

            Location jericho5 = new Location();
            jericho5.setName("Point 5");
            jericho5.setX(49.274113);
            jericho5.setY(-123.196761);
            artLocation.add(jericho5);

            Location jericho6 = new Location();
            jericho6.setName("Point 6");
            jericho6.setX(49.272621);
            jericho6.setY(-123.193091);
            artLocation.add(jericho6);

            Location jericho7 = new Location();
            jericho7.setName("Point 7");
            jericho7.setX(49.271104);
            jericho7.setY(-123.198627);
            artLocation.add(jericho7);

            Location jericho8 = new Location();
            jericho8.setName("Point 8");
            jericho8.setX(49.270938);
            jericho8.setY(-123.194945);
            artLocation.add(jericho8);

            Location jericho9 = new Location();
            jericho9.setName("Point 9");
            jericho9.setX(49.271108);
            jericho9.setY(-123.192595);
            artLocation.add(jericho9);

            Location jericho10 = new Location();
            jericho10.setName("Point 10");
            jericho10.setX(49.271781);
            jericho10.setY(-123.191594);
            artLocation.add(jericho10);
        }
        if(park.equals("Musqueam Park")){

            Location musq1 = new Location();
            musq1.setName("Point 1");
            musq1.setX(49.228619);
            musq1.setY(-123.188281);
            artLocation.add(musq1);

            Location musq2 = new Location();
            musq2.setName("Point 2");
            musq2.setX(49.229257);
            musq2.setY(-123.187258);
            artLocation.add(musq2);

            Location musq3 = new Location();
            musq3.setName("Point 3");
            musq3.setX(49.230026);
            musq3.setY(-123.188071);
            artLocation.add(musq3);

            Location musq4 = new Location();
            musq4.setName("Point 4");
            musq4.setX(49.229189);
            musq4.setY(-123.189921);
            artLocation.add(musq4);

            Location musq5 = new Location();
            musq5.setName("Point 5");
            musq5.setX(49.229469);
            musq5.setY(-123.191313);
            artLocation.add(musq5);

            Location musq6 = new Location();
            musq6.setName("Point 6");
            musq6.setX(49.228055);
            musq6.setY(-123.189591);
            artLocation.add(musq6);

            Location musq7 = new Location();
            musq7.setName("Point 7");
            musq7.setX(49.230150);
            musq7.setY(-123.193692);
            artLocation.add(musq7);

            Location musq8 = new Location();
            musq8.setName("Point 8");
            musq8.setX(49.229076);
            musq8.setY(-123.188602);
            artLocation.add(musq8);

            Location musq9 = new Location();
            musq9.setName("Point 9");
            musq9.setX(49.230465);
            musq9.setY(-123.189165);
            artLocation.add(musq9);

            Location musq10 = new Location();
            musq10.setName("Point 10");
            musq10.setX(49.230319);
            musq10.setY(-123.188732);
            artLocation.add(musq10);
        }

    }

    /**
     * Gets the x and y coord based on which point was selected
     */
    public void getPoint(){
        spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
        String point = spinnerPoint.getSelectedItem().toString();
        if(point.equals("Point 1")){
            Location loc = artLocation.get(0);
            x = loc.getX();
            y = loc.getY();
        }
        if(point.equals("Point 2")){
            Location loc = artLocation.get(1);
            x = loc.getX();
            y = loc.getY();
        }
        if(point.equals("Point 3")){
            Location loc = artLocation.get(2);
            x = loc.getX();
            y = loc.getY();
        }
        if(point.equals("Point 4")){
            Location loc = artLocation.get(3);
            x = loc.getX();
            y = loc.getY();
        }
        if(point.equals("Point 5")){
            Location loc = artLocation.get(4);
            x = loc.getX();
            y = loc.getY();
        }
        if(point.equals("Point 6")){
            Location loc = artLocation.get(5);
            x = loc.getX();
            y = loc.getY();
        }
        if(point.equals("Point 7")){
            Location loc = artLocation.get(6);
            x = loc.getX();
            y = loc.getY();
        }
        if(point.equals("Point 8")){
            Location loc = artLocation.get(7);
            x = loc.getX();
            y = loc.getY();
        }
        if(point.equals("Point 9")){
            Location loc = artLocation.get(8);
            x = loc.getX();
            y = loc.getY();
        }
        if(point.equals("Point 10")){
            Location loc = artLocation.get(9);
            x = loc.getX();
            y = loc.getY();
        }


    }

    /**
     * Creates a map with markers on points based on which park was selected on the spinner in the previous
     * CreateAHunt activity.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(park.equals("Strathcona Park")){
            System.out.println(park);
            LatLng parkLoc = new LatLng(49.274959, -123.085946);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkLoc, 16));
            for(Location n : artLocation){
                mMap.addMarker(new MarkerOptions().position(new LatLng(n.getX(), n.getY())).title(n.getName()));
            }
            LocAdapter markerInfoWindowAdapter = new LocAdapter(getApplicationContext());
            googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
        }

        if(park.equals("Jericho Beach Park")){
            System.out.println(park);
            LatLng parkLoc = new LatLng( 49.271520, -123.197868);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkLoc, 15));
            for(Location n : artLocation){
                mMap.addMarker(new MarkerOptions().position(new LatLng(n.getX(), n.getY())).title(n.getName()));
            }
            LocAdapter markerInfoWindowAdapter = new LocAdapter(getApplicationContext());
            googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
        }

        if(park.equals("Musqueam Park")){
            System.out.println(park);
            LatLng parkLoc = new LatLng(49.229412, -123.190269);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parkLoc, 16));
            for(Location n : artLocation){
                mMap.addMarker(new MarkerOptions().position(new LatLng(n.getX(), n.getY())).title(n.getName()));
            }
            LocAdapter markerInfoWindowAdapter = new LocAdapter(getApplicationContext());
            googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
        }
        //changes spinner based on which marker has been selected
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTitle().equals("Point 1")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(0);
                } else if(marker.getTitle().equals("Point 2")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(1);
                } else if(marker.getTitle().equals("Point 3")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(2);
                } else if(marker.getTitle().equals("Point 4")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(3);
                } else if(marker.getTitle().equals("Point 5")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(4);
                } else if(marker.getTitle().equals("Point 6")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(5);
                } else if(marker.getTitle().equals("Point 7")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(6);
                } else if(marker.getTitle().equals("Point 8")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(7);
                } else if(marker.getTitle().equals("Point 9")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(8);
                } else if(marker.getTitle().equals("Point 10")){
                    spinnerPoint = (Spinner) findViewById(R.id.spinnerPoint);
                    spinnerPoint.setSelection(9);
                }
                return false;
            }
        });
    }

    /**
     * Calls the getPoint function to grab x and y coord. Gets the artifact name and positions,
     * then passes all the info back to the CreateAHunt activity and closes the activity.
     *
     * @param view
     */
    public void back(View view) {
        getPoint();
        etArtName = findViewById(R.id.etArtifactName);
        name = etArtName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter an Artifact name", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("position", position);
        intent.putExtra("x", x);
        intent.putExtra("y", y);
        setResult(RESULT_OK, intent);
        finish();
    }

}
