package com.example.shopper.staffview.category.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.product.model.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailCategoryAdapter extends RecyclerView.Adapter<DetailCategoryAdapter.ProductsViewHolder> implements Filterable {
    private List<Product> productList;
    private List<Product> oldProductList;
    private Context context;

    private String categoryId;
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    // Biến mới
    private OnItemClickListener onItemClickListener;

    // Phương thức đặt OnItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public DetailCategoryAdapter(List<Product> productList, String categoryId) {
        this.productList = productList;
        this.oldProductList = productList;
        this.categoryId = categoryId;
        loadDataFromFirestore();
    }

    private void loadDataFromFirestore() {
        CollectionReference sanphamRef = FirebaseFirestore.getInstance().collection("PRODUCT");

        sanphamRef.whereEqualTo("categoryId", categoryId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList.clear();

                        for (DocumentSnapshot document : task.getResult()) {
                            // Retrieve field values from the document
                            List<String> hinhAnhSPList = (List<String>) document.get("imageUrl");
                            String hinhAnhSP = hinhAnhSPList != null && !hinhAnhSPList.isEmpty() ? hinhAnhSPList.get(0) : "";
                            String tenSP = document.getString("productName");
                            int giaSP = document.getLong("productPrice") != null ? document.getLong("productPrice").intValue() : 0;

                            // Create a Product object from the retrieved data
                            Product product = new Product(hinhAnhSP, tenSP, giaSP);

                            // Add the Product object to the list
                            productList.add(product);
                        }

                        notifyDataSetChanged();
                    } else {
                        // Handle unsuccessful query
                        Exception exception = task.getException();
                        // ...
                    }
                });
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getProductName());
        holder.price.setText(String.valueOf(product.getProductPrice()));
        Picasso.get().load(product.getImage()).into(holder.ava);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if(search.isEmpty()){
                    productList = oldProductList;
                }
                else{
                    ArrayList<Product> list = new ArrayList<>();
                    for(Product object : oldProductList){
                        if(object.getProductName().toLowerCase().contains(search.toLowerCase())){
                            list.add(object);
                        }
                    }
                    productList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productList = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price;
        private ImageView ava;
        private Button H, Edit;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_product_name);
            price = itemView.findViewById(R.id.txt_product_price);
            ava = itemView.findViewById(R.id.img_product_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Product clickedItem = productList.get(position);
                            onItemClickListener.onItemClick(clickedItem);
                        }
                    }
                }
            });
        }
    }
}
