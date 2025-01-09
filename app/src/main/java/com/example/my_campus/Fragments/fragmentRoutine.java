package com.example.my_campus.Fragments;

import static com.example.my_campus.MainActivity.clickAnimation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_campus.R;
import com.example.my_campus.loginState;
import com.example.my_campus.utility;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class fragmentRoutine extends Fragment {


    private TextView pdfName;
    private ConstraintLayout btnDownload;
    private String documentName;
    private String fieldName;
    private String pdfUrl;
    private ImageView pdfIcon;
    utility ut = new utility();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        pdfName = view.findViewById(R.id.pdfName);
        btnDownload = view.findViewById(R.id.btnDonwload);
        pdfIcon = view.findViewById(R.id.pdf_icon);


        Map<String, String> branchMap = new HashMap<>();
        branchMap.put("Computer Science & Engineering", "cse");
        branchMap.put("Civil Engineering", "civil");
        branchMap.put("Electrical Engineering", "electrical");
        branchMap.put("Electronics Engineering", "electronics");
        branchMap.put("Mechanical Engineering", "mech");
        branchMap.put("Automobile Engineering", "auto");

        documentName = branchMap.get(loginState.getUserBranch(requireContext()));
        fieldName = loginState.getUserYear(requireContext());

        getPdf(documentName, fieldName);

        btnDownload.setOnClickListener(click -> {
            clickAnimation(btnDownload);
            downloadPdf(pdfUrl);
        });




        return view;
    }

    private void getPdf(String documentName, String fieldName){
        DocumentReference docref = db.collection("routine").document(documentName);
        docref.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Toast.makeText(getContext(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()){
                pdfUrl = documentSnapshot.getString(fieldName);
                String fileName = documentSnapshot.getString(fieldName + " File Name");
                if (pdfUrl != null && !pdfUrl.isEmpty()){
                    btnDownload.setVisibility(View.VISIBLE);
                    pdfIcon.setVisibility(View.VISIBLE);
                    pdfName.setText(fileName);
                }
                else {
                    btnDownload.setVisibility(View.GONE);
                    pdfIcon.setVisibility(View.GONE);
                    pdfName.setText("No such file uploaded");
                }
            }
        });
    }

    private void downloadPdf(String url){
        if(url == null || url.isEmpty()){
            Toast.makeText(requireContext(), "No routine is uploaded", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
        requireContext().startActivity(intent);
    }
}