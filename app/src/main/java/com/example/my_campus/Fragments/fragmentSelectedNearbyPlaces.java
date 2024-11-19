package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_campus.CustomListAdapter;
import com.example.my_campus.NearbyAdapter;
import com.example.my_campus.routineCustomAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.my_campus.R;
import com.example.my_campus.nearbyListItem;

import java.util.ArrayList;
import java.util.List;


public class fragmentSelectedNearbyPlaces extends Fragment {

    TextView tittle;
    ListView listView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_nearby_places, container, false);

        tittle = view.findViewById(R.id.location);
        listView = view.findViewById(R.id.nearby_places_list);

        // getting bundle containing branch name from previous fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            String location = bundle.getString("reference");

            if (location.equals("reference")) {
                tittle.setText("Reference");

                List<String> locationNames = new ArrayList<>();
                List<String> locationUrls = new ArrayList<>();

                // Example for loading reference locations
                db.collection("nearby").document("reference")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.contains("sai")) {
                                    String url = documentSnapshot.getString("sai");
                                    locationUrls.add(url);
                                    locationNames.add("Sai mandir");
                                }
                                if (documentSnapshot.contains("traind")) {
                                    String url = documentSnapshot.getString("traind");
                                    locationUrls.add(url);
                                    locationNames.add("Haldi Ram");
                                }
                                if (documentSnapshot.contains("kurji")) {
                                    String url = documentSnapshot.getString("kurji");
                                    locationUrls.add(url);
                                    locationNames.add("P&M Mall");
                                }
                                if (documentSnapshot.contains("rajiv")) {
                                    String url = documentSnapshot.getString("rajiv");
                                    locationUrls.add(url);
                                    locationNames.add("Rajiv Nagar (Atal Path)");
                                }
                            }else {
                                Toast.makeText(getContext(), "No data found for this location.", Toast.LENGTH_SHORT).show();
                            }

                            NearbyAdapter adapter = new NearbyAdapter(getContext(), locationNames, locationUrls);
                            listView.setAdapter(adapter);

                        });
            }


            if (location.equals("stationary")){
                tittle.setText("Stationary");

                List<String> locationNames = new ArrayList<>();
                List<String> locationUrls = new ArrayList<>();

                // Example for loading reference locations
                db.collection("nearby").document("stationary")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.contains("gyanmandir")) {
                                    String url = documentSnapshot.getString("gyanmandir");
                                    locationUrls.add(url);
                                    locationNames.add("Gyan Mandir");
                                }
                                if (documentSnapshot.contains("jagdamba")) {
                                    String url = documentSnapshot.getString("jagdamba");
                                    locationUrls.add(url);
                                    locationNames.add("Jagdamba");
                                }
                                if (documentSnapshot.contains("santoshi")) {
                                    String url = documentSnapshot.getString("santoshi");
                                    locationUrls.add(url);
                                    locationNames.add("Maa Santoshi");
                                }
                            }else {
                                Toast.makeText(getContext(), "No data found for this location.", Toast.LENGTH_SHORT).show();
                            }

                            NearbyAdapter adapter = new NearbyAdapter(getContext(), locationNames, locationUrls);
                            listView.setAdapter(adapter);

                        });
            }

            if (location.equals("cybercafe")){
                tittle.setText("Cyber Cafe");

                List<String> locationNames = new ArrayList<>();
                List<String> locationUrls = new ArrayList<>();

                // Example for loading reference locations
                db.collection("nearby").document("cyber")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.contains("telecom")) {
                                    String url = documentSnapshot.getString("telecom");
                                    locationUrls.add(url);
                                    locationNames.add("Shubham Singh Telecom");
                                }
                                if (documentSnapshot.contains("sky")) {
                                    String url = documentSnapshot.getString("sky");
                                    locationUrls.add(url);
                                    locationNames.add("Sky Net");
                                }
                                if (documentSnapshot.contains("kurji")) {
                                    String url = documentSnapshot.getString("kurji");
                                    locationUrls.add(url);
                                    locationNames.add("");
                                }
                                if (documentSnapshot.contains("rajiv")) {
                                    String url = documentSnapshot.getString("rajiv");
                                    locationUrls.add(url);
                                    locationNames.add("");
                                }
                            }else {
                                Toast.makeText(getContext(), "No data found for this location.", Toast.LENGTH_SHORT).show();
                            }

                            NearbyAdapter adapter = new NearbyAdapter(getContext(), locationNames, locationUrls);
                            listView.setAdapter(adapter);

                        });
            }

            if (location.equals("hospital")){
                tittle.setText("Hospital");

                List<String> locationNames = new ArrayList<>();
                List<String> locationUrls = new ArrayList<>();

                // Example for loading reference locations
                db.collection("nearby").document("hospital")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.contains("ruban")) {
                                    String url = documentSnapshot.getString("ruban");
                                    locationUrls.add(url);
                                    locationNames.add("Ruban Memorial Hospital");
                                }
                                if (documentSnapshot.contains("chand")) {
                                    String url = documentSnapshot.getString("chand");
                                    locationUrls.add(url);
                                    locationNames.add("Chand memorial Hospital");
                                }
                                if (documentSnapshot.contains("asian")) {
                                    String url = documentSnapshot.getString("asian");
                                    locationUrls.add(url);
                                    locationNames.add("Asian City Hospital");
                                }
                                if (documentSnapshot.contains("sahyog")) {
                                    String url = documentSnapshot.getString("sahyog");
                                    locationUrls.add(url);
                                    locationNames.add("Sahyog Hospital");
                                }
                            }else {
                                Toast.makeText(getContext(), "No data found for this location.", Toast.LENGTH_SHORT).show();
                            }

                            NearbyAdapter adapter = new NearbyAdapter(getContext(), locationNames, locationUrls);
                            listView.setAdapter(adapter);

                        });
            }

            if (location.equals("medical")){
                tittle.setText("Medical Store");List<String> locationNames = new ArrayList<>();
                List<String> locationUrls = new ArrayList<>();

                // Example for loading reference locations
                db.collection("nearby").document("medical")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.contains("sawan")) {
                                    String url = documentSnapshot.getString("sawan");
                                    locationUrls.add(url);
                                    locationNames.add("Sawan Meddical");
                                }
                                if (documentSnapshot.contains("bharat")) {
                                    String url = documentSnapshot.getString("bharat");
                                    locationUrls.add(url);
                                    locationNames.add("Bharat drug Store");
                                }
                                if (documentSnapshot.contains("amitfarma")) {
                                    String url = documentSnapshot.getString("amitfarma");
                                    locationUrls.add(url);
                                    locationNames.add("Amit Farma");
                                }
                                if (documentSnapshot.contains("newlife")) {
                                    String url = documentSnapshot.getString("newlife");
                                    locationUrls.add(url);
                                    locationNames.add("New Life Line Medical Hall");
                                }
                                if (documentSnapshot.contains("pankaj")) {
                                    String url = documentSnapshot.getString("pankaj");

                                    locationUrls.add(url);
                                    locationNames.add("Pankaj Medical Hall");
                                }
                            }else {
                                Toast.makeText(getContext(), "No data found for this location.", Toast.LENGTH_SHORT).show();
                            }

                            NearbyAdapter adapter = new NearbyAdapter(getContext(), locationNames, locationUrls);
                            listView.setAdapter(adapter);

                        });


            }

        }
        return view;
    }

}

