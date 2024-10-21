package com.example.my_campus;

import static com.example.my_campus.MainActivity.clickAnimation;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class activityLogin extends AppCompatActivity {

    private LinearLayout loadingLayout;
    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.acitivity_login);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        ConstraintLayout loginButton = findViewById(R.id.loginButton);
        EditText loginMobileno = findViewById(R.id.loginMobileno);
        EditText loginPassword = findViewById(R.id.loginPassword);
        TextView register = findViewById(R.id.register);
        TextView forgotPassword = findViewById(R.id.forgotPassword);

        loadingLayout = findViewById(R.id.loadingLayout);
        loadingText = findViewById(R.id.loadingText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);

                String mobileNumber = loginMobileno.getText().toString();
                String enteredPassword = loginPassword.getText().toString();

                if (mobileNumber.length() != 10) {
                    loginMobileno.setError("Enter a valid Mobile Number");
                    loginMobileno.requestFocus();
                    return;
                }

                if (enteredPassword.isEmpty()) {
                    loginPassword.setError("Password cannot be empty");
                    loginPassword.requestFocus();
                    return;
                }

                showLoading("Logging in");

                DocumentReference docRef = db.collection("users").document(mobileNumber);
                // Check if user exists
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String password = document.getString("password");
                            if (password != null && password.equals(enteredPassword)) {
                                String name = document.getString("name");
                                String branch =document.getString("branch");
                                String year = document.getString("year");
                                String rollNo = document.getString("rollNo");
                                String role =document.getString("role");
                                String email = document.getString("email");
                                loginState.setLoginState(activityLogin.this,true, mobileNumber, email, name, branch, year, rollNo, role);
                                Intent intent = new Intent(activityLogin.this, MainActivity.class);
                                hideLoading();
                                startActivity(intent);
                                finish();
                            } else {
                                hideLoading();
                                Toast.makeText(activityLogin.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // User not authorized
                            hideLoading();
                            Toast.makeText(activityLogin.this, "User not found, please Register", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hideLoading();
                        Toast.makeText(activityLogin.this, "Unexpected error occurred, Try checking internet", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                Intent intent = new Intent(activityLogin.this, activityRegister.class);
                startActivity(intent);

            }
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

    // Show loading animation and processing text
    private void showLoading(String message) {
        loadingLayout.setVisibility(View.VISIBLE);
        loadingText.setText(message);
    }

    // Hide loading animation and processing text
    private void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
    }
}