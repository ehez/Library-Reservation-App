package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

   public TextView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notification = findViewById(R.id.text_view_notification);
        String message = getIntent().getStringExtra("message");
        notification.setText(message);
    }
}