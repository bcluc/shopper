package com.example.shopper.staffview.order.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.order.model.ItemOrder;
import com.example.shopper.staffview.order.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckProductAdapter extends RecyclerView.Adapter<CheckProductAdapter.ViewHolder> {
    private List<Order> orderList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTextView;
        public TextView customerNameTextView;
        private ImageView img_avatar;
        private RecyclerView recyclerViewProducts;
        private ProductAdapter productAdapter;
        private TextView getMaStaff;
        private Button Confirm;
        private TextView total;
        public ViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.maND);
            customerNameTextView = itemView.findViewById(R.id.ten);
            img_avatar = itemView.findViewById(R.id.img_ava);
            recyclerViewProducts = itemView.findViewById(R.id.RCV_details);
            total = itemView.findViewById(R.id.money_total);
            getMaStaff = itemView.findViewById(R.id.getMaND);
            Confirm = itemView.findViewById(R.id.btn_confirm);

        }
    }


    public CheckProductAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_product_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orderList.get(position);
        int receive = position;
        AtomicInteger totalMoney = new AtomicInteger(0);

        holder.orderIdTextView.setText(order.getOrderId());
        holder.customerNameTextView.setText(order.getUserName());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
        holder.recyclerViewProducts.setLayoutManager(layoutManager);
        holder.Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maDH = orderList.get(receive).getOrderId();
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
                                return;
                            }
                            docRef.update("state", newTrangThai)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Cập nhật thành công
                                                Toast.makeText(v.getContext(), "State has been updated", Toast.LENGTH_SHORT).show();
                                                refresh();
                                            } else {
                                                // Cập nhật thất bại
                                                Toast.makeText(v.getContext(), "State update failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                        CollectionReference maThongBaoRef = FirebaseFirestore.getInstance().collection("CUSTOMNOTIFY");
                        // Truy vấn để lấy MaTB dựa trên trạng thái "trangThai"
                        maThongBaoRef.whereEqualTo("notifyType", trangThai).limit(1).get()
                                .addOnSuccessListener(querySnapshot -> {
                                    // Kiểm tra xem có kết quả trả về không
                                    if (!querySnapshot.isEmpty()) {
                                        // Lấy MaTB từ tài liệu đầu tiên trong kết quả truy vấn
                                        String maTB = querySnapshot.getDocuments().get(0).getId();
                                        Boolean read = false;
                                        // Truy vấn để lấy MaND dựa trên maDH
                                        donHangRef.document(maDH).get()
                                                .addOnSuccessListener(documentSnapshotTB -> {
                                                    if (documentSnapshotTB.exists()) {
                                                        String maND = documentSnapshotTB.getString("userId");
                                                        Date currentTime = new Date();

                                                        Timestamp timestamp = new Timestamp(currentTime);
                                                        if (maND != null) {
                                                            // Tạo thông báo
                                                            Map<String, Object> thongBao = new HashMap<>();
                                                            thongBao.put("notifyId", maTB);
                                                            thongBao.put("orderId", maDH);
                                                            thongBao.put("userId", maND);
                                                            thongBao.put("read", read);
                                                            thongBao.put("timeStamp", timestamp);
                                                            // Thêm thông báo vào bộ sưu tập "THONGBAODONHANG"
                                                            CollectionReference thongBaoDonHangRef = FirebaseFirestore.getInstance().collection("ORDERNOTIFY");
                                                            thongBaoDonHangRef.add(thongBao)
                                                                    .addOnSuccessListener(documentReference1 -> {
                                                                        String thongBaoId = documentReference1.getId();
                                                                        thongBao.put("orderNotifyId", thongBaoId);
                                                                        documentReference1.set(thongBao)
                                                                                .addOnSuccessListener(aVoid -> {
                                                                                    notifyDataSetChanged();
                                                                                })
                                                                                .addOnFailureListener(e -> {
                                                                                    // Xử lý lỗi khi cập nhật tài liệu "THONGBAODONHANG"
                                                                                });

                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        // Tạo tài liệu thất bại
                                                                        // Xử lý lỗi nếu cần
                                                                    });
                                                        } else {
                                                            // Không tìm thấy MaND phù hợp với maDH
                                                            // Xử lý trường hợp này nếu cần
                                                        }
                                                    } else {
                                                        // Không tìm thấy tài liệu "DONHANG" tương ứng với maDH
                                                        // Xử lý trường hợp này nếu cần
                                                    }
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Xử lý lỗi nếu có lỗi khi truy vấn "DONHANG"
                                                });
                                    } else {
                                        // Không tìm thấy MaTB phù hợp với trạng thái "trangThai"
                                        // Xử lý trường hợp này nếu cần
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý lỗi nếu có lỗi khi truy vấn "MATHONGBAO"
                                });
                    } else {
                        Toast.makeText(v.getContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        // Truy vấn Firebase để lấy AnhDaiDien dựa trên maND
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("USERS");
        usersRef.document(order.getUserId()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String anhDaiDien = documentSnapshot.getString("avatar");
                        Picasso.get().load(anhDaiDien).into(holder.img_avatar);
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi truy vấn thất bại
                });
        FirebaseFirestore db_xacnhan = FirebaseFirestore.getInstance();
        CollectionReference xacnhanRef = db_xacnhan.collection("ORDERCONFIRM");
        xacnhanRef.whereEqualTo("orderId", order.getOrderId())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String maND = document.getString("userId");
                        if (maND != null) {
                            holder.getMaStaff.setText(maND);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi truy vấn thất bại
                });
        CollectionReference tongtienRef = FirebaseFirestore.getInstance().collection("ORDER");
        DocumentReference tienRef = tongtienRef.document(order.getOrderId());
        tienRef
                .addSnapshotListener((documentSnapshot,e) -> {
                    if (documentSnapshot.exists()) {
                        long tongtien = documentSnapshot.getLong("totalBill");
                        holder.total.setText(formatCurrency(Integer.parseInt(String.valueOf(tongtien))));
                    }
                });
        FirebaseFirestore db_con = FirebaseFirestore.getInstance();
        CollectionReference dathangRef = db_con.collection("ORDERACT");
        dathangRef.whereEqualTo("orderId", order.getOrderId())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<ItemOrder> itemOrderList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String maSP = document.getString("productId");
                        String color = document.getString("productColor");
                        String size = document.getString("productSize");
                        int number = document.getLong("quanity") != null ? Math.toIntExact(document.getLong("quanity")) : 0;

                        // Truy vấn Firebase để lấy thông tin sản phẩm từ MaSP
                        CollectionReference sanphamRef = db.collection("PRODUCT");
                        sanphamRef.document(maSP).get()
                                .addOnSuccessListener(sanphamDocument -> {
                                    if (sanphamDocument.exists()) {
                                        String tenSP = sanphamDocument.getString("productName");
                                        String hinhAnhSP = null;
                                        List<String> hinhAnhSPList = (List<String>) sanphamDocument.get("imageUrl");
                                        if (hinhAnhSPList != null && !hinhAnhSPList.isEmpty()) {
                                            hinhAnhSP = hinhAnhSPList.get(0); // Lấy phần tử đầu tiên trong mảng
                                        }
                                        else {

                                            // Xử lý khi giá trị `HinhAnhSP` là null hoặc mảng rỗng
                                        }
                                        Long giaSPLong = sanphamDocument.getLong("productPrice");
                                        int GiaSP = giaSPLong != null ? Math.toIntExact(giaSPLong) : 0;

                                        ItemOrder itemOrder = new ItemOrder(hinhAnhSP, tenSP, maSP, GiaSP, number, color, size);
                                        itemOrderList.add(itemOrder);

                                        // Tạo mới ProductAdapter và gán nó cho recyclerViewProducts
                                        holder.productAdapter = new ProductAdapter(itemOrderList);
                                        holder.recyclerViewProducts.setAdapter(holder.productAdapter);

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
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    private String formatCurrency(int amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(amount);
    }
    public void refresh() {
        notifyDataSetChanged();
    }
}
