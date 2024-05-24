package com.example.shopper.staffview.viewshop.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopper.R;
import com.example.shopper.staffview.category.activity.DetailCategory;
import com.example.shopper.staffview.category.model.MyCategory;
import com.example.shopper.staffview.viewshop.adapter.ListCategoriesAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListCategoriesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ListCategoriesAdapter adapter;
    private List<MyCategory> categoryItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_items, container, false);

        recyclerView = view.findViewById(R.id.RCV_item_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        categoryItemList = new ArrayList<>();
        adapter = new ListCategoriesAdapter(categoryItemList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListCategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyCategory categoryItem) {
                String categoryName = categoryItem.getCategoryName(); // Lấy tên danh mục

                // Tạo một truy vấn Firestore để lấy MaDM từ TenDM
                FirebaseFirestore.getInstance().collection("CATEGORY")
                        .whereEqualTo("categoryName", categoryName)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                // Lấy MaDM từ kết quả truy vấn
                                String categoryId = task.getResult().getDocuments().get(0).getId();

                                // Chuyển sang màn hình hiển thị sản phẩm với categoryId
                                Intent intent = new Intent(getActivity(), DetailCategory.class);
                                intent.putExtra("categoryId", categoryId);
                                startActivity(intent);
                            }
                        });
            }
        });
        // Load products from Firebase
        loadProducts();

        return view;
    }

    private void loadProducts() {
        // Query the "SANPHAM" collection in Firebase
        // Lấy danh mục từ Firestore và cập nhật danh sách
        FirebaseFirestore.getInstance().collection("CATEGORY")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("categoryName");
                            String image = document.getString("categoryImage");
                            int quantity = document.getLong("quanity").intValue();
                            MyCategory categoryItem = new MyCategory(name, image, quantity);
                            categoryItemList.add(categoryItem);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}