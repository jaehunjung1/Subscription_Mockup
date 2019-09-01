package com.jayjung.subscription_mockup;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotiContainerAdapter extends RecyclerView.Adapter<NotiContainerHolder> {
    private Context context;

    ArrayList<NotiContainer> containerArrayList;

    public NotiContainerAdapter(Context context, ArrayList<NotiContainer> containers) {
        this.context = context;
        this.containerArrayList = containers;
    }

    @NonNull
    @Override
    public NotiContainerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_view_notification, parent, false);
        final NotiContainerHolder holder = new NotiContainerHolder(view);
        final PopupMenu menu = new PopupMenu(context, holder.containerMenu);
        menu.getMenuInflater().inflate(R.menu.notification_popup_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_edit:
                        Intent intent = new Intent(context, EditActivity.class);
                        intent.putExtra("isAdd", false);
                        intent.putExtra("title", holder.titleTextView.getText().toString());
                        intent.putExtra("text", holder.textTextView.getText().toString());
                        // TODO channel intent로 넣어주기
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                        MainActivity mainActivity = (MainActivity)context;
                        mainActivity.editPos = holder.getAdapterPosition();
                        mainActivity.beenToEditActivity = true;
                        mainActivity.startActivityIfNeeded(intent, 0);
                        return true;
                    case R.id.popup_delete:
                        int pos = holder.getAdapterPosition();
                        containerArrayList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, containerArrayList.size());
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
    public void onBindViewHolder(@NonNull NotiContainerHolder holder, int position) {
        NotiContainer container = containerArrayList.get(position);
        holder.setContainerInfo(container);
    }

    @Override
    public int getItemCount() {
        return containerArrayList.size();
    }
}
