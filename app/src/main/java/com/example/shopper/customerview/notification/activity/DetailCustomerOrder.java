package com.example.shopper.customerview.notification.activity;

import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.order.adapter.MyProductAdapter;
import com.example.shopper.staffview.order.model.ItemOrder;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailCustomerOrder extends AppCompatActivity {

    private ImageView back;
    private List<ItemOrder> itemOrderList;
    private TextView TenNguoiMua, TenND, MaND, SoDT, Diachi, tongTienThanhToan;
    private TextView txtGiaTriDonHang, txtGiamGia, txtPhiVanChuyen, txtTongTien;

    private RecyclerView recyclerViewProducts;
    private MyProductAdapter myProductAdapter;
    private ImageView img_avatar;
    private Button btn_exp_receipt;
    private String avatar;

    public DetailCustomerOrder() {
    }

    private void convertXMLToPage(TextView tenNguoiMua, TextView tenND, TextView maND, TextView soDT, TextView diaChi, TextView tongTienThanhToan, TextView giaTriDonHang, TextView giamGia, TextView phiVanChuyen, TextView tongTien, String imageAvatar) {
        //Inflate the Layout
        TextView mTenNguoiMua, mTenND, mMaND, mSoDT, mDiachi, mtongTienThanhToan;
        TextView mtxtGiaTriDonHang, mtxtGiamGia, mtxtPhiVanChuyen, mtxtTongTien;
        ImageView mimg_avatar;
        RecyclerView mrecyclerViewProducts;
        View view = LayoutInflater.from(this).inflate(R.layout.item_invoice, null);
        mTenNguoiMua = view.findViewById(R.id.tennguoimua_customer);
        mMaND = view.findViewById(R.id.maND_customer);
        mimg_avatar = view.findViewById(R.id.img_avatar);
        mDiachi = view.findViewById(R.id.diachi_chitiet_customer);
        mrecyclerViewProducts = view.findViewById(R.id.list_sanpham_customer);
        mSoDT = view.findViewById(R.id.soDT_customer);
        mTenND = view.findViewById(R.id.tenND_customer);

        mtxtGiaTriDonHang = view.findViewById(R.id.giatridonhang_customer);
        mtxtGiamGia = view.findViewById(R.id.discount_customer);
        mtxtPhiVanChuyen = view.findViewById(R.id.phiVanChuyen_customer);
        mtxtTongTien = view.findViewById(R.id.TongTien_customer);
        mtongTienThanhToan = view.findViewById(R.id.total_customer);
        mTenNguoiMua.setText(tenNguoiMua.getText());
        mTenND.setText(tenND.getText());
        mMaND.setText(maND.getText());
        mSoDT.setText(soDT.getText());
        mDiachi.setText(diaChi.getText());
        mtxtGiaTriDonHang.setText(giaTriDonHang.getText());
        mtxtGiamGia.setText(giamGia.getText());
        mtxtTongTien.setText(tongTien.getText());
        mtxtPhiVanChuyen.setText(phiVanChuyen.getText());
        mtongTienThanhToan.setText(tongTienThanhToan.getText());
        Picasso.get().load(imageAvatar).into(mimg_avatar);
        mrecyclerViewProducts.setAdapter(myProductAdapter);
        mrecyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 1));

        itemOrderList = new ArrayList<>();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getDisplay().getRealMetrics(displayMetrics);
        } else this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));
        PdfDocument document = new PdfDocument();
        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMinimumHeight();
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        //Canvas
        Canvas canvas = page.getCanvas();
        view.draw(canvas);
        //Finish the page
        document.finishPage(page);
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "Invoice" + Timestamp.now() + ".pdf";
        File file = new File(downloadsDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Convert PDF Successfully!!!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customer_order);

        back = findViewById(R.id.back_customer);
        TenNguoiMua = findViewById(R.id.tennguoimua_customer);
        MaND = findViewById(R.id.maND_customer);
        img_avatar = findViewById(R.id.img_avatar);
        Diachi = findViewById(R.id.diachi_chitiet_customer);
        recyclerViewProducts = findViewById(R.id.list_sanpham_customer);
        SoDT = findViewById(R.id.soDT_customer);
        TenND = findViewById(R.id.tenND_customer);

        txtGiaTriDonHang = findViewById(R.id.giatridonhang_customer);
        txtGiamGia = findViewById(R.id.discount_customer);
        txtPhiVanChuyen = findViewById(R.id.phiVanChuyen_customer);
        txtTongTien = findViewById(R.id.TongTien_customer);
        tongTienThanhToan = findViewById(R.id.total_customer);

        itemOrderList = new ArrayList<>();
        btn_exp_receipt = findViewById(R.id.btn_exp_receipt);

        btn_exp_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertXMLToPage(TenNguoiMua, TenND, MaND, SoDT, Diachi, tongTienThanhToan, txtGiaTriDonHang, txtGiamGia, txtPhiVanChuyen, txtTongTien, avatar);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String maDH = getIntent().getStringExtra("orderId");
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference donHangRef = firebaseFirestore.collection("ORDER");
        DocumentReference docRef = donHangRef.document(maDH);

        // Truy vấn dữ liệu từ Firebase
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        String state = documentSnapshot.getString("state");
                        if (!state.equals("Delivered")) {
                            btn_exp_receipt.setVisibility(View.GONE);
                            Log.d("State", "Visible");
                        }
                        String tenNguoiMua = documentSnapshot.getString("userName");

                        TenND.setText(tenNguoiMua);
                        String maND = documentSnapshot.getString("userId");
                        MaND.setText(maND);
                        String MaDC = documentSnapshot.getString("addressId");

                        String MaDH = documentSnapshot.getString("orderId");

                        Long giaTriDonHang = documentSnapshot.getLong("orderBill");
                        Long giamGia = documentSnapshot.getLong("discount");
                        Long phiVanChuyen = documentSnapshot.getLong("transferFee");
                        Long tongTien = documentSnapshot.getLong("totalBill");

                        txtGiaTriDonHang.setText(giaTriDonHang + "");
                        txtGiamGia.setText(giamGia + "");
                        txtPhiVanChuyen.setText(phiVanChuyen + "");
                        txtTongTien.setText(tongTien + "");
                        tongTienThanhToan.setText(tongTien + "");


                        // Từ đây, xuất ra nhiều collection khác
                        CollectionReference nguoidung = firebaseFirestore.collection("USERS");
                        DocumentReference nguoidungref = nguoidung.document(maND);
                        nguoidungref.get().addOnCompleteListener(nguoidungdocument -> {
                            if (nguoidungdocument.getResult().exists()) {
                                avatar = nguoidungdocument.getResult().getString("avatar");
                                Picasso.get().load(avatar).into(img_avatar);
                            }
                        });

                        CollectionReference diachi = firebaseFirestore.collection("ADDRESS");
                        DocumentReference diachiRef = diachi.document(MaDC);
                        diachiRef.get().addOnCompleteListener(diachiDocument -> {
                            if (diachiDocument.getResult().exists()) {
                                String tenduong = diachiDocument.getResult().getString("address");
                                String phuongxa = diachiDocument.getResult().getString("ward");
                                String quanhuyen = diachiDocument.getResult().getString("district");
                                String tinhTP = diachiDocument.getResult().getString("city");
                                String DiaChi = tenduong + ", " + phuongxa + ", " + quanhuyen + ", " + tinhTP;
                                Diachi.setText(DiaChi);

                                String soDT = diachiDocument.getResult().getString("phoneNumber");
                                SoDT.setText(soDT);

                                String tenNguoiNhan = diachiDocument.getResult().getString("userName");
                                TenNguoiMua.setText(tenNguoiNhan);
                            }
                        });


                        CollectionReference dathangRef = firebaseFirestore.collection("ORDERACT");
                        dathangRef.whereEqualTo("orderId", MaDH)
                                .get()
                                .addOnSuccessListener(querySnapshot -> {
                                    List<ItemOrder> itemOrderList = new ArrayList<>();
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        String maSP = document.getString("productId");

                                        String mauSac = document.getString("productColor");
                                        String size = document.getString("productSize");

                                        int number = document.getLong("quanity") != null ? Math.toIntExact(document.getLong("quanity")) : 0;

                                        // Truy vấn Firebase để lấy thông tin sản phẩm từ MaSP
                                        CollectionReference sanphamRef = firebaseFirestore.collection("PRODUCT");
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

                                                        ItemOrder itemOrder = new ItemOrder(hinhAnhSP, tenSP, maSP, GiaSP, number, mauSac, size);
                                                        itemOrderList.add(itemOrder);
                                                        // Cập nhật tổng tiền
                                                        int totalPrice = GiaSP * number;

                                                        myProductAdapter = new MyProductAdapter(itemOrderList);
                                                        recyclerViewProducts.setAdapter(myProductAdapter);
                                                        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 1));

                                                    }
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Xử lý khi truy vấn thất bại
                                                });
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý khi truy vấn thất bại
                                });
                    } else {
                        // Dữ liệu không tồn tại
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi truy vấn thất bại
                });
    }
}