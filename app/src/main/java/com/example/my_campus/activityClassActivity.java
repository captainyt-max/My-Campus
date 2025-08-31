package com.example.my_campus;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activityClassActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final utility ut = new utility();

    private List<campusActivityItem> classActivityItemsList = new ArrayList<>();
    private campusActivityAdapter adapter;

    private ConstraintLayout btnSend, editIndicator;

    // Reply preview
    private ConstraintLayout replyIndicator;
    private TextView tvReplyTo, tvReplyMessage;
    private ImageView btnCancelReply;

    private EditText messageInput;
    private ImageView btnCancelEdit;

    private String editMessageDocID;
    private DocumentReference editMessageDoc;

    private boolean isReplying = false;
    private String replyText = null, replySenderName = null;

    private String classActivityCollection;  // e.g. "class activity cse first"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_campus); // ✅ same layout reused
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView btnBack = findViewById(R.id.btnBackCampus);
        btnSend = findViewById(R.id.btnSend);
        RecyclerView messageView = findViewById(R.id.campusActivityRecyclerView);
        ConstraintLayout messageInputParent = findViewById(R.id.messageInputParent);
        messageInput = findViewById(R.id.messageInputCampus);
        editIndicator = findViewById(R.id.editIndicator);
        btnCancelEdit = findViewById(R.id.btnCancelEdit);
        TextView activityTitle = findViewById(R.id.activityTitle);

        replyIndicator = findViewById(R.id.replyIndicatorCampus);
        tvReplyTo = findViewById(R.id.tvReplyTo);
        tvReplyMessage = findViewById(R.id.tvReplyQuestion);
        btnCancelReply = findViewById(R.id.btnCancelReply);

        btnBack.setOnClickListener(click -> finish());
        activityTitle.setText("Class Activity");

        // 🔽 Build collection name based on branch + year
        Map<String, String> branchMap = new HashMap<>();
        branchMap.put("Computer Science & Engineering", "cse");
        branchMap.put("Civil Engineering", "civil");
        branchMap.put("Automobile Engineering", "auto");
        branchMap.put("Electrical Engineering", "electrical");
        branchMap.put("Electronics Engineering", "electronic");
        branchMap.put("Mechanical Engineering", "mech");

        Map<String, String> yearMap = new HashMap<>();
        yearMap.put("First Year", "first");
        yearMap.put("Second Year", "second");
        yearMap.put("Third Year", "third");

        classActivityCollection = "class activity";

        messageInputParent.setVisibility(View.VISIBLE); // ✅ All students can send

        btnSend.setOnClickListener(click -> {
            ut.clickAnimation(click);
            if (editIndicator.getVisibility() == View.VISIBLE) {
                sendEditedMessage(editMessageDocID);
            } else {
                sendMessage();
            }
        });

        btnCancelEdit.setOnClickListener(click -> {
            ut.clickAnimation(click);
            editIndicator.setVisibility(View.GONE);
            messageInput.setText("");
        });

        btnCancelReply.setOnClickListener(v -> {
            hideReplyPreview();
            isReplying = false;
            replyText = null;
            replySenderName = null;
        });

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightGrey));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.lightGrey));

        // RecyclerView + adapter
        adapter = new campusActivityAdapter(this, classActivityItemsList,
                "class_activity", classActivityCollection,
                editMessageDocID -> {
                    this.editMessageDocID = editMessageDocID;
                    getMessageToEdit(editMessageDocID);
                });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messageView.setLayoutManager(layoutManager);
        messageView.setAdapter(adapter);

        // Swipe to reply
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder vh) { return 0.4f; }
            @Override public float getSwipeEscapeVelocity(float d) { return Float.MAX_VALUE; }
            @Override public float getSwipeVelocityThreshold(float d) { return Float.MAX_VALUE; }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                float max = vh.itemView.getWidth() * 0.2f;
                if (dX > max) dX = max;
                super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive);
            }

            @Override public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder v1, @NonNull RecyclerView.ViewHolder v2) { return false; }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                campusActivityItem item = classActivityItemsList.get(position);

                replyText = item.getMessage();
                String replySenderEmail = item.getSentBy();
                isReplying = true;

                tvReplyMessage.setText(replyText);
                replyIndicator.setVisibility(View.VISIBLE);

                db.collection("users").document(replySenderEmail)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists() && documentSnapshot.contains("name")) {
                                replySenderName = documentSnapshot.getString("name");
                            } else {
                                replySenderName = replySenderEmail;
                            }
                            tvReplyTo.setText(replySenderName);
                        })
                        .addOnFailureListener(e -> {
                            replySenderName = replySenderEmail;
                            tvReplyTo.setText(replySenderName);
                        });

                messageInput.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) imm.showSoftInput(messageInput, InputMethodManager.SHOW_IMPLICIT);

                adapter.notifyItemChanged(position);
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(messageView);

        // 🔽 Load messages
        Query q = db.collection("class activity")
                .whereEqualTo("branch", loginState.getUserBranch(this))
                .whereEqualTo("year",   loginState.getUserYear(this))
                .orderBy("timestamp", Query.Direction.ASCENDING); // keep orderBy LAST


        q.addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.d("classMessage", e.getMessage());
                        return;
                    }
                    if (snapshots != null) {
                        int prev = classActivityItemsList.size();
                        classActivityItemsList.clear();
                        for (DocumentSnapshot document : snapshots.getDocuments()) {
                            String message = document.getString("message");
                            String sentBy = document.getString("sentBy");
                            String sentTime = document.getString("sentTime");
                            String docId = document.getId();
                            String rMsg = document.getString("replyMessage");
                            String rSender = document.getString("replySender");

                            classActivityItemsList.add(new campusActivityItem(sentBy, message, sentTime, docId, rMsg, rSender));
                        }
                        adapter.notifyDataSetChanged();
                        if (classActivityItemsList.size() > prev) {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                if (adapter.getItemCount() > 0) {
                                    messageView.smoothScrollToPosition(adapter.getItemCount() - 1);
                                }
                            }, 700);
                        }
                    }
                });
    }

    private void hideReplyPreview() {
        replyIndicator.setVisibility(View.GONE);
        tvReplyTo.setText("");
        tvReplyMessage.setText("");
    }

    private void sendMessage() {
        if (!ut.isNetworkAvailable(this)) return;
        String message = messageInput.getText().toString().trim();
        if (message.isEmpty()) return;

        btnSend.setEnabled(false);
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("message", message);
        messageData.put("sentBy", loginState.getUserEmail(this));
        messageData.put("sentTime", ut.getDateTime());
        messageData.put("timestamp", System.currentTimeMillis());
        messageData.put("position", classActivityItemsList.size());
        messageData.put("branch", loginState.getUserBranch(this));
        messageData.put("year", loginState.getUserYear(this));

        if (isReplying && replyText != null && !replyText.isEmpty()) {
            messageData.put("replyMessage", replyText);
            messageData.put("replySender", replySenderName);
            isReplying = false;
            hideReplyPreview();
            replyText = null;
            replySenderName = null;
        }

        db.collection(classActivityCollection).add(messageData)
                .addOnSuccessListener(unused -> {
                    messageInput.setText("");
                    ut.playSentSound(this);
                    btnSend.setEnabled(true);
                    Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    btnSend.setEnabled(true);
                    Toast.makeText(this, "Error while sending message", Toast.LENGTH_SHORT).show();
                });
    }

    private void getMessageToEdit(String editMessageDocID) {
        editMessageDoc = db.collection(classActivityCollection).document(editMessageDocID);
        editMessageDoc.get()
                .addOnSuccessListener(snap -> {
                    if (snap != null && snap.exists()) {
                        messageInput.setText(snap.getString("message"));
                        messageInput.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        if (imm != null) imm.showSoftInput(messageInput, InputMethodManager.SHOW_IMPLICIT);
                        editIndicator.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void sendEditedMessage(String docId) {
        if (!ut.isNetworkAvailable(this)) return;
        String message = messageInput.getText().toString().trim();
        if (message.isEmpty()) return;

        btnSend.setEnabled(false);
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("message", message);
        messageData.put("sentTime", "edited " + ut.getDateTime());

        editMessageDoc.update(messageData)
                .addOnSuccessListener(unused -> {
                    messageInput.setText("");
                    ut.playSentSound(this);
                    btnSend.setEnabled(true);
                    editIndicator.setVisibility(View.GONE);
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    btnSend.setEnabled(true);
                    Toast.makeText(this, "Error while updating message", Toast.LENGTH_SHORT).show();
                });
    }
}
