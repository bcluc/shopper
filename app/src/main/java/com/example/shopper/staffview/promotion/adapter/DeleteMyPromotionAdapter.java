package com.example.shopper.staffview.promotion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopper.R;
import com.example.shopper.staffview.promotion.model.MyPromotionData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeleteMyPromotionAdapter extends RecyclerView.Adapter<DeleteMyPromotionAdapter.PromotionViewHolder> {
    private List<MyPromotionData> promotionList;


    public List<MyPromotionData> getSelectedCategories() {
        List<MyPromotionData> selectedCategories = new ArrayList<>();
        for (MyPromotionData categoryItem : promotionList) {
            if (categoryItem.getSelected()) {
                selectedCategories.add(categoryItem);
            }
        }
        return selectedCategories;
    }
    public DeleteMyPromotionAdapter(List<MyPromotionData> promotionList) {
        this.promotionList = promotionList;
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delete_my_promotion, parent, false);
        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        MyPromotionData promotion = promotionList.get(position);
        holder.titleTextView.setText(promotion.getChiTietKM());
        holder.descriptionTextView.setText(promotion.getTenKM());
        holder.LoaiKM.setText(promotion.getLoaiKhuyenMai());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                promotion.setSelected(isChecked);
            }
        });
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
        private CheckBox checkBox;
        PromotionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.chitietKhuyenmai_delete);
            descriptionTextView = itemView.findViewById(R.id.tenKhuyenmai_delete);
            pic_promotion=itemView.findViewById(R.id.hinhanh_promotion_delete);
            LoaiKM = itemView.findViewById(R.id.loaiKM_delete);
            dontoithieu = itemView.findViewById(R.id.miniOrder_delete);
            startDay = itemView.findViewById(R.id.Ngaybatdau_delete);
            endDay = itemView.findViewById(R.id.Ngayketthuc_delete);
            checkBox = itemView.findViewById(R.id.check_box_promotion);
        }
    }
}
