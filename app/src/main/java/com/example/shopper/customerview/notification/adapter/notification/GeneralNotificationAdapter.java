package com.example.shopper.customerview.notification.adapter.notification;

import android.content.Context;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GeneralNotificationAdapter extends RecyclerView.Adapter<GeneralNotificationAdapter.ProductsViewHolder> {
    private List<Voucher> voucherList;
    private Context context;

    public GeneralNotificationAdapter(List<Voucher> voucherList, Context context) {
        this.context = context;
        this.voucherList = voucherList;
        loadDataFromFirestore();
    }

    private void loadDataFromFirestore() {
        CollectionReference thongbaoRef = FirebaseFirestore.getInstance().collection("SENDNOTIFY");
        thongbaoRef.addSnapshotListener((querySnapshot, error) -> {
            if (error != null) {
                // Xử lý khi có lỗi xảy ra
                return;
            }

            if (querySnapshot != null) {
                voucherList.clear();

                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    // Lấy các trường dữ liệu từ document
                    // Lấy mảng địa chỉ ảnh
                    String MaKM = document.getString("promotionId");
                    String MaTB = document.getString("notifyId");
                    String TB = document.getString("sendNotifyId");
                    Timestamp Thoigian = document.getTimestamp("timeStamp");
                    CollectionReference mathongbaoRef = FirebaseFirestore.getInstance().collection("CUSTOMNOTIFY");
                    mathongbaoRef.whereEqualTo("notifyId", MaTB).addSnapshotListener((KMquerySnapshot, error1) -> {
                        for (DocumentSnapshot documentSnapshot : KMquerySnapshot.getDocuments()) {
                            String Noidung = documentSnapshot.getString("notifyContent");
                            String LoaiTB = documentSnapshot.getString("notifyType");
                            CollectionReference khuyenmaiRef = FirebaseFirestore.getInstance().collection("PROMOTION");
                            khuyenmaiRef.whereEqualTo("promotionId", MaKM).addSnapshotListener((KMquerySnapshot1, error2) -> {
                                for (DocumentSnapshot kmdocumentSnapshot : KMquerySnapshot1.getDocuments()) {
                                    String HinhAnhTB = kmdocumentSnapshot.getString("notifyImg");
                                    String HinhAnhKM = kmdocumentSnapshot.getString("promotionImg");

                                    String LoaiKhuyenMai = kmdocumentSnapshot.getString("promotionType");
                                    String TenKM = kmdocumentSnapshot.getString("promotionDetail");
                                    Voucher voucher = new Voucher(HinhAnhKM, HinhAnhTB, MaTB, MaKM, Noidung, TB, LoaiTB, TenKM, Thoigian);
                                    voucherList.add(voucher);
                                }
                                notifyDataSetChanged();
                            });
                        }
                        notifyDataSetChanged();
                    });
                    notifyDataSetChanged();
                }
            }
        });
    }


    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Voucher voucher = voucherList.get(position);
        holder.name.setText(voucher.getTenKM());
        holder.detail.setText(voucher.getNoiDung());
        Picasso.get().load(voucher.getHinhAnhTB()).into(holder.pic_no);
        Picasso.get().load(voucher.getHinhAnhKM()).into(holder.ava);
    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView ava, pic_no;
        private TextView detail;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtNameNotification);
            detail = itemView.findViewById(R.id.txtContentNotification);
            ava = itemView.findViewById(R.id.imgAvt);
            pic_no = itemView.findViewById(R.id.imgNoti);


        }
    }
}
