package com.phongnx.testapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by usr0102382 on 2017/05/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";
    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Process for display message
        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Notification Message: " + remoteMessage.getNotification());
            Log.d(TAG, "Notification Title: " + remoteMessage.getNotification().getTitle());
            createNotification(remoteMessage.getNotification().getTitle() ,remoteMessage.getNotification().getBody());

        }

        // Process for data message
        if(remoteMessage.getData() != null && !remoteMessage.getData().equals("")){
            Log.d(TAG, "Notification Data: " + remoteMessage.getData());
            String body = "code: " + remoteMessage.getData().get("code") + " message: " + remoteMessage.getData().get("message") + " id: " + remoteMessage.getData().get("id");
            String title = "DATA Message";
            createNotification(title, body);
        }
    }

    private void createNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
