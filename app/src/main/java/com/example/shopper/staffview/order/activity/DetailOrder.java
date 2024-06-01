package com.example.shopper.staffview.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.order.adapter.ProductAdapter;
import com.example.shopper.staffview.order.model.ItemOrder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailOrder extends AppCompatActivity {


    private ImageView back;
    private List<ItemOrder> itemOrderList;
    private TextView TenNguoiMua, TenND, MaND, SDT, Diachi;

    private TextView Phivanchuyen, pttt, Giatridonhang, giamgia, color, size, total;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private ImageView img_avatar;

    private Button btn_confirm;

    public DetailOrder() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        back = findViewById(R.id.back);
        TenNguoiMua = findViewById(R.id.tennguoimua);
        MaND = findViewById(R.id.maND);
        img_avatar = findViewById(R.id.img_avatar);
        Diachi = findViewById(R.id.diachi_chitiet);
        recyclerViewProducts = findViewById(R.id.list_sanpham);
        itemOrderList = new ArrayList<>();
        SDT = findViewById(R.id.soDT);
        btn_confirm = findViewById(R.id.btn_confirm);
        TenND = findViewById(R.id.tenND);
        total = findViewById(R.id.total);
        Phivanchuyen = findViewById(R.id.phivanchuyen);
        Giatridonhang = findViewById(R.id.giatridonhang);
        giamgia = findViewById(R.id.discount);
        pttt = findViewById(R.id.phuongthuctt);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maDH = getIntent().getStringExtra("orderId");
                FirebaseFirestore db_confirm = FirebaseFirestore.getInstance();
                CollectionReference donHangRef = db_confirm.collection("ORDER");
                DocumentReference docRef = donHangRef.document(maDH);

                docRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String trangThai = documentSnapshot.getString("state");
                        if (trangThai != null) {
                            String newTrangThai;
                            if (trangThai.equals("Confirm")) {
                                newTrangThai = "Wait";
                            } else if (trangThai.equals("Wait")) {
                                newTrangThai = "Delivering";
                            } else if (trangThai.equals("Delivering")) {
                                newTrangThai = "Delivered";
                            } else {
                                // Trạng thái không hợp lệ, không cần cập nhật
                                return;
                            }

                            docRef.update("state", newTrangThai)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(DetailOrder.this, "Update successful: " + newTrangThai, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(DetailOrder.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(DetailOrder.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailOrder.this, MyOrderActivity.class);
                startActivity(intent);
            }
        });
        String maDH = getIntent().getStringExtra("orderId");
        FirebaseFirestore db_donhang = FirebaseFirestore.getInstance();
        FirebaseFirestore db_nguoidung = FirebaseFirestore.getInstance();
        FirebaseFirestore db_diachi = FirebaseFirestore.getInstance();
        FirebaseFirestore db_dathang = FirebaseFirestore.getInstance();
        CollectionReference donHangRef = db_donhang.collection("ORDER");
        DocumentReference docRef = donHangRef.document(maDH);

        // Truy vấn dữ liệu từ Firebase
        docRef
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (documentSnapshot.exists()) {
                        String tenNguoiMua = documentSnapshot.getString("userName");
                        TenNguoiMua.setText(tenNguoiMua);
                        String maND = documentSnapshot.getString("userId");
                        MaND.setText(maND);
                        String MaDC = documentSnapshot.getString("addressId");
                        String phuongttt = documentSnapshot.getString("payMethod");
                        pttt.setText(phuongttt);
                        long phivc = documentSnapshot.getLong("transferFee");
                        Phivanchuyen.setText(String.valueOf(phivc));
                        long value_of_order = documentSnapshot.getLong("orderBill");
                        Giatridonhang.setText(String.valueOf(value_of_order));
                        long discount = documentSnapshot.getLong("discount");
                        giamgia.setText(String.valueOf(discount));
                        String MaDH = documentSnapshot.getString("orderId");

                        String sodt = documentSnapshot.getString("phoneNumber");
                        SDT.setText(sodt);
                        long tongtien = documentSnapshot.getLong("totalBill");
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        String formattedTongTien = decimalFormat.format(tongtien);
                        total.setText(formattedTongTien);
                        // Từ đây, xuất ra nhiều collection khác
                        CollectionReference nguoidung = db_nguoidung.collection("USERS");
                        DocumentReference nguoidungref = nguoidung.document(maND);
                        nguoidungref.get().addOnCompleteListener(nguoidungdocument -> {
                            if (nguoidungdocument.getResult().exists()) {
                                String anhDaiDien = nguoidungdocument.getResult().getString("avatar");
                                String fullname = nguoidungdocument.getResult().getString("fullName");
                                TenND.setText(fullname);
                                Picasso.get().load(anhDaiDien).into(img_avatar);
                            }
                        });
                        CollectionReference diachi = db_diachi.collection("ADDRESS");
                        DocumentReference diachiRef = diachi.document(MaDC);
                        diachiRef.get().addOnCompleteListener(diachiDocument -> {
                            if (diachiDocument.getResult().exists()) {
                                String tenduong = diachiDocument.getResult().getString("address");
                                String phuongxa = diachiDocument.getResult().getString("ward");
                                String quanhuyen = diachiDocument.getResult().getString("district");
                                String tinhTP = diachiDocument.getResult().getString("city");
                                String DiaChi = tenduong + ", " + phuongxa + ", " + quanhuyen + ", " + tinhTP;
                                Diachi.setText(DiaChi);
                            }
                        });


                        CollectionReference dathangRef = db_dathang.collection("ORDERACT");
                        dathangRef.whereEqualTo("orderId", MaDH)
                                .get()
                                .addOnSuccessListener(querySnapshot -> {
                                    List<ItemOrder> itemOrderList = new ArrayList<>();
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        String maSP = document.getString("productId");
                                        String color = document.getString("productColor");
                                        String size = document.getString("productSize");

                                        int number = document.getLong("quanity") != null ? Math.toIntExact(document.getLong("quanity")) : 0;

                                        // Truy vấn Firebase để lấy thông tin sản phẩm từ MaSP
                                        CollectionReference sanphamRef = db_dathang.collection("PRODUCT");
                                        sanphamRef.document(maSP).get()
                                                .addOnSuccessListener(sanphamDocument -> {
                                                    if (sanphamDocument.exists()) {
                                                        String tenSP = sanphamDocument.getString("productName");
                                                        String hinhAnhSP = null;
                                                        List<String> hinhAnhSPList = (List<String>) sanphamDocument.get("imageUrl");
                                                        if (hinhAnhSPList != null && !hinhAnhSPList.isEmpty()) {
                                                            hinhAnhSP = hinhAnhSPList.get(0); // Lấy phần tử đầu tiên trong mảng
                                                        } else {

                                                            // Xử lý khi giá trị `HinhAnhSP` là null hoặc mảng rỗng
                                                        }
                                                        Long giaSPLong = sanphamDocument.getLong("productPrice");
                                                        int GiaSP = giaSPLong != null ? Math.toIntExact(giaSPLong) : 0;

                                                        ItemOrder itemOrder = new ItemOrder(hinhAnhSP, tenSP, maSP, GiaSP, number, color, size);
                                                        itemOrderList.add(itemOrder);
                                                        // Cập nhật tổng tiền
                                                        int totalPrice = GiaSP * number;

                                                        productAdapter = new ProductAdapter(itemOrderList);
                                                        recyclerViewProducts.setAdapter(productAdapter);
                                                        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 1));

                                                    }
                                                });
                                    }
                                });

                    } else {
                        // Dữ liệu không tồn tại
                    }
                });


    }

}
