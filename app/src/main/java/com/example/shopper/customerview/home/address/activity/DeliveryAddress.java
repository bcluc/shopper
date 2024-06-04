package com.example.shopper.customerview.home.address.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopper.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DeliveryAddress extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    ImageView backIcon;
    TextView name, address, district, phuongxa, city, phone, btn_choose;

    String MaGG;
    String[] myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringArrayExtra("cartIdList") != null) {
                myList = new String[intent.getStringArrayExtra("cartIdList").length];
                myList = intent.getStringArrayExtra("cartIdList");
            }
            if (intent.getStringExtra("promotionId") != null) {
                MaGG = intent.getStringExtra(MaGG);
            }
        }

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        backIcon = findViewById(R.id.backIcon);
        phuongxa = findViewById(R.id.text_PX);
        name = findViewById(R.id.text_name);
        address = findViewById(R.id.text_address);
        district = findViewById(R.id.txt_district);
        city = findViewById(R.id.txt_city);
        phone = findViewById(R.id.text_phone);
        btn_choose = findViewById(R.id.btn_choose_adress);

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Ten = name.getText().toString();
                String SDT = phone.getText().toString();
                String Duong = address.getText().toString();
                String Px = phuongxa.getText().toString();
                String QH = district.getText().toString();
                String City = city.getText().toString();
                if (Ten.isEmpty() || SDT.isEmpty() || Duong.isEmpty() || Px.isEmpty() || QH.isEmpty() || City.isEmpty()) {
                    Toast.makeText(DeliveryAddress.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                } else {
                    Map<String, Object> data = new HashMap<>();
                    data.put("userName", Ten);
                    data.put("userId", firebaseAuth.getCurrentUser().getUid());
                    data.put("phoneNumber", SDT);
                    data.put("address", Duong);
                    data.put("ward", Px);
                    data.put("district", QH);
                    data.put("city", City);

                    db.collection("ADDRESS")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    documentReference.update("addressId", documentReference.getId());

                                    Intent t = new Intent(DeliveryAddress.this, SaveAddress.class);
                                    t.putExtra("addressId", documentReference.getId());
                                    t.putExtra("cartIdList", myList);
                                    if (MaGG != null) {
                                        t.putExtra("promotionId", MaGG);
                                    }
                                    startActivity(t);
                                }

                            });

                }

            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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