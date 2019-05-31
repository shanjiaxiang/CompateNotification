package com.jorchi.remote_view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;

public class NotificationUtils {
    private static NotificationUtils INSTANCE;
    private final String channel_id = "channel_1";
    private final CharSequence name = "channel";
    private final String description = "channel description";
    private final int importance = NotificationManager.IMPORTANCE_HIGH;
    private final int notification_id = 12234;
    private Context context;
    private NotificationManager notificationManager;
    private Notification notification;
    private NotificationChannel mChannel;


    public NotificationUtils(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static NotificationUtils getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (NotificationUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NotificationUtils(context);
                }
            }
        }
        return INSTANCE;
    }


    public void showRemoteViewNotification(String title, String content, int iconId, Class<?> cls, RemoteViews remoteViews) {
        Intent intent = new Intent(context, cls);
        PendingIntent pi = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT <= LOLLIPOP_MR1) {
            notification = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setWhen(System.currentTimeMillis())
                    .build();
        } else if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O && Build.VERSION.SDK_INT >= LOLLIPOP_MR1) {
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(iconId)
                    .setAutoCancel(true)
                    .setContent(remoteViews)
                    .setContentIntent(pi).build();
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            mChannel = new NotificationChannel(channel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);

            notification = new NotificationCompat.Builder(context, channel_id)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContent(remoteViews)
                    .setContentIntent(pi)
                    .build();
        }
        notificationManager.notify(notification_id, notification);
    }
}
