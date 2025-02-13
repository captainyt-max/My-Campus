package com.example.my_campus.Fragments;

import com.bumptech.glide.Glide;
import com.example.my_campus.activityCampusActivity;
import com.example.my_campus.utility;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;

import com.example.my_campus.activityHelp;
import com.example.my_campus.noticeListAdapter;
import com.example.my_campus.noticeListItems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_campus.R;
import com.example.my_campus.loginState;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class fragmentHomepage extends Fragment {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ArrayList <noticeListItems> noticeItemsArrayList = new ArrayList<noticeListItems>();
    private noticeListAdapter adapter;
    private TextView classActivityMessage, classActivityUpdated, question, questionReply, likeCount;
    private utility ut = new utility();
    private String helpDocId;
    private String classActivityCollection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        TextView campusActivityMessage = view.findViewById(R.id.campusActivityMessage);
        TextView campusActivityUpdated = view.findViewById(R.id.campusActivityUpdated);
        ImageView btnCampusInfo = view.findViewById(R.id.btnCampusInfo);

        TextView classActivityMessage = view.findViewById(R.id.classActivityMessage);
        TextView classActivityUpdated = view.findViewById(R.id.classActivityUpdated);
        ImageView btnClassInfo = view.findViewById(R.id.btnClassInfo);

        //Setting realtime message update to campus activity
        firestore.collection("campus activity")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener( (snapshot, e) ->{
                    if (e!= null){
                        return;
                    }
                    if (snapshot!=null ){
                        if ( !snapshot.getDocuments().isEmpty()) {
                            DocumentSnapshot messageDoc = snapshot.getDocuments().get(0);
                            if (messageDoc != null && messageDoc.exists()){
                                btnCampusInfo.setVisibility(View.VISIBLE);
                                campusActivityMessage.setText(messageDoc.getString("message"));
                                campusActivityUpdated.setText(messageDoc.getString("sentTime"));
                                String sender = messageDoc.getString("sentBy");
                                firestore.collection("users").document(sender)
                                                .addSnapshotListener( (snap, ex) -> {
                                                    if (e !=null ){
                                                        return;
                                                    }
                                                    else {
                                                        if (snap != null && snap.contains("profileImage")){
                                                            Glide.with(requireContext())
                                                                    .load(snap.getString("profileImage"))
                                                                    .circleCrop()
                                                                    .error(R.drawable.ic_default_user)
                                                                    .placeholder(R.drawable.ic_default_user)
                                                                    .into(btnCampusInfo);
                                                        }
                                                    }
                                                });
                                btnCampusInfo.setOnClickListener( click -> {
                                    ut.showProfile(requireContext(), messageDoc.getString("sentBy"));
                                });
                            }
                        }
                        else {
                            btnCampusInfo.setVisibility(View.GONE);
                            campusActivityMessage.setText("No message yet");
                            campusActivityUpdated.setText("");
                        }
                    }
                });


        Map <String, String> branchMap = new HashMap<>();
        branchMap.put("Computer Science & Engineering", "cse");
        branchMap.put("Civil Engineering", "civil");
        branchMap.put("Automobile Engineering", "auto");
        branchMap.put("Electrical Engineering", "electrical");
        branchMap.put("Electronics Engineering", "electronic");
        branchMap.put("Mechanical Engineering", "mech");

        Map <String, String> yearMap = new HashMap<>();
        yearMap.put("First Year", "first");
        yearMap.put("Second Year", "second");
        yearMap.put("Third Year", "third");

        classActivityCollection = "class activity " + branchMap.get(loginState.getUserBranch(requireContext()))+ " " + yearMap.get(loginState.getUserYear(requireContext()));

        //Setting realtime message on class activity section
        firestore.collection(classActivityCollection)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener( (snapshot, e) ->{
                    if (e!= null){
                        return;
                    }
                    if (snapshot!=null ){
                        if ( !snapshot.getDocuments().isEmpty()) {
                            DocumentSnapshot messageDoc = snapshot.getDocuments().get(0);
                            if (messageDoc != null && messageDoc.exists()){
                                btnClassInfo.setVisibility(View.VISIBLE);
                                classActivityMessage.setText(messageDoc.getString("message"));
                                classActivityUpdated.setText(messageDoc.getString("sentTime"));
                                String sender = messageDoc.getString("sentBy");
                                firestore.collection("users").document(sender)
                                        .addSnapshotListener( (snap, ex) -> {
                                            if (e !=null ){
                                                return;
                                            }
                                            else {
                                                if (snap != null && snap.contains("profileImage")){
                                                    Glide.with(requireContext())
                                                            .load(snap.getString("profileImage"))
                                                            .circleCrop()
                                                            .error(R.drawable.ic_default_user)
                                                            .placeholder(R.drawable.ic_default_user)
                                                            .into(btnClassInfo);
                                                }
                                            }
                                        });
                                btnClassInfo.setOnClickListener( click -> {
                                    ut.showProfile(requireContext(), messageDoc.getString("sentBy"));
                                });
                            }
                        }
                        else {
                            btnClassInfo.setVisibility(View.GONE);
                            classActivityMessage.setText("No message yet");
                            classActivityUpdated.setText("");
                        }
                    }
                });
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        classActivityMessage = view.findViewById(R.id.classActivityMessage);
        classActivityUpdated = view.findViewById(R.id.classActivityUpdated);
        ListView noticeListView = view.findViewById(R.id.noticeListView);
        LinearLayout helpTile = view.findViewById(R.id.helpTile);
        LinearLayout campusActivityTile = view.findViewById(R.id.campusActivityTile);
        LinearLayout classActivityTile = view.findViewById(R.id.classActivityTile);
        TextView editTextHelpHome = view.findViewById(R.id.editTextHelpHome);
        question = view.findViewById(R.id.tvQuestion);
        questionReply = view.findViewById(R.id.tvQuestionReply);
        likeCount = view.findViewById(R.id.questionLikeCount);
        ImageView btnLikeQuestion = view.findViewById(R.id.btnLikeQuestion);
        TextView noticeIsEmpty = view.findViewById(R.id.noticeIsEmpty);

        editTextHelpHome.setOnClickListener( click -> {
            Intent intent = new Intent( requireContext(), activityHelp.class);
            intent.putExtra("key", "clickedOnEditText");
            startActivity(intent);
        });

        campusActivityTile.setOnClickListener( click -> {
            Intent intent = new Intent(requireContext(), activityCampusActivity.class);
            intent.putExtra("key", "campus_activity");
            startActivity(intent);
        });

        classActivityTile.setOnClickListener(click ->{
            Intent intent = new Intent(requireContext(), activityCampusActivity.class);
            intent.putExtra("key", "class_activity");
            startActivity(intent);
        });

        // Initialize the notice list adapter
        adapter = new noticeListAdapter(requireContext(), noticeItemsArrayList);
        noticeListView.setAdapter(adapter);


        helpTile.setOnClickListener( click -> {
            Intent intent = new Intent(requireContext(), activityHelp.class);
            startActivity(intent);
        });

        firestore.collection("notice pdfs")
                .orderBy("uploadId", Query.Direction.DESCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null){
                        return;
                    }
                    if (queryDocumentSnapshots.getDocuments().isEmpty()){
                        noticeIsEmpty.setVisibility(View.VISIBLE);
                    }
                    else {
                        noticeIsEmpty.setVisibility(View.GONE);
                    }
                    noticeItemsArrayList.clear();
                    if (queryDocumentSnapshots!=null){
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            String fileName = doc.getString("fileName");
                            String uploadDate = doc.getString("uploadDate");
                            String downloadUrl = doc.getString("downloadUrl");
                            String documentId = doc.getId();
                            String uploadTime = doc.getString("uploadTime");
                            String fileSize = doc.getString("fileSize");
                            String uploadedBy = doc.getString("uploadedBy");
                            noticeItemsArrayList.add(new noticeListItems(fileName, uploadDate,downloadUrl, documentId, uploadTime, uploadedBy, fileSize));
                        }
                        adapter.notifyDataSetChanged();
                    }

                });

        //Setting up help tile
        firestore.collection("help")
                .orderBy("questionId", Query.Direction.DESCENDING)
                .addSnapshotListener( (querySnapshot, e) ->{
                    if (e!=null){
                        return;
                    }
                    if (querySnapshot!=null  && !querySnapshot.isEmpty()){
                        DocumentSnapshot helpDoc = querySnapshot.getDocuments().get(0);
                        if (helpDoc!= null && helpDoc.exists()){
                            likeCount.setVisibility(View.VISIBLE);
                            btnLikeQuestion.setVisibility(View.VISIBLE);
                            question.setText(helpDoc.getString("question"));
                            likeCount.setText(String.valueOf(helpDoc.getLong("likeCount")));
                            helpDocId = helpDoc.getId();
                            firestore.collection("help reply").document(helpDocId)
                                    .addSnapshotListener((snapshot, exception) -> {
                                        if (exception != null){
                                            return;
                                        }
                                        if (snapshot != null && snapshot.exists()){
                                            long replyCount = snapshot.getLong("replyCount");
                                            if (replyCount > 0 ){
                                                questionReply.setText("Reply : "+snapshot.getString("reply"+replyCount));
                                            }else {
                                                questionReply.setText("No reply yet");
                                            }
                                        }
                                    });

                            List<String> likedBy = (List<String>)  helpDoc.get("likedBy");
                            if (likedBy != null && likedBy.contains(loginState.getUserEmail(requireContext()))) {
                                //user has liked already
                                btnLikeQuestion.setColorFilter(null);
                                btnLikeQuestion.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                                btnLikeQuestion.setOnClickListener( click -> {
                                    removeUserFromLike(helpDoc.getId());
                                    long likeCount = 0;
                                    if (helpDoc.contains("likeCount")){
                                        likeCount = helpDoc.getLong("likeCount");
                                        likeCount -= 1;
                                        firestore.collection("help").document(helpDoc.getId()).update("likeCount", likeCount)
                                                .addOnFailureListener( ex -> {
                                                    Toast.makeText(requireContext(), "Error : "+ ex.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                            }
                            else {
                                //user has not liked
                                btnLikeQuestion.setColorFilter(null);
                                btnLikeQuestion.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                                btnLikeQuestion.setOnClickListener( click -> {
                                    addUserToLike(helpDoc.getId());
                                    long likeCount = 0;
                                    if (helpDoc.contains("likeCount")){
                                        likeCount = helpDoc.getLong("likeCount");
                                        likeCount += 1;
                                        firestore.collection("help").document(helpDoc.getId()).update("likeCount", likeCount)
                                                .addOnSuccessListener( unused -> {

                                                })
                                                .addOnFailureListener( ex -> {
                                                    Toast.makeText(requireContext(), "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                            }
                        }
                    }
                    else {
                        likeCount.setVisibility(View.INVISIBLE);
                        btnLikeQuestion.setVisibility(View.GONE);
                        question.setText("No question yet");
                        questionReply.setText("No reply yet");
                    }
                });
    }


    private void addUserToLike(String documentId){
        firestore.collection("help").document(documentId)
                .update("likedBy", FieldValue.arrayUnion(loginState.getUserEmail(requireContext())))
                .addOnSuccessListener( unused -> {
                    Toast.makeText(requireContext(), "Like added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Unable to add like "+e.getMessage() , Toast.LENGTH_SHORT).show();
                });
    }

    private void removeUserFromLike(String documentId){
        firestore.collection("help").document(documentId)
                .update("likedBy", FieldValue.arrayRemove(loginState.getUserEmail(requireContext())))
                .addOnSuccessListener( unused -> {
                    Toast.makeText(requireContext(), "Like Removed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Unable To remove like : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




}