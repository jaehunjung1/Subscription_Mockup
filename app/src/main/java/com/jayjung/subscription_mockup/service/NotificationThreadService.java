package com.jayjung.subscription_mockup.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.jayjung.subscription_mockup.MainActivity;
import com.jayjung.subscription_mockup.NotiContainer;
import com.jayjung.subscription_mockup.R;

import java.util.ArrayList;
import java.util.Random;

public class NotificationThreadService extends Service {
    ArrayList<NotiContainer> notiContainerArrayList;

    NotificationManager manager;
    ServiceThread thread;

    public NotificationThreadService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notiContainerArrayList = intent.getParcelableArrayListExtra("containers");

        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        ServiceHandler handler = new ServiceHandler(notiContainerArrayList);
        thread = new ServiceThread(handler);
        thread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (thread != null) {
            thread.stopForever();
            thread = null;
        }

//        super.onDestroy();
    }

    class ServiceHandler extends Handler {
        private ArrayList<NotiContainer> notiContainerArrayList;
        private Random random = new Random();

        ServiceHandler(ArrayList<NotiContainer> containers) {
            this.notiContainerArrayList = containers;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            NotiContainer container = notiContainerArrayList.get(
                    random.nextInt(notiContainerArrayList.size()));

            Intent intent = new Intent(NotificationThreadService.this, MainActivity.class);
            PendingIntent snoozeIntent = PendingIntent.getActivity(
                    NotificationThreadService.this, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    NotificationThreadService.this, container.channelName)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                    .setContentTitle(container.contentTitle)
                    .setContentText(container.contentText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(snoozeIntent)
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setSmallIcon(R.drawable.ic_launcher_foreground);;
                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel channel = new NotificationChannel(
                        container.channelName,
                        container.channelName,
                        importance);
                channel.setDescription(container.channelDesc);
                channel.setGroup(container.channelName);

                manager.createNotificationChannel(channel);
            } else {
                builder.setSmallIcon(R.mipmap.ic_launcher);
            }

            manager.notify(NotificationID.getID(), builder.build());
        }
    }
}

