package com.example.my_campus;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class questionReplyAdapter extends RecyclerView.Adapter<questionReplyAdapter.ReplyViewHolder> {

    private Context context;
    private List<questionReplyItem> replyList;
    private utility ut = new utility();

    public questionReplyAdapter(Context context, List<questionReplyItem> replyList) {
        setHasStableIds(true);
        this.replyList = replyList;
        this.context = context;
    }

    // Remove the static keyword
    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView replyBody, replyByName, replyTime;
        ImageView replyByProfileImage;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            replyBody = itemView.findViewById(R.id.replyBody);
            replyByName = itemView.findViewById(R.id.replyByName);
            replyTime = itemView.findViewById(R.id.replyTime);
            replyByProfileImage = itemView.findViewById(R.id.replyByProfileImage);
        }
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_reply_list_item, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        questionReplyItem currentReply = replyList.get(position);
        holder.replyBody.setText(currentReply.replyText);

        holder.replyTime.setText(currentReply.replyTime);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            db.collection("users").document(currentReply.replyBy)
                    .addSnapshotListener((snapshot, e) -> {
                        if (snapshot != null && snapshot.exists()){
                            holder.replyByName.setText(snapshot.getString("name"));
                            Glide.with(context)
                                    .load(snapshot.getString("profileImage"))
                                    .placeholder(R.drawable.ic_default_user)
                                    .error(R.drawable.ic_default_user)
                                    .fitCenter()
                                    .into(holder.replyByProfileImage);
                        }
                    });
        }, 100);


    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    @Override
    public long getItemId(int position) {
        return replyList.get(position).replyTime.hashCode();
    }
}

