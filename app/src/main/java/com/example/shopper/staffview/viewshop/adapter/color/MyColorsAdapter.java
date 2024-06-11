package com.example.shopper.staffview.viewshop.adapter.color;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.util.color.model.Colors;
import com.example.shopper.staffview.product.activity.DetailMyProduct;

import java.util.List;

public class MyColorsAdapter extends RecyclerView.Adapter<MyColorsAdapter.ColorViewHolder> {

    private List<Colors> mColor;
    DetailMyProduct detailMyProduct;

    private int selectedItem = -1;

    public void setMyData(List<Colors> list, DetailMyProduct detailProductActivity) {
        this.mColor = list;
        this.detailMyProduct = detailProductActivity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyColorsAdapter.ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
        return new MyColorsAdapter.ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyColorsAdapter.ColorViewHolder holder, int position) {

        Colors color = mColor.get(position);
        if (color == null) return;
        holder.tenMau.setText(color.getColorName());
        int mauSac = Color.parseColor(color.getColorCode());
        holder.maMau.setBackgroundColor(mauSac);
        boolean isSelected = (selectedItem == position);
        if (isSelected) {
            holder.layoutItemColor.setBackgroundResource(R.drawable.selected_item_background);
        } else {
            holder.layoutItemColor.setBackgroundResource(R.drawable.default_item_background);
        }

        holder.layoutItemColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    selectedItem = -1; // Bỏ chọn item nếu đã được chọn
                } else {
                    selectedItem = holder.getAdapterPosition(); // Đánh dấu item được chọn
                    Colors color = mColor.get(holder.getAdapterPosition());
                    detailMyProduct.onColorClick(color.getColorCode());
                }
                notifyDataSetChanged(); // Cập nhật
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mColor != null) {
            return mColor.size();
        }
        return 0;
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout maMau;
        private LinearLayout layoutItemColor;
        private TextView tenMau;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            maMau = itemView.findViewById(R.id.maMau);
            tenMau = itemView.findViewById(R.id.tenMau);
            layoutItemColor = itemView.findViewById(R.id.layoutItemColor);
        }
    }
}
