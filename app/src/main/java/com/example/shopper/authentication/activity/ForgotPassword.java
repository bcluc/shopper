package com.example.shopper.authentication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText mEmailEditText;
    private Button mVerifyButton;
    private ImageButton back;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Khởi tạo FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        back = findViewById(R.id.btn_back_to_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        // Lấy đối tượng EditText và Button từ layout
        mEmailEditText = findViewById(R.id.edtxt_email);
        mVerifyButton = findViewById(R.id.btn_verify);
        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPassword.this,
                            "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gửi email xác thực từ Firebase đến email của người dùng
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this,
                                            "Reset password email sent",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgotPassword.this, Success.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPassword.this,
                                            "Error sending reset password email",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}