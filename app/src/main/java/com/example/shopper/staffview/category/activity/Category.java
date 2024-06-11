package com.example.shopper.staffview.category.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.StaffHomePage;
import com.example.shopper.staffview.category.adapter.MyCategoryAdapter;
import com.example.shopper.staffview.category.model.MyCategory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity implements Filterable {

    private Button back;

    private Button btn;
    private Button btn_delete_categories;
    private RecyclerView rcvCategories;
    private MyCategoryAdapter adapter;
    private SearchView searchView;
    private List<MyCategory> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        back = findViewById(R.id.backIcon);
        rcvCategories = findViewById(R.id.rcvCategories);
        categoryList = new ArrayList<>();
        adapter = new MyCategoryAdapter(categoryList);
        rcvCategories.setLayoutManager(new LinearLayoutManager(this));
        rcvCategories.setAdapter(adapter);
        btn = findViewById(R.id.btn_create_new_category);
        btn_delete_categories = findViewById(R.id.btn_delete_category);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category.this, StaffHomePage.class);
                startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category.this, AddNewCategory.class);
                startActivity(intent);
            }
        });
        btn_delete_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Category.this, DeleteCategory.class);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new MyCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyCategory category) {
                // Xử lý sự kiện khi nhấp vào danh mục
                String categoryName = category.getCategoryName(); // Lấy tên danh mục

                // Tạo một truy vấn Firestore để lấy MaDM từ TenDM
                FirebaseFirestore.getInstance().collection("CATEGORY")
                        .whereEqualTo("categoryName", categoryName)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                // Lấy MaDM từ kết quả truy vấn
                                String categoryId = task.getResult().getDocuments().get(0).getId();

                                // Chuyển sang màn hình hiển thị sản phẩm với categoryId
                                Intent intent = new Intent(Category.this, DetailCategory.class);
                                intent.putExtra("categoryId", categoryId);
                                startActivity(intent);
                            }
                        });
            }
        });

        // Lấy danh mục từ Firestore và cập nhật danh sách
        FirebaseFirestore.getInstance().collection("CATEGORY")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("categoryName");
                            String image = document.getString("categoryImage");
                            int quantity = document.getLong("quanity").intValue();
                            MyCategory category = new MyCategory(name, image, quantity);
                            categoryList.add(category);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView = findViewById(R.id.searchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

    }
    @Override
    public Filter getFilter() {
        return null;
    }
}