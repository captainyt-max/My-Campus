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
import java.util.ArrayList;
import java.util.List;

public class HostelListAdapter extends RecyclerView.Adapter<HostelListAdapter.ViewHolder> {
    private Context context;
    private List<HostellListItem> hostelList; // No need for default initialization, handled in constructor

    public HostelListAdapter(Context context, List<HostellListItem> hostelList) {
        this.context = context;
        this.hostelList = (hostelList != null) ? hostelList : new ArrayList<>(); // Prevents null list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hostel_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (hostelList == null || hostelList.isEmpty()) {
            return; // Prevent crash when data is empty
        }

        HostellListItem item = hostelList.get(position);

        // Check for null before setting text to prevent crashes
        holder.name.setText(item.getName() != null ? item.getName() : "N/A");
        holder.designation.setText(item.getDesignation() != null ? item.getDesignation() : "N/A");
        holder.phoneNumber.setText(item.getPhoneNumber() != null ? item.getPhoneNumber() : "N/A");

        // Load image safely with Glide, using a placeholder
        if (item.getIconUrl() != null && !item.getIconUrl().isEmpty()) {
            Glide.with(holder.icon.getContext())
                    .load(item.getIconUrl())
                    .placeholder(R.drawable.default_icon)
                    .error(R.drawable.default_icon) // Set error fallback
                    .into(holder.icon);
        } else {
            holder.icon.setImageResource(R.drawable.default_icon); // Default image if URL is null
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
    }

    @Override
    public int getItemCount() {
        return (hostelList != null) ? hostelList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, designation, phoneNumber;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            designation = itemView.findViewById(R.id.designation);
            phoneNumber = itemView.findViewById(R.id.phone);
            icon = itemView.findViewById(R.id.icon);

            // Null check to prevent crashes if views are not found
            if (name == null || designation == null || phoneNumber == null || icon == null) {
                throw new NullPointerException("ViewHolder: Some views are not found in hostel_list_items.xml");
            }
        }
    }
}
