package com.example.my_campus.Fragments;

import android.os.Bundle;
import com.example.my_campus.utility;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fragmentSelectedSyllabus extends Fragment {



    TextView tittle;
    ListView listView;
    private String branchName;
    private utility ut = new utility();


    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_syllabus, container, false);

        tittle = view.findViewById(R.id.branch);
        listView = view.findViewById(R.id.pdflistview);

        Map <String, String> branchMap = new HashMap<>();
        branchMap.put("auto", "Automobile Engineering");
        branchMap.put("cse", "Computer Science & Engineering");
        branchMap.put("civil", "Civil Engineering");
        branchMap.put("electrical", "Electrical Engineering");
        branchMap.put("electronic", "Electronics Engineering");
        branchMap.put("mech", "Mechanical Engineering");




        //getting bundle containing branch name from previous fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("Syllabus") && bundle.getString("Syllabus") != null) {
                branchName = bundle.getString("Syllabus");
            }

            tittle.setText(branchMap.get(branchName));

            List<String> pdfUrls = new ArrayList<>();
            List<String> semesterNames = new ArrayList<>();

            ut.showBufferingDialog(requireContext(), "Loading files");
            db.collection("syllabus").document(branchName)
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
                            ut.dismissBufferingDialog();
                            // Set up ListView with the custom adapter
                            syllabusCustomAdapter adapter = new syllabusCustomAdapter(requireContext(),branchName, semesterNames, pdfUrls);
                            listView.setAdapter(adapter);
                        }
                    })
                    .addOnFailureListener(e -> {
                        ut.dismissBufferingDialog();
                        // Handle error
                    });


        }
        return view;
    }


}