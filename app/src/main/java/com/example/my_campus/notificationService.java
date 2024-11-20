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
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.DocumentSnapshot;

public class notificationService extends Service {
    private static final String CHANNEL_ID = "campus_activity_channel";
    private static final String CAMPUS_PREFS_NAME = "CampusActivityPrefs";
    private static final String CLASS_PREFS_NAME_1 = "ClassActivityPrefs1";
    private static final String CLASS_PREFS_NAME_2 = "ClassActivityPrefs2";
    private static final String CLASS_PREFS_NAME_3 = "ClassActivityPrefs3";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        listenForCampusActivityChanges();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Service will be restarted if it's killed
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // We don't want to bind this service
    }

    private void listenForCampusActivityChanges() {
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("homePage")
                .document("campusActivity");

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String message = documentSnapshot.getString("message");

                    // Retrieve the last message from SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences(CAMPUS_PREFS_NAME, MODE_PRIVATE);
                    String lastMessage = sharedPreferences.getString("campusLastMessage", "");

                    // Check if the message is different from the last message
                    if (!message.equals(lastMessage)) {
                        // Save the new message in SharedPreferences
                        sharedPreferences.edit().putString("campusLastMessage", message).apply();
                        sendNotification(message, "Campus Activity Update");  // Send notification if message is different
                    }
                }
            }
        });
    }

    private void listnerForClassActivityChanges(){}
    

    private void sendNotification(String messageBody, String tittle) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Replace with your notification icon
                .setContentTitle(tittle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Campus Activity Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
