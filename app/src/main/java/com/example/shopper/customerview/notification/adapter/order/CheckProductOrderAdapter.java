package com.example.shopper.customerview.notification.adapter.order;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.notification.activity.DetailCustomerOrder;
import com.example.shopper.staffview.order.adapter.MyProductAdapter;
import com.example.shopper.staffview.order.model.ItemOrder;
import com.example.shopper.staffview.order.model.Order;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckProductOrderAdapter extends RecyclerView.Adapter<CheckProductOrderAdapter.ViewHolder> {
    @NonNull

    private List<Order> orderList;

    public CheckProductOrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    public CheckProductOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_customer_product, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckProductOrderAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        int receive = position;
        AtomicInteger totalMoney = new AtomicInteger(0);

        holder.orderIdTextView.setText(order.getOrderId());
        holder.customerNameTextView.setText(order.getUserName());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
        holder.recyclerViewProducts.setLayoutManager(layoutManager);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maDH = orderList.get(receive).getOrderId();
                // Chuyển sang activity mới
                Intent intent = new Intent(v.getContext(), DetailCustomerOrder.class);
                intent.putExtra("orderId", maDH);
                v.getContext().startActivity(intent);
            }
        });


        // Truy vấn Firebase để lấy AnhDaiDien dựa trên maND
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference donHangRef = db.collection("ORDER");
        donHangRef.document(orderList.get(receive).getOrderId()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        Long tongTien = documentSnapshot.getLong("totalBill");
                        holder.total.setText(formatCurrency(tongTien.intValue()));
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi truy vấn thất bại
                });

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

        FirebaseFirestore db_con = FirebaseFirestore.getInstance();
        CollectionReference dathangRef = db_con.collection("ORDERACT");
        dathangRef.whereEqualTo("orderId", order.getOrderId())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<ItemOrder> itemOrderList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String maSP = document.getString("productId");

                        String mauSac = document.getString("productColor");
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
                                        } else {

                                            // Xử lý khi giá trị `HinhAnhSP` là null hoặc mảng rỗng
                                        }
                                        Long giaSPLong = sanphamDocument.getLong("productPrice");
                                        int GiaSP = giaSPLong != null ? Math.toIntExact(giaSPLong) : 0;

                                        ItemOrder itemOrder = new ItemOrder(hinhAnhSP, tenSP, maSP, GiaSP, number, mauSac, size);
                                        itemOrderList.add(itemOrder);
                                        // Cập nhật tổng tiền
                                        int totalPrice = GiaSP * number;
                                        totalMoney.addAndGet(totalPrice);
                                        // holder.total.setText(formatCurrency(totalMoney.get()));
                                        // Đặt giá trị tổng tiền vào TextView

                                        // Tạo mới MyProductAdapter và gán nó cho recyclerViewProducts
                                        holder.myProductAdapter = new MyProductAdapter(itemOrderList);
                                        holder.recyclerViewProducts.setAdapter(holder.myProductAdapter);

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTextView;
        public TextView customerNameTextView;
        private ImageView img_avatar;
        private RecyclerView recyclerViewProducts;
        private MyProductAdapter myProductAdapter;

        private Button btnDetail;
        private TextView total;


        public ViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.maND_customer);
            customerNameTextView = itemView.findViewById(R.id.ten_customer);
            img_avatar = itemView.findViewById(R.id.img_ava_customer);
            recyclerViewProducts = itemView.findViewById(R.id.RCV_details_customer);
            total = itemView.findViewById(R.id.money_total_customer);
            btnDetail = itemView.findViewById(R.id.btn_detail_customer);


        }
    }


}