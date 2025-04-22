package com.example.my_campus;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.*;

public class activityRegister extends AppCompatActivity {

    String [] passEmail = new String[1];
    boolean [] isValidated = {false};
    String [] storeOtp = new String[1];
    boolean [] isOtpSent = {false};
    private LinearLayout loadingLayout;
    private TextView loadingText;
    private final utility ut = new utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        loadingLayout = findViewById(R.id.loadingLayout);
        loadingText = findViewById(R.id.loadingText);

        //finding views of registration layout
        ConstraintLayout noEditField = findViewById(R.id.constraintLayout14);
        EditText registerMobileno = findViewById(R.id.otp);
        EditText registerEmail = findViewById(R.id.registerEmail);
        EditText createPassword = findViewById(R.id.newPassword);
        EditText confirmPassword = findViewById(R.id.confirmPassword);
        TextView setName = findViewById(R.id.setName);
        TextView setBranch = findViewById(R.id.setBranch);
        TextView setYear = findViewById(R.id.setYear);
        ConstraintLayout registerButton = findViewById(R.id.btnChange);
        ConstraintLayout registerValidate = findViewById(R.id.btnVerify);
        EditText registerOtp = findViewById(R.id.registerOtp);
        ConstraintLayout registerSendOtp = findViewById(R.id.sendOtp);
        TextView otpButtonText = findViewById(R.id.textView34);
        TextView registerFindEmail = findViewById(R.id.registerFindEmail);


//        registerEmail.setEnabled(false);
//        createPassword.setEnabled(false);
//        confirmPassword.setEnabled(false);

        noEditField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activityRegister.this, "You cannot edit this field", Toast.LENGTH_SHORT).show();
            }
        });

        registerFindEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activityRegister.this, registerFindEmail.class);
                startActivity(intent);
            }
        });

        registerValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValidated[0] = false;
                registerSendOtp.setVisibility(View.GONE);
                registerOtp.setVisibility(View.GONE);
                ut.clickAnimation(view);

                String email = registerEmail.getText().toString();

                if (!email.contains("@") || !email.contains(".")) {
                    registerEmail.setError("Enter a valid email id");
                    registerEmail.requestFocus();
                    return;
                }
                // Show the ProgressBar and "Processing" Text
                showLoading("Validating user...");

                DocumentReference docRef = db.collection("users").document(email);
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

                            registerSendOtp.setVisibility(View.VISIBLE);
                            registerOtp.setVisibility(View.VISIBLE);

                            //update mobile number array for passing in register button click listner
                            passEmail[0] = email;
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
                            Toast.makeText(activityRegister.this, "User not allowed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hideLoading();
                        Toast.makeText(activityRegister.this, "Unexpected error occurred. try checking internet", Toast.LENGTH_SHORT).show();
                        }
                });
            }

        });

        registerSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ut.clickAnimation(view);
                showLoading("Sending OTP");
                sendOtp();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ut.clickAnimation(view);

                String otp = registerOtp.getText().toString();
                String mobileNumber = registerMobileno.getText().toString();
                String password = createPassword.getText().toString();
                String confirmPasswordStr = confirmPassword.getText().toString();
                String email = passEmail[0];

                if(!isValidated[0]){
                    Toast.makeText(activityRegister.this, "Validate Email Id First", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isOtpSent[0]){
                    Toast.makeText(activityRegister.this, "Send OTP first", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mobileNumber.isEmpty() || password.isEmpty() || confirmPasswordStr.isEmpty()) {
                    Toast.makeText(activityRegister.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mobileNumber.length() != 10) {
                    registerMobileno.setError("Enter a valid mobile number");
                    registerMobileno.requestFocus();
                    return;
                }

                if(password.length()<8){
                    createPassword.setError("Password must be atleast 8 characters long");
                    createPassword.requestFocus();
                    return;
                }
                Pattern alphabetPattern = Pattern.compile("[a-zA-Z]");
                Pattern numberPattern = Pattern.compile("[0-9]");
                Pattern specialCharPattern = Pattern.compile("[!@#$%^&]");

                Matcher hasAlphabet = alphabetPattern.matcher(password);
                Matcher hasNumber = numberPattern.matcher(password);
                Matcher hasSpecialChar = specialCharPattern.matcher(password);

                if (!hasAlphabet.find() || !hasNumber.find() || !hasSpecialChar.find()){
                    createPassword.setError("Password must contain Alpha-numeric and special characters");
                    return;
                }

                if (!password.equals(confirmPasswordStr)) {
                    confirmPassword.setError("Passwords do not match");
                    confirmPassword.requestFocus();
                    return;
                }

                if(!otp.equals(storeOtp[0])){
                    registerOtp.setError("Invalid OTP");
                    registerOtp.requestFocus();
                    return;
                }
                // Show the ProgressBar and "Processing" Text
                showLoading("Registering user...");

                DocumentReference docRef = db.collection("users").document(email);
                // Check if user exists
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            docRef.update("mobileNumber", mobileNumber,  "password", password)
//                                    .addOnSuccessListener(aVoid -> {
//                                        hideLoading();
//                                        isValidated[0]=false;
//                                        passEmail[0] = "";
//                                        finish();
//                                        Toast.makeText(activityRegister.this, "Registration successful, Please Login", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        hideLoading();
//                                        Toast.makeText(activityRegister.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
//                                    });
//                            hideLoading();
//                        }
                        // inside registerButton.setOnClickListener...
                        if (document.exists()) {
                            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // 🔒 hash the password here

                            docRef.update("mobileNumber", mobileNumber, "password", hashedPassword)
                                    .addOnSuccessListener(aVoid -> {
                                        hideLoading();
                                        isValidated[0]=false;
                                        passEmail[0] = "";
                                        Toast.makeText(activityRegister.this, "Registration successful, Please Login", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        hideLoading();
                                        Toast.makeText(activityRegister.this, "Failed to upload data", Toast.LENGTH_SHORT).show();
                                    });
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

    private void sendOtp(){
        TextView otpButtonText = findViewById(R.id.textView34);
        isOtpSent[0] = false;
        String email = passEmail[0];

        // Run OTP sending in a separate thread to avoid blocking the UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Python py = Python.getInstance();
                    PyObject pyobj = py.getModule("sendOtpOnEmail");
                    PyObject otp = pyobj.callAttr("send_email_otp", passEmail[0]);
                    String sentOtp = otp.toString();

                    // Update UI after OTP is sent or failed
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading(); // Stop loading animation

                            if (!sentOtp.isEmpty()) {
                                storeOtp[0] = sentOtp;
                                otpButtonText.setText("resend otp");

                                isOtpSent[0] = true;
                                Toast.makeText(activityRegister.this, " OTP sent to " + email, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activityRegister.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle any errors in the OTP sending process
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideLoading(); // Stop loading animation
                            Toast.makeText(activityRegister.this, "Error sending OTP", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}