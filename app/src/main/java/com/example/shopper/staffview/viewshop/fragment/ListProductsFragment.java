package com.example.shopper.staffview.viewshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.product.activity.DetailMyProduct;
import com.example.shopper.staffview.product.model.Product;
import com.example.shopper.staffview.viewshop.adapter.ListProductsAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class ListProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListProductsAdapter adapter1;
    private List<Product> productList1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_products, container, false);

        recyclerView = view.findViewById(R.id.RCV_product_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productList1 = new ArrayList<>();
        CollectionReference sanphamRef = FirebaseFirestore.getInstance().collection("PRODUCT");
        sanphamRef.whereEqualTo("state", "Inventory").get(Source.SERVER)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productList1.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            List<String> hinhAnhSPList = (List<String>) document.get("imageUrl");

                            // Lấy địa chỉ ảnh đầu tiên trong mảng
                            String hinhAnhSP = hinhAnhSPList != null && !hinhAnhSPList.isEmpty() ? hinhAnhSPList.get(0) : "";
                            String tenSP = document.getString("productName");
                            int giaSP = document.getLong("productPrice") != null ? document.getLong("productPrice").intValue() : 0;
                            // Tạo đối tượng Product từ dữ liệu lấy được
                            Product product = new Product(hinhAnhSP, tenSP, giaSP);
                            // Thêm đối tượng Product vào danh sách
                            productList1.add(product);

                        }

                        adapter1.notifyDataSetChanged();
                    } else {
                        // Xử lý khi không thành công
                        Exception exception = task.getException();
                        // ...
                    }

                });

        adapter1 = new ListProductsAdapter(productList1, view.getContext());
        recyclerView.setAdapter(adapter1);
        // Load products from Firebase
        adapter1.setOnItemClickListener(new ListProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                String TenSP = product.getProductName(); // Lấy tên sản phẩm
                Log.d("ProductName", "Product Name: " + TenSP);

                // Tạo một truy vấn Firestore để lấy MaDM từ TenDM
                FirebaseFirestore.getInstance().collection("PRODUCT")
                        .whereEqualTo("productName", TenSP)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                // Lấy MaDM từ kết quả truy vấn
                                String productID = task.getResult().getDocuments().get(0).getId();
                                Intent intent = new Intent(getActivity(), DetailMyProduct.class);
                                intent.putExtra("productId", productID);
                                Log.d("ProductID", "Product id: " + productID);
                                startActivity(intent);
                            }
                        });
            }
        });


        return view;
    }
}