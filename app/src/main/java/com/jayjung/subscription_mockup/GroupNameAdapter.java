package com.jayjung.subscription_mockup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupNameAdapter extends RecyclerView.Adapter<GroupNameHolder> {
    private Context context;

    ArrayList<String> groupNameArrayList;

    public GroupNameAdapter(Context context, ArrayList<String> containers) {
        this.context = context;
        this.groupNameArrayList = containers;
    }

    @NonNull
    @Override
    public GroupNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_view_group, parent, false);
        final MainActivity mainActivity = (MainActivity)context;

        final GroupNameHolder holder = new GroupNameHolder(view);

        final PopupMenu menu = new PopupMenu(context, holder.containerMenu);
        menu.getMenuInflater().inflate(R.menu.notification_popup_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_edit:
                        Intent intent = new Intent(context, GroupEditActivity.class);
                        intent.putExtra("isAdd", false);
                        intent.putExtra("name", holder.groupNameTextView.getText().toString().substring(6));
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                        mainActivity.editPos = holder.getAdapterPosition();
                        mainActivity.beenToGroupEditActivity = true;
                        mainActivity.startActivityIfNeeded(intent, 0);
                        return true;
                    case R.id.popup_delete:
                        int pos = holder.getAdapterPosition();
                        groupNameArrayList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, groupNameArrayList.size());

                        mainActivity.groupFragment.checkEmpty();
                        return true;
                }
                return true;
            }
        });
        holder.containerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupNameHolder holder, int position) {
        String groupName = groupNameArrayList.get(position);
        holder.setContainerInfo(groupName);
    }

    @Override
    public int getItemCount() {
        return groupNameArrayList.size();
    }
}
