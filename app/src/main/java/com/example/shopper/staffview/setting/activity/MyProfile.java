package com.example.shopper.staffview.setting.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shopper.R;
import com.example.shopper.authentication.model.User;
import com.example.shopper.staffview.StaffHomePage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MyProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    User object;
    int position;
    ProgressDialog progressDialog;
    //ArrayList<User> AdminList;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    DocumentReference docRef;
    StorageReference storageReference;
    private String currentUserId;
    private LinearLayout layout;
    private String imagePath;
    private ImageView edImg, imgAvt;
    private String ImageUrl;
    private String oldImageUrl;
    private final ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if (o == null) {
                Toast.makeText(MyProfile.this, "No image was selected.", Toast.LENGTH_SHORT).show();
            } else {
                imagePath = String.valueOf(o);
                Glide.with(MyProfile.this).load(imagePath).into(edImg);
                updateImgInStorage(o);
            }
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_my_profile);
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        imgAvt = findViewById(R.id.img_avt_Profile);
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        docRef = FirebaseFirestore.getInstance()
                .collection("USERS").document(firebaseAuth.getUid());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EventInit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        findViewById(R.id.btn_changeInfo_Profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventEdit();
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfile.this, StaffHomePage.class);
                startActivity(intent);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            findViewById(R.id.constraintLayout).setLeftTopRightBottom(0, 0, 0, 180);
        }
        EventInit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference = FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status", "Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference = FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status", "Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

    }

    private void EventInit() {
        db.collection("USERS").document(firebaseAuth.getUid()).
                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        object = documentSnapshot.toObject(User.class);
                        ((TextView) findViewById(R.id.name_Profile)).setText(object.getFullName());
                        ((TextView) findViewById(R.id.sex_Profile)).setText(object.getSex());
                        ((TextView) findViewById(R.id.txt_dob_Profile)).setText(object.getDayOfBirth());
                        ((TextView) findViewById(R.id.txt_pnumP7_Profile)).setText(object.getPhoneNumber());
                        ((TextView) findViewById(R.id.txt_email_Profile)).setText(object.getEmail());
                        ((TextView) findViewById(R.id.txt_address_Profile)).setText(object.getAddress());

                        ImageUrl = object.getAvatar();
                        try {
                            if (ImageUrl.isEmpty()) {
                            } else {
                                Picasso.get().load(ImageUrl).into((ImageView) findViewById(R.id.img_avt_Profile));
                                storageReference = firebaseStorage.getReferenceFromUrl(ImageUrl);
                            }
                        } catch (Exception e) {

                        }
                        oldImageUrl = ImageUrl;
                    }
                });
    }


    private void EventEdit() {
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setGravity(Gravity.CENTER)
                .setContentHolder(new ViewHolder(R.layout.activity_edit_user))
                .setExpanded(false)
                .create();

        View holderView = (ScrollView) dialogPlus.getHolderView();

        edImg = holderView.findViewById(R.id.img_imageView);
        EditText edName = holderView.findViewById(R.id.txt_name);
        Spinner edSex = holderView.findViewById(R.id.txt_sex);
        TextView edDob = holderView.findViewById(R.id.txt_dob);
        EditText edPhone = holderView.findViewById(R.id.txt_phone);
        EditText edAddr = holderView.findViewById(R.id.txt_address);
        Button btnUpdate = holderView.findViewById(R.id.btn_update);
        Button btnCancel = holderView.findViewById(R.id.btn_cancel);


        ArrayAdapter<CharSequence> gender = ArrayAdapter.createFromResource(this, R.array.gender,
                R.layout.item_spinner);
        gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edSex.setAdapter(gender);
        edSex.setOnItemSelectedListener(this);


        edName.setText(object.getFullName());
        String gt = object.getSex();
        try {
            if (gt.isEmpty()) edSex.setSelection(0);
            else if (gt.equals("Male")) edSex.setSelection(1);
            else if (gt.equals("Female")) edSex.setSelection(2);
        } catch (Exception e) {

        }
        edAddr.setText(object.getAddress());
        edDob.setText(object.getDayOfBirth());
        edPhone.setText(object.getPhoneNumber());
        try {
            if (ImageUrl.isEmpty()) {
                if (!oldImageUrl.isEmpty()) Picasso.get().load(oldImageUrl).into(edImg);
            } else Picasso.get().load(ImageUrl).into(edImg);
        } catch (Exception e) {

        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlus.dismiss();
                try {
                    if (!ImageUrl.isEmpty() && !ImageUrl.equals(oldImageUrl)) {
                        DeleteOldImg(ImageUrl);
                        ImageUrl = oldImageUrl;
                    }
                } catch (Exception e) {
                }
            }
        });
        edImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }
        });

        edDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MyProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                updateUserAvatar(imagePathUri);
                Map<String, Object> map = new HashMap<>();
                map.put("fullName", edName.getText().toString());
                map.put("sex", edSex.getSelectedItem().toString());
                map.put("dayOfBirth", edDob.getText().toString());
                map.put("phoneNumber", edPhone.getText().toString());
                map.put("address", edAddr.getText().toString());
                map.put("avatar", ImageUrl);

                docRef.update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MyProfile.this, "Success updating the data", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                                try {
                                    if (!ImageUrl.equals(oldImageUrl)) {
                                        try {
                                            if (!oldImageUrl.isEmpty()) {
                                                DeleteOldImg(oldImageUrl);
                                                oldImageUrl = ImageUrl;
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                } catch (Exception e) {
                                }
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyProfile.this, "Fail to update the data", Toast.LENGTH_SHORT).show();
                                try {
                                    dialogPlus.dismiss();
                                    if (!ImageUrl.equals(oldImageUrl)) {
                                        try {
                                            if (!ImageUrl.isEmpty()) {
                                                DeleteOldImg(ImageUrl);
                                                ImageUrl = oldImageUrl;
                                            }
                                        } catch (Exception d) {
                                        }
                                    }
                                } catch (Exception d) {
                                }
                            }
                        });
            }
        });

        dialogPlus.show();

    }

    private void DeleteOldImg(String deleteImg) {
        progressDialog.setTitle("Waiting...");
        progressDialog.show();
        if (deleteImg != null) {
            StorageReference oldImageRef = firebaseStorage.getReferenceFromUrl(deleteImg);
            oldImageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    // File deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    // Uh-oh, an error occurred
                }
            });
        }
    }

    private void updateImgInStorage(Uri imageUri) {
        progressDialog.setTitle("Updating...");
        progressDialog.show();
        StorageReference storageRef = firebaseStorage.getReference();

        String imagePath = "userImages/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(imagePath);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                ImageUrl = downloadUri.toString();
                                progressDialog.dismiss();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MyProfile.this, "Fail to send image to firebase", Toast.LENGTH_LONG).show();
                        // Tải ảnh thất bại
                    }
                });

    }

    private void updateUserAvatar(Uri imageUri) {
        progressDialog.setTitle("Updating...");
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference avatarRef = storageRef.child("userImages/" + UUID.randomUUID().toString() + ".jpg");

        avatarRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        ImageUrl = uri.toString();
                        db.collection("USERS").document(currentUserId)
                                .update("avatar", ImageUrl)
                                .addOnSuccessListener(aVoid -> {
                                    try {
                                        Picasso.get().load(ImageUrl).into(imgAvt);
                                    } catch (Exception e) {
                                    }
                                    try {
                                        if (!ImageUrl.equals(oldImageUrl)) {
                                            try {
                                                if (!oldImageUrl.isEmpty()) {
                                                    DeleteOldImg(oldImageUrl);
                                                    oldImageUrl = ImageUrl;
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                    } catch (Exception e) {
                                    }

                                })
                                .addOnFailureListener(e -> {
                                    try {
                                        if (!ImageUrl.equals(oldImageUrl)) {
                                            try {
                                                if (!ImageUrl.isEmpty()) {
                                                    DeleteOldImg(ImageUrl);
                                                }
                                            } catch (Exception d) {
                                            }
                                        }
                                    } catch (Exception d) {
                                    }
                                    Toast.makeText(MyProfile.this, "Failed to update avatar", Toast.LENGTH_SHORT).show();
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MyProfile.this, "Failed to upload avatar", Toast.LENGTH_SHORT).show();
                });
        progressDialog.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
