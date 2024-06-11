package com.example.shopper.customerview.notification.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopper.R;
import com.example.shopper.customerview.navigation.fragment.NotificationFragment;

public class MyNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NotificationFragment())
                    .commit();
        }
    }
}