package com.jayjung.subscription_mockup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jayjung.subscription_mockup.service.NotificationThreadService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean beenToEditActivity = false;
    int editPos = -1;

    private RecyclerView recyclerView;
    private NotiContainerAdapter notiContainerAdapter;
    private ArrayList<NotiContainer> notiContainerArrayList;

    private FloatingActionButton fab;

    MaterialButton emptyButton;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RecyclerView Logic for notification card view
        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notiContainerArrayList = new ArrayList<>();
        notiContainerAdapter = new NotiContainerAdapter(this, notiContainerArrayList);
        recyclerView.setAdapter(notiContainerAdapter);

        notiContainerArrayList.add(new NotiContainer("LALALA Title", "LALALA HEHEHE", "Social"));
        notiContainerArrayList.add(new NotiContainer("LALALA Title 2", "LALALA HEHEHE 2", "Ad"));

        notiContainerAdapter.notifyDataSetChanged();

        // Floating Action Button Logic
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

        emptyButton = findViewById(R.id.empty_button);
        emptyText = findViewById(R.id.empty_text);
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

        checkEmpty();

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void onStartButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, NotificationThreadService.class);

        MaterialButton button = (MaterialButton)view;
        if (button.getText().toString().equals("START")) {
            if (notiContainerArrayList.isEmpty()) {
                Toast.makeText(MainActivity.this, "설정한 노티피케이션이 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putParcelableArrayListExtra("containers", notiContainerArrayList);
            startService(intent);

            button.setText("STOP");
        } else {
            stopService(intent);

            button.setText("START");
        }
    }

    void checkEmpty() {
        if (notiContainerArrayList.isEmpty()) {
            emptyButton.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyButton.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
    }
}
