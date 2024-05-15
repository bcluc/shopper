package com.example.shopper.staffview.category.adapter;

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
import com.example.shopper.staffview.category.model.MyCategory;

import java.util.ArrayList;
import java.util.List;

public class DeleteCategoryAdapter extends RecyclerView.Adapter<DeleteCategoryAdapter.ViewHolder> {

    private List<MyCategory> categoryList;

    public List<MyCategory> getSelectedCategories() {
        List<MyCategory> selectedCategories = new ArrayList<>();
        for (MyCategory category : categoryList) {
            if (category.isSelected()) {
                selectedCategories.add(category);
            }
        }
        return selectedCategories;
    }
    public DeleteCategoryAdapter(List<MyCategory> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delete_my_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyCategory category = categoryList.get(position);
        holder.bind(category); // Thêm dòng này để ánh xạ dữ liệu vào ViewHolder
        holder.txtName.setText(category.getCategoryName());
        holder.txtQuantity.setText(String.valueOf(category.getQuantity()));
        Glide.with(holder.itemView.getContext())
                .load(category.getCategoryImage())
                .into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgItem;
        public TextView txtName;
        public TextView txtQuantity;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item_img);
            txtName = itemView.findViewById(R.id.txt_item_name);
            txtQuantity = itemView.findViewById(R.id.txt_item_quanity);
            checkBox = itemView.findViewById(R.id.check);
        }

        public void bind(MyCategory category) {
            txtName.setText(category.getCategoryName());
            txtQuantity.setText(String.valueOf(category.getQuantity()));
            checkBox.setChecked(category.isSelected());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    category.setSelected(isChecked);
                }
            });
        }
    }
}
