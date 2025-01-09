package com.example.my_campus.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_campus.R;
import com.example.my_campus.loginState;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class fragmentHomepage extends Fragment {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public fragmentHomepage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        TextView campusActivityMessage = view.findViewById(R.id.campusActivityMessage);
        TextView campusActivityUpdated = view.findViewById(R.id.campusActivityUpdated);
        TextView classActivityMessage = view.findViewById(R.id.classActivityMessage);
        TextView classActivityUpdated = view.findViewById(R.id.classActivityUpdated);

        //Setting realtime message update to campus activity
        DocumentReference docRef = firestore.collection("homePage").document("campusActivity");
        docRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                // Handle the error
                Log.w("Firestore", "Listen failed.", e);
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Get the updated data
                String message = documentSnapshot.getString("message");
                String updateTime = documentSnapshot.getString("updateTime");

                // Update the TextViews with the new data
                if (message != null && !message.isEmpty()) {
                    campusActivityMessage.setText(message);
                    campusActivityUpdated.setText(updateTime);
                } else {
                    campusActivityMessage.setText("This place is left empty");
                }
            } else {
                Log.d("Firestore", "No such document");
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView classActivityMessage = view.findViewById(R.id.classActivityMessage);
        TextView classActivityUpdated = view.findViewById(R.id.classActivityUpdated);


        //Setting Class activity message
        //Third year
        if (loginState.getUserYear(requireContext()).equals("Third Year")) {
            DocumentReference docRefClass = firestore.collection("homePage").document("classActivityThird");

            if (loginState.getUserBranch(requireContext()).equals("Computer Science & Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Automobile Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Civil Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Electrical Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Electronics Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Mechanical Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }

        }

        //Second Year
        if (loginState.getUserYear(requireContext()).equals("Second Year")) {
            DocumentReference docRefClass = firestore.collection("homePage").document("classActivitySecond");

            if (loginState.getUserBranch(requireContext()).equals("Computer Science & Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Automobile Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Civil Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Electrical Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Electronics Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Mechanical Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }

        }

        //First Year
        if (loginState.getUserYear(requireContext()).equals("First Year")) {
            DocumentReference docRefClass = firestore.collection("homePage").document("classActivityFirst");

            if (loginState.getUserBranch(requireContext()).equals("Computer Science & Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Automobile Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Civil Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Electrical Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Electronics Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }
            if (loginState.getUserBranch(requireContext()).equals("Mechanical Engineering")) {
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
                        if (!message.isEmpty()) {
                            classActivityMessage.setText(message);
                            classActivityUpdated.setText(updateTime);

                        } else {
                            classActivityMessage.setText("This place is left empty");
                            classActivityUpdated.setText("");
                        }

                    } else {
                        Log.d("Firestore", "No such document");
                    }
                });
            }

        }
    }

}