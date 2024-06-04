package com.example.shopper.customerview.home.promotion.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.promotion.adapter.PromotionAdapter;
import com.example.shopper.customerview.home.promotion.model.Promotion;
import com.example.shopper.customerview.home.shoppingcart.billing.BuyNow;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ApplyPromotion extends AppCompatActivity {

    FirebaseFirestore db;
    PromotionAdapter promotionAdapter;
    List<Promotion> dataPromotion;
    RecyclerView recyclerView;
    TextView btn_done;
    ImageView btn_back;
    String MaGG;
    String MaDC;
    String[] myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_promotion);


        btn_back = findViewById(R.id.backIcon);
        btn_done = findViewById(R.id.btn_save);
        recyclerView = findViewById(R.id.rcyPromotion);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        if(intent != null){
            if(intent.getStringArrayExtra("cartIdList")!=null){
                myList = new String[intent.getStringArrayExtra("cartIdList").length];
                myList = intent.getStringArrayExtra("cartIdList");
            }
            if(intent.getStringExtra("addressId") != null){
                MaDC = intent.getStringExtra("addressId");
            }
            if(intent.getStringExtra("promotionId") != null){
                MaGG = intent.getStringExtra("promotionId");
            }
        }

        db = FirebaseFirestore.getInstance();
        promotionAdapter = new PromotionAdapter(this.getApplicationContext());

        getDataPromotion();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < dataPromotion.size(); i++){
                    if(dataPromotion.get(i).isCheck()){
                        MaGG = dataPromotion.get(i).getMaKM();
                    }
                }
                Intent t = new Intent(ApplyPromotion.this, BuyNow.class);
                t.putExtra("cartIdList", myList);
                if(MaDC!=null){
                    t.putExtra("addressId", MaDC);
                }
                if(MaGG!=null){
                    t.putExtra("promotionId", MaGG );
                }
                Log.d("Errr", MaGG);
                startActivity(t);
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
    private void getDataPromotion(){
        db.collection("PROMOTION")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        dataPromotion = new ArrayList<>();
                        for(DocumentSnapshot doc : value.getDocuments()){
                            String MaKM = doc.getString("promotionId");
                            String TenKM = doc.getString("promotionName");
                            String ChiTietKM = doc.getString("promotionDetail");
                            int DonTT = doc.getLong("miniumValue").intValue();
                            String HinhAnhKM = doc.getString("promotionImg");
                            Double TiLe = doc.getDouble("ratedValue");
                            String LoaiKM = doc.getString("promotionType");
                            Timestamp NgayBD = doc.getTimestamp("startDate");
                            Timestamp NgayKT = doc.getTimestamp("endDate");
                            dataPromotion.add(new Promotion(MaKM, TenKM, TiLe, HinhAnhKM, ChiTietKM, DonTT, LoaiKM, NgayBD, NgayKT, false));
                        }
                        promotionAdapter.setData(dataPromotion);
                        promotionAdapter.setCheckClick(new PromotionAdapter.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChange(int position) {
                                for(int i = 0; i < dataPromotion.size(); i++){
                                    dataPromotion.get(i).setCheck(false);
                                }
                                Promotion promotion = dataPromotion.get(position);
                                dataPromotion.get(position).setCheck(!promotion.isCheck());
                                promotionAdapter.setData(dataPromotion);
                            }
                        });
                        recyclerView.setAdapter(promotionAdapter);
                    }
                });
    }
}