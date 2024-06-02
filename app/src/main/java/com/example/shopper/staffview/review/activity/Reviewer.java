package com.example.shopper.staffview.review.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopper.R;
import com.example.shopper.staffview.review.adapter.ReviewAdapter;
import com.example.shopper.staffview.review.model.Review;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Reviewer extends AppCompatActivity {

    private TextView TongSoDanhGia, TrungBinh;
    private LinearLayout btn_addcomment;
    private ImageView backIcon;
    private RatingBar Rating;
    private String maSP;
    private Button btn_addreview, btnaddReviewByUser;
    private CardView cardAddReview;
    private TextView txtExit;
    private ImageView imageReview;
    private String imagePath;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser firebaseUser;
    private String Uid;
    private ImageView imageAvatar;
    private TextView txtNameUser;

    private String ImageUrl;
    private final ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if (o == null) {
                Toast.makeText(Reviewer.this, "No image was selected.", Toast.LENGTH_SHORT).show();
            } else {
                UploadImage(o);
            }
        }
    });
    private RatingBar ratingStarUser;
    private EditText editReview;
    private ReviewAdapter reviewAdapter;
    private RecyclerView data_recyclerview;
    private String avatarUri;
    private String nameUser;
    private Boolean allowReview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewer);
        TongSoDanhGia = findViewById(R.id.txt_tongsodanhgia);
        backIcon = findViewById(R.id.backIcon);

        // btn_addcomment = findViewById(R.id.btn_;
        TrungBinh = findViewById(R.id.txt_trungbinh);
        Rating = findViewById(R.id.ratingBar);
        btn_addreview = findViewById(R.id.btn_addreview);
        cardAddReview = findViewById(R.id.cardAddReview);
        txtExit = findViewById(R.id.txtExit);
        imageReview = findViewById(R.id.imageReview);
        imageAvatar = findViewById(R.id.imageAvatar);
        txtNameUser = findViewById(R.id.txtNameUser);
        btnaddReviewByUser = findViewById(R.id.btnaddReviewByUser);
        ratingStarUser = findViewById(R.id.ratingStarUser);
        editReview = findViewById(R.id.editReview);

        data_recyclerview = findViewById(R.id.data_recyclerview);


        Intent intent = getIntent();
        maSP = intent.getStringExtra("productId");


        setBtnAddReview();
        setImagePicker();


        setFirebaseUser();

        fetchDataReviewSanPham();
        checkAllowReview();
        setBackIcon();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        data_recyclerview.setLayoutManager(linearLayoutManager);


    }

    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference = FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status", "Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference = FirebaseFirestore.getInstance().
                collection("USERS").document(FirebaseAuth.getInstance().getUid());
        documentReference.update("status", "Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

    }

    private void setBackIcon() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkAllowReview() {
        firebaseFirestore.collection("ORDER")
                .whereEqualTo("userId", firebaseUser.getUid())

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<String> dataMaDH = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            Log.d("Test.", "111");
                            dataMaDH.add(doc.getString("orderId"));
                        }

                        for (String maDH : dataMaDH) {


                            firebaseFirestore.collection("ORDERACT")
                                    .whereEqualTo("orderId", maDH)
                                    .whereEqualTo("productId", maSP)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value,
                                                            @Nullable FirebaseFirestoreException e) {

                                            if (value.size() > 0) {
                                                btn_addreview.setVisibility(View.VISIBLE);
                                                return;
                                            }
                                        }
                                    });
                        }

                    }
                });


    }


    private void fetchDataReviewSanPham() {

        reviewAdapter = new ReviewAdapter(getApplicationContext());


        firebaseFirestore.collection("REVIEW")
                .whereEqualTo("productId", maSP)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Listen failed.", e);
                            return;
                        }

                        if (value.size() == 0) {
                            TongSoDanhGia.setText("0");
                            TrungBinh.setText("5.0");
                            Rating.setRating(5);
                            return;
                        }

                        List<Review> dataReview = new ArrayList<>();
                        for (DocumentSnapshot doc : value.getDocuments()) {

                            String avatar = doc.getString("avatar");
                            String name = doc.getString("fullName");
                            Timestamp timestamp = doc.getTimestamp("reviewDate");

                            Date date = timestamp.toDate();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String time = sdf.format(date);

                            String content = doc.getString("content");
                            float rating = doc.getDouble("rating").floatValue();
                            String image = doc.getString("reviewImage");


                            Review review = new Review(avatar, name, time, rating, content, image);
                            Log.d("time", review.getTime());
                            dataReview.add(review);

                        }


                        TongSoDanhGia.setText(String.valueOf(dataReview.size()));

                        float sum = 0;

                        for (Review review : dataReview) {
                            sum += review.getRating();
                        }

                        TrungBinh.setText(String.valueOf(sum / dataReview.size()));
                        Rating.setRating(sum / dataReview.size());

                        reviewAdapter.setData(dataReview);
                        data_recyclerview.setAdapter(reviewAdapter);

                    }
                });
    }


    private void setFirebaseUser() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Uid = firebaseUser.getUid();

        final DocumentReference docRef = firebaseFirestore.collection("USERS").document(Uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    avatarUri = snapshot.getString("avatar");
                    nameUser = snapshot.getString("fullName");

                    Picasso.get().load(avatarUri).into(imageAvatar);
                    txtNameUser.setText(nameUser);

                } else {
                    Log.d("Current data", "null");
                }
            }
        });

    }

    private void UploadImage(Uri imageUri) {
        StorageReference storageRef = firebaseStorage.getReference();

        String imagePath = "reviewImages/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(imagePath);


        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                Log.d("ImageURI: ", downloadUri.toString());
                                ImageUrl = downloadUri.toString();
                                Glide.with(Reviewer.this).load(imageUri).into(imageReview);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Tải ảnh thất bại
                    }
                });

    }

    private void setImagePicker() {
        imageReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
//            // Lấy đường dẫn hình ảnh được chọn
//            imagePath = data.getStringExtra(ImagePicker.EXTRA_FILE_PATH);
//            Glide.with(this).load(imagePath).into(imageReview);
//
//            UploadImage(data.getData());
//
//
//            // Sử dụng đường dẫn hình ảnh để hiển thị hoặc xử lý theo nhu cầu của bạn
//        }
//    }

    private void setBtnAddReview() {
        cardAddReview.setVisibility(View.GONE);
        btn_addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", "1111");
                if (cardAddReview.getVisibility() == View.GONE) {
                    cardAddReview.setVisibility(View.VISIBLE);
                }

            }
        });

        txtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardAddReview.getVisibility() == View.VISIBLE) {
                    cardAddReview.setVisibility(View.GONE);
                }
            }
        });

        btnaddReviewByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReview();

            }


        });
    }

    private synchronized void setReview() {
        float currentRating = ratingStarUser.getRating();
        Timestamp currentTime = Timestamp.now();

        try {


            Map<String, Object> data = new HashMap<>();
            data.put("reviewImage", ImageUrl);
            data.put("userId", Uid);
            data.put("productId", maSP);
            data.put("content", editReview.getText().toString());
            data.put("reviewDate", currentTime);
            data.put("rating", currentRating);
            data.put("avatar", avatarUri);
            data.put("fullName", nameUser);


            firebaseFirestore.collection("REVIEW")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("DocumentSnapshot written with ID: ", documentReference.getId());
                            Toast.makeText(Reviewer.this, "You have successfully reviewed!", Toast.LENGTH_SHORT).show();
                            cardAddReview.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error adding document", e);
                        }
                    });


        } catch (Exception e) {
            // Xử lý lỗi khi không thể lấy được imageURL
            e.printStackTrace();
            Log.d("Error", e.toString());
        }


    }
}