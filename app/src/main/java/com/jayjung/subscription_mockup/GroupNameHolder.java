package com.jayjung.subscription_mockup;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class GroupNameHolder extends RecyclerView.ViewHolder {
    MaterialTextView groupNameTextView;
    MaterialButton containerMenu;

    public GroupNameHolder(@NonNull View itemView) {
        super(itemView);

        groupNameTextView = itemView.findViewById(R.id.group_name_text_view);
        containerMenu = itemView.findViewById(R.id.container_menu);
    }

    public void setContainerInfo(String groupName) {
        groupNameTextView.setText(String.format("Group %s", groupName));
    }
}
