package com.example.shopper.customerview.notification.adapter.notification;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.notification.model.Voucher;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderNotificationAdapter extends RecyclerView.Adapter<OrderNotificationAdapter.ProductsViewHolder> {
    private List<Voucher> voucherList;
    private Context context;

    public OrderNotificationAdapter(List<Voucher> voucherList, Context context) {
        this.context = context;
        this.voucherList = voucherList;
        loadDataFromFirestore();
    }

    private void loadDataFromFirestore() {
        String UserId = FirebaseAuth.getInstance().getUid();
        CollectionReference thongbaoRef = FirebaseFirestore.getInstance().collection("ORDERNOTIFY");
        thongbaoRef.whereEqualTo("userId", UserId).addSnapshotListener((querySnapshot, error) -> {
            if (error != null) {
                // Xử lý khi có lỗi xảy ra
                return;
            }

            if (querySnapshot != null) {
                voucherList.clear();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    String MaDH = document.getString("orderId");
                    String MaND = document.getString("userId");
                    String MaTB = document.getString("notifyId");
                    String TBO = document.getString("orderNotifyId");
                    Timestamp Thoigian = document.getTimestamp("timeStamp");
                    Log.d("TIMESTAMPPP", Thoigian.toString());
                    CollectionReference mathongbaoRef = FirebaseFirestore.getInstance().collection("CUSTOMNOTIFY");
                    mathongbaoRef.whereEqualTo("notifyId", MaTB).addSnapshotListener((KMquerySnapshot, error1) -> {
                        for (DocumentSnapshot documentSnapshot : KMquerySnapshot.getDocuments()) {
                            String Noidung = documentSnapshot.getString("notifyContent");
                            String LoaiTB = documentSnapshot.getString("notifyType");
                            String AnhTB = documentSnapshot.getString("notifyImage");
                            Voucher voucher = new Voucher(AnhTB, LoaiTB, Noidung, MaTB, TBO, MaDH, MaND, Thoigian);
                            voucherList.add(voucher);
                        }
                        notifyDataSetChanged();
                    });
                }
                Collections.sort(voucherList, new Comparator<Voucher>() {
                    @Override
                    public int compare(Voucher o1, Voucher o2) {
                        return - o1.getThoigian().compareTo(o2.getThoigian());
                    }
                });
                notifyDataSetChanged();
            }
        });

    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_order, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);
        holder.id.setText(voucher.getMaDH());
        holder.detail.setText(voucher.getNoiDung());
        Picasso.get().load(voucher.getAnhTB()).into(holder.ava);
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private ImageView ava;
        private TextView detail;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.ID_order);
            detail = itemView.findViewById(R.id.txtContentNotification_order);
            ava = itemView.findViewById(R.id.imgAvt_order);
        }
    }
}
