package com.example.my_campus;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class activityEditProfile extends AppCompatActivity {

    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri destinationUri;
    private ImageView setDisplayImage;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String storagePath = "users/profile image/";
    private utility ut = new utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        //Set status bar and navigation bar color
        Window window = getWindow();
        // Set status bar color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightGrey));
        // Set navigation bar color
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.lightGrey));

        ImageView btnBack = findViewById(R.id.btnBack1);
        ConstraintLayout btnNewPhoto = findViewById(R.id.btnNewPhoto);
        ConstraintLayout btnDeletePhoto = findViewById(R.id.btnDeletePhoto);
        setDisplayImage = findViewById(R.id.setDisplayImage);



        btnNewPhoto.setOnClickListener( click -> {
            ut.clickAnimation(click);
            openImagePicker();
        });

        btnBack.setOnClickListener(click -> {
            finish();
        });

        btnDeletePhoto.setOnClickListener( click -> {
            ut.clickAnimation(click);
            deleteFile("users", loginState.getUserEmail(this), "profileImage");
        });

        db.collection("users").document(loginState.getUserEmail(this))
                .addSnapshotListener( (snapshot, e) -> {
                    if (e!= null){
                        return;
                    }
                    if (snapshot != null && snapshot.contains("profileImage")){
                        if (snapshot.getString("profileImage").isEmpty()){
                            btnDeletePhoto.setVisibility(View.GONE);
                        }
                        else {
                            btnDeletePhoto.setVisibility(View.VISIBLE);
                        }
                        ut.setProfileImage(this, snapshot.getString("profileImage"), setDisplayImage);
                    }
                });



        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        cropImage();

                        // Handle your upload logic here if needed
                    }
                }
        );
    }



    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        imagePickerLauncher.launch(intent);
    }


    private void cropImage() {
        destinationUri = Uri.fromFile(new File(getCacheDir(), "croppedImage.jpg"));

        // Start UCrop with 1:1 aspect ratio
        UCrop.of(selectedImageUri, destinationUri)
                .withAspectRatio(1, 1)
                .start(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri croppedUri = UCrop.getOutput(data);
            if (croppedUri != null) {
                destinationUri = croppedUri;
                String userEmail = loginState.getUserEmail(this);
                uploadImage(storagePath, destinationUri, userEmail);
            }
        } else if (requestCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "Cropping failed: " + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String storagePath, Uri fileUri, String userEmail){
        Toast.makeText(this, "Changing Profile Picture", Toast.LENGTH_SHORT).show();
        StorageReference storageReference = storage.getReference(storagePath);
        String fileName = loginState.getUserName(this) + "_" + loginState.getUserBranch(this) + "_" + loginState.getUserYear(this) + ".jpg";
        StorageReference fileRef = storageReference.child(fileName);
        UploadTask uploadTask= fileRef.putFile(fileUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(DownloadUri -> {
                String downloadUrl = DownloadUri.toString();
                DocumentReference docref = db.collection("users").document(userEmail);
                docref.get().addOnSuccessListener( documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        docref.update("profileImage", downloadUrl).addOnSuccessListener( unused -> {
                            Toast.makeText(this, "Profile picture changed", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            });
        }).addOnFailureListener( e -> {
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }



    void deleteFile(String collection, String documentName, String fieldName){
        DocumentReference docref = db.collection(collection).document(documentName);
        docref.get().addOnSuccessListener( documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        String fileUrl = documentSnapshot.getString(fieldName);
                        assert fileUrl != null;
                        if (fileUrl != null && !fileUrl.isEmpty()){
                            StorageReference fileRef = storage.getReferenceFromUrl(fileUrl);
                            fileRef.delete().addOnSuccessListener(unused ->
                                    docref.update(fieldName, "")
                                            .addOnSuccessListener(
                                                    unused1 -> Toast.makeText(this, "Profile picture deleted", Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener( e -> Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show())
                            ).addOnFailureListener( e -> Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }
                })
                .addOnFailureListener( e -> Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }


}