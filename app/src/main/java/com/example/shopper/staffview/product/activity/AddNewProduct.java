package com.example.shopper.staffview.product.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.category.adapter.MyCategoryListAdapter;
import com.example.shopper.staffview.category.model.MyCategory;
import com.example.shopper.staffview.product.adapter.MyColorAdapter;
import com.example.shopper.staffview.product.adapter.MySizeAdapter;
import com.example.shopper.staffview.product.model.MyColor;
import com.example.shopper.staffview.product.model.MySize;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewProduct extends AppCompatActivity {
    private List<MySize> sizeList;
    private List<MyCategory> myCategoryItemList;
    private List<MyColor> colorList;
    private MySizeAdapter sizeAdapter;
    private MyCategoryListAdapter myCategoryListAdapter;
    private RecyclerView recyclerView_size;
    private RecyclerView recyclerView_color;
    private RecyclerView recyclerView_category;
    private MyColorAdapter colorAdapter;
    private FirebaseFirestore db_size, db_color, db_category;

    // XỬ LÝ ADD SẢN PHẨM MỚI
    private ImageView imageView;
    private List<Uri> selectedImageUris;
    private Uri selectedImageUri; // URI của ảnh đã chọn
    private Button btn_addnew;
    private ImageButton button_back;
    private EditText productName, decriptions, price, delivery_fee, amount;
    private CheckBox check_color, check_size, check_category;
    private static final int REQUEST_IMAGE_PICK = 1;
    private FirebaseFirestore firestore;
    private ImageView btn_add_new_color;
    private StorageReference storageReference;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_product);
        btn_add_new_color = findViewById(R.id.img_add_new_color);
        btn_add_new_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewProduct.this, AddNewColor.class);
                startActivity(intent);
            }
        });
        db_size = FirebaseFirestore.getInstance();
        db_color = FirebaseFirestore.getInstance();
        db_category = FirebaseFirestore.getInstance();

        recyclerView_size = findViewById(R.id.RCV_size_picker);
        recyclerView_size.setLayoutManager(new GridLayoutManager(this, 3));

        recyclerView_color = findViewById(R.id.RCV_colorPicker);
        recyclerView_color.setLayoutManager(new GridLayoutManager(this, 1));

        recyclerView_category = findViewById(R.id.RCV_category_picker);
        recyclerView_category.setLayoutManager(new GridLayoutManager(this, 1));

        colorList = new ArrayList<>();
        sizeList = new ArrayList<>();
        myCategoryItemList = new ArrayList<>();

        sizeAdapter = new MySizeAdapter(sizeList);
        colorAdapter = new MyColorAdapter(colorList);
        myCategoryListAdapter = new MyCategoryListAdapter(myCategoryItemList);

        recyclerView_size.setAdapter(sizeAdapter);
        recyclerView_color.setAdapter(colorAdapter);
        recyclerView_category.setAdapter(myCategoryListAdapter);

        // Thay thế đoạn này bằng cách lấy danh sách MySize từ Firestore thông qua MaDM

        getSizeListFromFirestore();
        getColorListFromFirestore();
        getCategoryListFromFirestore();
        // CODE CHÍNH
        selectedImageUris = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        price = findViewById(R.id.prices);
        decriptions = findViewById(R.id.decriptions);
        delivery_fee = findViewById(R.id.delivery_fee);
        productName = findViewById(R.id.editTextCategoryName);
        amount = findViewById(R.id.soluong);
        btn_addnew = findViewById(R.id.btn_add_new);
        button_back = findViewById(R.id.btn_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewProduct.this, MyProduct.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.image_add_product_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        btn_addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy các giá trị từ các trường nhập liệu
                String TenSP = productName.getText().toString().trim();
                String MoTaSP = decriptions.getText().toString().trim();
                String GiaSPString = price.getText().toString().trim();
                String PhiVanChuyenString = delivery_fee.getText().toString().trim();
                String SoLuongSPString = amount.getText().toString().trim();

                // Kiểm tra các trường thông tin có hợp lệ hay không
                if (selectedImageUris == null || TenSP.isEmpty() || MoTaSP.isEmpty() || GiaSPString.isEmpty() || PhiVanChuyenString.isEmpty() || SoLuongSPString.isEmpty()) {
                    Toast.makeText(AddNewProduct.this, "Please fill out all information", Toast.LENGTH_SHORT).show();
                    return;
                }
                long SoLuongSP = Long.parseLong(SoLuongSPString);
                long GiaSP = Long.parseLong(GiaSPString);
                long PhiVanChuyen = Long.parseLong(PhiVanChuyenString);

                long SoLuongDaBan = 0;
                long SoLuongConLai = SoLuongSP - SoLuongDaBan;
                long SoLuongYeuThich = 0;
                String TrangThai = "Inventory";
                Boolean Trending = false;
                // Tạo một DocumentReference cho sản phẩm mới trong collection "PRODUCT"
                DocumentReference productRef = firestore.collection("PRODUCT").document();

                // Tạo một HashMap để lưu các thuộc tính của sản phẩm
                Map<String, Object> product = new HashMap<>();
                // Lấy ID của document và gán cho MaSP
                String MaSP = productRef.getId();
                product.put("productId", MaSP);
                product.put("productName", TenSP);
                product.put("description", MoTaSP);
                product.put("productPrice", GiaSP);
                product.put("deliveryFee", PhiVanChuyen);
                product.put("quanity", SoLuongSP);
                product.put("warehouse", SoLuongConLai);
                product.put("sold", SoLuongDaBan);
                product.put("love", SoLuongYeuThich);
                product.put("state", TrangThai);
                product.put("trending", Trending);
                // Thêm hình ảnh vào storage và lấy URL
                uploadImagesToStorage(selectedImageUris, new OnImageUploadListener() {
                    @Override
                    public void onSuccess(List<String> imageUrls) {
                        // Thêm URL của hình ảnh vào thuộc tính của sản phẩm
                        product.put("imageUrl", imageUrls);
                        // Thêm màu sắc được chọn vào thuộc tính của sản phẩm
                        List<String> selectedColors = colorAdapter.getSelectedColors();
                        product.put("productColor", selectedColors);

                        // Thêm size được chọn vào thuộc tính của sản phẩm
                        List<String> selectedSizes = sizeAdapter.getSelectedSizes();
                        product.put("productSize", selectedSizes);

                        // Lấy danh mục được chọn
                        String selectedCategory = myCategoryListAdapter.getSelectedCategory();
                        product.put("categoryId", selectedCategory);

                        // Thêm sản phẩm vào Firestore
                        productRef.set(product)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        increaseCategoryProductCount(selectedCategory);
                                        Toast.makeText(AddNewProduct.this, "Add product successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddNewProduct.this, MyProduct.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddNewProduct.this, "Fail to add product", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(AddNewProduct.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


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

    public interface OnImageUploadListener {
        void onSuccess(List<String> imageUrls);

        void onFailure(String errorMessage);
    }

    private void uploadImagesToStorage(List<Uri> imageUris, OnImageUploadListener listener) {
        List<String> imageUrls = new ArrayList<>();
        int totalImages = imageUris.size();
        final int[] uploadedCount = {0};

        for (Uri imageUri : imageUris) {
            // Tạo một StorageReference cho hình ảnh trong Firebase Storage
            StorageReference imageRef = storageReference.child("productImages").child(imageUri.getLastPathSegment());

            // Tải lên hình ảnh lên Storage
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Lấy URL của hình ảnh đã tải lên
                            imageRef.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            imageUrls.add(imageUrl);
                                            uploadedCount[0]++;

                                            // Kiểm tra nếu đã tải lên đủ số lượng hình ảnh
                                            if (uploadedCount[0] == totalImages) {
                                                listener.onSuccess(imageUrls);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            listener.onFailure("Fail to fetch image url");
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onFailure("Fail to upload image");
                        }
                    });
        }
    }


    private void getCategoryListFromFirestore() {
        db_category.collection("CATEGORY")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        myCategoryItemList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String TenDM = document.getString("categoryName");
                            int SoLuongSP = Math.toIntExact(document.getLong("quanity"));
                            String MaDM = document.getId();
                            boolean isSelected = false;
                            MyCategory myCategoryItem = new MyCategory(TenDM, isSelected, MaDM, SoLuongSP);
                            myCategoryItemList.add(myCategoryItem);
                        }
                        myCategoryListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AddNewProduct.this, "Failed to fetch category list", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void increaseCategoryProductCount(String selectedCategory) {
        DocumentReference categoryRef = db_category.collection("CATEGORY").document(selectedCategory);
        categoryRef.update("quanity", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Số lượng sản phẩm trong danh mục đã được tăng lên 1
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Có lỗi xảy ra khi cập nhật số lượng sản phẩm trong danh mục
                    }
                });
    }

    private void getColorListFromFirestore() {
        db_color.collection("COLOR")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        colorList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String TenMau = document.getString("colorName");
                            String colorCode = document.getString("colorCode");
                            String MaMau = document.getId();
                            boolean checked = false;

                            MyColor myColor = new MyColor(TenMau, colorCode, MaMau, checked);
                            colorList.add(myColor);
                        }
                        colorAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AddNewProduct.this, "Failed to fetch color list", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getSizeListFromFirestore() {
        db_size.collection("SIZE")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sizeList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String sizeName = document.getString("size");
                            String maSize = document.getId();
                            boolean checked = false;

                            MySize mySize = new MySize(sizeName, maSize, checked);
                            sizeList.add(mySize);
                        }
                        sizeAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AddNewProduct.this, "Failed to fetch size list", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Xóa danh sách hình ảnh đã chọn trước đó
            selectedImageUris.clear();

            // Lấy danh sách URI của hình ảnh đã chọn
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                int count = clipData.getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                selectedImageUris.add(imageUri);
            }

            // Hiển thị hình ảnh đầu tiên
            if (!selectedImageUris.isEmpty()) {
                Uri firstImageUri = selectedImageUris.get(0);
                imageView.setImageURI(firstImageUri);
            }
        }
    }
    // Phần khai báo và các phương thức khác trong activity_add_new_product.java không thay đổi

}
