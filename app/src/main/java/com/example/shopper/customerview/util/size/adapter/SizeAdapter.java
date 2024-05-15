package com.example.shopper.customerview.util.size.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.product.activity.DetailProduct;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {

    private List<String> sizes;
    private DetailProduct detailProductActivity;
    private int selectedItem = -1;

    public void setData(List<String> list, DetailProduct detailProductActivity) {
        this.sizes = list;
        this.detailProductActivity = detailProductActivity;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_size, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {

        String size = sizes.get(position);


        holder.txtItemSize.setText(size);

        boolean isSelected = (selectedItem == position);
        if (isSelected) {
            holder.item_size.setBackgroundResource(R.drawable.selected_item_background);
        } else {
            holder.item_size.setBackgroundResource(R.drawable.default_item_background);
        }

        holder.item_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    selectedItem = -1; // Bỏ chọn item nếu đã được chọn
                } else {
                    selectedItem = holder.getAdapterPosition(); // Đánh dấu item được chọn
                    String size = sizes.get(holder.getAdapterPosition());
                    detailProductActivity.onSizeClick(size);
                }
                notifyDataSetChanged(); // Cập nhật
            }
        });
    }

    @Override
    public int getItemCount() {
        if (sizes != null) return sizes.size();
        return 0;
    }

    public class SizeViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItemSize;
        private LinearLayout item_size;

        public SizeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemSize = itemView.findViewById(R.id.txtItemSize);
            item_size = itemView.findViewById(R.id.item_size);
        }
    }
}
