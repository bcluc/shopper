package com.example.shopper.customerview.util.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.product.activity.DetailProduct;
import com.example.shopper.customerview.home.product.adapter.ProductCardAdapter;
import com.example.shopper.customerview.home.product.model.ProductCard;
import com.example.shopper.customerview.home.shoppingcart.ShoppingCart;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Searching extends AppCompatActivity implements Filterable {

    private List<String> dataGiohang;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private RecyclerView rcvProductTrending;
    SearchView searchView;

    ProductCardAdapter productCardAdapter;
    List<ProductCard> mTrendingCard;

    ImageView backICon, shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        rcvProductTrending = findViewById(R.id.rcvProductTrending);
        searchView = findViewById(R.id.searchView);
        backICon = findViewById(R.id.backIcon);
        shoppingCart = findViewById(R.id.ShoppingCart);
        productCardAdapter = new ProductCardAdapter();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        setRcvProductTrending();
        setOnClickBackIcon();
        SoLuongShoppingCart();
        SearchTrending();
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Searching.this, ShoppingCart.class );
                startActivity(intent);
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
    private void SearchTrending(){
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                productCardAdapter.getFilter().filter(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                productCardAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
    private void setOnClickBackIcon() {
        backICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setRcvProductTrending() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rcvProductTrending.setLayoutManager(gridLayoutManager);
        firebaseFirestore.collection("PRODUCT")
                .whereEqualTo("state", "Inventory")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        mTrendingCard = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            if (documentSnapshot.exists()) {
                                String masp = documentSnapshot.getString("productId");
                                String name = documentSnapshot.getString("productName");
                                int price = Objects.requireNonNull(documentSnapshot.getLong("productPrice")).intValue();
                                List<String> Anh = (List<String>) documentSnapshot.get("imageUrl");
                                mTrendingCard.add(new ProductCard(Anh.get(0), name, price, masp));
                            }
                        }
                        productCardAdapter.setData(mTrendingCard);
                        productCardAdapter.setOnItemClick(new ProductCardAdapter.OnItemClick() {
                            @Override
                            public void onButtonItemClick(int position) {
                                Intent t = new Intent(Searching.this, DetailProduct.class);
                                t.putExtra("productId", mTrendingCard.get(position).getProductId());
                                startActivity(t);
                            }
                        });
                        rcvProductTrending.setAdapter(productCardAdapter);
                    }
                });
    }
    private void SoLuongShoppingCart(){
        firebaseFirestore.collection("CART")
                .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @OptIn(markerClass = ExperimentalBadgeUtils.class)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        dataGiohang = new ArrayList<>();
                        for(DocumentSnapshot doc: value.getDocuments()){
                            if(doc.exists()){
                                String ma = doc.getString("cartId");
                                dataGiohang.add(ma);
                            }
                        }
                        BadgeDrawable badgeDrawable = BadgeDrawable.create(Searching.this);
                        badgeDrawable.setNumber(dataGiohang.size());

                        BadgeUtils.attachBadgeDrawable(badgeDrawable, shoppingCart, null);
                    }
                });

    }

    @Override
    public Filter getFilter() {
        return null;
    }
}