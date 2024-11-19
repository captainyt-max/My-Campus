package com.example.my_campus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HostelListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HostellListItem> listItem;

    public HostelListAdapter(Context context, ArrayList<HostellListItem> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.hostel_list_items, parent, false);
        }

        HostellListItem currentItem = (HostellListItem) getItem(position);

        ImageView imageView = convertView.findViewById(R.id.icon);
        TextView staff_name = convertView.findViewById(R.id.itemName);
        TextView staff_designation = convertView.findViewById(R.id.designation);
        TextView staff_phone = convertView.findViewById(R.id.phone);



        imageView.setImageResource(currentItem.getIcon());
        staff_name.setText(currentItem.getName());
        staff_designation.setText(currentItem.getDesignation());
        staff_phone.setText(currentItem.getPhone());

        return convertView;
    }
}
