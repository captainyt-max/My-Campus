package com.example.my_campus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.chrisbanes.photoview.PhotoView;

public class activityProfileImage extends AppCompatActivity {
    private utility ut = new utility();
    private int imageResId;
    private String imageName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_image);

        PhotoView setImage = findViewById(R.id.setImage);
        ImageView btnBack = findViewById(R.id.btnBack);
        TextView setName = findViewById(R.id.setName);

        btnBack.setOnClickListener(click -> {
            finish();
        });

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        String categoryCode = intent.getStringExtra("categoryCode");
        Log.d("profile image", "category code: " + categoryCode);

        if (categoryCode.equals("facultiesProfileImage")){
            ut.setProfileImage(this, intent.getStringExtra("imageUrl"), setImage);
            setName.setText(intent.getStringExtra("imageName"));
        }


        if (categoryCode.equals("profileImage")){
            imageResId = intent.getIntExtra("profileImageRes", -1);
            imageName = intent.getStringExtra("imageName");
            setImage.setImageResource(imageResId);
            setName.setText(imageName);

            ut.setProfileImage(this, loginState.getProfileImageUrl(this), setImage);
        }

        if (categoryCode.equals("noticeInfo")){
            ut.setProfileImage(this, intent.getStringExtra("imageUrl"), setImage);
            setName.setText(intent.getStringExtra("imageName"));
        }



    }
}