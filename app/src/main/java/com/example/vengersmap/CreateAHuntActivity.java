package com.example.vengersmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateAHuntActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private String TAG = StartupActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private static String SERVICE_URL = "https://opendata.vancouver.ca/api/records/1.0/search/?dataset=parks&rows=216";
    private ArrayList<Location> locList;
    private Marker mark;
    public SupportMapFragment mapFragment;
    private ListView lvArtifacts;
    private List<Artifact> artifactList;
    private TextView tvSeek;
    private Spinner parkSpinner;
    private DatabaseReference databaseHunt;
    private Button createHunt;
    private EditText etHuntName;
    private EditText etHuntPass;
    private String huntName;
    private String huntPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createahunt);

        //Gets to our database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseHunt = database.getReference("hunts");

        etHuntName = findViewById(R.id.etHuntName);
        etHuntPass = findViewById(R.id.etHuntPass);

        //click listener for the create a hunt button
        createHunt = findViewById(R.id.btnCreateHunt);
        createHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHunt();
            }
        });

        //Seek bar used to allow the user to choose how many artifacts they want to add to the hunt
        SeekBar sk = (SeekBar) findViewById(R.id.sbHuntObjects);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvSeek =(TextView)findViewById(R.id.tvSeekBar);
                tvSeek.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                artifactList.clear();
                ArtifactAdapter adapter = new ArtifactAdapter(CreateAHuntActivity.this, artifactList);
                lvArtifacts.setAdapter(adapter);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvSeek =(TextView)findViewById(R.id.tvSeekBar);

                int numArtifacts = Integer.parseInt(tvSeek.getText().toString());
                for(int j = 1; j < numArtifacts + 1; j++){
                    String artName = "Artifact " + j;
                    Artifact a = new Artifact(artName);
                    artifactList.add(a);
                }

                ArtifactAdapter adapter = new ArtifactAdapter(CreateAHuntActivity.this, artifactList);
                lvArtifacts.setAdapter(adapter);

            }
        });

        //Adds artifacts into the artifact list view
        lvArtifacts = findViewById(R.id.lvArtifacts);
        artifactList = new ArrayList<Artifact>();
        lvArtifacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //When artifact in the list view is clicked, opens new addArtifact activity and passes needed information
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(CreateAHuntActivity.this, AddArtifact.class);
                parkSpinner = (Spinner) findViewById(R.id.spinnerPark);
                String park = parkSpinner.getSelectedItem().toString();
                appInfo.putExtra("park", park);
                appInfo.putExtra("position", position);
                appInfo.putExtra("artifact", artifactList.get(position));
                startActivityForResult(appInfo, 1);
            }
        });

        locList = new ArrayList<Location>();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new getLocations().execute();

    }

    /**
     * When the OK buttons is pressed on the AddArtifact Activity, this function receives the needed information
     * of each artifact and updates the name, x, y values in the correct position of the artifact array list.
     *
     * Also displays an updated artifact with the new name in the artifact listview.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String artName = data.getStringExtra("name");
                int pos = data.getIntExtra("position", 1);
                double x = data.getDoubleExtra("x", 1);
                double y = data.getDoubleExtra("y", 1);
                System.out.println(x + " " + y);
                artifactList.get(pos).setArtName(artName);
                artifactList.get(pos).setX(x);
                artifactList.get(pos).setY(y);
                ArtifactAdapter adapter = new ArtifactAdapter(CreateAHuntActivity.this, artifactList);
                lvArtifacts.setAdapter(adapter);
            }
        }
    }

    /**
     * Adds the hunt to the database. Creates a unique id for each hunt and gets the name and password
     * from the edit texts above and passes them into the database. Also gets the list of artifacts
     * and adds that as well.
     */
    public void addHunt(){
        String id = databaseHunt.push().getKey();
        huntName = etHuntName.getText().toString();
        huntPass = etHuntPass.getText().toString();
        parkSpinner = (Spinner) findViewById(R.id.spinnerPark);
        String park = parkSpinner.getSelectedItem().toString();

        HuntItem hunt = null;
        hunt = new HuntItem(id, huntName, huntPass, park);
        Task setPark = databaseHunt.child(id).child("Park").setValue(park);
        Task setNameTask = databaseHunt.child(id).child("Name").setValue(huntName);
        Task setPassword = databaseHunt.child(id).child("Password").setValue(huntPass);
        Task setArtifactTask = databaseHunt.child(id).child("Artifacts").setValue(artifactList);
        setArtifactTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(CreateAHuntActivity.this,
                        "Hunt Added",Toast.LENGTH_LONG).show();
            }
        });
        finish();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "location clicked!!!!", Toast.LENGTH_SHORT).show();
    }


    /**
     * Gets the location data of the three parks we have chosen for our hunts from the opendata URL
     */
    private class getLocations extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateAHuntActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = null;
            jsonStr = sh.makeServiceCall(SERVICE_URL);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonParks = jsonObj.getJSONArray("records");
                    for (int i = 0; i < jsonParks.length(); i++) {
                        JSONObject c = jsonParks.getJSONObject(i).getJSONObject("fields");
                        String name = c.getString("name");
                        if (name.equals("Strathcona Park") || name.equals("Jericho Beach Park") || name.equals("Musqueam Park")){
                            JSONArray loc = c.getJSONArray("googlemapdest");
                            double x_loc = loc.getDouble(0);
                            double y_loc = loc.getDouble(1);
                            Location locat = new Location();
                            locat.setName(name);
                            locat.setX(x_loc);
                            locat.setY(y_loc);
                            locList.add(locat);
                        }

                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //Toon[] toonArray = toonList.toArray(new Toon[toonList.size()]);


            mapFragment.getMapAsync(CreateAHuntActivity.this);

        }
    }

    /**
     * Creates the google map that is zoomed into the area that we need to see all the parks
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng vancouver = new LatLng(49.246292, -123.116226);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vancouver, 11));
        for(Location n : locList) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(n.getX(), n.getY())).title(n.getName()));
        }
        LocAdapter markerInfoWindowAdapter = new LocAdapter(getApplicationContext());
        googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);

        //changes spinner based on which marker has been selected
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTitle().equals("Strathcona Park")){
                    parkSpinner = (Spinner) findViewById(R.id.spinnerPark);
                    parkSpinner.setSelection(0);
                } else if(marker.getTitle().equals("Jericho Beach Park")){
                    parkSpinner = (Spinner) findViewById(R.id.spinnerPark);
                    parkSpinner.setSelection(1);
                } else if(marker.getTitle().equals("Musqueam Park")){
                    parkSpinner = (Spinner) findViewById(R.id.spinnerPark);
                    parkSpinner.setSelection(2);
                }
                return false;
            }
        });
    }
}
