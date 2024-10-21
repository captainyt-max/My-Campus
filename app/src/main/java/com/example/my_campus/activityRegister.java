package com.example.my_campus;

import static com.example.my_campus.MainActivity.clickAnimation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class activityRegister extends AppCompatActivity {

    String [] passMobile = new String[1];
    boolean [] isValidated = {false};
    private LinearLayout loadingLayout;
    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        loadingLayout = findViewById(R.id.loadingLayout);
        loadingText = findViewById(R.id.loadingText);



        //finding views of registration layout
        ConstraintLayout noEditField = findViewById(R.id.constraintLayout14);
        EditText registerMobileno = findViewById(R.id.registerMobileno);
        EditText registerEmail = findViewById(R.id.registerEmail);
        EditText createPassword = findViewById(R.id.createPassword);
        EditText confirmPassword = findViewById(R.id.confirmPassword);
        TextView setName = findViewById(R.id.setName);
        TextView setBranch = findViewById(R.id.setBranch);
        TextView setYear = findViewById(R.id.setYear);
        ConstraintLayout registerButton = findViewById(R.id.registerButton);
        ConstraintLayout registerValidate = findViewById(R.id.registerValidate);

//        registerEmail.setEnabled(false);
//        createPassword.setEnabled(false);
//        confirmPassword.setEnabled(false);

        noEditField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activityRegister.this, "You cannot edit this field", Toast.LENGTH_SHORT).show();
            }
        });


        registerValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);
                String mobileNumber = registerMobileno.getText().toString();
                if (mobileNumber.length() != 10){
                    registerMobileno.setError("Enter a valid Mobile Number");
                    registerMobileno.requestFocus();
                    return;
                }
                // Show the ProgressBar and "Processing" Text
                showLoading("Validating user...");

                DocumentReference docRef = db.collection("users").document(mobileNumber);
                // Check if user exists
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //check if already registered
                            if(!document.getString("password").isEmpty()){
                                hideLoading();
                                Toast.makeText(activityRegister.this, "Already Registered, Please Login", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // User exists and not registered, fetch details
                            String name = document.getString("name");
                            String branch = document.getString("branch");
                            String year = document.getString("year");
                            setName.setText(name);
                            setBranch.setText(branch);
                            setYear.setText(year);

                            //update mobile number array for passing in register button click listner
                            passMobile[0] = mobileNumber;
                            isValidated[0] = true;

//                            //Enable email and password fields
//                            registerEmail.setEnabled(true);
//                            createPassword.setEnabled(true);
//                            confirmPassword.setEnabled(true);

                            hideLoading();
                            // Continue with signup

                        } else {
                            // User not authorized
                            hideLoading();
                            Toast.makeText(activityRegister.this, "Invalid User", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hideLoading();
                        Toast.makeText(activityRegister.this, "Unexpected error occurred. try checking internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAnimation(view);

                String email = registerEmail.getText().toString();
                String password = createPassword.getText().toString();
                String confirmPasswordStr = confirmPassword.getText().toString();
                String mobileNumber = passMobile[0];

                if(!isValidated[0]){
                    Toast.makeText(activityRegister.this, "Validate Mobile Number First", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty() || password.isEmpty() || confirmPasswordStr.isEmpty()) {
                    Toast.makeText(activityRegister.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<8){
                    createPassword.setError("Password must be atleast 8 characters long");
                    createPassword.requestFocus();
                    return;
                }

                if (!password.equals(confirmPasswordStr)) {
                    confirmPassword.setError("Passwords do not match");
                    confirmPassword.requestFocus();
                    return;
                }
                // Show the ProgressBar and "Processing" Text
                showLoading("Registering user...");

                DocumentReference docRef = db.collection("users").document(mobileNumber);
                // Check if user exists
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            docRef.update("email", email, "password", password)
                                    .addOnSuccessListener(aVoid -> {
                                        hideLoading();
                                        isValidated[0]=false;
                                        passMobile[0] = "";
                                        Toast.makeText(activityRegister.this, "Registration successful, Please Login", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        hideLoading();
                                        Toast.makeText(activityRegister.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
                                    });
                            hideLoading();
                        } else {
                            // User not validated
                            hideLoading();
                            Toast.makeText(activityRegister.this, "Validate Phone Number First", Toast.LENGTH_SHORT).show();
                            hideLoading();
                        }
                    } else {
                        hideLoading();
                        Toast.makeText(activityRegister.this, "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                        hideLoading();
                    }
                });
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