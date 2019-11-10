package com.example.vengersmap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.List;

public class ArtifactAdapter extends ArrayAdapter<Artifact> {

    private Activity context;
    private List<Artifact> artifactList;

    public ArtifactAdapter(Activity context, List<Artifact> artifactList) {
        super(context, R.layout.list_view_object, artifactList);
        this.context = context;
        this.artifactList = artifactList;
    }

    public ArtifactAdapter(Context context, int resource, List<Artifact> objects, Activity context1, List<Artifact> artifactList) {
        super(context, resource, objects);
        this.context = context1;
        this.artifactList = artifactList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_view_object, null, true);

        TextView tvName = listViewItem.findViewById(R.id.tvArtifact);


        Artifact art = artifactList.get(position);

        tvName.setText(art.getArtName());


        return listViewItem;
    }


}
