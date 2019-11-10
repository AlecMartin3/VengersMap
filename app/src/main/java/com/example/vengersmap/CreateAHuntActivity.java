package com.example.vengersmap;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createahunt);

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

        lvArtifacts = findViewById(R.id.lvArtifacts);
        artifactList = new ArrayList<Artifact>();

        lvArtifacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(CreateAHuntActivity.this, AddArtifact.class);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String artName = data.getStringExtra("name");
                int pos = data.getIntExtra("position", 1);
                artifactList.get(pos).setArtName(artName);
                System.out.println(artName + " " + pos);
                ArtifactAdapter adapter = new ArtifactAdapter(CreateAHuntActivity.this, artifactList);
                lvArtifacts.setAdapter(adapter);

            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "location clicked!!!!", Toast.LENGTH_SHORT).show();
    }


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
//        Marker m1 = mMap.addMarker(new MarkerOptions().position(vancouver).title("van"));


    }
}
