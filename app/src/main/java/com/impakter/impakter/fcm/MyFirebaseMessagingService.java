package com.impakter.impakter.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static int NOTIFICATION_ID = 0;
    private static int REQUEST_CODE = 0;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG, "onNewToken: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived: " + "Tao da vao day");
        Log.d(TAG, "From: " + remoteMessage.getData());
        Log.d(TAG, "Data: " + remoteMessage.getData().toString());
//        sendNotification(this,remoteMessage.getData().get("message").toString());
        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData().get("message"));
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String messageBody) {
        Log.i(TAG, "sendNotification: " + messageBody);
        vibratorAndRingtone();
        Intent intent = new Intent(this, MainActivity.class);
//        Bundle b = new Bundle();
//        b.putString(Constants.MATCH_ID, matchId);
//        intent.putExtra(Constants.DATA, b);
        intent.setAction("push");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
//        Constants.IS_PUSH = true;
        REQUEST_CODE++;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String channelId = getString(R.string.app_name);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_app)
                        .setContentTitle(getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        NOTIFICATION_ID = NOTIFICATION_ID + 1;
        notificationManager.notify(NOTIFICATION_ID /* ID of notification */, notificationBuilder.build());

    }

    private void vibratorAndRingtone() {
        // For Vibrate
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        // For Sound
        Uri notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                notification);
        r.play();
    }
}