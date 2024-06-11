package com.example.shopper.authentication.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.shopper.authentication.model.User;
import com.example.shopper.customerview.navigation.activity.BottomNavigationCustomerActivity;
import com.example.shopper.staffview.StaffHomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.shopper.R;

import java.util.Base64;

public class Login extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    // Xử lý sự kiện click vào Fortgot password và Sign up
    private TextView forgotPassword, signUp;
    private Boolean see;
    private Button btnLogin;
    private Button btnShowPassword;
    public ProgressBar progressBar;
    private User getStaff;
    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // initialising all views through id defined above
        emailTextView = findViewById(R.id.edtxt_login_email);
        passwordTextView = findViewById(R.id.edtxt_login_password);
        btnLogin = findViewById(R.id.btn_login);
        forgotPassword = (TextView)findViewById(R.id.tv_forgot_password);
        btnShowPassword = findViewById(R.id.btn_show_password);
        signUp = (TextView)findViewById(R.id.tv_sign_up);
        getStaff = new User();

        see = false;
        btnShowPassword.setBackground(getResources().getDrawable(R.drawable.ic_unsee));
        // Set on Click Listener on Sign-in button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = emailTextView.getText().toString();
                password = passwordTextView.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter all required information.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Xác thực thành công
                                String emailHandle = mAuth.getCurrentUser().getEmail();
                                String emailChange = Base64.getEncoder().encodeToString(emailHandle.getBytes());
                                DatabaseReference userRef = firebaseDatabase.getReference("Users").child(emailChange);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String userType = dataSnapshot.child("userType").getValue(String.class);
                                        if (userType != null) {
                                            //Log.d("TYPE", userType);
                                            switch (userType) {
                                                case "Customer":
                                                    // Người dùng là khách hàng
                                                    Toast.makeText(getApplicationContext(), "Login as customer",
                                                            Toast.LENGTH_SHORT).show();
                                                    // Chuyển người dùng đến màn hình khách hàng
                                                    Intent customerIntent = new Intent(Login.this, BottomNavigationCustomerActivity.class);
                                                    startActivity(customerIntent);
                                                    finish();
                                                    break;
                                                case "Staff":
                                                    // Người dùng là nhân viên
                                                    Toast.makeText(getApplicationContext(), "Login as staff",
                                                            Toast.LENGTH_SHORT).show();
                                                    // Chuyển người dùng đến màn hình nhân viên
                                                    Intent staffIntent = new Intent(Login.this, StaffHomePage.class);
                                                    startActivity(staffIntent);
                                                    finish();
                                                    break;
                                                case "Admin":
                                                    // Người dùng là admin
                                                    Toast.makeText(getApplicationContext(), "Login as admin",
                                                            Toast.LENGTH_SHORT).show();
                                                    // Chuyển người dùng đến màn hình admin
                                                    Intent adminIntent = new Intent(Login.this, StaffHomePage.class);
                                                    startActivity(adminIntent);
                                                    finish();
                                                    break;
                                                default:
                                                    // Giá trị không hợp lệ
                                                    Toast.makeText(getApplicationContext(), "Invalid user type",
                                                            Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Xử lý lỗi nếu có
                                    }
                                });
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference dbRef = db.collection("Users");
                                DocumentReference docRef = dbRef.document(emailHandle);
                                docRef.get().addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        String User = documentSnapshot.getString("userId");
                                        getStaff.setUserId(User);
                                    }
                                });
                            } else if (!task.isSuccessful()) {
                                Toast.makeText(
                                                getApplicationContext(),
                                                "Login failed!!"
                                                        + " Please try again later",
                                                Toast.LENGTH_LONG)
                                        .show();

                            }
                        }

                    });
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
        btnShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(see == false) {
                    see = true;
                    btnShowPassword.setBackground(getResources().getDrawable(R.drawable.ic_see));
                    passwordTextView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    see = false;
                    btnShowPassword.setBackground(getResources().getDrawable(R.drawable.ic_unsee));
                    passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                passwordTextView.setSelection(passwordTextView.length());
            }
        });
    }
}