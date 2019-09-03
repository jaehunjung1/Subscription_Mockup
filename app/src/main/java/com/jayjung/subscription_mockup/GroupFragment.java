package com.jayjung.subscription_mockup;

import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class GroupFragment extends Fragment {
    MainActivity activity;
    ConstraintLayout layout;

    private RecyclerView recyclerView;
    private GroupNameAdapter groupNameAdapter;
    ArrayList<String> groupNameArrayList;

    private FloatingActionButton fab;

    MaterialButton emptyButton;
    TextView emptyText;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout =  (ConstraintLayout)inflater.inflate(R.layout.fragment_group, container, false);

        // RecyclerView Logic for group card view
        recyclerView = layout.findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        groupNameArrayList = new ArrayList<>();
        groupNameAdapter = new GroupNameAdapter(activity, groupNameArrayList);
        recyclerView.setAdapter(groupNameAdapter);

        // Floating Action Button Logic
        fab = layout.findViewById(R.id.group_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.beenToGroupEditActivity = true;
                Intent intent = new Intent(activity, GroupEditActivity.class);
                intent.putExtra("isAdd", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivityIfNeeded(intent, 0);
            }
        });

        emptyButton = layout.findViewById(R.id.empty_button);
        emptyText = layout.findViewById(R.id.empty_text);
        return layout;
    }

    void addOrEditGroup(boolean isAdd, String groupName) {
        if (isAdd) {
            groupNameArrayList.add(groupName);
            groupNameAdapter.notifyDataSetChanged();
        } else {
            groupNameArrayList.set(activity.groupEditPos, groupName);
            groupNameAdapter.notifyDataSetChanged();

            activity.groupEditPos = -1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkEmpty();
    }

    void checkEmpty() {
        if (groupNameArrayList.isEmpty()) {
            emptyButton.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyButton.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void onStartButtonClick(NotificationManager notificationManager, View view) {
        MaterialButton button = (MaterialButton) view;

        if (button.getText().toString().equals("START")) {


            for (NotificationChannelGroup group : notificationManager.getNotificationChannelGroups()) {
                notificationManager.deleteNotificationChannelGroup(group.getId());
            }

            ArrayList<NotificationChannelGroup> groupList = new ArrayList<>();

            for (String groupName : groupNameArrayList) {
                groupList.add(new NotificationChannelGroup(groupName, groupName));
            }

            notificationManager.createNotificationChannelGroups(groupList);
        }
    }

}
