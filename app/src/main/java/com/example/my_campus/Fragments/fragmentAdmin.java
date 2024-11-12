package com.example.my_campus.Fragments;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.my_campus.MainActivity.clickAnimation;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_campus.R;

import com.example.my_campus.utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.net.ConnectivityManager;

public class fragmentAdmin extends Fragment {

    private String [] campusCurrentMessageText = new String[1];
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private boolean [] campusMessageUpdated = {true};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

     View view = inflater.inflate(R.layout.fragment_admin, container, false);
     utility ut = new utility();


     TextView campusActivityCurrentMessage = view.findViewById(R.id.campusActivityCurrentMessage);
     ImageView campusActivityDelBtn = view.findViewById(R.id.campusActivityDelBtn);
     ImageView campusActivityEditBtn = view.findViewById(R.id.campusActivityEditBtn);
     TextView campusActivityUpdateTime = view.findViewById(R.id.campusActivityUpdateTime);
     EditText campusActivityNewMessage = view.findViewById(R.id.campusActivtyNewMessage);
     ConstraintLayout campusActivityNewMessageSendBtn = view.findViewById(R.id.campusActivityNewMessageSendBtn);
     ConstraintLayout currentMessageCancelBtn = view.findViewById(R.id.currentMessageCancelBtn);
     ConstraintLayout campusActivityEditMessageSendBtn = view.findViewById(R.id.campusActivityEditMessageSendBtn);

     EditText convertToEditText = new EditText(getActivity());


     firestore = FirebaseFirestore.getInstance();
     firebaseAuth = FirebaseAuth.getInstance();

    campusActivityEditBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //ConstraintLayout campusCurrentMessageLayout = view.findViewById(R.id.campusCurrentMessageLayout);
            convertToEditText.setLayoutParams(campusActivityCurrentMessage.getLayoutParams());
            convertToEditText.setText(campusActivityCurrentMessage.getText());
            campusCurrentMessageText [0] = campusActivityCurrentMessage.getText().toString();

            convertToEditText.setBackgroundResource(R.drawable.message_input_background);
            convertToEditText.setPadding(25,27,25,27);

            ViewGroup parent = (ViewGroup) campusActivityCurrentMessage.getParent();
            int index = parent.indexOfChild(campusActivityCurrentMessage);
            parent.removeView(campusActivityCurrentMessage);
            parent.addView(convertToEditText, index);
            convertToEditText.requestFocus();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.showSoftInput(convertToEditText, InputMethodManager.SHOW_IMPLICIT);

            campusActivityEditBtn.setEnabled(false);
            campusActivityEditBtn.setColorFilter(Color.parseColor("#00a6ff"), PorterDuff.Mode.SRC_IN);
            currentMessageCancelBtn.setVisibility(View.VISIBLE);
            campusActivityEditMessageSendBtn.setVisibility(View.VISIBLE);
        }
    });

    currentMessageCancelBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clickAnimation(view);
            ut.dialogBox(requireContext(), "Are you sure to cancel ?", new utility.DialogCallback() {
                @Override
                public void onConfirm() {
                    campusActivityCurrentMessage.setText(campusCurrentMessageText[0]);

                    ViewGroup parent = (ViewGroup) convertToEditText.getParent();
                    int index = parent.indexOfChild(convertToEditText);
                    parent.removeView(convertToEditText);
                    parent.addView(campusActivityCurrentMessage, index);

                    campusActivityEditMessageSendBtn.setVisibility(View.GONE);
                    currentMessageCancelBtn.setVisibility(View.GONE);
                    campusActivityEditBtn.setEnabled(true);
                    campusActivityEditBtn.clearColorFilter();
                }

                @Override
                public void onCancel() {
                    //Do nothing
                }
            });

        }
    });

    //Connecting to firebase firestore, homepage collection and accessing campusActivity document
    DocumentReference docRef = firestore.collection("homePage").document("campusActivity");


    //updating campus activity message
    campusActivityNewMessageSendBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clickAnimation(view);
            ut.dialogBox(requireContext(), "Are sure to send message ?", new utility.DialogCallback(){
                @Override
                public void onConfirm() {
                    if(!ut.isNetworkAvailable(requireContext())){
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String newMessage = campusActivityNewMessage.getText().toString();
                    if(newMessage.isEmpty()){
                        Toast.makeText(getActivity(), "Please enter a message", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    docRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if(newMessage.equals(document.getString("message"))){
                                    Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM hh:mm a");
                                String capturedTime = dateFormat.format(calendar.getTime());

                                docRef.update("message", newMessage, "updateTime", capturedTime)
                                        .addOnSuccessListener(aVoid -> {
                                            campusActivityNewMessage.setText("");
                                            Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }else{
                            Toast.makeText(getActivity(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancel() {
                    //Do nothing
                }
            });



        }
    });

        //Setting message on current message section
        docRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                // Handle the error
                Log.w("Firestore", "Listen failed.", e);
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Get the updated data
                String message = documentSnapshot.getString("message");
                String updateTime = documentSnapshot.getString("updateTime");
                if(!message.isEmpty()){
                    campusActivityCurrentMessage.setText(message);
                    campusActivityUpdateTime.setText("updated " + updateTime);
                    campusActivityDelBtn.setVisibility(View.VISIBLE);
                    campusActivityEditBtn.setVisibility(View.VISIBLE);
                }
                else {
                    campusActivityCurrentMessage.setText("This place is left empty");
                    campusActivityUpdateTime.setText("updated " + updateTime);
                }
                // Update the TextViews with the new data

            } else {
                Log.d("Firestore", "No such document");
            }
        });

        campusActivityEditMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                ut.dialogBox(requireContext(), "Are sure to update message ?", new utility.DialogCallback(){
                    @Override
                    public void onConfirm() {
                        if(!ut.isNetworkAvailable(requireContext())){
                            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String newMessage = convertToEditText.getText().toString();
                        if(newMessage.isEmpty()){
                            Toast.makeText(getActivity(), "Please enter a message", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        docRef.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(newMessage.equals(document.getString("message"))){
                                        Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM hh:mm a");
                                    String capturedTime = dateFormat.format(calendar.getTime());
                                    docRef.update("message", newMessage, "updateTime", capturedTime)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                campusActivityCurrentMessage.setText(newMessage);

                                                // Switch EditText back to TextView
                                                ViewGroup parent = (ViewGroup) convertToEditText.getParent();
                                                int index = parent.indexOfChild(convertToEditText);
                                                parent.removeView(convertToEditText);
                                                parent.addView(campusActivityCurrentMessage, index);

                                                // Reset visibility and button states
                                                campusActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                currentMessageCancelBtn.setVisibility(View.GONE);
                                                campusActivityEditBtn.setEnabled(true);
                                                campusActivityEditBtn.clearColorFilter();

                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            }else{
                                Toast.makeText(getActivity(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancel() {
                        //do nothing
                    }
                });

            }
        });

        campusActivityDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ut.dialogBox(requireContext(), "Are you sure to delete current message", new utility.DialogCallback() {
                    @Override
                    public void onConfirm() {
                        if(!ut.isNetworkAvailable(requireContext())){
                            Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        docRef.get().addOnCompleteListener( task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    docRef.update("message", "", "updateTime","");
                                    Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                    campusActivityDelBtn.setVisibility(View.GONE);
                                    campusActivityEditBtn.setVisibility(View.GONE);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }
                });


            }
        });


        return view;


    }

//    public boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
}