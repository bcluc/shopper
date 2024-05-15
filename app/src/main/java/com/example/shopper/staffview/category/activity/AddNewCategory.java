package com.example.shopper.staffview.category.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopper.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AddNewCategory extends AppCompatActivity {

    private ImageButton button_back;
    private ImageView imageView;
    private Uri selectedImageUri; // URI của ảnh đã chọn

    private Button btn_addnew;
    private static final int REQUEST_IMAGE_PICK = 1;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    private TextView TenDM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String MaSP = getIntent().getStringExtra("categoryId");
        setContentView(R.layout.activity_add_new_category);
        button_back = findViewById(R.id.btn_back);
        btn_addnew = findViewById(R.id.btn_add_new);
        TenDM = findViewById(R.id.editTextCategoryName);
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewCategory.this, Category.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        btn_addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    uploadCategory(selectedImageUri);
                } else {
                    Toast.makeText(AddNewCategory.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void uploadCategory(Uri imageUri) {
        // Tạo tham chiếu tới vị trí lưu trữ trên Firebase Storage
        StorageReference imageRef = storageReference.child("categoryImages").child(imageUri.getLastPathSegment());

        try {
            // Chuyển đổi ảnh thành byte array
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            // Tải lên ảnh lên Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Lấy URL của ảnh đã tải lên
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Lưu thông tin của category vào Firestore
                    Map<String, Object> category = new HashMap<>();
                    category.put("categoryImage", uri.toString());
                    category.put("categoryId", "");
                    category.put("quanity", 0);
                    category.put("categoryName", TenDM.getText().toString());

                    firestore.collection("CATEGORY")
                            .add(category)
                            .addOnSuccessListener(documentReference -> {
                                // Cập nhật MaDM với id của tài liệu vừa tạo
                                String MaDM = documentReference.getId();
                                documentReference.update("categoryId", MaDM)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(AddNewCategory.this, "Category added successfully", Toast.LENGTH_SHORT).show();
                                            // Reset các trường dữ liệu và hiển thị ảnh mặc định
                                            selectedImageUri = null;
                                            imageView.setImageResource(R.drawable.add_category);
                                            TenDM.setText("");
                                            Intent intent = new Intent(AddNewCategory.this, Category.class);
                                            startActivity(intent);
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(AddNewCategory.this, "Failed to update category", Toast.LENGTH_SHORT).show());
                            })
                            .addOnFailureListener(e -> Toast.makeText(AddNewCategory.this, "Failed to add category", Toast.LENGTH_SHORT).show());
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(AddNewCategory.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null) {
                Uri newImageUri = data.getData();
                if (newImageUri != null) {
                    try {
                        // Lấy hình ảnh từ URI và hiển thị trong ImageView
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), newImageUri);
                        imageView.setImageBitmap(bitmap);
                        selectedImageUri = newImageUri;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}