package com.jayjung.subscription_mockup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotiContainerAdapter extends RecyclerView.Adapter<NotiContainerHolder> {
    private Context context;

    private ArrayList<NotiContainer> containerArrayList;

    public NotiContainerAdapter(Context context, ArrayList<NotiContainer> containers) {
        this.context = context;
        this.containerArrayList = containers;
    }

    @NonNull
    @Override
    public NotiContainerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_view_notification, parent, false);

        return new NotiContainerHolder(view);
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
