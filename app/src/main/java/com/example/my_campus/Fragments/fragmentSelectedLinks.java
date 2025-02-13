package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_campus.R;
import com.example.my_campus.linkAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class fragmentSelectedLinks extends Fragment {

    TextView tittle;
    ListView listView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_links, container, false);

        tittle = view.findViewById(R.id.links);
        listView = view.findViewById(R.id.link_places_list);

        // getting bundle containing links of the previous fragment
        Bundle bundle = getArguments();
        if(bundle != null) {
            String links = bundle.getString("link");

            if(links.equals("sbte")){
                tittle.setText("SBTE");

                List<String> linkNames = new ArrayList<>();
                List<String> linkUrls = new ArrayList<>();

                // Example for loading reference locations
                db.collection("link").document("sbte")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.contains("sbte")) {
                                    String url = documentSnapshot.getString("sbte");
                                    linkUrls.add(url);
                                    linkNames.add("Official Portal");
                                }
                                if (documentSnapshot.contains("login")) {
                                    String url = documentSnapshot.getString("login");
                                    linkUrls.add(url);
                                    linkNames.add("Student's Login Portal");
                                }
                            } else {
                                Toast.makeText(getContext(), "No data found for this location.", Toast.LENGTH_SHORT).show();
                            }

                            linkAdapter adapter = new linkAdapter(getContext(), linkNames, linkUrls);
                            listView.setAdapter(adapter);
                        });
            }

            if(links.equals("ngp")){
                tittle.setText("NGP");

                List<String> linkNames = new ArrayList<>();
                List<String> linkUrls = new ArrayList<>();

                // Example for loading reference locations
                db.collection("link").document("ngp")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.contains("website")) {
                                    String url = documentSnapshot.getString("website");
                                    linkUrls.add(url);
                                    linkNames.add("Website");
                                }
                                if (documentSnapshot.contains("face")) {
                                    String url = documentSnapshot.getString("face");
                                    linkUrls.add(url);
                                    linkNames.add("Facebook");
                                }
                                if (documentSnapshot.contains("insta")) {
                                    String url = documentSnapshot.getString("insta");
                                    linkUrls.add(url);
                                    linkNames.add("Instagram");
                                }
                                if (documentSnapshot.contains("liinkedin")) {
                                    String url = documentSnapshot.getString("linkedin");
                                    linkUrls.add(url);
                                    linkNames.add("Linkedin");
                                }
                                if (documentSnapshot.contains("twitter")) {
                                    String url = documentSnapshot.getString("twitter");
                                    linkUrls.add(url);
                                    linkNames.add("Twitter");
                                }
                                if (documentSnapshot.contains("youtube")) {
                                    String url = documentSnapshot.getString("youtube");
                                    linkUrls.add(url);
                                    linkNames.add("You Tube");
                                }
                                if (documentSnapshot.contains("hostel")) {
                                    String url = documentSnapshot.getString("hostel");
                                    linkUrls.add(url);
                                    linkNames.add("Hostel");
                                }
                            } else {
                                Toast.makeText(getContext(), "No data found for this location.", Toast.LENGTH_SHORT).show();
                            }

                            linkAdapter adapter = new linkAdapter(getContext(), linkNames, linkUrls);
                            listView.setAdapter(adapter);
                        });
            }

            if(links.equals("scholar")){
                tittle.setText("Scholarship");

                List<String> linkNames = new ArrayList<>();
                List<String> linkUrls = new ArrayList<>();

                // Example for loading reference locations
                db.collection("link").document("scholar")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.contains("nsp")) {
                                    String url = documentSnapshot.getString("nsp");
                                    linkUrls.add(url);
                                    linkNames.add("NSP");
                                }
                                if (documentSnapshot.contains("pms")) {
                                    String url = documentSnapshot.getString("pms");
                                    linkUrls.add(url);
                                    linkNames.add("PMS");
                                }
                            } else {
                                Toast.makeText(getContext(), "No data found for this location.", Toast.LENGTH_SHORT).show();
                            }

                            linkAdapter adapter = new linkAdapter(getContext(), linkNames, linkUrls);
                            listView.setAdapter(adapter);
                        });
            }

        }

        return view;
    }
}