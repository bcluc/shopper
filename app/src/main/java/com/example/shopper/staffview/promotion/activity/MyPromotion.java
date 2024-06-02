package com.example.shopper.staffview.promotion.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.StaffHomePage;
import com.example.shopper.staffview.promotion.adapter.MyPromotionAdapter;
import com.example.shopper.staffview.promotion.model.MyPromotionData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyPromotion extends AppCompatActivity {
    private ImageButton btn_back;
    private Button btn_add_new, btn_delete;
    private List<MyPromotionData> promotionList;
    private RecyclerView recyclerView;
    private MyPromotionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_promotion);
        btn_back = findViewById(R.id.imgbtn_back);
        btn_add_new=findViewById(R.id.btn_add_new_promotions);
        recyclerView = findViewById(R.id.RCV_promotions);
        promotionList = new ArrayList<>();
        adapter = new MyPromotionAdapter(promotionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        btn_delete = findViewById(R.id.btn_delete_promotion);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPromotion.this, StaffHomePage.class);
                startActivity(intent);
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPromotion.this, DeleteMyPromotion.class);
                startActivity(intent);
            }
        });
        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPromotion.this, AddMyPromotion.class);
                startActivity(intent);
            }
        });
        // Lấy danh mục từ Firestore và cập nhật danh sách
        FirebaseFirestore.getInstance().collection("PROMOTION")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String hinhAnhKM = document.getString("promotionImg");
                            String ChitietKM = document.getString("promotionDetail");
                            String tenKM = document.getString("promotionName");
                            String loaiKhuyenMai = document.getString("promotionType");
                            String maKM = document.getString("promotionId");
                            long donToiThieu = document.getLong("miniumValue");
                            double tiLe = document.getDouble("ratedValue");
                            Timestamp ngayBatDau = document.getTimestamp("startDate");
                            Timestamp ngayKetThuc = document.getTimestamp("endDate");
                            MyPromotionData promotion = new MyPromotionData(tenKM, ChitietKM, loaiKhuyenMai, hinhAnhKM, maKM, donToiThieu, tiLe, ngayBatDau, ngayKetThuc);
                            promotionList.add(promotion);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });



    }
    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

    }

}