package com.example.shopper.customerview.home.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.shoppingcart.adapter.ShoppingCartAdapter;
import com.example.shopper.customerview.home.shoppingcart.billing.BuyNow;
import com.example.shopper.customerview.home.shoppingcart.model.Cart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerViewdata;
    List<Cart> dataCheck;
    ShoppingCartAdapter shoppingAdapter;
    List<Cart> data;
    FirebaseFirestore db;
    ImageView backIcon;
    CheckBox checktotal;
    TextView Price, ButtonCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        dataCheck = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        recyclerViewdata = findViewById(R.id.RecyclerData);
        backIcon = findViewById(R.id.backIcon);
        checktotal = findViewById(R.id.radio_btn_choose);
        Price = findViewById(R.id.txt_tonggia);
        ButtonCheckOut = findViewById(R.id.btn_checkout);
        shoppingAdapter = new ShoppingCartAdapter(this.getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewdata.setLayoutManager(linearLayoutManager);
        getDataCart();
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checktotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checktotal.isChecked()) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setCheck(true);
                    }
                    shoppingAdapter.setData(data);
                    SetToTal(data);
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setCheck(false);
                    }
                    shoppingAdapter.setData(data);
                    SetToTal(data);
                }
            }
        });

        ButtonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String[] listmaGH = new String[data.size()];
                    Log.d("Errrrrrrrr", String.valueOf(listmaGH.length));
                    int j = 0;
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).isCheck()) {
                            listmaGH[j] = data.get(i).getMaGH();
                            j++;
                        }
                    }
                    if (listmaGH[0] != null) {
                        Intent t = new Intent(ShoppingCart.this, BuyNow.class);
                        t.putExtra("cartIdList", listmaGH);
                        startActivity(t);
                    }
                } catch (Exception e) {
                    Log.d("Errrrrrrrr", e.getMessage());
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

    private void getDataCart() {
        //get Data
        db.collection("CART")
                .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Error", "listen:error", e);
                            return;
                        }
                        data = new ArrayList<>();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            String TenSP = doc.getString("productName");
                            String Size = doc.getString("productSize");
                            int SoLuong = doc.getLong("quanity").intValue();
                            List<String> Anh = (List<String>) doc.get("imageUrl");
                            int GiaSP = doc.getLong("productPrice").intValue();
                            int GiaTien = doc.getLong("totalPrice").intValue();
                            String MauSac = doc.getString("productColor");
                            String MaGH = doc.getString("cartId");
                            String MaSP = doc.getString("productId");
                            boolean check = false;
                            data.add(new Cart(MaSP, MaGH, Anh.get(0), TenSP, GiaSP, SoLuong, GiaTien, Size, MauSac, check));
                        }
                        shoppingAdapter.setData(data);
                        SetToTal(data);
                        shoppingAdapter.setOnButtonAddClickListener(new ShoppingCartAdapter.OnButtonAddClickListener() {
                            @Override
                            public void onButtonAddClick(int position) {

                                Cart shoppingCart = data.get(position);

                                AddSoLuong(shoppingCart);
                            }
                        });
                        shoppingAdapter.setOnButtonMinusClickListener(new ShoppingCartAdapter.OnButtonMinusClickListener() {
                            @Override
                            public void onButtonMinusClick(int position) {
                                Cart shoppingCart = data.get(position);
                                if (shoppingCart.getSoLuong() - 1 >= 1) {
                                    MinusSoLuong(shoppingCart);
                                }
                                if (shoppingCart.getSoLuong() - 1 == 0) {
                                    DeleteCart(shoppingCart);
                                }
                            }
                        });
                        shoppingAdapter.setOnButtonDeleteClick(new ShoppingCartAdapter.OnButtonDeleteClick() {
                            @Override
                            public void onButtonDeleteClick(int position) {
                                Cart shoppingCart = data.get(position);
                                DeleteCart(shoppingCart);
                            }
                        });
                        shoppingAdapter.setOnCheckedChangeListener(new ShoppingCartAdapter.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChange(int position) {
                                Cart shoppingCart = data.get(position);
                                data.get(position).setCheck(!shoppingCart.isCheck());
                                shoppingAdapter.setData(data);
                                boolean check = true;
                                for (int i = 0; i < data.size(); i++) {
                                    if (!data.get(i).isCheck())
                                        check = false;
                                }
                                checktotal.setChecked(check);
                                SetToTal(data);
                            }
                        });
                        recyclerViewdata.setAdapter(shoppingAdapter);
                    }
                });
    }

    private void AddSoLuong(Cart shoppingCart) {
        try {
            db.collection("CART")
                    .whereEqualTo("cartId", shoppingCart.getMaGH())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                String docId = documentSnapshot.getId();
                                Map<String, Object> newData = new HashMap<>();
                                newData.put("quanity", shoppingCart.getSoLuong() + 1);
                                newData.put("totalPrice", (shoppingCart.getSoLuong() + 1) * shoppingCart.getGiaSP());
                                DocumentReference docRef = db.collection("CART").document(docId);
                                docRef.update(newData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Access", "SoLuong updated successfully");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Error", "Error updating SoLuong", e);
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error", "Error fetching document", e);
                        }
                    });
        } catch (Exception ex) {

        }

    }

    private void MinusSoLuong(Cart shoppingCart) {
        try {
            db.collection("CART")
                    .whereEqualTo("cartId", shoppingCart.getMaGH())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                String docId = documentSnapshot.getId();
                                Map<String, Object> newData = new HashMap<>();
                                newData.put("quanity", shoppingCart.getSoLuong() - 1);
                                newData.put("totalPrice", (shoppingCart.getSoLuong() - 1) * shoppingCart.getGiaSP());
                                DocumentReference docRef = db.collection("CART").document(docId);
                                docRef.update(newData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Access", "Quanity updated successfully");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Error", "Error updating Quanity", e);
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error", "Error fetching document", e);
                        }
                    });
        } catch (Exception ex) {

        }

    }

    private void DeleteCart(Cart shoppingCart) {
        try {
            DocumentReference docRef = db.collection("CART").document(shoppingCart.getMaGH());
            docRef.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Sucess", "Data deleted successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error", "Error deleting data", e);
                        }
                    });
        } catch (Exception ex) {

        }

    }

    private void SetToTal(List<Cart> data) {
        int Sum = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isCheck()) {
                Sum += data.get(i).getGiaTien();
            }
        }
        Price.setText(String.valueOf(Sum));
    }
}