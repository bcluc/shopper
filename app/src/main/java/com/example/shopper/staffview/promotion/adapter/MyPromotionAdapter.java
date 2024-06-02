package com.example.shopper.staffview.promotion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopper.R;
import com.example.shopper.staffview.promotion.model.MyPromotionData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyPromotionAdapter extends RecyclerView.Adapter<MyPromotionAdapter.PromotionViewHolder> {
    private List<MyPromotionData> promotionList;

    public MyPromotionAdapter(List<MyPromotionData> promotionList) {
        this.promotionList = promotionList;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_promotion, parent, false);
        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        MyPromotionData promotion = promotionList.get(position);
        holder.titleTextView.setText(promotion.getChiTietKM());
        holder.descriptionTextView.setText(promotion.getTenKM());
        holder.LoaiKM.setText(promotion.getLoaiKhuyenMai());
        holder.dontoithieu.setText(String.valueOf(promotion.getDonToiThieu()) );
        // Chuyển đổi Timestamp thành Date
        Date startDate = promotion.getNgayBatDau().toDate();

        // Định dạng ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startformattedDate = sdf.format(startDate);

        Date endDate = promotion.getNgayKetThuc().toDate();

        // Định dạng ngày tháng
        SimpleDateFormat edf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String endformattedDate = sdf.format(endDate);
        // Hiển thị ngày bắt đầu lên TextView
        holder.startDay.setText(startformattedDate);
        holder.endDay.setText(endformattedDate);
        // Sử dụng Glide để tải và hiển thị ảnh từ URL trong ImageView
        // Sử dụng Glide để tải và hiển thị ảnh từ URL trong ImageView
        Glide.with(holder.itemView.getContext())
                .load(promotion.getHinhAnhKM())
                .into(holder.pic_promotion);
        // Các trường thông tin khác của khuyến mãi có thể được hiển thị tại đây.
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    static class PromotionViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView pic_promotion;
        private TextView startDay, endDay;
        private TextView dontoithieu;
        private TextView LoaiKM;
        PromotionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.chitietKhuyenmai);
            descriptionTextView = itemView.findViewById(R.id.tenKhuyenmai);
            pic_promotion=itemView.findViewById(R.id.hinhanh_promotion);
            LoaiKM = itemView.findViewById(R.id.loaiKM);
            dontoithieu = itemView.findViewById(R.id.miniOrder);
            startDay = itemView.findViewById(R.id.Ngaybatdau);
            endDay = itemView.findViewById(R.id.Ngayketthuc);
        }
    }
}
