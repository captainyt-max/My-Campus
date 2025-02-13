package com.example.my_campus;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class activityLogin extends AppCompatActivity {

    private LinearLayout loadingLayout;
    private TextView loadingText;
    private final utility ut = new utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.acitivity_login);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        ConstraintLayout loginButton = findViewById(R.id.loginButton);
        EditText loginEmail = findViewById(R.id.loginEmail);
        EditText loginPassword = findViewById(R.id.loginPassword);
        TextView register = findViewById(R.id.register);
        TextView forgotPassword = findViewById(R.id.forgotPassword);

        loadingLayout = findViewById(R.id.loadingLayout);
        loadingText = findViewById(R.id.loadingText);

        loginButton.setOnClickListener(view -> {
            ut.clickAnimation(view);

            String email = loginEmail.getText().toString();
            String enteredPassword = loginPassword.getText().toString();

            if (!email.contains("@") || !email.contains(".")) {
                loginEmail.setError("Enter a valid email id");
                loginEmail.requestFocus();
                return;
            }

            if (enteredPassword.length() < 8 ) {
                loginPassword.setError("Password must be minimum 8 characters long");
                loginPassword.requestFocus();
                return;
            }

            ut.showBufferingDialog(this, "Logging in");

            DocumentReference docRef = db.collection("users").document(email);
            // Check if user exists
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String password = document.getString("password");
                        if (password.isEmpty()) {
                            ut.dismissBufferingDialog();
                            Toast.makeText(activityLogin.this, "Please register first", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (password != null && password.equals(enteredPassword)) {
                            String name = document.getString("name");
                            String branch =document.getString("branch");
                            String year = document.getString("year");
                            String rollNo = document.getString("rollNo");
                            String mobileNumber = document.getString("mobileNumber");
                            loginState.setLoginState(activityLogin.this,true, mobileNumber, email, name, branch, year, rollNo);
                            Intent intent = new Intent(activityLogin.this, MainActivity.class);
                            ut.dismissBufferingDialog();
                            startActivity(intent);
                            finish();
                        } else {
                            ut.dismissBufferingDialog();
                            Toast.makeText(activityLogin.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User not authorized
                        ut.dismissBufferingDialog();
                        Toast.makeText(activityLogin.this, "User not allowed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ut.dismissBufferingDialog();
                    Toast.makeText(activityLogin.this, "Unexpected error occurred, Try checking internet", Toast.LENGTH_SHORT).show();
                }
            });


        });

        register.setOnClickListener(view -> {
            ut.clickAnimation(view);
            Intent intent = new Intent(activityLogin.this, activityRegister.class);
            startActivity(intent);
        });


        forgotPassword.setOnClickListener(click -> {
            ut.clickAnimation(forgotPassword);
            Intent intent = new Intent(activityLogin.this, activityChangePassword.class);
            startActivity(intent);
        });


    }

    public void clickAnimation(View v){
        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                v.animate().scaleX(1f).scaleY(1f).setDuration(100);
            }
        });
    }
}