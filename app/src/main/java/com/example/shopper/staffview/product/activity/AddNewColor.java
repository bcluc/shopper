package com.example.shopper.staffview.product.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopper.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddNewColor extends AppCompatActivity {
    private ImageButton button_back;

    private Button btn_addnew;
    private static final int REQUEST_IMAGE_PICK = 1;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private EditText colorname, colorCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_color);
        button_back = findViewById(R.id.btn_back_color);
        btn_addnew = findViewById(R.id.btn_add_new_color);
        colorCode = findViewById(R.id.color_code);
        colorname = findViewById(R.id.color_name);

        firestore = FirebaseFirestore.getInstance();
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewColor.this.finish();
            }
        });


        btn_addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MaMau = colorCode.getText().toString();
                String TenMau = colorname.getText().toString();
                Map<String, Object> colorData = new HashMap<>();
                CollectionReference mausacCollection = firestore.collection("COLOR");
                colorData.put("colorCode", MaMau);
                colorData.put("colorName", TenMau);
                // Tạo một thể hiện của collection MAUSAC trong Firestore
                mausacCollection.add(colorData)
                        .addOnSuccessListener(documentReference -> {
                            String documentId = documentReference.getId();
                            mausacCollection.document(documentId)
                                    .update("colorId", documentId)
                                    .addOnSuccessListener(aVoid -> {
                                        // Cập nhật thành công
                                        // Điều hướng đến màn hình mới
                                        Intent intent = new Intent(AddNewColor.this, AddNewProduct.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xảy ra lỗi khi cập nhật
                                        // (Bạn có thể thêm mã logic xử lý lỗi ở đây)
                                    });
                        })
                        .addOnFailureListener(e -> {
                        });

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

}

