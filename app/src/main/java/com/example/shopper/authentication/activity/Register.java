package com.example.shopper.authentication.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopper.R;
import com.example.shopper.authentication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Base64;
import java.util.Calendar;

public class Register extends AppCompatActivity {

    private EditText tvEmail, tvUserName, tvPhoneNumber, tvPassword, tvConfirmPassword;
    private EditText tvDayOfBirth;
    private Button btnRegister;
    private boolean see = false;
    private ImageView imgShowPassword, imgShowConfirmPassword;
    private ImageButton btnBackToLogin;
    private FrameLayout layoutProgressBar;
    private ScrollView layoutRegister;
    private boolean isComplete;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        // initialising all views through id defined above
        layoutProgressBar = findViewById(R.id.progress_bar_layout);
        layoutRegister = findViewById(R.id.register_layout);
        tvEmail = findViewById(R.id.edtxt_email);
        tvPassword = findViewById(R.id.editTextPassword);
        tvConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        tvUserName = findViewById(R.id.edtxt_full_name);
        tvPhoneNumber = findViewById(R.id.editTextPhone);
        tvDayOfBirth = findViewById(R.id.editTextDayOfBirth);
        imgShowPassword = findViewById(R.id.showpassword);
        btnBackToLogin = findViewById(R.id.btn_back_to_login);

        layoutProgressBar.setVisibility(View.GONE);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (!see) {
                    see = true;
                    imgShowPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_unsee));
                    tvPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                } else {
                    see = false;
                    imgShowPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_see));

                    tvPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                tvPassword.setSelection(tvPassword.length());
            }
        });
        imgShowConfirmPassword = findViewById(R.id.showconfirmpassword);
        imgShowConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (!see) {
                    see = true;
                    imgShowConfirmPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_unsee));
                    tvConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    see = false;
                    imgShowConfirmPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_see));
                    tvConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                tvConfirmPassword.setSelection(tvPassword.length());
            }
        });
        tvDayOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DayPicker();
                }
            }
        });
        btnRegister = findViewById(R.id.buttonRegister);
        // Set on Click Listener on Registration button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = tvUserName.getText().toString();
                final String emailUTF = tvEmail.getText().toString();
                final String email = Base64.getEncoder().encodeToString(emailUTF.getBytes());
                //String encodedEmailFromDatabase = "bGVkYW5ndGh1b25nMjAwM0BnbWFpbC5jb20=";
                //String decodedEmail = new String(Base64.getDecoder().decode(encodedEmailFromDatabase));
                // Cách mã hóa lại code
                final String password = tvPassword.getText().toString();
                final String confirmPassword = tvConfirmPassword.getText().toString();
                final String phoneNumber = tvPhoneNumber.getText().toString();
                final String dayOfBirth = tvDayOfBirth.getText().toString();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                } else if (password.length() < 6) {
                    Toast.makeText(Register.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                //
                //  ADD CHECKER
                //
                else if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Password is not matching, please check Password and  Confirm Password again", Toast.LENGTH_SHORT).show();
                } else {
                    isComplete = false;
                    checkProgress();
                    reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(email)) {
                                Toast.makeText(Register.this, "Email has already registered, try another Email", Toast.LENGTH_SHORT).show();
                                isComplete = true;
                                checkProgress();
                            } else {
                                // sending data to firebase realtime
                                reference.child("Users").child(email).child("email").setValue(emailUTF);
                                reference.child("Users").child(email).child("userType").setValue("Customer");
                                registerNewUser();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),
                                            "Process canceled!!",
                                            Toast.LENGTH_LONG)
                                    .show();
                            isComplete = true;
                            checkProgress();
                        }
                    });
                }
            }
        });
    }

    public static String sanitizeEmail(String email) {
        String sanitizedEmail = email.replaceAll(".", ",");
        return sanitizedEmail;
    }

    private void checkProgress() {
        if(!isComplete){
            layoutProgressBar.setVisibility(View.VISIBLE);
            layoutRegister.setEnabled(false);
        }
        else{
            layoutProgressBar.setVisibility(View.GONE);
            layoutRegister.setEnabled(true);
        }
    }

    public void DayPicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // Update EditText with selected date
                        EditText editTextDOB = findViewById(R.id.editTextDayOfBirth);
                        editTextDOB.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void registerNewUser() {
        // Take the value of two edit texts in Strings
        String email, password, fullName, phoneNumber, dayOfBirth;
        String avatar = null;
        String address = null;
        String sex = null;

        String status = "Online";
        String userID;
        email = tvEmail.getText().toString();
        password = tvPassword.getText().toString();
        fullName = tvUserName.getText().toString();
        phoneNumber = tvPhoneNumber.getText().toString();
        dayOfBirth = tvDayOfBirth.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // Trong phương thức registerNewUser()


        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userID = task.getResult().getUser().getUid();
                            User user = new User(fullName, email, dayOfBirth, phoneNumber, userID, avatar,
                                    address, sex, status, "Customer");
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference usersCollection = db.collection("USERS");

                            usersCollection.document(userID).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Register.this, "Register Successful, Login Now!", Toast.LENGTH_SHORT).show();
                                            isComplete = true;
                                            checkProgress();
                                            Intent intent
                                                    = new Intent(Register.this,
                                                    Login.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                                            isComplete = true;
                                            checkProgress();
                                        }
                                    });
                        } else {

                            // Registration failed
                            Toast.makeText(
                                            getApplicationContext(),
                                            "Registration failed!!"
                                                    + " Please try again later",
                                            Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                        }

                    }
                });
    }
}