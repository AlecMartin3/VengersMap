package com.example.vengersmap;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private String TAG = StartupActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private static String SERVICE_URL = "https://opendata.vancouver.ca/api/records/1.0/search/?dataset=parks&rows=216";
    private ArrayList<locations> locList;
    private Marker mark;
    public SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locList = new ArrayList<locations>();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new getLocations().execute();



    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "location clicked!!!!", Toast.LENGTH_SHORT).show();
    }


    private class getLocations extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
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
                            locations locat = new locations();
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


            mapFragment.getMapAsync(MapsActivity.this);

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng vancouver = new LatLng(49.246292, -123.116226);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vancouver, 11));
        for(locations n : locList) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(n.getX(), n.getY())).title(n.getName()));
        }
        LocAdapter markerInfoWindowAdapter = new LocAdapter(getApplicationContext());
        googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
//        Marker m1 = mMap.addMarker(new MarkerOptions().position(vancouver).title("van"));


    }
}
