package com.example.shopper.staffview.viewshop.activity.detail.category;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.category.adapter.DetailCategoryAdapter;
import com.example.shopper.staffview.product.model.Product;
import com.example.shopper.staffview.viewshop.activity.ViewShop;
import com.example.shopper.staffview.viewshop.activity.detail.product.DetailProductView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DetailCategoryView extends AppCompatActivity implements Filterable {

    private RecyclerView recyclerView;
    private DetailCategoryAdapter adapter;
    private List<Product> productList;

    private Button button;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category_view);

        // Lấy categoryId từ Intent
        String categoryId = getIntent().getStringExtra("categoryId");

        recyclerView = findViewById(R.id.RCV_Details_Category);
        productList = new ArrayList<>();
        adapter = new DetailCategoryAdapter(productList, categoryId);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        button = findViewById(R.id.backIcon);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailCategoryView.this, ViewShop.class);
                startActivity(intent);
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
        adapter.setOnItemClickListener(new DetailCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                // Xử lý sự kiện khi nhấp vào danh mục
                String TenSP = product.getProductName(); // Lấy tên danh mục
                Log.d("ProductID", "ProductID: " + TenSP);

                // Tạo một truy vấn Firestore để lấy MaDM từ TenDM
                FirebaseFirestore.getInstance().collection("PRODUCT")
                        .whereEqualTo("productName", TenSP)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                // Lấy MaDM từ kết quả truy vấn
                                String productID = task.getResult().getDocuments().get(0).getId();
                                Intent intent = new Intent(DetailCategoryView.this, DetailProductView.class);
                                intent.putExtra("productId", productID);
                                startActivity(intent);
                            }
                        });
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