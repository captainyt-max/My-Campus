package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.my_campus.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class fragmentHomepage extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public fragmentHomepage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        TextView campusActivityMessage = view.findViewById(R.id.campusActivityMessage);
        TextView campusActivityUpdated = view.findViewById(R.id.campusActivityUpdated);

        //Setting realtime message update to campus activity
        DocumentReference docRef = firestore.collection("homePage").document("campusActivity");
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

                // Update the TextViews with the new data
                if (!message.isEmpty()){
                    campusActivityMessage.setText(message);
                    campusActivityUpdated.setText("updated " + updateTime);
                }
                else {
                    campusActivityMessage.setText("This place is left empty");
                }


            } else {
                Log.d("Firestore", "No such document");
            }
        });


        return view;
    }
}