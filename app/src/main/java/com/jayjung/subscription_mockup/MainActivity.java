package com.jayjung.subscription_mockup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean beenToEditActivity = false;
    int editPos = -1;

    private NotificationManager notificationManager;

    private RecyclerView recyclerView;
    private NotiContainerAdapter notiContainerAdapter;
    private ArrayList<NotiContainer> notiContainerArrayList;

    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // User Permission
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Button b = findViewById(R.id.test_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerNotification();
            }
        });

        // RecyclerView Logic for notification card view
        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notiContainerArrayList = new ArrayList<>();
        notiContainerAdapter = new NotiContainerAdapter(this, notiContainerArrayList);
        recyclerView.setAdapter(notiContainerAdapter);

        notiContainerArrayList.add(new NotiContainer("LALALA Title", "LALALA HEHEHE", "Social"));
        notiContainerArrayList.add(new NotiContainer("LALALA Title 2", "LALALA HEHEHE 2", "Ad"));

        notiContainerAdapter.notifyDataSetChanged();

        fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beenToEditActivity = true;
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("isAdd", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (beenToEditActivity) {
            beenToEditActivity = false;
            Intent intent = getIntent();
            if (intent.getBooleanExtra("canceled", false))
                return;

            boolean isAdd = intent.getBooleanExtra("isAdd", false);

            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            String channel = intent.getStringExtra("channel");

            if (isAdd) {
                notiContainerArrayList.add(new NotiContainer(title, text, channel));
                notiContainerAdapter.notifyDataSetChanged();
            } else {
                NotiContainer editTarget = notiContainerArrayList.get(editPos);
                editTarget.contentTitle = title;
                editTarget.contentText = text;
                editTarget.channelName = channel;
                notiContainerAdapter.notifyDataSetChanged();

                editPos = -1;
            }
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void triggerNotification() {
        PendingIntent snoozeIntent =
                PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                MainActivity.this, generateChannelId())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle("Test Content Title")
                .setContentText("Test Content Text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(snoozeIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            String channelName = "Test Noti Channel";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(generateChannelId(), channelName, importance);
            channel.setDescription(description);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }

        notificationManager.notify(1234, builder.build());
    }

    private String generateChannelId() {
        return "10001";
    }
}
