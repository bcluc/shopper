package com.example.shopper.customerview.notification.adapter.order;

import android.content.Intent;
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
import com.example.shopper.authentication.model.User;
import com.example.shopper.customerview.notification.activity.DetailCustomerOrder;
import com.example.shopper.staffview.order.adapter.MyProductAdapter;
import com.example.shopper.staffview.order.model.ItemOrder;
import com.example.shopper.staffview.order.model.Order;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class CustomerOrderCancelAdapter extends RecyclerView.Adapter<CustomerOrderCancelAdapter.ViewHolder> {
    private List<Order> orderList;
    private String userID;
    private User user;

    public CustomerOrderCancelAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    public CustomerOrderCancelAdapter(List<Order> orderList, String userID) {
        this.orderList = orderList;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cancel_oder_customer, parent, false);
        CustomerOrderCancelAdapter.ViewHolder viewHolder = new CustomerOrderCancelAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        int receive = position;

        AtomicInteger totalMoney = new AtomicInteger(0);
        userID = FirebaseAuth.getInstance().getUid();
        holder.orderIdTextView.setText(order.getOrderId());
        holder.customerNameTextView.setText(order.getUserName());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
        holder.recyclerViewProducts.setLayoutManager(layoutManager);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maDH = orderList.get(receive).getOrderId();
                // Chuyển sang activity mới
                Intent intent = new Intent(v.getContext(), DetailCustomerOrder.class);
                intent.putExtra("orderId", maDH);
                v.getContext().startActivity(intent);
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.createNewOrder(order);
                Toast.makeText(v.getContext(), "Thực hiện việc ReOrder thành công, đơn hàng của bạn đã được tạo", Toast.LENGTH_SHORT).show();

            }
        });
        // Truy vấn Firebase để lấy AnhDaiDien dựa trên maND
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        CollectionReference usersRef = db.collection("USERS");
        usersRef.document(firebaseUser.getUid()).get()
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
                                        holder.total.setText(formatCurrency(totalMoney.get()));
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

    public void refresh() {
        notifyDataSetChanged();
    }

    private String formatCurrency(int amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(amount);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTextView;
        public TextView customerNameTextView;
        private ImageView img_avatar;
        private RecyclerView recyclerViewProducts;
        private MyProductAdapter myProductAdapter;

        private TextView total;
        private Button button;
        private Button cancel;

        public ViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.tv_orderID_customer);
            customerNameTextView = itemView.findViewById(R.id.tv_ordername_customer);
            img_avatar = itemView.findViewById(R.id.img_avatar_customer);
            recyclerViewProducts = itemView.findViewById(R.id.RCVcard_view_customer);
            total = itemView.findViewById(R.id.moneytotal_customer);
            button = itemView.findViewById(R.id.btn_detail_customer);
            cancel = itemView.findViewById(R.id.confirm_customer);
        }

        public void createNewOrder(Order order) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference donHangCollection = db.collection("ORDER");
            Map<String, Object> newOrder = new HashMap<>();
            newOrder.put("planDelivered", order.getPlanDelivered()); // Thay "MaSP" bằng trường thông tin mã sản phẩm trong đơn hàng cũ
            newOrder.put("discount", order.getDiscount()); // Thay "SoLuong" bằng trường thông tin số lượng trong đơn hàng cũ
            newOrder.put("addressId", order.getAddressId()); // Thay "GiaTien" bằng trường thông tin giá tiền trong đơn hàng cũ
            newOrder.put("discountId", order.getDiscountId());
            newOrder.put("userId", order.getUserId());
            newOrder.put("orderDate", order.getOrderDate());
            newOrder.put("transferFee", order.getTransferFee());
            newOrder.put("payMethod", order.getPayMethod());
            newOrder.put("phoneNumber", order.getPhoneNumber());
            newOrder.put("orderBill", order.getOrderBill());
            newOrder.put("userName", order.getUserName());
            newOrder.put("totalBill", order.getTotalBill());
            newOrder.put("state", "Confirm");
            donHangCollection.add(newOrder)
                    .addOnSuccessListener(documentReference -> {
                        // Lấy mã đơn hàng mới tạo
                        String newMaDH = documentReference.getId();
                        newOrder.put("orderId", newMaDH);
                        // Sau khi tạo đơn hàng mới, sao chép các sản phẩm từ đơn hàng cũ và thêm vào đơn hàng mới
                        FirebaseFirestore db_con = FirebaseFirestore.getInstance();
                        CollectionReference dathangRef = db_con.collection("ORDERACT");
                        dathangRef.whereEqualTo("orderId", order.getOrderId())
                                .get()
                                .addOnSuccessListener(querySnapshot -> {
                                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                        // Sao chép thông tin sản phẩm từ đơn hàng cũ
                                        String maSP = document.getString("productId");
                                        String mauSac = document.getString("productColor");
                                        String size = document.getString("productSize");
                                        long thanhtien = document.getLong("total");
                                        int number = document.getLong("quanity") != null ? Math.toIntExact(document.getLong("quanity")) : 0;

                                        // Tạo một HashMap chứa thông tin sản phẩm mới
                                        Map<String, Object> newProduct = new HashMap<>();
                                        newProduct.put("productId", maSP);
                                        newProduct.put("productColor", mauSac);
                                        newProduct.put("productSize", size);
                                        newProduct.put("quanity", number);
                                        newProduct.put("total", thanhtien);
                                        newProduct.put("orderId", newMaDH);
                                        // Thêm sản phẩm vào đơn hàng mới
                                        db.collection("ORDERACT").add(newProduct)
                                                .addOnSuccessListener(documentReference1 -> {
                                                    String newdathang = documentReference1.getId();
                                                    documentReference.set(newOrder)
                                                            .addOnSuccessListener(aVoid -> {
                                                                refresh();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                // Xử lý lỗi khi cập nhật tài liệu "THONGBAODONHANG"
                                                            });
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Xử lý khi có lỗi xảy ra khi thêm sản phẩm vào đơn hàng mới
                                                });
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý khi truy vấn thất bại
                                });
                        FirebaseFirestore db_confirm = FirebaseFirestore.getInstance();
                        CollectionReference donHangRef = db_confirm.collection("ORDER");
                        DocumentReference docRef = donHangRef.document(newMaDH);
                        docRef.get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String trangThai = documentSnapshot.getString("state");
                                CollectionReference maThongBaoRef = FirebaseFirestore.getInstance().collection("CUSTOMNOTIFY");
                                // Truy vấn để lấy MaTB dựa trên trạng thái "trangThai"
                                maThongBaoRef.whereEqualTo("notifyType", "ReOrder").limit(1).get()
                                        .addOnSuccessListener(querySnapshot -> {
                                            // Kiểm tra xem có kết quả trả về không
                                            if (!querySnapshot.isEmpty()) {
                                                // Lấy MaTB từ tài liệu đầu tiên trong kết quả truy vấn
                                                String maTB = querySnapshot.getDocuments().get(0).getId();
                                                Boolean read = false;
                                                // Truy vấn để lấy MaND dựa trên maDH
                                                donHangRef.document(newMaDH).get()
                                                        .addOnSuccessListener(documentSnapshotTB -> {
                                                            if (documentSnapshotTB.exists()) {
                                                                String maND = documentSnapshotTB.getString("userId");
                                                                Date currentTime = new Date();
                                                                Timestamp timestamp = new Timestamp(currentTime);
                                                                if (maND != null) {
                                                                    // Tạo thông báo
                                                                    Map<String, Object> thongBao = new HashMap<>();
                                                                    thongBao.put("notifyId", maTB);
                                                                    thongBao.put("orderId", newMaDH);
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
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý khi có lỗi xảy ra khi tạo đơn hàng mới
                    });
        }
    }

}