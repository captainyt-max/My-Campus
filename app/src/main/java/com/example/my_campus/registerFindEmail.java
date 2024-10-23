package com.example.my_campus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class registerFindEmail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_find_email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText rollNoInput = findViewById(R.id.rollNoInput);
        ConstraintLayout buttonSearchEmail = findViewById(R.id.buttonSearchEmail);
        TextView setEmail = findViewById(R.id.setEmail);


        buttonSearchEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rollNo = rollNoInput.getText().toString();
                if(!rollNo.contains("-")){
                    rollNoInput.setError("Enter a valid Roll No.");
                    rollNoInput.requestFocus();
                    return;
                }
                Toast.makeText(registerFindEmail.this, "Searching email", Toast.LENGTH_SHORT).show();

                db.collection("users")
                        .whereEqualTo("rollNo", rollNo)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                setEmail.setBackgroundResource(R.drawable.rounded_ractangle_white);
                                String email = "default";
                                // Check if any document was found
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Get the email attribute from the document
                                    email = document.getString("email");
                                    setEmail.setText(" Your email is : "+email);
                                    Toast.makeText(registerFindEmail.this, "Email found", Toast.LENGTH_SHORT).show();
                                }
                                if(email.equals("default")){
                                    setEmail.setBackgroundResource(R.drawable.error_background);
                                    setEmail.setText("No such user found");
                                    Toast.makeText(registerFindEmail.this, "Email not found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(registerFindEmail.this, "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }

}