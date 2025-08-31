package com.example.my_campus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (!remoteMessage.getData().isEmpty()) {
            Log.d("FCM_RECEIVED", "onMessageReceived: " + remoteMessage.getData().get("title"));
            sendCustomNotification(remoteMessage);
        }
    }

    private void sendCustomNotification(RemoteMessage remoteMessage) {
        Intent intent;

        String clickAction = remoteMessage.getData().get("click_action");
        String messageType = remoteMessage.getData().get("messageType");
        String messagePos = remoteMessage.getData().get("messagePosition");
        int messagePosition = -1;
        if (messagePos!=null){
            messagePosition = Integer.parseInt(Objects.requireNonNull(remoteMessage.getData().get("messagePosition")));
        }

        Log.d("INTENT_KEY", "chatID: "+ messageType);
        Log.d("INTENT_KEY", "messagePosition: "+ messagePosition);
        Log.d("INTENT_KEY", "clickAction: "+ clickAction);

        if ("OPEN_CAMPUS_ACTIVITY".equals(clickAction)) {
            intent = new Intent(this, activityCampusActivity.class);
            intent.putExtra("messagePosition", messagePosition);
        }else if ("OPEN_CLASS_ACTIVITY".equals(clickAction)){
            intent = new Intent(this, activityClassActivity.class);
            intent.putExtra("messagePosition", messagePosition);
            Log.d("INTENT_KEY", "intent class : "+ intent);
        }
        else {
            intent = new Intent(this, MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "activity_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(messageType);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
