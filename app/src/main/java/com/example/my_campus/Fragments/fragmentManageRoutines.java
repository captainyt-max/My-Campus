package com.example.my_campus.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;



public class fragmentManageRoutines extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView pdfIcon;
    private TextView pdfName, selectedPdfName;
    private ConstraintLayout btnDeleteRoutine, btnSelectFile, btnUploadPdf;
    utility ut = new utility();
    private Uri selectedPdfUri;
    ImageView btnReset;

    private ActivityResultLauncher<Intent> pdfPickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_routines, container, false);
        //code here

        pdfIcon = view.findViewById(R.id.pdf_icon);
        pdfName = view.findViewById(R.id.pdfName);
        btnDeleteRoutine = view.findViewById(R.id.btnDonwload);
        btnSelectFile = view.findViewById(R.id.btnSelectFile);
        selectedPdfName = view.findViewById(R.id.selectedPdfName);
        btnUploadPdf = view.findViewById(R.id.btnUploadPdf);
        btnReset = view.findViewById(R.id.btnReset);

        // Map branches to Firestore document names
        Map<String, String> branchMap = new HashMap<>();
        branchMap.put("Computer Science & Engineering", "cse");
        branchMap.put("Civil Engineering", "civil");
        branchMap.put("Electrical Engineering", "electrical");
        branchMap.put("Electronics Engineering", "electronics");
        branchMap.put("Mechanical Engineering", "mech");
        branchMap.put("Automobile Engineering", "auto");

        String userBranch = loginState.getUserBranch(requireContext());
        String userYear = loginState.getUserYear(requireContext());

        String documentName = branchMap.get(userBranch);
        if (documentName != null) {
            handelRoutine(documentName, userYear);
        }

        btnSelectFile.setOnClickListener(click ->{
            ut.clickAnimation(btnSelectFile);
            openPDFPicker();
        });



        btnUploadPdf.setOnClickListener(click -> {
            ut.clickAnimation(btnUploadPdf);
            ut.dialogBox(requireContext(), "You are going to upload " + ut.getFileName(requireContext(), selectedPdfUri) + " as your class routine", new utility.DialogCallback() {
                @Override
                public void onConfirm() {
                    uploadPdf(documentName, userYear, selectedPdfUri);
                }
                @Override
                public void onCancel() {
                    // Do nothing
                }
            });
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

        btnReset.setOnClickListener( click -> {
            resetSelectedPdf();
        });

        return view;

    }

    private void handelRoutine(String documentName, String fieldName){
        isPdfUploaded(documentName, fieldName);
        btnDeleteRoutine.setOnClickListener(click -> {
            deleteFile("routine", documentName, fieldName);
        });
    }

    void deleteFile(String collection, String documentName, String fieldName){
        DocumentReference docref = db.collection(collection).document(documentName);
        docref.get().addOnSuccessListener( documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        String pdfUrl = documentSnapshot.getString(fieldName);
                        assert pdfUrl != null;
                        if (pdfUrl != null && !pdfUrl.isEmpty()){
                            StorageReference pdfRef = storage.getReferenceFromUrl(pdfUrl);
                            pdfRef.delete().addOnSuccessListener(unused ->
                                    docref.update(fieldName, "")
                                            .addOnSuccessListener(
                                                    unused1 -> Toast.makeText(getContext(), "Routine Deleted", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener( e -> Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show())
                            ).addOnFailureListener( e -> Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }
                })
                .addOnFailureListener( e -> Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void isPdfUploaded(String documentName, String fieldName){
        DocumentReference docref = db.collection("routine").document(documentName);
        docref.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Toast.makeText(getContext(), "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()){
                String pdfUrl = documentSnapshot.getString(fieldName);
                String fileName = documentSnapshot.getString(fieldName + " File Name");
                if (pdfUrl != null && !pdfUrl.isEmpty()){
                    btnDeleteRoutine.setVisibility(View.VISIBLE);
                    pdfIcon.setVisibility(View.VISIBLE);
                    pdfName.setText(fileName);
                }
                else {
                    btnDeleteRoutine.setVisibility(View.GONE);
                    pdfIcon.setVisibility(View.GONE);
                    pdfName.setText("No such file uploaded");
                }
            }
        });
    }


    private void openPDFPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        pdfPickerLauncher.launch(intent);
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        if (getContext() != null && uri.getScheme().equals("content")) {
            try (Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            }
        }

        if (fileName == null) {
            fileName = uri.getLastPathSegment();// Fallback to the last segment if no name is found
        }
        return fileName;
    }

    private void uploadPdf(String userBranch, String userYear, Uri fileUri){
        btnUploadPdf.setVisibility(View.GONE);
        String filePath = "routine/" + userBranch;
        StorageReference storageReference = storage.getReference(filePath);
        StorageReference fileRef = storageReference.child(userBranch + " " + userYear + ".pdf");
        UploadTask uploadTask= fileRef.putFile(fileUri);
        ut.showUploadProgressDialog(requireContext(), uploadTask);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(DownloadUri -> {
                String pdfDownloadUrl = DownloadUri.toString();
                DocumentReference docref = db.collection("routine").document(userBranch);
                docref.get().addOnSuccessListener( documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        docref.update(userYear, pdfDownloadUrl, userYear + " File Name", "routine " + ut.getDateTime() + ".pdf").addOnSuccessListener( unused -> {
                            Toast.makeText(requireContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                            resetSelectedPdf();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(requireContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            });
        }).addOnFailureListener( e -> {
            Toast.makeText(requireContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void resetSelectedPdf(){
        selectedPdfUri = null;
        selectedPdfName.setText("");
        btnUploadPdf.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
    }

}