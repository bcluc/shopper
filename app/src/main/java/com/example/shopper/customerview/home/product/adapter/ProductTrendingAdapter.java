package com.example.shopper.customerview.home.product.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.product.model.ProductTrending;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductTrendingAdapter extends  RecyclerView.Adapter<ProductTrendingAdapter.ProductViewHolder> implements Filterable {

    private List<ProductTrending> mProducts;
    private List<ProductTrending> mProductsOld;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
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
                    ArrayList<ProductTrending> list = new ArrayList<>();
                    for(ProductTrending object : mProductsOld){
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
                mProducts = (ArrayList<ProductTrending>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClick {
        void onButtonItemClick(int position);
    }
    public void setData(List<ProductTrending> list)
    {
        this.mProducts = list;
        this.mProductsOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductTrending product = mProducts.get(position);
        if(product == null) return;
        //holder.imgProduct.setImageResource(product.getResouceId());
        Picasso.get().load(product.getResouceId()).into(holder.imgProduct);
        holder.txtNameProduct.setText(product.getName());
        holder.price.setText(String.valueOf(product.getPriceProduct()));
        holder.layoutProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick != null){
                    onItemClick.onButtonItemClick(holder.getAdapterPosition());
                }
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

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgProduct;
        private TextView txtNameProduct;
        private TextView price;
        private RelativeLayout layoutProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_trending_card);
            txtNameProduct = itemView.findViewById(R.id.txt_name_trending_card);
            price = itemView.findViewById(R.id.txt_price_trending_card);
            layoutProduct = itemView.findViewById(R.id.layout);

        }

    }
}