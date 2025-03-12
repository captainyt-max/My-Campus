package com.example.my_campus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ListItem> listItems;

    public CustomListAdapter(Context context, ArrayList<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.faculties_list_items, parent, false);
        }

        ListItem currentItem = (ListItem) getItem(position);

        ImageView imageView = convertView.findViewById(R.id.icon);
        TextView faculty_name = convertView.findViewById(R.id.Itemname);
        TextView faculty_designation = convertView.findViewById(R.id.designation);
        TextView faculty_phone = convertView.findViewById(R.id.phone);
        TextView faculty_email = convertView.findViewById(R.id.email);



        imageView.setImageResource(currentItem.getIcon());
        faculty_name.setText(currentItem.getName());
        faculty_designation.setText(currentItem.getDesignation());
        faculty_phone.setText(currentItem.getPhone());
        faculty_email.setText(currentItem.getEmail());

        faculty_phone.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + currentItem.getPhone()));
            context.startActivity(intent);
        });

        faculty_email.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + currentItem.getEmail()));
            context.startActivity(intent);
        });

        utility ut = new utility();

        imageView.setOnClickListener(click -> {
            ut.navigateToProfileImage(context, currentItem.getIcon(), currentItem.getName(), "facultiesProfileImage");

        });


        return convertView;
    }
}
