package com.example.shopper.customerview.navigation.fragment;

import static android.content.ContentValues.TAG;
import static android.content.Context.SEARCH_SERVICE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.product.adapter.ProductCardAdapter;
import com.example.shopper.customerview.home.product.model.ProductCard;
import com.example.shopper.customerview.navigation.activity.BottomNavigationCustomerActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowFragment#} factory method to
 * create an instance of this fragment.
 */
public class FollowFragment extends Fragment implements Filterable {
    ProgressDialog progressDialog;
    private List<String> dataGiohang;
    private ImageView shoppingCart, chatBtn;
    private RecyclerView rcvFollow;
    private TextView btnEmpty;
    private SearchView searchView;
    private RelativeLayout layoutEmpty;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProductCardAdapter productAdapter;
    private BottomNavigationCustomerActivity bottomNavigationCustomActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        rcvFollow = view.findViewById(R.id.rcvFollow);
        btnEmpty = view.findViewById(R.id.btnEmpty);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        shoppingCart = view.findViewById(R.id.ShoppingCart);
        searchView = view.findViewById(R.id.searchView);
        chatBtn = view.findViewById(R.id.chatBtn);
        bottomNavigationCustomActivity = (BottomNavigationCustomerActivity) getActivity();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvFollow.setLayoutManager(gridLayoutManager);

        setOnCLickChatbtn();
        getFirebase();
        setFollow();
        SoLuongShoppingCart();
        setOnClickShoppingCart();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
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

        btnEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoSearchingActivity();
            }
        });

        return view;
    }

    private void getFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

    }

    private void setOnCLickChatbtn() {
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoMessageActivity();
            }
        });
    }


    private void onClickGoToDetailProduct(ProductCard product) {
        bottomNavigationCustomActivity.gotoDetailProduct(product);
    }

    private void setOnClickShoppingCart() {
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoDetail();
            }
        });
    }

    private void setFollow() {
        productAdapter = new ProductCardAdapter();

        List<ProductCard> products = new ArrayList<>();

        firebaseFirestore.collection("FAVORITE")
                .whereEqualTo("userId", firebaseUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        products.clear();
                        if (value.size() == 0) {
                            layoutEmpty.setVisibility(View.VISIBLE);
                        } else {
                            layoutEmpty.setVisibility(View.GONE);
                            for (QueryDocumentSnapshot doc : value) {
                                String maSP = doc.getString("productId");
                                final DocumentReference docRef = firebaseFirestore.collection("PRODUCT").document(maSP);
                                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                        @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            Log.w(TAG, "Listen failed.", e);
                                            return;
                                        }

                                        if (snapshot != null && snapshot.exists()) {
                                            Log.d(TAG, "Current data: " + snapshot.getData());
                                            String masp = snapshot.getString("productId");
                                            String name = snapshot.getString("productName");
                                            Long giaSP = snapshot.getLong("productPrice");
                                            List<String> Anh = (List<String>) snapshot.get("imageUrl");
                                            products.add(new ProductCard(Anh.get(0), name, giaSP.intValue(), maSP));

                                            productAdapter.setData(products);
                                            productAdapter.setOnItemClick(new ProductCardAdapter.OnItemClick() {
                                                @Override
                                                public void onButtonItemClick(int position) {
                                                    onClickGoToDetailProduct(products.get(position));
                                                }
                                            });
                                            rcvFollow.setAdapter(productAdapter);
                                        } else {

                                        }
                                    }
                                });
                            }

                        }
                    }
                });
        progressDialog.dismiss();
    }

    private void SoLuongShoppingCart() {
        try {
            firebaseFirestore.collection("CART")
                    .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @OptIn(markerClass = ExperimentalBadgeUtils.class)
                        @Override
                        public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.w("Error", "listen:error", error);
                                return;
                            }
                            dataGiohang = new ArrayList<>();
                            for (DocumentSnapshot doc : value.getDocuments()) {
                                if (doc.exists()) {
                                    String ma = doc.getString("cartId");
                                    dataGiohang.add(ma);
                                }
                            }
                            if (isAdded()) {
                                BadgeDrawable badgeDrawable = BadgeDrawable.create(requireContext());
                                badgeDrawable.setNumber(dataGiohang.size());

                                BadgeUtils.attachBadgeDrawable(badgeDrawable, shoppingCart, null);
                            }

                        }
                    });

        } catch (Exception ex) {

        }

    }

    @Override
    public Filter getFilter() {
        return null;
    }
}