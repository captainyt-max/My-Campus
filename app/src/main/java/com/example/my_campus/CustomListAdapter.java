package com.example.my_campus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {
    private Context context;
    private List<ListItem> facultyList; // No need for default initialization, handled in constructor
    private utility ut = new utility();

    public CustomListAdapter(Context context, List<ListItem> facultyList) {
        this.context = context;
        this.facultyList = (facultyList != null) ? facultyList : new ArrayList<>(); // Prevents null list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculties_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (facultyList == null || facultyList.isEmpty()) {
            return; // Prevent crash when data is empty
        }

        ListItem item = facultyList.get(position);

        // Check for null before setting text to prevent crashes
        holder.name.setText(item.getName() != null ? item.getName() : "N/A");
        holder.designation.setText(item.getDesignation() != null ? item.getDesignation() : "N/A");
        holder.phoneNumber.setText(item.getPhoneNumber() != null ? item.getPhoneNumber() : "N/A");
        holder.email.setText((item.getEmail() != null ? item.getEmail() : "N/A"));

        // Load image safely with Glide, using a placeholder
        if (item.getIconUrl() != null && !item.getIconUrl().isEmpty()) {
            Glide.with(holder.icon.getContext())
                    .load(item.getIconUrl())
                    .placeholder(R.drawable.default_icon)
                    .error(R.drawable.default_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Stronger caching
                    .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade()) // Smooth fade-in
                    .into(holder.icon);
        } else {
            holder.icon.setImageResource(R.drawable.default_icon);
        }


        // Set onClickListener safely
        holder.phoneNumber.setOnClickListener(v -> {
            if (item.getPhoneNumber() != null && !item.getPhoneNumber().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + item.getPhoneNumber()));

                if (context != null) {
                    context.startActivity(intent);
                }
            }
        });
        // Set onClickListener safely for email
        holder.email.setOnClickListener(v -> {
            if (item.getEmail() != null && !item.getEmail().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + item.getEmail()));

                if (context != null) {
                    context.startActivity(intent);
                }
            }
        });

        // Set onClickListener safely for icon
        holder.icon.setOnClickListener(v -> {
            ut.navigateToProfileImage(context, item.getIconUrl(), item.getName(), "facultiesProfileImage");
        });

    }

    @Override
    public int getItemCount() {
        return (facultyList != null) ? facultyList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, designation, phoneNumber, email;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Itemname);
            designation = itemView.findViewById(R.id.designation);
            phoneNumber = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            icon = itemView.findViewById(R.id.icon);

            // Null check to prevent crashes if views are not found
            if (name == null || designation == null || phoneNumber == null || icon == null || email == null) {
                throw new NullPointerException("ViewHolder: Some views are not found in faculty_list_items.xml");
            }
        }
    }
}
