package com.example.my_campus;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HelpListAdapter extends RecyclerView.Adapter<HelpListAdapter.HelpViewHolder> {

    private final Context context;
    private final ArrayList<helpListItem> helpListItems;
    private FirebaseFirestore db;
    private OnReplyClickListener listener;
    private utility ut = new utility();
    private long replyCount;
    private final Map<String, Boolean> userLikeStatusMap = new HashMap<>();

    // Constructor to initialize context and the data list
    public HelpListAdapter(Context context, ArrayList<helpListItem> helpListItems, OnReplyClickListener listener) {
        setHasStableIds(true);
        this.context = context;
        this.helpListItems = helpListItems;
        this.db = FirebaseFirestore.getInstance();
        this.listener = listener;
    }
    // ViewHolder class to hold item views
    public static class HelpViewHolder extends RecyclerView.ViewHolder {
        TextView questionBody;
        TextView askedBy;
        TextView askedTime, tvLikeCount, btnReply, btnShowReply;
        ImageView askedByProfileImage, btnLike;
        RecyclerView replyView;
        ConstraintLayout questionLayout;

        public HelpViewHolder(@NonNull View itemView) {
            super(itemView);
            questionBody = itemView.findViewById(R.id.questionBody);
            askedBy = itemView.findViewById(R.id.askedBy);
            askedTime = itemView.findViewById(R.id.askedTime);
            askedByProfileImage = itemView.findViewById(R.id.askedByProfileImage);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnReply = itemView.findViewById(R.id.btnReply);
            btnShowReply = itemView.findViewById(R.id.btnShowReply);
            replyView = itemView.findViewById(R.id.replyRecyclerView);
            questionLayout = itemView.findViewById(R.id.questionLayout);
        }
    }
    @NonNull
    @Override
    public HelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View view = LayoutInflater.from(context).inflate(R.layout.help_list_item, parent, false);
        return new HelpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpViewHolder holder, int position) {
        // Bind the data to each view
        helpListItem currentItem = helpListItems.get(position);
        holder.questionBody.setText(currentItem.question);
        holder.askedTime.setText(currentItem.askedTime);
        if (currentItem.askedBy.equals(loginState.getUserEmail(context))){
            holder.questionLayout.setBackgroundResource(R.drawable.registration_details_background);
        }

        //Fetch like count and liked status
        DocumentReference helpDocRef = db.collection("help").document(currentItem.documentId);
        helpDocRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                // Handle errors if needed
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                long likeCount = 0;
                if (snapshot.contains("likeCount")) {
                    likeCount = snapshot.getLong("likeCount");
                    holder.tvLikeCount.setText(String.valueOf(likeCount));
                }
                List<String> likedBy = (List<String>) snapshot.get("likedBy");
                if (likedBy != null && likedBy.contains(loginState.getUserEmail(context))) {
                    holder.btnLike.setColorFilter(null);
                    if (holder.btnLike.getColorFilter() == null || !isColorFilterBlue(holder.btnLike)) {
                        holder.btnLike.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                    }
                }
                else {
                    holder.btnLike.setColorFilter(null);
                    if (holder.btnLike.getColorFilter() == null || isColorFilterBlue(holder.btnLike)) {
                        holder.btnLike.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    }
                }
            }
        });
        //Fetch reply Count and set show reply button visibility
        db.collection("help reply")
                .document(currentItem.documentId)
                .addSnapshotListener( (snapshot, e) -> {
                    if (e!=null){
                        return;
                    }
                    if (snapshot != null && snapshot.exists()){
                        if (snapshot.contains("replyCount")){
                            replyCount = snapshot.getLong("replyCount");
                        }
                        if (replyCount > 0){
                            holder.btnShowReply.setVisibility(View.VISIBLE);
                            holder.btnShowReply.setText("Show replies ("+replyCount+")");
                        }
                        else{
                            holder.btnShowReply.setVisibility(View.GONE);
                        }
                        if (holder.replyView.getVisibility() == View.VISIBLE){
                            holder.btnShowReply.setText("Hide Replies");
                        }
                    }
                });
        // Fetch sender details from Firestore
        DocumentReference userDoc = db.collection("users").document(currentItem.askedBy);
        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String askedByName = documentSnapshot.getString("name");
                String profileImageUrl = documentSnapshot.getString("profileImage");
                // Set user name if available
                if (askedByName != null) {
                    holder.askedBy.setText(askedByName);
                }
                // Load profile image using Glide if available
                if (profileImageUrl != null && !profileImageUrl.isEmpty()){
                    ut.setProfileImage(context, profileImageUrl, holder.askedByProfileImage);
                }
            }
        }).addOnFailureListener(e -> {
            // Handle errors if needed
        });

        holder.btnLike.setOnClickListener( click -> {
            ut.clickAnimation(click);
            helpDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot != null && documentSnapshot.exists()){
                    List<String> likedBy = (List<String>) documentSnapshot.get("likedBy");
                    if (likedBy != null && likedBy.contains(loginState.getUserEmail(context))){
                        //user has already liked
                        removeUserFromLike(currentItem.documentId);
                        long likeCount = 0;
                        if (documentSnapshot.getLong("likeCount") != null){
                            likeCount = documentSnapshot.getLong("likeCount");
                            likeCount -= 1;
                            helpDocRef.update("likeCount", likeCount)
                                    .addOnFailureListener( e -> {
                                        Toast.makeText(context, "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                    else {
                        //user has not liked
                        addUserToLike(currentItem.documentId);
                        long likeCount = 0;
                        if (documentSnapshot.getLong("likeCount") != null){
                            likeCount = documentSnapshot.getLong("likeCount");
                            likeCount += 1;
                            helpDocRef.update("likeCount", likeCount)
                                    .addOnSuccessListener( unused -> {

                                    })
                                    .addOnFailureListener( e -> {
                                        Toast.makeText(context, "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                }
            });
        });
        holder.btnReply.setOnClickListener(click -> {
            ut.clickAnimation(holder.btnReply);
            if (listener != null) {
                listener.onReplyClicked(currentItem.question, currentItem.askedBy, currentItem.documentId);
            }
        });

        List<questionReplyItem> replyList = new ArrayList<>();
        questionReplyAdapter replyAdapter = new questionReplyAdapter(context, replyList);
        holder.replyView.setAdapter(replyAdapter);
        holder.replyView.setLayoutManager(new LinearLayoutManager(context));
        db.collection("help reply").document(currentItem.documentId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null){
                        return;
                    }
                    if (snapshot.exists()) {
                        replyList.clear();
                        long index = snapshot.getLong("replyCount");
                        while (snapshot.contains("reply" + index)) {
                            String replyText = snapshot.getString("reply" + index);
                            String replyBy = snapshot.getString("reply" + index + "By");
                            String replyTime = snapshot.getString("reply" + index + "Time");

                            if (replyText != null) {
                                replyList.add(new questionReplyItem(replyText, replyBy, replyTime));
                            }
                            index--;
                        }
                        replyAdapter.notifyDataSetChanged();
                    }
                });

        holder.btnShowReply.setOnClickListener( click -> {
            if (holder.replyView.getVisibility() == View.VISIBLE){
                holder.replyView.setVisibility(View.GONE);
                getReplyCount(currentItem.documentId, count -> {
                    holder.btnShowReply.setText("Show replies ("+count+")");
                });
            }
            else{
                holder.replyView.setVisibility(View.VISIBLE);
                holder.btnShowReply.setText("Hide replies");
            }
        });

        holder.askedByProfileImage.setOnClickListener( click -> {
            ut.clickAnimation(click);
            ut.showProfile(context, currentItem.askedBy);
        });

        holder.itemView.setOnLongClickListener(view -> {
            showContextMenu(view, currentItem.askedBy, currentItem.documentId);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return helpListItems.size();
    }

    @Override
    public long getItemId(int position) {
        return helpListItems.get(position).documentId.hashCode();
    }

    private void addUserToLike(String documentId){
        db.collection("help").document(documentId)
                .update("likedBy", FieldValue.arrayUnion(loginState.getUserEmail(context)))
                .addOnSuccessListener( unused -> {
                    Toast.makeText(context, "Like added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Unable to add like "+e.getMessage() , Toast.LENGTH_SHORT).show();
                });
    }

    private void removeUserFromLike(String documentId){
        db.collection("help").document(documentId)
                .update("likedBy", FieldValue.arrayRemove(loginState.getUserEmail(context)))
                .addOnSuccessListener( unused -> {
                    Toast.makeText(context, "Like Removed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Unable To remove like : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void getReplyCount(String docId, ReplyCountCallback callback) {
        db.collection("help reply").document(docId).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot != null && snapshot.exists()) {
                        long count = snapshot.contains("replyCount") ? snapshot.getLong("replyCount") : 0;
                        callback.onCallback(count);
                    } else {
                        callback.onCallback(0); // Handle cases when the document doesn't exist
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    callback.onCallback(0); // Handle errors gracefully
                });
    }
    // Callback interface
    public interface ReplyCountCallback {
        void onCallback(long count);
    }
    private boolean isColorFilterBlue(ImageView imageView) {
        // Assuming Color.BLUE is the target color
        ColorFilter filter = imageView.getColorFilter();
        return filter != null;
    }

    private void showContextMenu(View view, String askedBy, String docId) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.help_context_menu);// Define menu in res/menu/context_menu.xml

        if (askedBy.equals(loginState.getUserEmail(context))){
            MenuItem deleteItem = popupMenu.getMenu().findItem(R.id.menu_delete);
            if (deleteItem != null) {
                deleteItem.setVisible(true);
                deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                // Handle Delete Item
                deleteHelp(docId);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void deleteHelp(String docId) {
        db.collection("help").document(docId)
                .delete()
                .addOnSuccessListener( unused -> {

                }).addOnFailureListener( e -> {
                    Toast.makeText(context, "Delete error", Toast.LENGTH_SHORT).show();
                });
        db.collection("help reply").document(docId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener( e -> {
                    Toast.makeText(context, "Delete error", Toast.LENGTH_SHORT).show();
                });
    }
}

