package com.example.shopper.staffview.promotion.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.promotion.adapter.DeleteMyPromotionAdapter;
import com.example.shopper.staffview.promotion.model.MyPromotionData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DeleteMyPromotion extends AppCompatActivity {

    private ImageButton btn_back;
    private Button btn_delete_promotion;
    private List<MyPromotionData> promotionList;
    private RecyclerView recyclerView;
    private DeleteMyPromotionAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_my_promotion);
        btn_back = findViewById(R.id.imgbtn_back_delete);
        recyclerView = findViewById(R.id.RCV_promotions_delete);
        promotionList = new ArrayList<>();
        adapter = new DeleteMyPromotionAdapter(promotionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        firestore = FirebaseFirestore.getInstance();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteMyPromotion.this, MyPromotion.class);
                startActivity(intent);
            }
        });
        btn_delete_promotion = findViewById(R.id.btn_delete_promotion);
        btn_delete_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MyPromotionData> selectedCategories = adapter.getSelectedCategories();
                if (selectedCategories.isEmpty()) {
                    Toast.makeText(DeleteMyPromotion.this, "No categories selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (MyPromotionData categoryItem : selectedCategories) {
                    deletePromotion(categoryItem);
                }
            }
        });
        // Lấy danh mục từ Firestore và cập nhật danh sách
        FirebaseFirestore.getInstance().collection("PROMOTION")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String hinhAnhKM = document.getString("promotionImg");
                            String hinhAnhTB = document.getString("notifyImg");
                            String ChitietKM = document.getString("promotionDetail");
                            String tenKM = document.getString("promotionName");
                            String loaiKhuyenMai = document.getString("promotionType");
                            String maKM = document.getString("promotionId");
                            long donToiThieu = document.getLong("miniumValue");
                            double tiLe = document.getDouble("ratedValue");
                            Timestamp ngayBatDau = document.getTimestamp("startDate");
                            Timestamp ngayKetThuc = document.getTimestamp("endDate");
                            Boolean check = false;
                            MyPromotionData promotion = new MyPromotionData(tenKM, ChitietKM, loaiKhuyenMai, hinhAnhKM, maKM, donToiThieu, tiLe, ngayBatDau, ngayKetThuc, check, hinhAnhTB);
                            promotionList.add(promotion);
                        }
                        adapter.notifyDataSetChanged();
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

    private void deletePromotion(MyPromotionData categoryItem) {
        boolean check = categoryItem.getSelected();

        if (check) {
            String tenDMToDelete = categoryItem.getTenKM();

            firestore.collection("PROMOTION")
                    .whereEqualTo("promotionName", tenDMToDelete)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get the MaKM of the promotion to be deleted
                                String maKMToDelete = document.getId();
                                // Delete the promotion document
                                document.getReference().delete();
                                // Delete related notifications in "THONGBAO" collection
                                firestore.collection("SENDNOTIFY")
                                        .whereEqualTo("notifyId", maKMToDelete)
                                        .get()
                                        .addOnCompleteListener(notificationTask -> {
                                            if (notificationTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot notificationDoc : notificationTask.getResult()) {
                                                    notificationDoc.getReference().delete();
                                                }
                                            } else {
                                                // Handle the failure if querying "THONGBAO" fails
                                                Toast.makeText(DeleteMyPromotion.this, "Failed to delete related notifications", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            Toast.makeText(DeleteMyPromotion.this, "MyPromotion and related notifications deleted successfully", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent(DeleteMyPromotion.this, MyPromotion.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DeleteMyPromotion.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(DeleteMyPromotion.this, "Cannot delete category with products, because this category has more than 2 products", Toast.LENGTH_SHORT).show();
        }
    }

}