package com.example.kidshealthv3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyNotificationPublisher extends BroadcastReceiver {

MediaPlayer mp1;
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("NotifyChild", "KidsHealth Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context , "NotifyChild");
        builder.setContentTitle("KidsHealth");
        builder.setContentText("Hi, Let's Move!");
        builder.setSmallIcon(R.drawable.logo);

        MediaPlayer.create(context, R.raw.fastfeel).start();

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setShowWhen(true);

        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(BitmapFactory.decodeResource(context.getResources(), R.mipmap.notify_img));
        builder.setStyle(style);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());

    }



}