package com.example.shopper.staffview.viewshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopper.R;
import com.example.shopper.staffview.category.model.MyCategory;

import java.util.List;

public class ListCategoriesAdapter extends RecyclerView.Adapter<ListCategoriesAdapter.ViewHolder> {

    private static List<MyCategory> categoryItemList;
    private Context context;
    private static OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(MyCategory categoryItem);
    }

    public ListCategoriesAdapter(List<MyCategory> categoryItemList, Context context) {
        this.categoryItemList = categoryItemList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyCategory categoryItem = categoryItemList.get(position);
        holder.txtName.setText(categoryItem.getCategoryName());
        holder.txtQuantity.setText(String.valueOf(categoryItem.getQuantity()));
        Glide.with(holder.itemView.getContext())
                .load(categoryItem.getCategoryImage())
                .into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgItem;
        public TextView txtName;
        public TextView txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item_img);
            txtName = itemView.findViewById(R.id.txt_item_name);
            txtQuantity = itemView.findViewById(R.id.txt_item_quanity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            MyCategory clickedItem = categoryItemList.get(position);
                            onItemClickListener.onItemClick(clickedItem);
                        }
                    }
                }
            });
        }
    }
}
