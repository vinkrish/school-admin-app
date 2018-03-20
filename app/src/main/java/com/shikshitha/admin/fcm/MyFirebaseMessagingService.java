package com.shikshitha.admin.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shikshitha.admin.App;
import com.shikshitha.admin.R;
import com.shikshitha.admin.chat.ChatActivity;
import com.shikshitha.admin.chathome.ChatsActivity;
import com.shikshitha.admin.model.MessageEvent;
import com.shikshitha.admin.util.AppGlobal;
import com.shikshitha.admin.util.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        AppGlobal.setSqlDbHelper(getApplicationContext());

        if (remoteMessage.getData().size() > 0 &&
                SharedPreferenceUtil.isNotifiable(getApplicationContext()) &&
                !SharedPreferenceUtil.getTeacher(getApplicationContext()).getAuthToken().equals("")) {
            if(App.isActivityVisible()) {
                EventBus.getDefault().post(new MessageEvent(remoteMessage.getData().get("message"),
                        Long.valueOf(remoteMessage.getData().get("sender_id"))));
            } else {
                chatNotification(remoteMessage);
            }
        }
    }

    private void chatNotification(RemoteMessage remoteMessage) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_stat_notify)
                        .setContentTitle(remoteMessage.getData().get("sender_name"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")));

        mBuilder.setAutoCancel(true);

        Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
        resultIntent.putExtra("recipientId", Long.valueOf(remoteMessage.getData().get("sender_id")));
        resultIntent.putExtra("recipientName", remoteMessage.getData().get("sender_name"));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(ChatsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
