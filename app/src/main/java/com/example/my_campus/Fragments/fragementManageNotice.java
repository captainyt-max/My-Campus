package com.example.my_campus.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class fragementManageNotice extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private TextView selectedPdfName;
    private ConstraintLayout btnSelectFile, btnUploadPdf;
    utility ut = new utility();
    private Uri selectedPdfUri;
    private ImageView btnReset;

    private ActivityResultLauncher<Intent> pdfPickerLauncher;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragement_manage_notice, container, false);
        selectedPdfName = view.findViewById(R.id.selectedPdfName);
        btnSelectFile = view.findViewById(R.id.btnSelectFile);
        btnUploadPdf = view.findViewById(R.id.btnUploadPdf);
        btnReset = view.findViewById(R.id.btnReset);

        btnSelectFile.setOnClickListener( click -> {
            openPDFPicker();
        });

        btnReset.setOnClickListener( click -> {
            resetSelectedPdf();
        });

        btnUploadPdf.setOnClickListener( click -> {
            ut.clickAnimation(btnUploadPdf);
            uploadPdf();
        });

        if (selectedPdfUri == null){
            btnReset.setVisibility(View.GONE);
        }

        pdfPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedPdfUri = result.getData().getData();
                        if (selectedPdfUri != null){
                            btnReset.setVisibility(View.VISIBLE);
                        }
                        String fileName = ut.getFileName(requireContext(), selectedPdfUri);
                        selectedPdfName.setText(fileName);
                        btnUploadPdf.setVisibility(View.VISIBLE);
                        // Handle your upload logic here if needed
                    }
                }
        );



        return view;
    }

    private void openPDFPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        pdfPickerLauncher.launch(intent);
    }


    private void resetSelectedPdf(){
        selectedPdfUri = null;
        selectedPdfName.setText("");
        btnUploadPdf.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
    }

    private void uploadPdf(){
        String fileName = ut.getFileName(requireContext(), selectedPdfUri);
        String fileSize = ut.getFileSize(requireContext(), selectedPdfUri);
        String userEmail = loginState.getUserEmail(requireContext());
        StorageReference storageRef = storage.getReference().child("notice pdfs/" + fileName);
        UploadTask uploadTask = storageRef.putFile(selectedPdfUri);
        ut.showUploadProgressDialog(requireContext(), uploadTask);
        uploadTask.addOnSuccessListener(success -> {
            storageRef.getDownloadUrl().addOnSuccessListener( uri -> {
                Map<String, Object> pdfData = new HashMap<>();
                pdfData.put("fileName", fileName);
                pdfData.put("downloadUrl", uri.toString());
                pdfData.put("uploadId", System.currentTimeMillis());
                pdfData.put("uploadDate", ut.getDate());
                pdfData.put("uploadTime", ut.getDateTime());
                pdfData.put("fileSize", fileSize);
                pdfData.put("uploadedBy", userEmail);
                db.collection("notice pdfs").add(pdfData).addOnSuccessListener(documentReference -> {
                    managePdfLimit();
                    resetSelectedPdf();
                }).addOnFailureListener( e -> {
                    Toast.makeText(requireContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            });
        }).addOnFailureListener( e -> {
            Toast.makeText(requireContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void managePdfLimit() {
        db.collection("notice pdfs")
                .orderBy("uploadId", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener( queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() > 10){
                        DocumentSnapshot oldestDocument = queryDocumentSnapshots.getDocuments().get(0);
                        storage.getReference().child("notice pdfs/" + oldestDocument.getString("fileName")).delete();
                        db.collection("notice pdfs").document(oldestDocument.getId()).delete();
                    }
                });
    }
}