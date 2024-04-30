package com.example.shopper.customerview.home.product.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.product.model.Product;
import com.example.shopper.customerview.itf.IClickItemProductListener;
import com.example.shopper.staffview.order.model.ItemOrder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends  RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    private List<Product> mProducts;
    private List<Product> mProductsOld;
    private IClickItemProductListener iClickItemProductListener;

    public ProductAdapter() {

    }

    public void setData(List<Product> list, IClickItemProductListener listener)
    {
        this.mProducts = list;
        this.mProductsOld = list;
        this.iClickItemProductListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mProducts.get(position);
        if(product == null) return;

        //holder.imgProduct.setImageResource(product.getResouceId());
        Picasso.get().load(product.getResouceId()).into(holder.imgProduct);
        holder.txtNameProduct.setText(product.getName());
        holder.txtPriceProduct.setText(String.valueOf(product.getPrice()) + " VND");

        holder.layoutProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemProductListener.onClickItemProduct(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mProducts != null){
            return mProducts.size();
        }
        return 0;
    }

    public void setItemOrderList(List<ItemOrder> itemOrderList) {
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if(search.isEmpty()){
                    mProducts = mProductsOld;
                }
                else{
                    ArrayList<Product> list = new ArrayList<>();
                    for(Product object : mProductsOld){
                        if(object.getName().toLowerCase().contains(search.toLowerCase())){
                            list.add(object);
                        }
                    }
                    mProducts = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mProducts;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mProducts = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgProduct;
        private TextView txtNameProduct, txtPriceProduct;
        private GridLayout layoutProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product_img);
            txtNameProduct = itemView.findViewById(R.id.txt_product_name);
            txtPriceProduct = itemView.findViewById(R.id.txt_product_price);
            layoutProduct = itemView.findViewById(R.id.layout_product);
        }
    }
}