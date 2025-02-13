package com.example.my_campus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activityHelp extends AppCompatActivity {
    private EditText helpInput;
    private utility ut = new utility();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList <helpListItem> helpListItemArrayList = new ArrayList<helpListItem>();
    private HelpListAdapter helpListAdapter;
    private RecyclerView helpRecyclerView;
    private String replyMessageDocId;
    private ConstraintLayout replyIndicator, btnSend;
    private TextView tvReplyTo, tvReplyQuestion;
    private ImageView btnCancelReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set status bar and navigation bar color
        Window window = getWindow();
        // Set status bar color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightGrey));
        // Set navigation bar color
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.lightGrey));

        ImageView btnBack = findViewById(R.id.btnBackHelp);
        btnSend = findViewById(R.id.btnSend);
        helpInput  = findViewById(R.id.helpInput);
        helpRecyclerView = findViewById(R.id.helpRecyclerView);
        replyIndicator = findViewById(R.id.replyIndicator);
        tvReplyTo = findViewById(R.id.tvReplyTo);
        tvReplyQuestion = findViewById(R.id.tvReplyQuestion);
        btnCancelReply = findViewById(R.id.btnCancelReply);

        btnBack.setOnClickListener( click -> {
            finish();
        });

        btnSend.setOnClickListener( click -> {
            ut.clickAnimation(btnSend);
            if (!ut.isNetworkAvailable(this)){
                return;
            }
            sendHelpMessage();
        });

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        if (key != null && key.equals("clickedOnEditText")){
            helpInput.requestFocus();
        }

        helpListAdapter = new HelpListAdapter(this, helpListItemArrayList, (question, askedBy, docId) -> {
                this.replyMessageDocId = docId;
                db.collection("users").document(askedBy).get()
                                .addOnSuccessListener( snapshot -> {
                                    if (snapshot.exists()){
                                        String name = snapshot.getString("name");
                                        tvReplyTo.setText(name);
                                    }
                                });
                replyIndicator.setVisibility(View.VISIBLE);
                tvReplyQuestion.setText(question);
                helpInput.requestFocus();
                // Open the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(helpInput, InputMethodManager.SHOW_IMPLICIT);
                helpInput.setHint("Add a reply");
        });
        helpRecyclerView.setAdapter(helpListAdapter);
        helpRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnCancelReply.setOnClickListener( click -> {
            replyIndicator.setVisibility(View.GONE);
            helpInput.setText("");
            helpInput.setHint("Ask a question");
        });

        db.collection("help")
                .orderBy("questionId", Query.Direction.DESCENDING)
                .addSnapshotListener((QueryDocumentSnapshot, e) -> {
                    if(e != null){
                        Toast.makeText(this, "Error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (QueryDocumentSnapshot != null){
                        helpListItemArrayList.clear();
                        for (DocumentSnapshot doc : QueryDocumentSnapshot.getDocuments()){
                            String question = doc.getString("question");
                            String askedByEmail = doc.getString("askedBy");
                            String askedByName;
                            String askedTime = doc.getString("askedTime");
                            String documentId = doc.getId();

                            boolean hasUserLiked = false;
                            helpListItemArrayList.add(new helpListItem(question, askedByEmail, askedTime, documentId));
                        }
                        helpListAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void sendHelpMessage(){
        if (!ut.isNetworkAvailable(this)){
            return;
        }
        String message = helpInput.getText().toString();
        if (message.isEmpty()){
            return;
        }
        btnSend.setEnabled(false);
        if (replyIndicator.getVisibility() == View.VISIBLE){
            DocumentReference replyDoc = db.collection("help reply").document(replyMessageDocId);
                    replyDoc.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot!= null && documentSnapshot.exists()){
                            long replyCount = documentSnapshot.getLong("replyCount");
                            replyCount += 1;
                            Map <String, Object> replyData = new HashMap<>();
                            replyData.put("replyCount", replyCount);
                            replyData.put("reply"+replyCount, message);
                            replyData.put("reply"+replyCount+"By", loginState.getUserEmail(this));
                            replyData.put("reply"+replyCount+"Time", ut.getDateTime());
                            replyDoc.update(replyData).addOnSuccessListener( unused -> {
                                helpInput.setText("");
                                btnSend.setEnabled(true);
                                helpInput.setHint("Ask a question");
                                replyIndicator.setVisibility(View.GONE);
                                ut.playSentSound(this);
                                Toast.makeText(this, "Reply added", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener( e -> {
                                Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }).addOnFailureListener( e -> {
                        Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            return;
        }

        List<String> initialLikeArray = new ArrayList<>();
        Map<String, Object> helpData = new HashMap<>();
        helpData.put("question", message);
        helpData.put("askedBy", loginState.getUserEmail(this));
        helpData.put("askedTime", ut.getDateTime());
        helpData.put("questionId", System.currentTimeMillis());
        helpData.put("likeCount", 0);
        helpData.put("likedBy", initialLikeArray);

        Map<String, Object> replyData = new HashMap<>();
        replyData.put("replyCount", 0);

        db.collection("help").add(helpData)
                .addOnSuccessListener( documentReference -> {
                    String docId = documentReference.getId();
                    db.collection("help reply")
                            .document(docId)
                            .set(replyData)
                            .addOnSuccessListener( unused -> {
                                ut.playSentSound(this);
                                helpInput.setText("");
                                btnSend.setEnabled(true);
                            })
                            .addOnFailureListener(e ->{
                                Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                    Toast.makeText(this, "sent", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener( e -> {
                    Toast.makeText(this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}