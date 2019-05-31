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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
                remoteViews.setTextViewText(R.id.notification_titile, "remote title");
                remoteViews.setTextViewText(R.id.notification_content, "remote content");
                remoteViews.setImageViewResource(R.id.notification_iv, R.mipmap.ic_launcher);

                NotificationUtils.getInstance(MainActivity.this)
                        .showRemoteViewNotification("utilTitle", "utilContent", android.R.drawable.stat_sys_download_done,
                                Main2Activity.class, remoteViews);
//                showNotification2();
            }
        });
    }

    public void showNotification2() {

        final int NOTIFICATION_ID = 12234;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, Main2Activity.class);
        Notification notification = null;
        String contentTitle = "title";
        String contentText = "content";
        PendingIntent pi = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.notification_titile, "remote title");
        remoteViews.setTextViewText(R.id.notification_content, "remote content");
        remoteViews.setImageViewResource(R.id.notification_iv, R.mipmap.ic_launcher);
        remoteViews.setOnClickPendingIntent(R.id.notification_iv, pi);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT <= LOLLIPOP_MR1) {
            notification = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setWhen(System.currentTimeMillis())
                    .build();
        } else if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O && Build.VERSION.SDK_INT >= LOLLIPOP_MR1) {
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setAutoCancel(true)
                    .setContent(remoteViews)
                    .setContentIntent(pi).build();
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = null;
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);



            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle(contentTitle)
                    .setAutoCancel(true)
                    .setContent(remoteViews)
                    .setContentText(contentText).build();
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}