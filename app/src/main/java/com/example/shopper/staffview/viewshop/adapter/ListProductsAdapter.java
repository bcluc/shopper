package com.example.shopper.staffview.viewshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.ProductsViewHolder> {
    private List<Product> productList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ListProductsAdapter(List<Product> productList, Context context) {
        this.context = context;
        this.productList = new ArrayList<>();
        loadDataFromFirestore();
    }

    private void loadDataFromFirestore() {
        CollectionReference sanphamRef = FirebaseFirestore.getInstance().collection("PRODUCT");

        sanphamRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productList.clear();

                for (DocumentSnapshot document : task.getResult()) {
                    // Lấy các trường dữ liệu từ document
                    // Lấy mảng địa chỉ ảnh
                    List<String> hinhAnhSPList = (List<String>) document.get("imageUrl");

                    // Lấy địa chỉ ảnh đầu tiên trong mảng
                    String hinhAnhSP = hinhAnhSPList != null && !hinhAnhSPList.isEmpty() ? hinhAnhSPList.get(0) : "";
                    String tenSP = document.getString("productName");
                    int giaSP = document.getLong("productPrice") != null ? document.getLong("productPrice").intValue() : 0;

                    // Tạo đối tượng Product từ dữ liệu lấy được
                    Product product = new Product(hinhAnhSP, tenSP, giaSP);

                    // Thêm đối tượng Product vào danh sách
                    productList.add(product);
                }

                notifyDataSetChanged();
            } else {
                // Xử lý khi không thành công
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

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price;
        private ImageView ava;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_product_name);
            price = itemView.findViewById(R.id.txt_product_price);
            ava = itemView.findViewById(R.id.img_product_img);
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Product product = productList.get(position);
                        onItemClickListener.onItemClick(product);
                    }
                }
            });

        }
    }
}
