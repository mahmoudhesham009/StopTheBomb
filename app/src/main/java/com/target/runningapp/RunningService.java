package com.target.runningapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class RunningService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification= new NotificationCompat.Builder(this,"myId")
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Keep Running")
                .setContentText("you are doing agood job")
                .build();
        startForeground(1,notification);
        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
