package com.example.my_campus;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class campusActivityAdapter extends RecyclerView.Adapter<campusActivityAdapter.MessageViewHolder> {

    private Context context;
    private List<campusActivityItem> messageList;
    private FirebaseFirestore db;
    private utility ut = new utility();
    private editMessageCallback callback;
    private String adapterMode;
    private String classActivityCollection;

    // Constructor for adapter
    public campusActivityAdapter(Context context, List<campusActivityItem> messageList, String adapterMode, editMessageCallback callback) {
        setHasStableIds(true);
        this.context = context;
        this.messageList = messageList;
        this.db = FirebaseFirestore.getInstance();
        this.callback = callback;
        this.adapterMode = adapterMode;
    }
    public campusActivityAdapter(Context context, List<campusActivityItem> messageList, String adapterMode, String classActivityCollection, editMessageCallback callback) {
        setHasStableIds(true);
        this.context = context;
        this.messageList = messageList;
        this.db = FirebaseFirestore.getInstance();
        this.callback = callback;
        this.adapterMode = adapterMode;
        this.classActivityCollection = classActivityCollection;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context).inflate(R.layout.campus_activity_list_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // Bind data to the view
        campusActivityItem currentItem = messageList.get(position);
        holder.msgBody.setText(currentItem.getMessageBody());
        holder.sentTime.setText(currentItem.getSentTime());

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.messageLayoutParent.getLayoutParams();
        if (isUserSender(currentItem.getSenderEmail())){
            holder.messageLayout.setBackgroundResource(R.drawable.registration_details_background);
            holder.imageLayout.setVisibility(View.GONE);
            // Set start and end margins (in pixels)
            int marginStart = (int) (95 * context.getResources().getDisplayMetrics().density);
            int marginEnd = (int) (12 * context.getResources().getDisplayMetrics().density);
            params.setMarginStart(marginStart);
            params.setMarginEnd(marginEnd);
            holder.messageLayoutParent.setLayoutParams(params);
        }
        else {
            holder.messageLayout.setBackgroundResource(R.drawable.rounded_ractangle_1);
            holder.imageLayout.setVisibility(View.VISIBLE);
            int marginStart = (int) (0 * context.getResources().getDisplayMetrics().density);
            int marginEnd = (int) (60 * context.getResources().getDisplayMetrics().density);
            params.setMarginStart(marginStart);
            params.setMarginEnd(marginEnd);
            holder.messageLayoutParent.setLayoutParams(params);
        }

        holder.imageLayout.setOnClickListener( click-> {
            ut.clickAnimation(click);
            ut.showProfile(context, currentItem.getSenderEmail());
        });

        holder.itemView.setOnLongClickListener( view -> {
            showContextMenu(view, currentItem.getSenderEmail(), currentItem.getDocID());
            return true;
        });

        db.collection("users").document(currentItem.getSenderEmail())
                .addSnapshotListener( (snapshot, e) -> {
                    if (e != null){
                        Log.d("campusActivityAdapter", "onBindViewHolder: "+ e.getMessage());
                        return;
                    }
                    if (snapshot != null && snapshot.exists()){
                        if (isUserSender(currentItem.getSenderEmail())){
                            holder.senderName.setText("You");
                        }
                        else {
                            holder.senderName.setText(snapshot.getString("name"));
                        }
                        String imageUrl = snapshot.getString("profileImage");
                        Glide.with(context)
                                .load(imageUrl)
                                .placeholder(R.drawable.ic_default_user)
                                .error(R.drawable.ic_default_user)
                                .into(holder.senderProfileImage);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public long getItemId(int position) {
        return messageList.get(position).getDocID().hashCode();
    }

    // ViewHolder class
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView senderName, msgBody, sentTime;
        ImageView senderProfileImage;
        ConstraintLayout messageLayoutParent, messageLayout;
        CardView imageLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.senderName);
            msgBody = itemView.findViewById(R.id.msgBody);
            sentTime = itemView.findViewById(R.id.sentTime);
            senderProfileImage = itemView.findViewById(R.id.senderProfileImage);
            messageLayoutParent = itemView.findViewById(R.id.messageLayoutParent);
            messageLayout = itemView.findViewById(R.id.messageLayout);
            imageLayout = itemView.findViewById(R.id.imageLayout);
        }
    }

    private boolean isUserSender(String senderEmail){
        return senderEmail.equals(loginState.getUserEmail(context));
    }

    private void showContextMenu(View view, String sender, String docId) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.campus_context_menu);// Define menu in res/menu/context_menu.xml

        if (sender.equals(loginState.getUserEmail(context)) && loginState.getUserRole(context).equals("admin")){
            MenuItem deleteItem = popupMenu.getMenu().findItem(R.id.menu_delete);
            MenuItem editItem = popupMenu.getMenu().findItem(R.id.menu_edit);
            if (deleteItem != null) {
                deleteItem.setVisible(true);
                deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
            if (editItem != null) {
                editItem.setVisible(true);
                editItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                // Handle Delete Item
                deleteMessage(docId);
                return true;
            }
            if (item.getItemId() == R.id.menu_edit){
                callback.passMessageDocId(docId);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void deleteMessage(String docId) {

        if (adapterMode.equals(activityCampusActivity.campusActivity)){
            db.collection("campus activity").document(docId)
                    .delete()
                    .addOnSuccessListener( unused -> {
                        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener( e -> {
                        Toast.makeText(context, "Error While Deleting", Toast.LENGTH_SHORT).show();
                    });
        }else {
            db.collection(classActivityCollection).document(docId)
                    .delete()
                    .addOnSuccessListener( unused -> {
                        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener( e -> {
                        Toast.makeText(context, "Error While Deleting", Toast.LENGTH_SHORT).show();
                    });
        }


    }

    public interface editMessageCallback{
        void passMessageDocId(String docID);
    }
}
