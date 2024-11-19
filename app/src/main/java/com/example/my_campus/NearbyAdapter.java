package com.example.my_campus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class NearbyAdapter extends BaseAdapter {
    private Context context;
    private List<String> locationNames;
    private List<String> locationUrls;

    public NearbyAdapter(Context context, List<String>locationNames, List<String>locationUrls) {
        this.context = context;
        this.locationNames = locationNames;
        this.locationUrls = locationUrls;
    }

    @Override
    public int getCount() {
        return locationNames.size();
    }

    @Override
    public Object getItem(int position) {
        return locationNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.nearby_list_item, parent, false);
        }

        TextView locationTextView = convertView.findViewById(R.id.nearbyName);
        ConstraintLayout openButton = convertView.findViewById(R.id.btnLocate);

        // Set location name
        locationTextView.setText(locationNames.get(position));

        // Set click listener on the "Locate" button to open the map link
//        openButton.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
//            context.startActivity(intent);
//        });


        //
        openButton.setOnClickListener(v -> {
            String locationUrl = locationUrls.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl));
            context.startActivity(intent);
        });

        return convertView;
    }
}
