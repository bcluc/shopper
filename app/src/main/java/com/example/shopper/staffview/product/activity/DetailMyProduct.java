package com.example.shopper.staffview.product.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.product.activity.DetailProduct;
import com.example.shopper.customerview.util.color.model.Colors;
import com.example.shopper.staffview.review.activity.Reviewer;
import com.example.shopper.staffview.viewshop.adapter.color.MyColorsAdapter;
import com.example.shopper.staffview.viewshop.adapter.size.MyProductSizeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailMyProduct extends AppCompatActivity {

    private ImageView backIcon;
    private ImageButton show;
    private TextView detail;
    private boolean isTextViewVisible = false;
    private String MaDM;
    private ImageView hinhanhSP, btnEdit;
    private TextView name, price;
    private TextView description, review;
    private RecyclerView color, size;
    private Button trending;
    private String MaSP;

    DetailProduct detailProductActivity;
    private Boolean check = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_product);

        MaDM = getIntent().getStringExtra("categoryId");
        MaSP = getIntent().getStringExtra("productId");
        backIcon = findViewById(R.id.icon_back);
        show = findViewById(R.id.show);
        btnEdit = findViewById(R.id.icon_edit);
        detail = findViewById(R.id.txt_detail);
        hinhanhSP = findViewById(R.id.hinhanhSP);
        name = findViewById(R.id.name_product);
        price = findViewById(R.id.price_product);
        description = findViewById(R.id.txt_detail);
        review = findViewById(R.id.review);
        color = findViewById(R.id.RCV_color);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.HORIZONTAL, false);
        color.setLayoutManager(linearLayoutManager);
        color.setEnabled(false);
        size = findViewById(R.id.RCV_size);
        LinearLayoutManager linearLayoutManagerSize = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.HORIZONTAL, false);
        size.setLayoutManager(linearLayoutManagerSize);
        size.setEnabled(false);
        trending = findViewById(R.id.trending);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailMyProduct.this, EditProduct.class);
                intent.putExtra("productId", MaSP);
                startActivity(intent);
            }
        });
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một truy vấn Firestore để lấy MaDM từ TenDM
                FirebaseFirestore.getInstance().collection("REVIEW")
                        .whereEqualTo("productId", MaSP)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                // Lấy MaDM từ kết quả truy vấn
                                String categoryId = task.getResult().getDocuments().get(0).getId();

                                // Chuyển sang màn hình hiển thị sản phẩm với categoryId
                                Intent intent = new Intent(DetailMyProduct.this, Reviewer.class);
                                intent.putExtra("productId", MaSP);
                                startActivity(intent);
                            }
                        });
            }
        });
        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference donHangRef = FirebaseFirestore.getInstance().collection("PRODUCT");
                DocumentReference docRef = donHangRef.document(MaSP);
                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Boolean trangThai = documentSnapshot.getBoolean("trending");
                        boolean newTrangThai;
                        if (trangThai) {
                            newTrangThai = false;
                        } else if (!trangThai) {
                            newTrangThai = true;
                        } else {
                            return;
                        }
                        docRef.update("trending", newTrangThai)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Cập nhật thành công
                                            Toast.makeText(DetailMyProduct.this, "Trending is updated: " + String.valueOf(newTrangThai), Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Cập nhật thất bại
                                            Toast.makeText(DetailMyProduct.this, "Trending update failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });
            }
        });
        CollectionReference trendingfalseRef = FirebaseFirestore.getInstance().collection("PRODUCT");


        CollectionReference sanphamRef = FirebaseFirestore.getInstance().collection("PRODUCT");
        CollectionReference mausacRef = FirebaseFirestore.getInstance().collection("COLOR");
        CollectionReference sizeRef = FirebaseFirestore.getInstance().collection("SIZE");
        sanphamRef.whereEqualTo("productId", MaSP).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (!querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        String tenSP = documentSnapshot.getString("productName");
                        List<String> hinhanh = (List<String>) documentSnapshot.get("imageUrl");
                        String hinhanhSPurl = hinhanh.get(0);
                        Picasso.get().load(hinhanhSPurl).into(hinhanhSP);
                        int giaSP = documentSnapshot.getLong("productPrice") != null ? documentSnapshot.getLong("productPrice").intValue() : 0;
                        String moTa = documentSnapshot.getString("description");
                        description.setText(moTa);
                        price.setText(String.valueOf(giaSP));
                        name.setText(tenSP);

                        List<String> mauSacList = (List<String>) documentSnapshot.get("productColor");
                        if (mauSacList != null && !mauSacList.isEmpty()) {
                            List<Colors> listmau = new ArrayList<>();
                            MyColorsAdapter colorAdapter = new MyColorsAdapter();
                            for (String mauSacId : mauSacList) {
                                // Truy vấn từng MauSac trong collection "MAUSAC" theo mã màu sắc (mauSacId)
                                mausacRef.document(mauSacId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot mausacDocumentSnapshot) {
                                        if (mausacDocumentSnapshot.exists()) {
                                            String tenMauSac = mausacDocumentSnapshot.getString("colorName");
                                            String maMau = mausacDocumentSnapshot.getString("colorCode");
                                            String colorID = mausacDocumentSnapshot.getString("colorId");
                                            Colors colors = new Colors(colorID, maMau , tenMauSac);
                                            listmau.add(colors);
                                            colorAdapter.setMyData(listmau, DetailMyProduct.this);
                                            color.setAdapter(colorAdapter);
                                        }
                                    }
                                });
                            }
                        }
                        List<String> sizeList = (List<String>) documentSnapshot.get("productSize");
                        if (sizeList != null && !sizeList.isEmpty()) {
                            List<String> sizeList1 = new ArrayList<>();
                            MyProductSizeAdapter sizeAdapter = new MyProductSizeAdapter();
                            for (String sizeId : sizeList) {
                                sizeRef.document(sizeId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshotsize) {
                                        if (documentSnapshotsize.exists()) {
                                            String Masize = documentSnapshotsize.getString("size");
                                            sizeList1.add(Masize);
                                            sizeAdapter.setMyData(sizeList1, DetailMyProduct.this);
                                            size.setAdapter(sizeAdapter);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nếu TextView đang ẩn, thì hiển thị nó và đặt biến isTextViewVisible thành true
                if (!isTextViewVisible) {
                    detail.setVisibility(View.VISIBLE);
                    isTextViewVisible = true;
                    show.setRotation(270);
                } else {
                    // Ngược lại, ẩn TextView và đặt biến isTextViewVisible thành false
                    detail.setVisibility(View.INVISIBLE);
                    isTextViewVisible = false;
                    show.setRotation(180);
                }
            }
        });

        setOnClickBackICon();
    }
    public void onColorClick(String colorCode) {
        // Xử lý sự kiện khi màu được chọn
        // Dữ liệu màu tên và mã màu được truyền từ adapter qua activity
    }

    public void onSizeClick(String size) {
        // Xử lý sự kiện khi màu được chọn
        // Dữ liệu màu tên và mã màu được truyền từ adapter qua activity
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

    private void setOnClickBackICon() {

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailMyProduct.this.finish();
            }
        });
    }
}