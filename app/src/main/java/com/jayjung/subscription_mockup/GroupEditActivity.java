package com.jayjung.subscription_mockup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class GroupEditActivity extends AppCompatActivity {
    private Intent intent;
    private boolean isAdd; // true if adding new notification, false if editing

    MaterialTextView headline;
    TextInputEditText groupNameEditText;

    MaterialButton addButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        headline = findViewById(R.id.edit_activity_headline);

        View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view instanceof TextInputEditText)
                    ((TextInputEditText)view).setError(null);
                else
                    ((AutoCompleteTextView)view).setError(null);
            }
        };

        groupNameEditText = findViewById(R.id.group_name_edit_text);
        groupNameEditText.setOnFocusChangeListener(focusListener);


        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupNameEditText.getText().toString().trim().isEmpty()) {
                    groupNameEditText.setError("Group Name should not be empty.");
                } else {
                    groupNameEditText.setError(null);
                }

                Intent intent = new Intent(GroupEditActivity.this, MainActivity.class);
                intent.putExtra("isAdd", isAdd);
                intent.putExtra("name", groupNameEditText.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupEditActivity.this, MainActivity.class);
                intent.putExtra("canceled", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        intent = getIntent();
        isAdd = intent.getBooleanExtra("isAdd", true);

        setViewWithAddMode(isAdd);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    private void setViewWithAddMode(boolean isAdd) {
        if (isAdd) {
            headline.setText("Add Group");
            groupNameEditText.setText("");

            addButton.setText("ADD");
        } else {
            headline.setText("Edit Group");
            groupNameEditText.setText(intent.getStringExtra("name"));

            addButton.setText("DONE");
        }
    }
}
