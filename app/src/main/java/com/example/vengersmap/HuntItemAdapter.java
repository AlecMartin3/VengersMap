package com.example.vengersmap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Allows adding hunts to a list view showing name and park
 */
public class HuntItemAdapter extends ArrayAdapter<HuntItem> {
    private Activity context;
    private List<HuntItem> HuntList;
    public HuntItemAdapter(Activity context, List<HuntItem> HuntList) {
        super(context, R.layout.list_hunt_object, HuntList);
        this.context = context;
        this.HuntList = HuntList;
    }

    /**
     * Gets the view for the adapter and sets the textViews.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_hunt_object, null, true);


        TextView tvHuntName = listViewItem.findViewById(R.id.huntName);
        TextView tvHuntPark = listViewItem.findViewById(R.id.huntPark);

        HuntItem hunt = HuntList.get(position);
        tvHuntName.setText(hunt.getHuntName());
        tvHuntPark.setText(hunt.getHuntPark());


        return listViewItem;
    }
}
