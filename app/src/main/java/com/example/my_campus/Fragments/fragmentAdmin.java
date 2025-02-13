package com.example.my_campus.Fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_campus.R;

import com.example.my_campus.loginState;
import com.example.my_campus.utility;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.net.ConnectivityManager;
import androidx.fragment.app.FragmentActivity;

public class fragmentAdmin extends Fragment {

    private String [] campusCurrentMessageText = new String[1];
    private String [] classCurrentMessageText = new String[1];
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private boolean [] campusMessageUpdated = {true};
    utility ut = new utility();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

     View view = inflater.inflate(R.layout.fragment_admin, container, false);
     


     TextView campusActivityCurrentMessage = view.findViewById(R.id.campusActivityCurrentMessage);
     ImageView campusActivityDelBtn = view.findViewById(R.id.campusActivityDelBtn);
     ImageView campusActivityEditBtn = view.findViewById(R.id.campusActivityEditBtn);
     TextView campusActivityUpdateTime = view.findViewById(R.id.campusActivityUpdateTime);
     EditText campusActivityNewMessage = view.findViewById(R.id.campusActivtyNewMessage);
     ConstraintLayout campusActivityNewMessageSendBtn = view.findViewById(R.id.campusActivityNewMessageSendBtn);
     ConstraintLayout campusCurrentMessageCancelBtn = view.findViewById(R.id.currentMessageCancelBtn);
     ConstraintLayout campusActivityEditMessageSendBtn = view.findViewById(R.id.campusActivityEditMessageSendBtn);
     EditText convertToEditTextCampus = new EditText(getActivity());


     TextView classActivityCurrentMessage = view.findViewById(R.id.classActivityCurrentMessage);
     ImageView classActivityDelBtn = view.findViewById(R.id.classActivityDelBtn);
     ImageView classActivityEditBtn = view.findViewById(R.id.classActivityEditBtn);
     TextView classActivityUpdateTime = view.findViewById(R.id.classActivityUpdateTime);
     EditText classActivityNewMessage = view.findViewById(R.id.classActivtyNewMessage);
     ConstraintLayout classActivityNewMessageSendBtn = view.findViewById(R.id.classActivityNewMessageSendBtn);
     ConstraintLayout classCurrentMessageCancelBtn = view.findViewById(R.id.classCurrentMessageCancelBtn);
     ConstraintLayout classActivityEditMessageSendBtn = view.findViewById(R.id.classActivityEditMessageSendBtn);
     EditText convertToEditTextClass = new EditText(getActivity());

     ConstraintLayout btnManageRoutine = view.findViewById(R.id.btnManageRoutine);
     ConstraintLayout btnManageNotices = view.findViewById(R.id.btnManageNotices);


     firestore = FirebaseFirestore.getInstance();
     firebaseAuth = FirebaseAuth.getInstance();


     //Campus activity section start////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    campusActivityEditBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //ConstraintLayout campusCurrentMessageLayout = view.findViewById(R.id.campusCurrentMessageLayout);
            convertToEditTextCampus.setLayoutParams(campusActivityCurrentMessage.getLayoutParams());
            convertToEditTextCampus.setText(campusActivityCurrentMessage.getText());
            campusCurrentMessageText [0] = campusActivityCurrentMessage.getText().toString();

            convertToEditTextCampus.setBackgroundResource(R.drawable.message_input_background);
            convertToEditTextCampus.setPadding(25,27,25,27);

            ViewGroup parent = (ViewGroup) campusActivityCurrentMessage.getParent();
            int index = parent.indexOfChild(campusActivityCurrentMessage);
            parent.removeView(campusActivityCurrentMessage);
            parent.addView(convertToEditTextCampus, index);
            convertToEditTextCampus.requestFocus();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.showSoftInput(convertToEditTextCampus, InputMethodManager.SHOW_IMPLICIT);

            campusActivityEditBtn.setEnabled(false);
            campusActivityEditBtn.setColorFilter(Color.parseColor("#00a6ff"), PorterDuff.Mode.SRC_IN);
            campusCurrentMessageCancelBtn.setVisibility(View.VISIBLE);
            campusActivityEditMessageSendBtn.setVisibility(View.VISIBLE);
        }
    });

    campusCurrentMessageCancelBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ut.clickAnimation(view);
            ut.dialogBox(requireContext(), "Are you sure to cancel ?", new utility.DialogCallback() {
                @Override
                public void onConfirm() {
                    campusActivityCurrentMessage.setText(campusCurrentMessageText[0]);

                    ViewGroup parent = (ViewGroup) convertToEditTextCampus.getParent();
                    int index = parent.indexOfChild(convertToEditTextCampus);
                    parent.removeView(convertToEditTextCampus);
                    parent.addView(campusActivityCurrentMessage, index);

                    campusActivityEditMessageSendBtn.setVisibility(View.GONE);
                    campusCurrentMessageCancelBtn.setVisibility(View.GONE);
                    campusActivityEditBtn.setEnabled(true);
                    campusActivityEditBtn.clearColorFilter();
                }

                @Override
                public void onCancel() {
                    //Do nothing
                }
            });

        }
    });

    //Connecting to firebase firestore, homepage collection and accessing campusActivity document
    DocumentReference docRefCampus = firestore.collection("homePage").document("campusActivity");
    DocumentReference docRefClass;


    //updating campus activity message
    campusActivityNewMessageSendBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ut.clickAnimation(view);
            if(!ut.isNetworkAvailable(requireContext())){
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
            ut.dialogBox(requireContext(), "Are sure to send message ?", new utility.DialogCallback(){
                @Override
                public void onConfirm() {
                    String newMessage = campusActivityNewMessage.getText().toString();
                    if(newMessage.isEmpty()){
                        Toast.makeText(getActivity(), "Please enter a message", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    docRefCampus.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if(newMessage.equals(document.getString("message"))){
                                    Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM hh:mm a");
                                String capturedTime = dateFormat.format(calendar.getTime());

                                docRefCampus.update("message", newMessage, "updateTime", capturedTime)
                                        .addOnSuccessListener(aVoid -> {
                                            campusActivityNewMessage.setText("");
                                            Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }else{
                            Toast.makeText(getActivity(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancel() {
                    //Do nothing
                }
            });



        }
    });

        //Setting message on campus current message section
        docRefCampus.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                // Handle the error
                Log.w("Firestore", "Listen failed.", e);
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Get the updated data
                String message = documentSnapshot.getString("message");
                String updateTime = documentSnapshot.getString("updateTime");
                assert message != null;
                if(!message.isEmpty()){
                    campusActivityCurrentMessage.setText(message);
                    campusActivityUpdateTime.setText("updated " + updateTime);
                    campusActivityDelBtn.setVisibility(View.VISIBLE);
                    campusActivityEditBtn.setVisibility(View.VISIBLE);
                }
                else {
                    campusActivityCurrentMessage.setText("This place is left empty");
                    campusActivityUpdateTime.setText("");
                    campusActivityDelBtn.setVisibility(View.GONE);
                    campusActivityEditBtn.setVisibility(View.GONE);
                }
                // Update the TextViews with the new data

            } else {
                Log.d("Firestore", "No such document");
            }
        });

        campusActivityEditMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ut.clickAnimation(view);
                if(!ut.isNetworkAvailable(requireContext())){
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                ut.dialogBox(requireContext(), "Are sure to update message ?", new utility.DialogCallback(){
                    @Override
                    public void onConfirm() {
                        String newMessage = convertToEditTextCampus.getText().toString().trim();
                        if(newMessage.isEmpty()){
                            Toast.makeText(getActivity(), "Please enter a message", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        docRefCampus.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(newMessage.equals(document.getString("message"))){
                                        Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM hh:mm a");
                                    String capturedTime = dateFormat.format(calendar.getTime());
                                    docRefCampus.update("message", newMessage, "updateTime", capturedTime)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                campusActivityCurrentMessage.setText(newMessage);

                                                // Switch EditText back to TextView
                                                ViewGroup parent = (ViewGroup) convertToEditTextCampus.getParent();
                                                int index = parent.indexOfChild(convertToEditTextCampus);
                                                parent.removeView(convertToEditTextCampus);
                                                parent.addView(campusActivityCurrentMessage, index);

                                                // Reset visibility and button states
                                                campusActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                campusCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                campusActivityEditBtn.setEnabled(true);
                                                campusActivityEditBtn.clearColorFilter();

                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            }else{
                                Toast.makeText(getActivity(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancel() {
                        //do nothing
                    }
                });

            }
        });

        campusActivityDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ut.clickAnimation(view);
                if(!ut.isNetworkAvailable(requireContext())){
                    Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                ut.dialogBox(requireContext(), "Are you sure to delete current message", new utility.DialogCallback() {
                    @Override
                    public void onConfirm() {
                        docRefCampus.get().addOnCompleteListener( task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    docRefCampus.update("message", "", "updateTime","");
                                    Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
//                                    campusActivityDelBtn.setVisibility(View.GONE);
//                                    campusActivityEditBtn.setVisibility(View.GONE);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }
                });


            }
        });

        //Campus Activity section Ends here//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Class Activity section starts here//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        classActivityEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ConstraintLayout campusCurrentMessageLayout = view.findViewById(R.id.campusCurrentMessageLayout);
                convertToEditTextClass.setLayoutParams(classActivityCurrentMessage.getLayoutParams());
                convertToEditTextClass.setText(classActivityCurrentMessage.getText());
                classCurrentMessageText [0] = campusActivityCurrentMessage.getText().toString();

                convertToEditTextClass.setBackgroundResource(R.drawable.message_input_background);
                convertToEditTextClass.setPadding(25,27,25,27);

                ViewGroup parent = (ViewGroup) classActivityCurrentMessage.getParent();
                int index = parent.indexOfChild(classActivityCurrentMessage);
                parent.removeView(classActivityCurrentMessage);
                parent.addView(convertToEditTextClass, index);
                convertToEditTextClass.requestFocus();

                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(convertToEditTextClass, InputMethodManager.SHOW_IMPLICIT);

                classActivityEditBtn.setEnabled(false);
                classActivityEditBtn.setColorFilter(Color.parseColor("#00a6ff"), PorterDuff.Mode.SRC_IN);
                classCurrentMessageCancelBtn.setVisibility(View.VISIBLE);
                classActivityEditMessageSendBtn.setVisibility(View.VISIBLE);
            }
        });

        classCurrentMessageCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ut.clickAnimation(view);
                ut.dialogBox(requireContext(), "Are you sure to cancel ?", new utility.DialogCallback() {
                    @Override
                    public void onConfirm() {
                        classActivityCurrentMessage.setText(classCurrentMessageText[0]);

                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                        int index = parent.indexOfChild(convertToEditTextClass);
                        parent.removeView(convertToEditTextClass);
                        parent.addView(classActivityCurrentMessage, index);

                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                        classActivityEditBtn.setEnabled(true);
                        classActivityEditBtn.clearColorFilter();
                    }

                    @Override
                    public void onCancel() {
                        //Do nothing
                    }
                });

            }
        });

        classActivityNewMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ut.clickAnimation(view);
                if(!ut.isNetworkAvailable(requireContext())){
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                ut.dialogBox(requireContext(), "Are sure to send message ?", new utility.DialogCallback(){
                    @Override
                    public void onConfirm() {

                        String newMessage = classActivityNewMessage.getText().toString().trim();
                        if(newMessage.isEmpty()){
                            Toast.makeText(getActivity(), "Please enter a message", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // For third year students
                        if(loginState.getUserYear(requireActivity()).equals("Third Year")){
                            DocumentReference docRefClass = firestore.collection("homePage").document("classActivityThird");
                            docRefClass.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM hh:mm a");
                                        String capturedTime = dateFormat.format(calendar.getTime());

                                        //For cse third year students
                                        if(loginState.getUserBranch(requireActivity()).equals("Computer Science & Engineering")){
                                            if(newMessage.equals(document.getString("cseMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("cseMessage", newMessage, "cseUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Automobile Engineering")){
                                            if(newMessage.equals(document.getString("autoMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("autoMessage", newMessage, "autoUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Civil Engineering")){
                                            if(newMessage.equals(document.getString("civilMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("civilMessage", newMessage, "civilUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Electrical Engineering")){
                                            if(newMessage.equals(document.getString("electricalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electricalMessage", newMessage, "electricalUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Electronics Engineering")){
                                            if(newMessage.equals(document.getString("electronicsMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electronicsMessage", newMessage, "electronicsUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Mechanical Engineering")){
                                            if(newMessage.equals(document.getString("mechanicalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("mechanicalMessage", newMessage, "mechanicalUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }

                                    }
                                }else{
                                    Toast.makeText(getActivity(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                        //for first year students
                        if(loginState.getUserYear(requireActivity()).equals("First Year")){
                            DocumentReference docRefClass = firestore.collection("homePage").document("classActivityFirst");
                            docRefClass.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM hh:mm a");
                                        String capturedTime = dateFormat.format(calendar.getTime());

                                        //For cse third year students
                                        if(loginState.getUserBranch(requireActivity()).equals("Computer Science & Engineering")){
                                            if(newMessage.equals(document.getString("cseMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("cseMessage", newMessage, "cseUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Automobile Engineering")){
                                            if(newMessage.equals(document.getString("autoMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("autoMessage", newMessage, "autoUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Civil Engineering")){
                                            if(newMessage.equals(document.getString("civilMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("civilMessage", newMessage, "civilUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Electrical Engineering")){
                                            if(newMessage.equals(document.getString("electricalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electricalMessage", newMessage, "electricalUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Electronics Engineering")){
                                            if(newMessage.equals(document.getString("electronicsMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electronicsMessage", newMessage, "electronicsUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if (loginState.getUserBranch(requireActivity()).equals("Mechanical Engineering")){
                                            if(newMessage.equals(document.getString("mechanicalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("mechanicalMessage", newMessage, "mechanicalUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }

                                    }
                                }else{
                                    Toast.makeText(getActivity(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        //For second year students
                        if(loginState.getUserYear(requireActivity()).equals("Second Year")){
                            DocumentReference docRefClass = firestore.collection("homePage").document("classActivitySecond");
                            docRefClass.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM hh:mm a");
                                        String capturedTime = dateFormat.format(calendar.getTime());

                                        //For cse second year students
                                        if(loginState.getUserBranch(requireActivity()).equals("Computer Science & Engineering")){
                                            if(newMessage.equals(document.getString("cseMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("cseMessage", newMessage, "cseUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        //For auto second year students
                                        if (loginState.getUserBranch(requireActivity()).equals("Automobile Engineering")){
                                            if(newMessage.equals(document.getString("autoMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("autoMessage", newMessage, "autoUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        //For civil second year students
                                        if (loginState.getUserBranch(requireActivity()).equals("Civil Engineering")){
                                            if(newMessage.equals(document.getString("civilMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("civilMessage", newMessage, "civilUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        //For electrical second year students
                                        if (loginState.getUserBranch(requireActivity()).equals("Electrical Engineering")){
                                            if(newMessage.equals(document.getString("electricalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electricalMessage", newMessage, "electricalUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        //For electronics second year students
                                        if (loginState.getUserBranch(requireActivity()).equals("Electronics Engineering")){
                                            if(newMessage.equals(document.getString("electronicsMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electronicsMessage", newMessage, "electronicsUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        //For mechanical second year students
                                        if (loginState.getUserBranch(requireActivity()).equals("Mechanical Engineering")){
                                            if(newMessage.equals(document.getString("mechanicalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("mechanicalMessage", newMessage, "mechanicalUpdated", capturedTime)
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                }else{
                                    Toast.makeText(getActivity(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancel() {
                        //Do nothing
                    }
                });
            }
        });

        //Setting message on class current message section

        //First year
        if(loginState.getUserYear(requireContext()).equals("First Year")){
            docRefClass = firestore.collection("homePage").document("classActivityFirst");

            if(loginState.getUserBranch(requireContext()).equals("Computer Science & Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("cseMessage");
                        String updateTime = documentSnapshot.getString("cseUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Automobile Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("autoMessage");
                        String updateTime = documentSnapshot.getString("autoUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Civil Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("civilMessage");
                        String updateTime = documentSnapshot.getString("civilUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Electrical Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("electricalMessage");
                        String updateTime = documentSnapshot.getString("electricalUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Electronics Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("electronicsMessage");
                        String updateTime = documentSnapshot.getString("electronicsUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Mechanical Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("mechanicalMessage");
                        String updateTime = documentSnapshot.getString("mechanicalUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
        }

        //Second Year
        if(loginState.getUserYear(requireContext()).equals("Second Year")){
            docRefClass = firestore.collection("homePage").document("classActivitySecond");

            if(loginState.getUserBranch(requireContext()).equals("Computer Science & Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("cseMessage");
                        String updateTime = documentSnapshot.getString("cseUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Automobile Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("autoMessage");
                        String updateTime = documentSnapshot.getString("autoUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Civil Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("civilMessage");
                        String updateTime = documentSnapshot.getString("civilUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Electrical Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("electricalMessage");
                        String updateTime = documentSnapshot.getString("electricalUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Electronics Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("electronicsMessage");
                        String updateTime = documentSnapshot.getString("electronicsUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Mechanical Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("mechanicalMessage");
                        String updateTime = documentSnapshot.getString("mechanicalUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
        }

        //third year
        if(loginState.getUserYear(requireContext()).equals("Third Year")){
            docRefClass = firestore.collection("homePage").document("classActivityThird");

            if(loginState.getUserBranch(requireContext()).equals("Computer Science & Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("cseMessage");
                        String updateTime = documentSnapshot.getString("cseUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Automobile Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("autoMessage");
                        String updateTime = documentSnapshot.getString("autoUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Civil Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("civilMessage");
                        String updateTime = documentSnapshot.getString("civilUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Electrical Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("electricalMessage");
                        String updateTime = documentSnapshot.getString("electricalUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Electronics Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("electronicsMessage");
                        String updateTime = documentSnapshot.getString("electronicsUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if(loginState.getUserBranch(requireContext()).equals("Mechanical Engineering")){
                docRefClass.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle the error
                        Log.w("Firestore", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get the updated data
                        String message = documentSnapshot.getString("mechanicalMessage");
                        String updateTime = documentSnapshot.getString("mechanicalUpdated");
                        assert message != null;
                        if(!message.isEmpty()){
                            classActivityCurrentMessage.setText(message);
                            classActivityUpdateTime.setText("updated " + updateTime);
                            classActivityDelBtn.setVisibility(View.VISIBLE);
                            classActivityEditBtn.setVisibility(View.VISIBLE);
                        }
                        else {
                            classActivityCurrentMessage.setText("This place is left empty");
                            classActivityUpdateTime.setText("");
                            classActivityDelBtn.setVisibility(View.GONE);
                            classActivityEditBtn.setVisibility(View.GONE);
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
        }

        classActivityDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ut.clickAnimation(view);
                if(!ut.isNetworkAvailable(requireContext())){
                    Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                ut.dialogBox(requireContext(), "Are you sure to delete current message", new utility.DialogCallback() {
                    @Override
                    public void onConfirm() {

                        // Third Year
                        if(loginState.getUserYear(requireActivity()).equals("Third Year")){
                            DocumentReference docRefClass = firestore.collection("homePage").document("classActivityThird");

                            if (loginState.getUserBranch(requireActivity()).equals("Automobile Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("autoMessage", "", "autoUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Civil Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("civilMessage", "", "civilUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Electrical Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("electricalMessage", "", "electricalUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Electronics Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("electronicsMessage", "", "electronicsUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Mechanical Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("mechanicalMessage", "", "mechanicalUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Computer Science & Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("cseMessage", "", "cseUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                        }

                        //Second Year
                        if(loginState.getUserYear(requireActivity()).equals("Second Year")){
                            DocumentReference docRefClass = firestore.collection("homePage").document("classActivitySecond");

                            if (loginState.getUserBranch(requireActivity()).equals("Automobile Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("autoMessage", "", "autoUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Civil Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("civilMessage", "", "civilUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Electrical Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("electricalMessage", "", "electricalUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Electronics Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("electronicsMessage", "", "electronicsUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Mechanical Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("mechanicalMessage", "", "mechanicalUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Computer Science & Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("cseMessage", "", "cseUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                        }

                        //First Year
                        if(loginState.getUserYear(requireActivity()).equals("First Year")){
                            DocumentReference docRefClass = firestore.collection("homePage").document("classActivityFirst");

                            if (loginState.getUserBranch(requireActivity()).equals("Automobile Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("autoMessage", "", "autoUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Civil Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("civilMessage", "", "civilUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Electrical Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("electricalMessage", "", "electricalUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Electronics Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("electronicsMessage", "", "electronicsUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Mechanical Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("mechanicalMessage", "", "mechanicalUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                            if (loginState.getUserBranch(requireActivity()).equals("Computer Science & Engineering")){
                                docRefClass.get().addOnCompleteListener( task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            docRefClass.update("cseMessage", "", "cseUpdated","")
                                                    .addOnSuccessListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Message deleted", Toast.LENGTH_SHORT).show();
                                                        classActivityDelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setVisibility(View.GONE);
                                                    })
                                                    .addOnFailureListener(aVoid ->{
                                                        Toast.makeText(getActivity(), "Unable to delete message", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        classActivityEditMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ut.isNetworkAvailable(requireActivity())){
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                ut.dialogBox(requireContext(), "Are You sure to send message", new utility.DialogCallback() {
                    @Override
                    public void onConfirm() {
                        String newMessage = convertToEditTextClass.getText().toString().trim();
                        if(loginState.getUserYear(requireActivity()).equals("Third Year")){
                            DocumentReference docRefClass = firestore.collection("homePage").document("classActivityThird");
                            docRefClass.get().addOnCompleteListener(Task ->{
                                if(Task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = Task.getResult();
                                    if(documentSnapshot.exists()){
                                        if(loginState.getUserBranch(requireActivity()).equals("Computer Science & Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("cseMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("cseMessage", newMessage, "cseUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Automobile Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("autoMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("autoMessage", newMessage, "autoUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Civil Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("civilMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("civilMessage", newMessage, "civilUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Electrical Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("electricalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electricalMessage", newMessage, "electricalUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Electronics Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("electronicsMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electronicsMessage", newMessage, "electronicsUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Mechanical Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("mechanicalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("mechanicalMessage", newMessage, "mechanicalUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                }
                            });
                        }
                        if(loginState.getUserYear(requireActivity()).equals("First Year")){
                            DocumentReference docRefClass = firestore.collection("homePage").document("classActivityFirst");
                            docRefClass.get().addOnCompleteListener(Task ->{
                                if(Task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = Task.getResult();
                                    if(documentSnapshot.exists()){


                                        if(loginState.getUserBranch(requireActivity()).equals("Computer Science & Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("cseMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("cseMessage", newMessage, "cseUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Automobile Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("autoMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("autoMessage", newMessage, "autoUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Civil Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("civilMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("civilMessage", newMessage, "civilUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Electrical Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("electricalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electricalMessage", newMessage, "electricalUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Electronics Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("electronicsMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electronicsMessage", newMessage, "electronicsUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Mechanical Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("mechanicalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("mechanicalMessage", newMessage, "mechanicalUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                }
                            });
                        }
                        if(loginState.getUserYear(requireActivity()).equals("Second Year")){
                            DocumentReference docRefClass = firestore.collection("homePage").document("classActivitySecond");
                            docRefClass.get().addOnCompleteListener(Task ->{
                                if(Task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = Task.getResult();
                                    if(documentSnapshot.exists()){


                                        if(loginState.getUserBranch(requireActivity()).equals("Computer Science & Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("cseMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("cseMessage", newMessage, "cseUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Automobile Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("autoMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("autoMessage", newMessage, "autoUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Civil Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("civilMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("civilMessage", newMessage, "civilUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Electrical Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("electricalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electricalMessage", newMessage, "electricalUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Electronics Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("electronicsMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("electronicsMessage", newMessage, "electronicsUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                        if(loginState.getUserBranch(requireActivity()).equals("Mechanical Engineering")){
                                            if(newMessage.equals(documentSnapshot.getString("mechanicalMessage"))){
                                                Toast.makeText(getActivity(), "No changes made", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            docRefClass.update("mechanicalMessage", newMessage, "mechanicalUpdated", ut.getDateTime())
                                                    .addOnSuccessListener(aVoid -> {
                                                        classActivityNewMessage.setText("");
                                                        Toast.makeText(getActivity(), "Message updated", Toast.LENGTH_SHORT).show();

                                                        // Switch EditText back to TextView
                                                        ViewGroup parent = (ViewGroup) convertToEditTextClass.getParent();
                                                        int index = parent.indexOfChild(convertToEditTextClass);
                                                        parent.removeView(convertToEditTextClass);
                                                        parent.addView(classActivityCurrentMessage, index);

                                                        // Reset visibility and button states
                                                        classActivityEditMessageSendBtn.setVisibility(View.GONE);
                                                        classCurrentMessageCancelBtn.setVisibility(View.GONE);
                                                        classActivityEditBtn.setEnabled(true);
                                                        classActivityEditBtn.clearColorFilter();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancel() {
                        //Do nothing
                    }
                });
            }

        });

        btnManageRoutine.setOnClickListener( click -> {
            fragmentManageRoutines fragManageRoutines = new fragmentManageRoutines();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,  // enter animation
                    R.anim.slide_out_left,  // exit animation
                    R.anim.slide_in_left,   // pop enter (when returning back)
                    R.anim.slide_out_right );
            transaction.replace(R.id.mainLayout, fragManageRoutines);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnManageNotices.setOnClickListener( click -> {
            ut.replaceFragment(getParentFragmentManager(), new fragementManageNotice(), R.id.mainLayout);
        });



        return view;
    }
}