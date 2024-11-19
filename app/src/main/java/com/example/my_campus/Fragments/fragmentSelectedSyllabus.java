package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.my_campus.CustomListAdapter;
import com.example.my_campus.ListItem;
import com.example.my_campus.R;
import com.example.my_campus.syllabusCustomAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class fragmentSelectedSyllabus extends Fragment {



    TextView tittle;
    ListView listView;


    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_syllabus, container, false);

        tittle = view.findViewById(R.id.branch);
        listView = view.findViewById(R.id.pdflistview);




        //getting bundle containing branch name from previous fragment
        Bundle bundle = getArguments();
        if (bundle != null){
            String branchName = bundle.getString("Syllabus");

            if (branchName.equals("automobile")){
                assert tittle!= null;
                tittle.setText("Automobile Engineering");

                List<String> pdfUrls = new ArrayList<>();
                List<String> semesterNames = new ArrayList<>();

                db.collection("syllabus").document("auto")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                for (int i = 1; i <= 6; i++) {
                                    String semesterField = "sem" + i;
                                    if (documentSnapshot.contains(semesterField)) {
                                        String url = documentSnapshot.getString(semesterField);
                                        pdfUrls.add(url);
                                        semesterNames.add("Semester " + i);
                                    }
                                }
                                // Set up ListView with the custom adapter
                                syllabusCustomAdapter adapter = new syllabusCustomAdapter(getContext(), semesterNames, pdfUrls);
                                listView.setAdapter(adapter);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                        });
            }



            if (branchName.equals("cse")){
                assert tittle!= null;
                tittle.setText("Computer Science & Enginnering");

                List<String> pdfUrls = new ArrayList<>();
                List<String> semesterNames = new ArrayList<>();

                db.collection("syllabus").document("cse")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                for (int i = 1; i <= 6; i++) {
                                    String semesterField = "sem" + i;
                                    if (documentSnapshot.contains(semesterField)) {
                                        String url = documentSnapshot.getString(semesterField);
                                        pdfUrls.add(url);
                                        semesterNames.add("Semester " + i);
                                    }
                                }
                                // Set up ListView with the custom adapter
                                syllabusCustomAdapter adapter = new syllabusCustomAdapter(getContext(), semesterNames, pdfUrls);
                                listView.setAdapter(adapter);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                        });

            }



            if (branchName.equals("civil")) {
                assert tittle!= null;
                tittle.setText("Civil Engineering");
                List<String> pdfUrls = new ArrayList<>();
                List<String> semesterNames = new ArrayList<>();

                db.collection("syllabus").document("civil")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                for (int i = 1; i <= 6; i++) {
                                    String semesterField = "sem" + i;
                                    if (documentSnapshot.contains(semesterField)) {
                                        String url = documentSnapshot.getString(semesterField);
                                        pdfUrls.add(url);
                                        semesterNames.add("Semester " + i);
                                    }
                                }
                                // Set up ListView with the custom adapter
                                syllabusCustomAdapter adapter = new syllabusCustomAdapter(getContext(), semesterNames, pdfUrls);
                                listView.setAdapter(adapter);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                        });


            }

            if (branchName.equals("electrical")) {
                assert tittle!= null;
                tittle.setText("Electrical Engineering");
                List<String> pdfUrls = new ArrayList<>();
                List<String> semesterNames = new ArrayList<>();

                db.collection("syllabus").document("electrical")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                for (int i = 1; i <= 6; i++) {
                                    String semesterField = "sem" + i;
                                    if (documentSnapshot.contains(semesterField)) {
                                        String url = documentSnapshot.getString(semesterField);
                                        pdfUrls.add(url);
                                        semesterNames.add("Semester " + i);
                                    }
                                }
                                // Set up ListView with the custom adapter
                                syllabusCustomAdapter adapter = new syllabusCustomAdapter(getContext(), semesterNames, pdfUrls);
                                listView.setAdapter(adapter);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                        });


            }

            if (branchName.equals("electronics")) {
                assert tittle!= null;
                tittle.setText("Electronics Engineering");
                List<String> pdfUrls = new ArrayList<>();
                List<String> semesterNames = new ArrayList<>();

                db.collection("syllabus").document("electronic")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                for (int i = 1; i <= 6; i++) {
                                    String semesterField = "sem" + i;
                                    if (documentSnapshot.contains(semesterField)) {
                                        String url = documentSnapshot.getString(semesterField);
                                        pdfUrls.add(url);
                                        semesterNames.add("Semester " + i);
                                    }
                                }
                                // Set up ListView with the custom adapter
                                syllabusCustomAdapter adapter = new syllabusCustomAdapter(getContext(), semesterNames, pdfUrls);
                                listView.setAdapter(adapter);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                        });


            }

            if (branchName.equals("mechanical")) {
                assert tittle!= null;
                tittle.setText("Mechanical Engineering");

                List<String> pdfUrls = new ArrayList<>();
                List<String> semesterNames = new ArrayList<>();

                db.collection("syllabus").document("mech")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                for (int i = 1; i <= 6; i++) {
                                    String semesterField = "sem" + i;
                                    if (documentSnapshot.contains(semesterField)) {
                                        String url = documentSnapshot.getString(semesterField);
                                        pdfUrls.add(url);
                                        semesterNames.add("Semester " + i);
                                    }
                                }
                                // Set up ListView with the custom adapter
                                syllabusCustomAdapter adapter = new syllabusCustomAdapter(getContext(), semesterNames, pdfUrls);
                                listView.setAdapter(adapter);
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                        });


            }

        }

        return view;
    }
}