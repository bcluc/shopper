package com.example.shopper.staffview.order.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.order.model.ItemOrder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ItemOrder> itemOrderList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameTextView;
        public TextView number;
        public TextView number_of;
        public ImageView image;
        public TextView color, size;

        public ViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.product_name);
            image = itemView.findViewById(R.id.img_product);
            number = itemView.findViewById(R.id.product_price);
            number_of = itemView.findViewById(R.id.number_of_product);
            color = itemView.findViewById(R.id.color_product);
            size = itemView.findViewById(R.id.size_product);
        }
    }


    public ProductAdapter(List<ItemOrder> itemOrderList) {
        this.itemOrderList = itemOrderList;
    }
    public ProductAdapter() {
        this.itemOrderList = new ArrayList<>();
    }

    public void setItemOrderList(List<ItemOrder> itemOrderList) {
        this.itemOrderList = itemOrderList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemOrder itemOrder = itemOrderList.get(position);

        // Hiển thị thông tin sản phẩm
        holder.productNameTextView.setText(itemOrder.getProductName());
        holder.number.setText(String.valueOf(itemOrder.getProductPrice()));
        holder.number_of.setText(String.valueOf(itemOrder.getQuanity()));
        holder.color.setText(itemOrder.getProductColor());
        holder.size.setText(itemOrder.getProductSize());
        if(itemOrder.getImage() == null)
        {
            Picasso.get().load(R.drawable.anh1).into(holder.image);
        }
        else
        {
            Picasso.get().load(itemOrder.getImage()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return itemOrderList.size();
    }
    private String formatCurrency(int amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(amount);
    }
}

