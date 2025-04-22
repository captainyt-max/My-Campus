package com.example.my_campus;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class notificationService extends Service {

    private static final String CHANNEL_ID = "campus_activity_channel";
    private static final String CAMPUS_PREFS_NAME = "CampusActivityPrefs";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        listenForCampusActivityChanges();
        listenForClassActivityChanges();
        Log.d("NotificationService", "Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Campus Service Running")
                .setContentText("")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(1, notificationBuilder.build());

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void listenForCampusActivityChanges() {
        FirebaseFirestore.getInstance()
                .collection("campus activity")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.d("NotificationService", "Error: " + e.getMessage());
                        return;
                    }

                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        SharedPreferences sharedPreferences = getSharedPreferences(CAMPUS_PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String message = document.getString("message");
                            String docId = document.getId(); // Unique per doc

                            if (message != null) {
                                String savedMessage = sharedPreferences.getString("msg_" + docId, "");

                                if (!message.equals(savedMessage)) {
                                    editor.putString("msg_" + docId, message);
                                    editor.apply();

                                    sendNotification(message, "Campus Activity Update");
                                }
                            }
                        }
                    } else {
                        Log.d("NotificationService", "No documents found in campus activity collection");
                    }
                });
    }

    private void listenForClassActivityChanges() {
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("homePage")
                .document("classActivity");

        docRef.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.d("NotificationService", "Error: " + e.getMessage());
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                String message = documentSnapshot.getString("message");

                if (message != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences(CAMPUS_PREFS_NAME, MODE_PRIVATE);
                    String lastClassMsg = sharedPreferences.getString("classLastMessage", "");

                    if (!message.equals(lastClassMsg)) {
                        sharedPreferences.edit().putString("classLastMessage", message).apply();
                        sendNotification(message, "Class Activity Update");
                    }
                }
            }
        });
    }

    private void sendNotification(String messageBody, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = (int) System.currentTimeMillis(); // Unique ID
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Campus Activity Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for campus and class updates");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
