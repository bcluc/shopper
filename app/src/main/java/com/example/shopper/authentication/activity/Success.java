package com.example.shopper.authentication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopper.R;

public class Success extends AppCompatActivity {

    private Button btn_continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        LinearLayout checkmark = findViewById(R.id.complete);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);

        btn_continue = findViewById(R.id.btn_verify);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Success.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                checkmark.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }
}