package com.example.shopper.customerview.home.category.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.product.activity.DetailProduct;
import com.example.shopper.customerview.home.product.adapter.ProductTrendingAdapter;
import com.example.shopper.customerview.home.product.model.ProductTrending;
import com.example.shopper.customerview.home.shoppingcart.ShoppingCart;
import com.example.shopper.customerview.navigation.activity.BottomNavigationCustomerActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

@OptIn(markerClass = com.google.android.material.badge.ExperimentalBadgeUtils.class)
public class CategoriesDetail extends AppCompatActivity implements Filterable {
    private List<String> cartData;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private BottomNavigationCustomerActivity bottomNavigationCustomActivity;
    private RecyclerView recyclerView;
    List<ProductTrending> productCardList;
    private ProductTrendingAdapter productAdapter;
    TextView textView;
    private String TenDM, MaDM;
    SearchView searchView;
    ImageView backICon, shoppingCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        if(intent != null){
            TenDM = intent.getStringExtra("categoryName");
            MaDM = intent.getStringExtra("categoryId");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_detail);

        productAdapter = new ProductTrendingAdapter();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.rcv_product);
        searchView = findViewById(R.id.search_view);
        backICon = findViewById(R.id.btn_back);
        textView = findViewById(R.id.txt_name_categories);
        shoppingCart = findViewById(R.id.btn_shopping_cart);
        int numberOfColumns = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);


        textView.setText(TenDM);

        SearchCategory();
        getDataCategories();
        SoLuongShoppingCart();

        backICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesDetail.this, ShoppingCart.class );
                startActivity(intent);
            }
        });
    }

    private void SearchCategory(){
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                productAdapter.getFilter().filter(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                productAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void getDataCategories(){
        db.collection("PRODUCT")
                .whereEqualTo("categoryId", MaDM)
                .whereEqualTo("state", "Inventory")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        productCardList = new ArrayList<>();
                        for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                            String masp = documentSnapshot.getString("productId");
                            String name = documentSnapshot.getString("productName");
                            List<String> images = (List<String>) documentSnapshot.get("image");
                            int price = documentSnapshot.getLong("productPrice").intValue();
                            productCardList.add(new ProductTrending(masp, images.get(0), name, price));
                            Log.d("Err", name);
                        }
                        productAdapter.setData(productCardList);
                        productAdapter.setOnItemClick(new ProductTrendingAdapter.OnItemClick() {
                            @Override
                            public void onButtonItemClick(int position) {
                                Intent t = new Intent(CategoriesDetail.this, DetailProduct.class);
                                t.putExtra("productId", productCardList.get(position).getProductId());
                                startActivity(t);
                            }
                        });
                        recyclerView.setAdapter(productAdapter);
                    }
                });
    }
    private void SoLuongShoppingCart(){
        db.collection("CART")
                .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        cartData = new ArrayList<>();
                        for(DocumentSnapshot doc: value.getDocuments()){
                            if(doc.exists()){
                                String ma = doc.getString("cartId");
                                cartData.add(ma);
                            }
                        }
                        BadgeDrawable badgeDrawable = BadgeDrawable.create(CategoriesDetail.this);
                        badgeDrawable.setNumber(cartData.size());

                        BadgeUtils.attachBadgeDrawable(badgeDrawable, shoppingCart, null);
                    }
                });

    }

    @Override
    public Filter getFilter() {
        return null;
    }
}