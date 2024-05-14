package com.example.shopper.staffview.product.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopper.R;
import com.example.shopper.staffview.product.adapter.OnWaitAdapter;
import com.example.shopper.staffview.product.model.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnWaitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnWaitFragment extends Fragment {

    private RecyclerView recyclerView;
    private OnWaitAdapter adapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_wait, container, false);
        recyclerView = view.findViewById(R.id.RCV_onwait);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        adapter = new OnWaitAdapter(productList, getContext());
        adapter.setHideButtonClickListener(new OnWaitAdapter.HideButtonClickListener() {
            @Override
            public void onHideButtonClick(int position) {
                adapter.updateProductStatus(position);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void loadProductsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference sanphamRef = db.collection("PRODUCT");

        sanphamRef.whereEqualTo("state", "Onwait")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Xử lý lỗi nếu cần thiết
                        return;
                    }
                    productList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        List<String> hinhAnhSPList = (List<String>) document.get("imageUrl");

                        String moTaSP = document.getString("description");
                        List<String> color = (List<String>) document.get("productColor");
                        List<String> size = (List<String>) document.get("productSize");
                        String trangthai = document.getString("state");
                        // Lấy địa chỉ ảnh đầu tiên trong mảng
                        String hinhAnhSP = hinhAnhSPList != null && !hinhAnhSPList.isEmpty() ? hinhAnhSPList.get(0) : "";
                        String tenSP = document.getString("productName");
                        String MaSP  =  document.getString("productId");
                        int price = document.getLong("productPrice") != null ? document.getLong("productPrice").intValue() : 0;
                        int warehouse = document.getLong("warehouse") != null ? document.getLong("warehouse").intValue() : 0;
                        int sold = document.getLong("sold") != null ? document.getLong("sold").intValue() : 0;
                        int love = document.getLong("love") != null ? document.getLong("love").intValue() : 0;
                        int view = document.getLong("quanity") != null ? document.getLong("quanity").intValue() : 0;
                        Product product = new Product(hinhAnhSP, tenSP, price, warehouse, sold, love, view, MaSP);
                        // Thêm đối tượng Product vào danh sách
                        product.setDescription(moTaSP);
                        product.setProductSize(size);
                        product.setProductColor(color);
                        product.setState(trangthai);
                        productList.add(product);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
    @Override
    public void onResume() {
        super.onResume();
        loadProductsFromFirestore();
    }
}
