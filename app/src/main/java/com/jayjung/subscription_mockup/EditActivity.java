package com.jayjung.subscription_mockup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class EditActivity extends AppCompatActivity {
    private Intent intent;
    private boolean isAdd; // true if adding new notification, false if editing

    MaterialTextView headline;
    TextInputEditText titleEditText, textEditText;
    AutoCompleteTextView channelDropDown;

    MaterialButton addButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

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

        titleEditText = findViewById(R.id.title_edit_text);
        titleEditText.setOnFocusChangeListener(focusListener);

        textEditText = findViewById(R.id.text_edit_text);
        textEditText.setOnFocusChangeListener(focusListener);


        channelDropDown = findViewById(R.id.channel_dropdown);
        channelDropDown.setOnFocusChangeListener(focusListener);
        // TODO change this to real channel
        String[] channels = new String[] {"Channel 1", "Channel 2", "Test Channel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu_item,
                channels);
        channelDropDown.setAdapter(adapter);

        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleEditText.getText().toString().trim().isEmpty()) {
                    titleEditText.setError("Title should not be empty.");
                } else {
                    titleEditText.setError(null);
                }

                if (textEditText.getText().toString().trim().isEmpty()) {
                    textEditText.setError("Content should not be empty.");
                    return;
                } else {
                    textEditText.setError(null);
                }

                if (channelDropDown.getText().toString().trim().isEmpty()) {
                    channelDropDown.setError("Channel should be chosen.");
                    return;
                } else {
                    channelDropDown.setError(null);
                }

                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.putExtra("isAdd", isAdd);
                Log.e("isAdd exiting edit", String.valueOf(isAdd));
                intent.putExtra("title", titleEditText.getText().toString());
                intent.putExtra("text", textEditText.getText().toString());
                intent.putExtra("channel", channelDropDown.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        intent = getIntent();
        isAdd = intent.getBooleanExtra("isAdd", true);
        Log.e("isAdd from Edit", String.valueOf(isAdd));
        setViewWithAddMode(isAdd);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    private void setViewWithAddMode(boolean isAdd) {
        if (isAdd) {
            headline.setText("Add Notification");
            titleEditText.setText("");
            textEditText.setText("");
            channelDropDown.setText("");
            // TODO add channel drop down update here

            addButton.setText("ADD");
        } else {
            headline.setText("Edit Notification");
            titleEditText.setText(intent.getStringExtra("title"));
            textEditText.setText(intent.getStringExtra("text"));
            // TODO add channel drop down update here

            addButton.setText("DONE");

        }
    }
}
