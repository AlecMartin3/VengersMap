package com.example.vengersmap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

class LocAdapter implements GoogleMap.InfoWindowAdapter {
    Context context;

    LocAdapter(Context context){
        this.context = context.getApplicationContext();
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.map_info, null);

        String title = marker.getTitle();
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(title);
        return v;
    }
}

