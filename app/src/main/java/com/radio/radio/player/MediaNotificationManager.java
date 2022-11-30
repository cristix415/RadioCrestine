package com.radio.radio.player;

import static com.google.common.reflect.Reflection.getPackageName;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.exoplayer2.util.Log;
import com.radio.radio.BuildConfig;
import com.radio.radio.MainActivity;
import com.radio.radio.R;

import java.io.InputStream;

public class MediaNotificationManager {

    public static final int NOTIFICATION_ID = 555;
    private final String PRIMARY_CHANNEL = "PRIMARY_CHANNEL_ID";
    private final String PRIMARY_CHANNEL_NAME = "PRIMARY";

    private RadioService service;

    private String strAppName, strLiveBroadcast;

    private Resources resources;
    int c=0;

    private NotificationManagerCompat notificationManager;

    public MediaNotificationManager(RadioService service) {

        this.service = service;
        this.resources = service.getResources();
        Log.e("notificare", "notiffffffffff");
        strAppName = resources.getString(R.string.app_name);
        if (service.shoutcast != null)
            strLiveBroadcast = service.shoutcast.getName();

        notificationManager = NotificationManagerCompat.from(service);
    }


    public void startNotify(String playbackStatus) {
        notificationManager.cancel(NOTIFICATION_ID);
c++;
        strLiveBroadcast = service.shoutcast.getName();
        String imageName =  service.shoutcast.getIcon();
        InputStream imageInputStream = null;
        try {

              imageInputStream = resources.getAssets().open(imageName);

        }
        catch (Exception ex)
        {
            Log.e( "eroare",  ex.getMessage().toString());
        }

                //;BuildConfig.APPLICATION_ID +  "/drawable/" + service.shoutcast.getIcon();
       // Log.e( String.valueOf(imageUrl),  imageUrl + String.valueOf(c));
        Bitmap largeIcon = BitmapFactory.decodeStream(imageInputStream);

        int icon = R.drawable.ic_pause_white;
        Intent playbackAction = new Intent(service, RadioService.class);
        playbackAction.setAction(RadioService.ACTION_PAUSE);
        PendingIntent action = PendingIntent.getService(service, 1, playbackAction, PendingIntent.FLAG_IMMUTABLE);

        if (playbackStatus.equals(PlaybackStatus.PAUSED)) {

            icon = R.drawable.ic_play_white;
            playbackAction.setAction(RadioService.ACTION_PLAY);
            action = PendingIntent.getService(service, 2, playbackAction, PendingIntent.FLAG_IMMUTABLE);

        }

        Intent stopIntent = new Intent(service, RadioService.class);
        stopIntent.setAction(RadioService.ACTION_STOP);
        PendingIntent stopAction = PendingIntent.getService(service, 3, stopIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent intent = new Intent(service, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        notificationManager.cancel(NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL, PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, PRIMARY_CHANNEL)
                .setAutoCancel(false)
                .setContentTitle(strLiveBroadcast)
                .setContentText(strLiveBroadcast)
                .setLargeIcon(largeIcon)

                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                .addAction(icon, "pause", action)
                .addAction(R.drawable.ic_stop_white, "stop", stopAction)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //   .setWhen(System.currentTimeMillis())
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(service.getMediaSession().getSessionToken())
                        .setShowActionsInCompactView(0, 1)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(stopAction));


        service.startForeground(NOTIFICATION_ID, builder.build());
    }

    public void cancelNotify() {

        service.stopForeground(true);
    }

}
