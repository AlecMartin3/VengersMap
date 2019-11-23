package com.example.vengersmap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * A player adapter that takes a player list and displays all their artifacts that they have
 * collected.
 */
public class PlayerAdapter extends ArrayAdapter<Player> {
    private Activity context;
    private List<Player> PlayerList;

    public PlayerAdapter(Activity context, List<Player> PlayerList) {
        super(context, R.layout.list_view_object, PlayerList);
        this.context = context;
        this.PlayerList = PlayerList;
    }

    public PlayerAdapter(Context context, int resource, List<Player> objects, Activity context1, List<Player> PlayerList) {
        super(context, resource, objects);
        this.context = context1;
        this.PlayerList = PlayerList;
    }

    /**
     * Gets the view for the adapter.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_view_object, null, true);

        TextView tvName = listViewItem.findViewById(R.id.tvArtifact);

        Player art = PlayerList.get(position);

        tvName.setText(art.getArtName());


        return listViewItem;
    }
}
