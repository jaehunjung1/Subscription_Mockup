package com.jayjung.subscription_mockup;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

public class NotiContainerHolder extends RecyclerView.ViewHolder {
    private MaterialTextView titleTextView, textTextView, channelNameTextView;

    public NotiContainerHolder(@NonNull View itemView) {
        super(itemView);

        titleTextView = itemView.findViewById(R.id.content_title_text_view);
        textTextView = itemView.findViewById(R.id.content_text_text_view);
        channelNameTextView = itemView.findViewById(R.id.channel_name_text_view);

    }

    public void setContainerInfo(NotiContainer container) {
        titleTextView.setText(container.contentTitle);
        textTextView.setText(container.contentText);
        channelNameTextView.setText(String.format("in Channel %s", container.channelName));

    }
}
