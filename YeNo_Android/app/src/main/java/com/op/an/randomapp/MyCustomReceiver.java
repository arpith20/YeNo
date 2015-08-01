package com.op.an.randomapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MyCustomReceiver extends ParsePushBroadcastReceiver {
    private static final String TAG = "MyCustomReceiver";

    protected void onPushReceive (Context context, Intent intent) {
        try {
            showNotification(context);
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString(
                    "com.parse.Data"));

            Log.d(TAG, "got action " + action + " on channel " + channel
                    + " with:");
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                Log.d(TAG, "..." + key + " => " + json.getString(key));
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }

    }

    private void showNotification(Context context) {
        Intent intent = new Intent(context, WriteQueryDatabase.class);
        PendingIntent pIntent = PendingIntent
                .getActivity(context, 0, intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(context)
                .setContentTitle("A new disaster has been reported near you")
                .setContentText("Click to see more details")
                .setSmallIcon(R.drawable.incidentmarker).setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.nothing, "View", pIntent).build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }
}
