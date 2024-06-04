package com.example.shopper.customerview.home.shoppingcart.billing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopper.R;
import com.example.shopper.customerview.home.address.activity.SaveAddress;
import com.example.shopper.customerview.home.address.model.Address;
import com.example.shopper.customerview.home.promotion.activity.ApplyPromotion;
import com.example.shopper.customerview.home.promotion.model.Promotion;
import com.example.shopper.customerview.home.shoppingcart.model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyNow extends AppCompatActivity {
    Integer TienTamTinh, GiamGia, VanChuyen, TongTien;
    Timestamp NgayBD1;
    Timestamp NgayKT1;
    List<Promotion> promotionList;

    List<Address> list;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    ImageView backbtn;
    String MaDC;
    String LoaiKhuyenMai;
    String MaGG;
    String[] myList;
    List<Cart> myData;
    TextView Subtotal, Delivery, CheckOut, Total, Discount;
    //
    TextView TenSP, KichThuoc, MauSac, SoLuong, Price;
    ImageView imageProduct;
    ImageView NextProduct;
    LinearLayout HaveDC, DontHaveDC, KhuyenMai, NotKhuyenMai;
    RelativeLayout ClickPayment, ClickAddress, ClickGiamGia;
    //
    TextView Ten, SDT, Duong, DC;
    //
    ImageView imageView;
    TextView DonToiThieu;
    TextView TenKM;
    TextView NgayBD;
    TextView NgayKT;
    Double TiLeKM;
    Integer DonToiThieuGia;
    //
    int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        j = 0;
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringArrayExtra("cartIdList") != null) {
                myList = new String[intent.getStringArrayExtra("cartIdList").length];
                myList = intent.getStringArrayExtra("cartIdList");
                myData = new ArrayList<>();
            }
            if (intent.getStringExtra("addressId") != null) {
                MaDC = intent.getStringExtra("addressId");
            }
            if (intent.getStringExtra("promotionId") != null) {
                MaGG = intent.getStringExtra("promotionId");
            }
        }
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_buy_now);
        HaveDC = findViewById(R.id.layout_address);
        DontHaveDC = findViewById(R.id.layout_not_address);
        //
        imageProduct = findViewById(R.id.image_product);
        TenSP = findViewById(R.id.txt_tensp);
        MauSac = findViewById(R.id.txt_MauSac);
        KichThuoc = findViewById(R.id.txt_KichThuoc);
        Price = findViewById(R.id.txt_giaban);
        SoLuong = findViewById(R.id.txt_soluong);
        NextProduct = findViewById(R.id.image_next);
        //
        backbtn = findViewById(R.id.backIcon);
        //
        Subtotal = findViewById(R.id.subtotal_text);
        Delivery = findViewById(R.id.delivery_text);
        Total = findViewById(R.id.total_text);
        Discount = findViewById(R.id.discount_text);
        //
        CheckOut = findViewById(R.id.btn_checkout);
        ClickGiamGia = findViewById(R.id.click_giamgia);
        ClickAddress = findViewById(R.id.click_address);
        ClickPayment = findViewById(R.id.click_payment);

        //
        Ten = findViewById(R.id.txt_Ten);
        SDT = findViewById(R.id.txt_SDT);
        Duong = findViewById(R.id.txt_PX);
        DC = findViewById(R.id.txt_DC);
        //
        KhuyenMai = findViewById(R.id.layout_promotion);
        NotKhuyenMai = findViewById(R.id.layout_not_promotion);
        imageView = findViewById(R.id.imagePromotion);
        TenKM = findViewById(R.id.txtNamePromotion);
        DonToiThieu = findViewById(R.id.txtDetailsPromotion);
        NgayBD = findViewById(R.id.txtStartPromotion);
        NgayKT = findViewById(R.id.txtEndPromotion);

        getDataProduct();
        //

        //
        if (myList != null) {
            if (myList.length > 1) {
                if (myList[1] == null) {
                    NextProduct.setVisibility(View.INVISIBLE);
                }
            } else {
                NextProduct.setVisibility(View.INVISIBLE);
            }
        }
        //


        NextProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j++;
                if (j >= myData.size()) {
                    j = 0;
                }
                Picasso.get().load(myData.get(j).getDataImage()).into(imageProduct);
                TenSP.setText(myData.get(j).getTenSanPham());
                MauSac.setText(myData.get(j).getMauSac());
                KichThuoc.setText(myData.get(j).getSize());
                SoLuong.setText(String.valueOf("x" + myData.get(j).getSoLuong()));
                Price.setText(String.valueOf(myData.get(j).getGiaTien()));
            }
        });

        ClickPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent t = new Intent(BuyNow.this, CheckOut.class);
//                t.putExtra("ListMaGH", myList);
//                if(MaDC != null){
//                    t.putExtra("MaDC", MaDC );
//                }
//                if(MaGG != null){
//                    t.putExtra("MaGG", MaGG );
//                }
//                startActivity(t);
            }
        });
        ClickAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(BuyNow.this, SaveAddress.class);
                t.putExtra("cartIdList", myList);
                if (MaGG != null) {
                    t.putExtra("promotionId", MaGG);
                }
                if (MaDC != null) {
                    t.putExtra("addressId", MaDC);
                }
                startActivity(t);
            }
        });
        ClickGiamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(BuyNow.this, ApplyPromotion.class);
                t.putExtra("cartIdList", myList);
                if (MaDC != null) {
                    t.putExtra("addressId", MaDC);
                }
                if (MaGG != null) {
                    t.putExtra("promotionId", MaGG);
                }
                startActivity(t);
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        CheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MaDC != null && MaGG != null) {
                    AddDatHang();
                    Intent t = new Intent(BuyNow.this, Done.class);
                    startActivity(t);
                } else {
                    Toast.makeText(BuyNow.this, "Please fill all information", Toast.LENGTH_LONG).show();
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

    private void getDataProduct() {
        try {
            if (myList != null) {
                for (int i = 0; i < myList.length; i++) {
                    Log.d("Ma Gio Hang", myList[i]);
                    db.collection("CART")
                            .whereEqualTo("cartId", myList[i])
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
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
                                            Log.d("Errrrrrrrrrr", Anh.get(0));
                                            myData.add(new Cart(MaSP, MaGH, Anh.get(0), TenSP, GiaSP, SoLuong, GiaTien, Size, MauSac, check));
                                        }
                                        Picasso.get().load(myData.get(0).getDataImage()).into(imageProduct);
                                        TenSP.setText(myData.get(0).getTenSanPham());
                                        MauSac.setText(myData.get(0).getMauSac());
                                        KichThuoc.setText(myData.get(0).getSize());
                                        SoLuong.setText(String.valueOf("x" + myData.get(0).getSoLuong()));
                                        Price.setText(String.valueOf(myData.get(0).getGiaTien()));

                                        updatePriceNotKM();
                                    } else {
                                        Log.d("Error", "Error getting documents: ", task.getException());
                                    }
                                    if (MaDC == null) {
                                        HaveDC.setVisibility(View.GONE);
                                    } else {
                                        DontHaveDC.setVisibility(View.GONE);
                                        GetSetDataDC();
                                    }
                                    if (MaGG == null) {
                                        KhuyenMai.setVisibility(View.GONE);
                                    } else {
                                        NotKhuyenMai.setVisibility(View.GONE);
                                        getDataGG();

                                    }
                                }
                            });
                }
            }
        } catch (Exception e) {

        }
    }

    private void GetSetDataDC() {
        try {
            if (MaDC != null) {
                db.collection("ADDRESS")
                        .whereEqualTo("addressId", MaDC)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    list = new ArrayList<>();
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        String MaDC = doc.getString("addressId");
                                        String Name = doc.getString("userName");
                                        String SDT = doc.getString("phoneNumber");
                                        String DiaChi = doc.getString("address");
                                        String DCDetails = doc.getString("ward") + ", "
                                                + doc.getString("district") + ", "
                                                + doc.getString("city");
                                        list.add(new Address(MaDC, Name, SDT, DiaChi, DCDetails, false));
                                    }
                                    Ten.setText(list.get(0).getName());
                                    SDT.setText(list.get(0).getSdt());
                                    Duong.setText(list.get(0).getPhuongXa());
                                    DC.setText(list.get(0).getDiaChi());
                                }
                            }
                        });
            }
        } catch (Exception e) {

        }

    }

    private void UpdatePrice() {
        int Sum = 0;
        for (int i = 0; i < myData.size(); i++) {
            Sum += myData.get(i).getGiaTien();
        }
        Date date = new Date();
        Date start = NgayBD1.toDate();
        Date end = NgayKT1.toDate();
        int comparison1 = date.compareTo(start);
        int comparison2 = date.compareTo(end);
        Log.d("Ngay Bat Dau", String.valueOf(comparison1));
        Log.d("Ngay Ket Thuc", String.valueOf(comparison2));
        Log.d("Tong Tien", String.valueOf(Sum));
        Log.d("Don Toi Thieu", String.valueOf(DonToiThieuGia));
        if (comparison1 > 0 && comparison2 < 0 && Sum >= DonToiThieuGia) {
            switch (LoaiKhuyenMai) {
                case "FreeShip":
                    TienTamTinh = Sum;
                    GiamGia = 0;
                    VanChuyen = 0;
                    TongTien = Sum;
                    Subtotal.setText(String.valueOf(Sum));
                    Discount.setText("0");
                    Delivery.setText("0");
                    Total.setText(String.valueOf(Sum));
                    break;
                case "Discount":
                    TienTamTinh = Sum;
                    GiamGia = Math.round(Sum * TiLeKM.intValue());
                    VanChuyen = 20000;
                    TongTien = Math.round(Sum - Sum * TiLeKM.intValue() + 20000);
                    Subtotal.setText(String.valueOf(Sum));
                    Discount.setText("-" + String.valueOf(Math.round(Sum * TiLeKM)));
                    Delivery.setText("20000");
                    Total.setText(String.valueOf(Math.round(Sum - Sum * TiLeKM + 20000)));
                    break;
            }
        } else {
            Toast.makeText(BuyNow.this, "Can't apply discount", Toast.LENGTH_LONG).show();
            TienTamTinh = Sum;
            GiamGia = 0;
            VanChuyen = 20000;
            TongTien = Sum + 20000;
            Subtotal.setText(String.valueOf(Sum));
            Discount.setText("0");
            Delivery.setText("20000");
            Total.setText(String.valueOf(Sum + 20000));
        }
    }

    private void updatePriceNotKM() {
        int Sum = 0;
        for (int i = 0; i < myData.size(); i++) {
            Sum += myData.get(i).getGiaTien();
        }
        if (MaGG == null) {
            TienTamTinh = Sum;
            GiamGia = 0;
            VanChuyen = 20000;
            TongTien = Sum + 20000;
            Subtotal.setText(String.valueOf(Sum));
            Discount.setText("0");
            Delivery.setText("20000");
            Total.setText(String.valueOf(Sum + 20000));
        }
    }

    private void getDataGG() {
        try {
            if (MaGG != null) {
                db.collection("PROMOTION").whereEqualTo("promotionId", MaGG)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                promotionList = new ArrayList<>();
                                for (DocumentSnapshot doc : task.getResult()) {
                                    String MaKM = doc.getString("promotionId");
                                    String TenKM = doc.getString("promotionName");
                                    String ChiTietKM = doc.getString("promotionDetail");
                                    int DonTT = doc.getLong("miniumValue").intValue();
                                    String HinhAnhKM = doc.getString("promotionImg");
                                    Double TiLe = doc.getDouble("ratedValue");
                                    String LoaiKM = doc.getString("promotionType");
                                    NgayBD1 = doc.getTimestamp("startDate");
                                    NgayKT1 = doc.getTimestamp("endDate");
                                    promotionList.add(new Promotion(MaKM, TenKM, TiLe, HinhAnhKM, ChiTietKM, DonTT, LoaiKM, NgayBD1, NgayKT1, false));
                                }
                                TenKM.setText(promotionList.get(0).getTenKM());
                                Picasso.get().load(promotionList.get(0).getHinhAnhKM()).into(imageView);
                                DonToiThieu.setText("Bill from " + String.valueOf(promotionList.get(0).getDonToiThieu()));
                                NgayBD.setText(promotionList.get(0).getNgayBatDau());
                                NgayKT.setText(promotionList.get(0).getNgayKetThuc());
                                LoaiKhuyenMai = promotionList.get(0).getLoaiGiamGia();
                                TiLeKM = promotionList.get(0).getTiLe();
                                DonToiThieuGia = promotionList.get(0).getDonToiThieu();
                                UpdatePrice();
                            }
                        });
            }
        } catch (Exception e) {

        }

    }

    private void AddDatHang() {
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);

        calendar.add(Calendar.DAY_OF_YEAR, 5);

        Date nexttime = calendar.getTime();


        Map<String, Object> donHangData = new HashMap<>();
        donHangData.put("addressId", MaDC);
        donHangData.put("userId", firebaseAuth.getCurrentUser().getUid());
        donHangData.put("promotionId", MaGG);
        donHangData.put("orderDate", new Timestamp(currentTime));
        donHangData.put("transferFee", 20000);
        donHangData.put("userName", list.get(0).getName());
        donHangData.put("phoneNumber", list.get(0).getSdt());
        donHangData.put("planDelivered", new Timestamp(nexttime));
        donHangData.put("orderBill", TienTamTinh);
        donHangData.put("discount", GiamGia);
        donHangData.put("totalBill", TongTien);
        donHangData.put("payMethod", "CashPayment");
        donHangData.put("state", "Confirm");

        db.collection("ORDER")
                .add(donHangData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        documentReference.update("orderId", documentReference.getId());
                        AddDatHang(documentReference.getId());
                    }
                });
    }

    private void AddDatHang(String MaDH) {
        for (int i = 0; i < myData.size(); i++) {
            Map<String, Object> datHangData = new HashMap<>();
            datHangData.put("orderId", MaDH);
            datHangData.put("productId", myData.get(i).getMaSP());
            datHangData.put("productColor", myData.get(i).getMauSac());
            datHangData.put("productSize", myData.get(i).getSize());
            datHangData.put("quanity", myData.get(i).getSoLuong());
            datHangData.put("total", myData.get(i).getGiaTien());
            db.collection("ORDERACT")
                    .add(datHangData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Sucess", "Data deleted successfully");
                        }
                    });
            Log.d("Error", myData.get(i).getMaGH());
            DocumentReference docReff = db.collection("CART").document(myData.get(i).getMaGH());
            docReff.delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Sucess", "Xoa thanh cong GIOHANG");
                        }
                    });

            int SoLuong = myData.get(i).getSoLuong();
            db.collection("PRODUCT")
                    .whereEqualTo("productId", myData.get(i).getMaSP())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                                String docId = documentSnapshot.getId();
                                int SoLuongDaBan = documentSnapshot.getLong("sold").intValue();
                                int SoLuongConLai = documentSnapshot.getLong("warehouse").intValue();
                                Map<String, Object> newData = new HashMap<>();
                                newData.put("sold", SoLuongDaBan + SoLuong);
                                newData.put("warehouse", SoLuongConLai - SoLuong);
                                DocumentReference docRef = db.collection("PRODUCT").document(docId);
                                docRef.update(newData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Access", "Quanity updated successfully");
                                            }
                                        });

                            }
                        }
                    });
        }

    }
}