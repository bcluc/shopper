package com.example.shopper.customerview.navigation.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopper.R;
import com.example.shopper.customerview.home.category.activity.CategoriesDetail;
import com.example.shopper.customerview.home.category.adapter.CategoriesAdapter;
import com.example.shopper.customerview.home.category.model.Categories;
import com.example.shopper.customerview.itf.IClickItemProductListener;
import com.example.shopper.customerview.navigation.activity.BottomNavigationCustomerActivity;
import com.example.shopper.customerview.home.product.adapter.ProductAdapter;
import com.example.shopper.customerview.home.product.model.Product;
import com.example.shopper.customerview.notification.adapter.ViewPagerPromotionAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#} factory method to
 * create an instance of this fragment.
 */
@OptIn(markerClass = com.google.android.material.badge.ExperimentalBadgeUtils.class)
public class HomeFragment extends Fragment {
    private List<String> cartData;
    private ViewPager2 vpBanner;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private BottomNavigationCustomerActivity bottomNavigationCustomActivity;
    private RecyclerView rcvProduct;
    private List<Product> listProduct;
    private ProductAdapter productAdapter;
    private RecyclerView rcvCategories;
    private List<Categories> listCategories;
    private CategoriesAdapter categoriesAdapter;
    private TextView txtTrendingExpand, txtOrderExpand;
    private Button editSearch;
    private ImageView chatBtn;
    private ImageView shoppingCart;
    private List<String> imagePro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bottomNavigationCustomActivity = (BottomNavigationCustomerActivity) getActivity();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        vpBanner = view.findViewById(R.id.view_title_img);
        rcvProduct = view.findViewById(R.id.rcv_product);
        rcvCategories = view.findViewById(R.id.rcv_categories);
        txtTrendingExpand = view.findViewById(R.id.txt_trending_expand);
        editSearch = view.findViewById(R.id.btn_edit_search);
        chatBtn = view.findViewById(R.id.btn_chat);
        shoppingCart = view.findViewById(R.id.btn_shopping_cart);
        txtOrderExpand = view.findViewById(R.id.txt_order_cart_expand);

        setDataRcvProduct();
        setDataRcvCategories();
        setOnClickTxtSeeall();
        setOnClickEditSearch();
        setOnCLickChatbtn();
        getDataPromotion();
        numShoppingCart();
        setOnClickTxtOrder();
        setOnClickShoppingCart();
        // Inflate the layout for this fragment
        return view;
    }

    private void setOnClickTxtOrder() {
        txtOrderExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoSearchingActivity();
            }
        });
    }

    private void setOnClickShoppingCart() {
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoDetail();
            }
        });
    }

    private void setOnCLickChatbtn() {
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoMessageActivity();
            }
        });
    }

    private void setOnClickEditSearch() {
        listProduct = new ArrayList<>();
        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoSearchingActivity();
            }
        });
    }

    private void setOnClickTxtSeeall() {
        txtTrendingExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationCustomActivity.gotoTrendingActivity();
            }
        });
    }

    private void setDataRcvProduct() {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        rcvProduct.setLayoutManager(linearLayoutManager);

        productAdapter = new ProductAdapter();
        firebaseFirestore.collection("PRODUCT")
                .whereEqualTo("trending", true)
                .whereEqualTo("state", "Inventory")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        listProduct = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            String masp = documentSnapshot.getString("productId");
                            String name = documentSnapshot.getString("productName");
                            Long giaSP = documentSnapshot.getLong("productPrice");
                            List<String> Anh = (List<String>) documentSnapshot.get("image");
                            listProduct.add(new Product(Anh.get(0), name, masp, giaSP));
                        }
                        productAdapter.setData(listProduct, new IClickItemProductListener() {
                            @Override
                            public void onClickItemProduct(Product product) {
                                onClickGoToDetailProduct(product);
                            }
                        });
                        rcvProduct.setAdapter(productAdapter);
                    }
                });
        //Lay Du Lieu San Pham
    }

    private void onClickGoToDetailProduct(Product product) {
        bottomNavigationCustomActivity.gotoDetailProduct(product);
    }

    private void getDataPromotion() {
        firebaseFirestore.collection("PROMOTION")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("Error", "listen:error", error);
                            return;
                        }
                        imagePro = new ArrayList<>();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            if (doc.exists()) {
                                String image = doc.getString("notifyImg");
                                //HinhAnhTB
                                //Log.d("Ma Khuyen Mai", image);
                                imagePro.add(image);
                            }
                        }

                        ViewPagerPromotionAdapter imageAdapter = new ViewPagerPromotionAdapter(getContext());
                        imageAdapter.setData(imagePro);
                        vpBanner.setAdapter(imageAdapter);

//                        TimerTask autoScrollTask = new AutoScrollTask(viewPager2);
//                        Timer timer = new Timer();
//                        timer.schedule(autoScrollTask, 2000, 2000);
                    }
                });
    }

    private void setDataRcvCategories() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rcvCategories.setLayoutManager(linearLayoutManager);
        categoriesAdapter = new CategoriesAdapter();
        try {
            firebaseFirestore.collection("CATEGORY")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.w("Error", "listen:error", error);
                                return;
                            }
                            listCategories = new ArrayList<>();
                            for (DocumentSnapshot document : value.getDocuments()) {
                                if (document.exists()) {
                                    String categoryName = document.getString("categoryName");
                                    String categoryImg = document.getString("categoryImg");
                                    String categoryId = document.getString("categoryId");
                                    listCategories.add(new Categories(categoryName, categoryImg, categoryId));
                                } else {
                                    Log.d("Error", "No such document");
                                }
                            }
                            categoriesAdapter.setData(listCategories);
                            categoriesAdapter.setOnItemClick(new CategoriesAdapter.OnItemClick() {
                                @Override
                                public void onButtonItemClick(int position) {
                                    Intent t = new Intent(bottomNavigationCustomActivity, CategoriesDetail.class);
                                    t.putExtra("categoryId", listCategories.get(position).getCategoryId());
                                    t.putExtra("categoryName", listCategories.get(position).getCategoryName());
                                    startActivity(t);
                                }
                            });
                            rcvCategories.setAdapter(categoriesAdapter);
                        }
                    });
        } catch (Exception ex) {

        }

    }

    private void numShoppingCart() {
        try {
            firebaseFirestore.collection("CART")
                    .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()) //maND
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.w("Error", "listen:error", error);
                                return;
                            }
                            cartData = new ArrayList<>();
                            for (DocumentSnapshot doc : value.getDocuments()) {
                                if (doc.exists()) {
                                    String ma = doc.getString("cartId");
                                    cartData.add(ma);
                                }
                            }
                            if (isAdded()) {
                                BadgeDrawable badgeDrawable = BadgeDrawable.create(requireContext());
                                badgeDrawable.setNumber(cartData.size());

                                BadgeUtils.attachBadgeDrawable(badgeDrawable, shoppingCart, null);
                            }
                        }
                    });
        } catch (Exception ex) {

        }

    }
}
