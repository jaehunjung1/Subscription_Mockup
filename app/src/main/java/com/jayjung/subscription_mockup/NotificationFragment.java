package com.jayjung.subscription_mockup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jayjung.subscription_mockup.service.NotificationThreadService;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {
    MainActivity activity;
    ConstraintLayout layout;

    private RecyclerView recyclerView;
    private NotiContainerAdapter notiContainerAdapter;
    ArrayList<NotiContainer> notiContainerArrayList;

    private FloatingActionButton fab;

    MaterialButton emptyButton;
    TextView emptyText;


    public NotificationFragment() {
        // Required empty public constructor
    }

    public NotificationFragment(ArrayList<NotiContainer> initNotiContainerList) {
        this.notiContainerArrayList = initNotiContainerList;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        layout = (ConstraintLayout)inflater.inflate(R.layout.fragment_notification, container, false);

        // RecyclerView Logic for notification card view
        recyclerView = layout.findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        notiContainerAdapter = new NotiContainerAdapter(activity, notiContainerArrayList);
        recyclerView.setAdapter(notiContainerAdapter);


        // Floating Action Button Logic
        fab = layout.findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.beenToEditActivity = true;
                Intent intent = new Intent(activity, EditActivity.class);
                intent.putExtra("isAdd", true);
                intent.putExtra("channelArray", activity.groupFragment.groupNameArrayList);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivityIfNeeded(intent, 0);
            }
        });

        emptyButton = layout.findViewById(R.id.empty_button);
        emptyText = layout.findViewById(R.id.empty_text);
        return layout;
    }

    void addOrEditNotification(boolean isAdd, String title, String text, String channel) {
        if (isAdd) {
            notiContainerArrayList.add(new NotiContainer(title, text, channel));
            notiContainerAdapter.notifyDataSetChanged();
        } else {
            NotiContainer editTarget = notiContainerArrayList.get(activity.editPos);
            editTarget.contentTitle = title;
            editTarget.contentText = text;
            editTarget.channelName = channel;
            notiContainerAdapter.notifyDataSetChanged();

            activity.editPos = -1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkEmpty();
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

    void onStartButtonClick(View view) {
        Intent intent = new Intent(activity, NotificationThreadService.class);

        MaterialButton button = (MaterialButton)view;
        if (button.getText().toString().equals("START")) {
            if (notiContainerArrayList.isEmpty()) {
                Toast.makeText(activity, "설정한 노티피케이션이 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putParcelableArrayListExtra("containers", notiContainerArrayList);
            activity.startService(intent);

            button.setText("STOP");
        } else {
            activity.stopService(intent);

            button.setText("START");
        }
    }

}
