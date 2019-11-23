package com.example.vengersmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Location adapter to convert from a location to a textView so that it can be displayed.
 */
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

