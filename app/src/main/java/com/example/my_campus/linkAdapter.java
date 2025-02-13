package com.example.my_campus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class linkAdapter extends BaseAdapter {
    private Context context;
    private List<String> linkNames;
    private List<String> linkUrls;
    public linkAdapter(Context context, List<String> linkNames, List<String> linkUrls) {
        this.context = context;
        this.linkNames = linkNames;
        this.linkUrls = linkUrls;
    }

    @Override
    public int getCount() {
        return linkNames.size();
    }

    @Override
    public Object getItem(int position) {
        return linkNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.link_list_item, parent, false);
        }

        TextView linkTextView = convertView.findViewById(R.id.linkName);
        ConstraintLayout openButton = convertView.findViewById(R.id.btnOpen);

        // Set the link name
        linkTextView.setText(linkNames.get(position));

        // Open the link in a browser or web view
        openButton.setOnClickListener(v -> {
            String linkUrl = linkUrls.get(position); // Get the URL for the current item
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl));
            try {
                context.startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(context, "Unable to open the link.", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

}
