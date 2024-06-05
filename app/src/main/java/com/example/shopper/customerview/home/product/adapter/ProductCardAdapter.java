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
import com.example.shopper.customerview.home.product.model.ProductCard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.ProductCardViewHolder> implements Filterable {

    List<ProductCard> mProductCard;
    List<ProductCard> mProductCardOld;


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
                if (search.isEmpty()) {
                    mProductCard = mProductCardOld;
                } else {
                    ArrayList<ProductCard> list = new ArrayList<>();
                    for (ProductCard object : mProductCardOld) {
                        if (object.getProductName().toLowerCase().contains(search.toLowerCase())) {
                            list.add(object);
                        }
                    }
                    mProductCard = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mProductCard;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mProductCard = (ArrayList<ProductCard>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setData(List<ProductCard> list) {
        this.mProductCard = list;
        this.mProductCardOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending_product, parent, false);

        return new ProductCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position) {
        ProductCard productCard = mProductCard.get(position);

        if (productCard == null) return;

        Picasso.get().load(productCard.getImageResouce()).into(holder.imgProductCard);
        holder.txtNameProductCard.setText(productCard.getProductName());
        holder.txtPriceProductCard.setText(String.valueOf(productCard.getProductPrice()));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onButtonItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mProductCard != null) return mProductCard.size();
        return 0;
    }

    public interface OnItemClick {
        void onButtonItemClick(int position);
    }

    public class ProductCardViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProductCard;
        private TextView txtNameProductCard, txtPriceProductCard;
        private RelativeLayout relativeLayout;

        public ProductCardViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductCard = itemView.findViewById(R.id.img_trending_card);
            txtNameProductCard = itemView.findViewById(R.id.txt_name_trending_card);
            txtPriceProductCard = itemView.findViewById(R.id.txt_price_trending_card);
            relativeLayout = itemView.findViewById(R.id.layout);
        }
    }
}
