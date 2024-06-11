package com.example.shopper.customerview.home.product.activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shopper.R;
import com.example.shopper.customerview.home.product.model.Product;
import com.example.shopper.customerview.home.shoppingcart.ShoppingCart;
import com.example.shopper.customerview.home.shoppingcart.billing.BuyNow;
import com.example.shopper.customerview.navigation.activity.BottomNavigationCustomerActivity;
import com.example.shopper.customerview.util.AutoScrollTask;
import com.example.shopper.customerview.util.adapters.ViewPagerImageAdapter;
import com.example.shopper.customerview.util.color.adapter.ColorsAdapter;
import com.example.shopper.customerview.util.color.model.Colors;
import com.example.shopper.customerview.util.size.adapter.SizeAdapter;
import com.example.shopper.staffview.review.activity.Reviewer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DetailProduct extends AppCompatActivity {
    private String dataColor, dataSize;

    private List<String> dataGiohang;

    private ImageView backIcon, shoppingCart;
    private Product product;
    private TextView txtProductNameDetail;
    private TextView txtPriceProductDetail;
    private String maSP;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView revColor;
    private ColorsAdapter colorAdapter;
    private List<String> colors;
    private RecyclerView rcvSize;
    private SizeAdapter sizeAdapter;
    private List<String> sizes;
    private TextView btnDown, btnUp, txtSoLuong;

    private TextView txtMoTaSP;
    private ImageView btnAnHienMoTa;
    private ViewPager2 viewpagerImage;
    private List<String> imageUrls;
    private FirebaseAuth firebaseAuth;
    private TextView btnAddToCard;
    private TextView btnBuyNow;
    private FirebaseUser firebaseUser;
    private Long giaSP;
    private AlertDialog.Builder builder;
    private TextView txtSeeReview;
    private String idGioHang = null;
    private List<Colors> mauSacs;
    private ImageView heartIcon;
    private Boolean yeuThich;
    private String maYT;
    private RatingBar starRating;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        txtProductNameDetail = findViewById(R.id.txtProductNameDetail);
        txtPriceProductDetail = findViewById(R.id.txtPriceProductDetail);
        revColor = findViewById(R.id.rcvCorlor);
        rcvSize = findViewById(R.id.rcvSize);
        btnDown = findViewById(R.id.btnDown);
        btnUp = findViewById(R.id.btnUp);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        txtMoTaSP = findViewById(R.id.txtMoTaSP);
        btnAnHienMoTa = findViewById(R.id.btnAnHienMoTa);
        viewpagerImage = findViewById(R.id.viewpagerImage);
        btnAddToCard = findViewById(R.id.btnAddToCard);
        txtSeeReview = findViewById(R.id.txtSeeReview);
        heartIcon = findViewById(R.id.heartIcon);
        shoppingCart = findViewById(R.id.cartIcon);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        starRating = findViewById(R.id.starRating);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.d("User", firebaseUser.getUid());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.HORIZONTAL, false);
        revColor.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManagerSize = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.HORIZONTAL, false);
        rcvSize.setLayoutManager(linearLayoutManagerSize);

        firebaseFirestore = FirebaseFirestore.getInstance();

        backIcon = findViewById(R.id.backIcon);

        Intent intent = getIntent();
        maSP = intent.getStringExtra("productId");


        setBtnUpDown();
        getProduct(maSP);
        setMoTaSP();
        setAddToCard();
        setBuyNow();
        setDialog();
        setBtnSeeReview();
        getYeuThich();
        setYeuThich();
        SoLuongShoppingCart();
        getStarRating();
        setOnClickBackICon();

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProduct.this, ShoppingCart.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status", "Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status", "Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    private void getStarRating() {
        firebaseFirestore.collection("REVIEW").whereEqualTo("productId", maSP).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (value.size() == 0) {
                    starRating.setRating(5);
                    return;
                }

                Long sum = Long.valueOf(0);
                for (QueryDocumentSnapshot doc : value) {
                    sum += doc.getLong("rating");
                }

                starRating.setRating(sum / value.size());
            }
        });

    }

    private void setBuyNow() {
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((dataColor.isEmpty() || dataColor == null) || (dataSize.isEmpty() || dataSize == null))
                        return;
                    else {
                        Map<String, Object> data = new HashMap<>();
                        data.put("userId", firebaseUser.getUid());
                        data.put("productColor", dataColor);
                        data.put("productSize", dataSize);
                        data.put("imageUrl", imageUrls);
                        data.put("productName", txtProductNameDetail.getText());
                        data.put("quanity", Integer.valueOf((String) txtSoLuong.getText()));
                        data.put("productPrice", giaSP);
                        data.put("totalPrice", Integer.valueOf((String) txtPriceProductDetail.getText()));
                        data.put("productId", maSP);

                        firebaseFirestore.collection("CART").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                                DocumentReference updateRef = firebaseFirestore.collection("CART").document(documentReference.getId());
                                updateRef.update("cartId", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");

                                        String[] listmaGH = new String[1];
                                        listmaGH[0] = documentReference.getId();
                                        Intent t = new Intent(DetailProduct.this, BuyNow.class);
                                        t.putExtra("cartIdList", listmaGH);
                                        startActivity(t);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });

                                idGioHang = documentReference.getId();
//                                    Toast.makeText(getApplicationContext(), "Add to cart successfully!", Toast.LENGTH_LONG);
//                                    Intent intent = new Intent(DetailProduct.this, BottomNavigationCustomActivity.class);
//                                    startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                    }
                } catch (Exception ex) {
                    Toast.makeText(DetailProduct.this, "Please choose all", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setYeuThich() {

        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yeuThich) {
                    Log.d("Heart", "1");
                    firebaseFirestore.collection("FAVORITE").document(maYT).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
                } else {
                    Log.d("Heart", "2");
                    Map<String, Object> data = new HashMap<>();
                    data.put("userId", firebaseUser.getUid());
                    data.put("productId", maSP);

                    firebaseFirestore.collection("FAVORITE").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            maYT = documentReference.getId();
                            DocumentReference updateRef = firebaseFirestore.collection("FAVORITE").document(documentReference.getId());
                            updateRef.update("favoriteId", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
                }
            }
        });
    }

    private void getYeuThich() {

        firebaseFirestore.collection("FAVORITE").whereEqualTo("userId", firebaseUser.getUid()).whereEqualTo("productId", maSP).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (value.size() > 0) {
                    heartIcon.setImageDrawable(getResources().getDrawable(R.drawable.heart_red));
                    yeuThich = true;
                    for (QueryDocumentSnapshot doc : value) {
                        maYT = doc.getString("favoriteId");
                    }

                } else {
                    heartIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                    yeuThich = false;
                }

            }
        });
    }

    private void setBtnSeeReview() {
        txtSeeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProduct.this, Reviewer.class);
                intent.putExtra("productId", maSP);
                startActivity(intent);
            }
        });
    }

    private void setDialog() {
        builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Title").setMessage("Dialog's content").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Xử lý sự kiện khi người dùng bấm nút đồng ý
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Xử lý sự kiện khi người dùng bấm nút hủy
            }
        });
    }


    private void setAddToCard() {

        btnAddToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((dataColor.isEmpty() || dataColor == null) || (dataSize.isEmpty() || dataSize == null))
                        return;
                    else {
                        Map<String, Object> data = new HashMap<>();
                        data.put("userId", firebaseUser.getUid());
                        data.put("productColor", dataColor);
                        data.put("productSize", dataSize);
                        data.put("imageUrl", imageUrls);
                        data.put("productName", txtProductNameDetail.getText());
                        data.put("quanity", Integer.valueOf((String) txtSoLuong.getText()));
                        data.put("productPrice", giaSP);
                        data.put("totalPrice", Integer.valueOf((String) txtPriceProductDetail.getText()));
                        data.put("productId", maSP);

                        firebaseFirestore.collection("CART").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                                DocumentReference updateRef = firebaseFirestore.collection("CART").document(documentReference.getId());
                                updateRef.update("cartId", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        Toast.makeText(getApplicationContext(), "Add to cart successfully!", Toast.LENGTH_LONG);
                                        Intent intent = new Intent(DetailProduct.this, BottomNavigationCustomerActivity.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });

                                idGioHang = documentReference.getId();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                    }
                } catch (Exception ex) {
                    Toast.makeText(DetailProduct.this, "Please choose all", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void setMoTaSP() {
        txtMoTaSP.setVisibility(View.GONE);

        btnAnHienMoTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtMoTaSP.getVisibility() == View.GONE) {
                    txtMoTaSP.setVisibility(View.VISIBLE);
                } else {
                    txtMoTaSP.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setBtnUpDown() {
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soLuong = Integer.parseInt((String) txtSoLuong.getText());
                if (soLuong > 1) {
                    soLuong--;
                    txtSoLuong.setText(String.valueOf(soLuong));
                    txtPriceProductDetail.setText(String.valueOf(soLuong * giaSP));
                }
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soLuong = Integer.parseInt((String) txtSoLuong.getText());
                soLuong++;
                txtSoLuong.setText(String.valueOf(soLuong));
                txtPriceProductDetail.setText(String.valueOf(soLuong * giaSP));
            }
        });
    }

    private void getProduct(String maSP) {


        colors = new ArrayList<>();

        sizeAdapter = new SizeAdapter();
        sizes = new ArrayList<>();

        imageUrls = new ArrayList<>();


        final DocumentReference docRef = firebaseFirestore.collection("PRODUCT").document(maSP);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    String tenSP = snapshot.getString("productName");
                    giaSP = snapshot.getLong("productPrice");

                    int soLuong = Integer.parseInt((String) txtSoLuong.getText());
                    txtPriceProductDetail.setText(String.valueOf(soLuong * giaSP));

                    String moTaSP = snapshot.getString("description");
                    txtProductNameDetail.setText(tenSP);
                    // txtPriceProductDetail.setText(String.valueOf(giaSP));

                    txtMoTaSP.setText(moTaSP);

                    imageUrls = (List<String>) snapshot.get("imageUrl");

                    ViewPagerImageAdapter imageAdapter = new ViewPagerImageAdapter(getApplicationContext(), imageUrls);
                    viewpagerImage.setAdapter(imageAdapter);

                    TimerTask autoScrollTask = new AutoScrollTask(viewpagerImage);
                    Timer timer = new Timer();
                    timer.schedule(autoScrollTask, 2000, 2000);

                    sizes = (List<String>) snapshot.get("productSize");
                    colors = (List<String>) snapshot.get("productColor");

                    getSize(sizes);
                    getMauSac(colors);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void getMauSac(List<String> colors) {
        mauSacs = new ArrayList<>();
        colorAdapter = new ColorsAdapter();

        Log.d("Mau", colors.get(0));

        for (String color : colors) {
            final DocumentReference docRef = firebaseFirestore.collection("COLOR").document(color);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        String maMau = (String) snapshot.get("colorCode");
                        String tenMau = (String) snapshot.get("colorName");
                        String maMauSac = (String) snapshot.get("colorId");
                        Colors mauSac = new Colors(maMauSac, maMau, tenMau);


                        mauSacs.add(mauSac);
                        colorAdapter.setData(mauSacs, DetailProduct.this);
                        revColor.setAdapter(colorAdapter);
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
    }

    private void getSize(List<String> sizes) {
        List<String> listSize = new ArrayList<>();
        sizeAdapter = new SizeAdapter();

        for (String size : sizes) {
            final DocumentReference docRef = firebaseFirestore.collection("SIZE").document(size);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());

                        String sizee = (String) snapshot.get("size");
                        listSize.add(sizee);

                        sizeAdapter.setData(listSize, DetailProduct.this);
                        rcvSize.setAdapter(sizeAdapter);
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
    }

    private void setOnClickBackICon() {

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void SoLuongShoppingCart() {
        firebaseFirestore.collection("CART").whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @OptIn(markerClass = ExperimentalBadgeUtils.class)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
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
                BadgeDrawable badgeDrawable = BadgeDrawable.create(DetailProduct.this);
                badgeDrawable.setNumber(dataGiohang.size());

                BadgeUtils.attachBadgeDrawable(badgeDrawable, shoppingCart, null);
            }
        });

    }

    public void onColorClick(String colorCode) {
        // Xử lý sự kiện khi màu được chọn
        // Dữ liệu màu tên và mã màu được truyền từ adapter qua activity
        dataColor = colorCode;
        Log.d("MyColor", dataColor);
    }

    public void onSizeClick(String size) {
        // Xử lý sự kiện khi màu được chọn
        // Dữ liệu màu tên và mã màu được truyền từ adapter qua activity
        dataSize = size;
        Log.d("Size", dataSize);
    }
}