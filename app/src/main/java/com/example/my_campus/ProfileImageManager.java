package com.example.my_campus;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfileImageManager {
    public interface ProfileImageUpdateListener {
        void onProfileImageUpdated(String imageUrl);
    }


    private static ProfileImageManager instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String profileImageUrl = null;
    private final List<ProfileImageUpdateListener> listeners = new ArrayList<>();

    // Private constructor for Singleton
    private ProfileImageManager() {}

    public static synchronized ProfileImageManager getInstance() {
        if (instance == null) {
            instance = new ProfileImageManager();
        }
        return instance;
    }

    // Initialize the listener
    public void init(String userEmail) {
        DocumentReference docRef = db.collection("users").document(userEmail);
        docRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e("ProfileImageManager", "Error: " + e.getMessage());
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                String newUrl = documentSnapshot.getString("profileImage");
                if (newUrl != null && !newUrl.equals(profileImageUrl)) {
                    profileImageUrl = newUrl;

                    // Notify all listeners about the update
                    for (ProfileImageUpdateListener listener : listeners) {
                        listener.onProfileImageUpdated(newUrl);
                    }
                }
            }
        });
    }

    // Add a listener
    public void addListener(ProfileImageUpdateListener listener) {
        listeners.add(listener);
        if (profileImageUrl != null) {
            listener.onProfileImageUpdated(profileImageUrl);
        }
    }

    // Remove a listener
    public void removeListener(ProfileImageUpdateListener listener) {
        listeners.remove(listener);
    }
}

