package com.example.shopper.staffview.product.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.product.adapter.MyColorAdapter;
import com.example.shopper.staffview.product.adapter.ImageAdapter;
import com.example.shopper.staffview.product.adapter.MySizeAdapter;
import com.example.shopper.staffview.product.model.MyColor;
import com.example.shopper.staffview.product.model.Product;
import com.example.shopper.staffview.product.model.MySize;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class EditProductWithoutAmount extends AppCompatActivity {

    private ImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();
    private List<MyColor> allColors = new ArrayList<>();
    private List<String> selectedColors = new ArrayList<>();
    private List<MySize> allSize = new ArrayList<>();
    private List<String> selectedSizes = new ArrayList<>();
    private EditText name, description, price;
    private String MaSP;
    private RecyclerView recyclerView_color, recyclerView_size, recyclerView_image;
    private ImageView img_add;
    private ImageButton btn_back;
    private LinearLayoutManager layoutManager;
    private Button btn_update, btn_delete;
    private List<String> updatedImageUrls;
    private Uri imageUri;
    private ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if (o == null) {
                Toast.makeText(EditProductWithoutAmount.this, "No image was selected.", Toast.LENGTH_SHORT).show();
            } else {
                updateImageUri(o);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_without_amount);
        name = findViewById(R.id.edt_name);
        description = findViewById(R.id.edit_decription);
        price = findViewById(R.id.edit_price);
        Intent intent = getIntent();
        MaSP = intent.getStringExtra("productId");
        recyclerView_color = findViewById(R.id.RCV_color_edit);
        recyclerView_size = findViewById(R.id.RCV_size_edit);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_color.setLayoutManager(layoutManager);
        recyclerView_size.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView_image = findViewById(R.id.recycler_view_images);
        recyclerView_image.setLayoutManager(new GridLayoutManager(this, 3));
        img_add = findViewById(R.id.imgView_add_new_picture);
        btn_back = findViewById(R.id.imgbtn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_back = new Intent(EditProductWithoutAmount.this, MyProduct.class);
                startActivity(intent_back);
            }
        });
        // Step 1: Lấy danh sách các sản phẩm (SANPHAM) từ Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("PRODUCT");
        productsRef.whereEqualTo("productId", MaSP).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot productSnapshot) {
                List<Product> productList = productSnapshot.toObjects(Product.class);
                CollectionReference colorsRef = db.collection("COLOR");
                colorsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot colorSnapshot) {
                        allColors.clear();
                        // Duyệt qua danh sách sản phẩm
                        for (Product product : productList) {
                            List<String> currentColors = product.getProductColor();

                            // Duyệt qua danh sách màu sắc
                            for (DocumentSnapshot colorDoc : colorSnapshot) {
                                String colorId = colorDoc.getString("colorId");
                                String colorCode = colorDoc.getString("colorCode");
                                String colorName = colorDoc.getString("colorName");
                                boolean check = currentColors.contains(colorId);

                                MyColor color = new MyColor(colorName, colorCode, colorId, check);

                                allColors.add(color);
                            }
                        }

                        // Step 4: Hiển thị danh sách sản phẩm kết quả lên RecyclerView
                        MyColorAdapter colorAdapter = new MyColorAdapter(allColors);
                        recyclerView_color.setAdapter(colorAdapter);
                    }
                });
                CollectionReference sizeRef = db.collection("SIZE");
                sizeRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot colorSnapshot) {
                        allSize.clear();
                        // Duyệt qua danh sách sản phẩm
                        for (Product product : productList) {
                            List<String> currentSize = product.getProductSize();

                            // Duyệt qua danh sách màu sắc
                            for (DocumentSnapshot colorDoc : colorSnapshot) {
                                String sizeId = colorDoc.getString("sizeId");

                                String sizeName = colorDoc.getString("size");
                                boolean check = currentSize.contains(sizeId);

                                MySize size = new MySize(sizeName, sizeId, check);
                                allSize.add(size);
                            }
                        }

                        // Step 4: Hiển thị danh sách sản phẩm kết quả lên RecyclerView
                        MySizeAdapter sizeAdapter = new MySizeAdapter(allSize);
                        recyclerView_size.setAdapter(sizeAdapter);
                    }
                });
            }
        });

        FirebaseFirestore db_sanpham = FirebaseFirestore.getInstance();
        CollectionReference sanphamRef = db_sanpham.collection("PRODUCT");
        sanphamRef.whereEqualTo("productId", MaSP)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Xử lý lỗi nếu cần thiết
                        return;
                    }

                    for (DocumentSnapshot document : queryDocumentSnapshots) {

                        imageUrls = (List<String>) queryDocumentSnapshots.getDocuments().get(0).get("imageUrl");
                        imageAdapter = new ImageAdapter(imageUrls);

                        imageAdapter.setOnImageClickListener(new ImageAdapter.OnImageClickListener() {
                            @Override
                            public void onImageClick(int clickedPosition) {
                                // Xóa ảnh tại vị trí đã chọn khi nhấp vào ImageButton
                                if (clickedPosition >= 0 && clickedPosition < imageUrls.size()) {
                                    imageUrls.remove(clickedPosition);
                                    imageAdapter.notifyDataSetChanged();
                                }
                            }

                        });
                        img_add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Gọi hộp thoại chọn hình ảnh từ thư viện hoặc camera
                                launcher.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
                            }
                        });


                        String tenSP = document.getString("productName");
                        name.setText(tenSP);
                        String mota = document.getString("description");
                        description.setText(mota);
                        String giaSP = String.valueOf(document.getLong("productPrice"));
                        price.setText(giaSP);
                    }
                    recyclerView_image.setAdapter(imageAdapter);
                });


        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }
    private void deleteProduct() {
        // Get a reference to the document of the product to be deleted
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("PRODUCT");
        DocumentReference productRef = productsRef.document(MaSP);

        // Delete the product from Firestore
        productRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProductWithoutAmount.this, "Delete product success", Toast.LENGTH_SHORT).show();
                // Redirect the user to the MyProduct activity or any other desired activity
                Intent intent_done = new Intent(EditProductWithoutAmount.this, MyProduct.class);
                startActivity(intent_done);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProductWithoutAmount.this, "Delete product fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateImageUri(Uri o){
        Uri imageUri = o;
        if (imageUri != null) {
            imageUrls.add(imageUri.toString());
            imageAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(EditProductWithoutAmount.this, "Failed to get image URI.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProduct() {
        // Tải ảnh lên Firebase Storage và sau khi hoàn thành, tiến hành cập nhật thông tin sản phẩm
        if (imageUrls.isEmpty() || imageUrls.size() > 3) {
            Toast.makeText(this, "Please add image, not over 3 images", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadImagesAndSaveProduct();
    }
    private void uploadImagesAndSaveProduct() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("productImages");
        updatedImageUrls = new ArrayList<>();
        List<Task<Uri>> imageUploadTasks = new ArrayList<>();
        List<String> localImageUrls = new ArrayList<>();
        List<String> storageImageUrls = new ArrayList<>();

        for (String imageUrl : imageUrls) {
            if (imageUrl.contains("firebasestorage.googleapis.com")) {
                // Ảnh đã là URL Storage Firebase
                storageImageUrls.add(imageUrl);
            }
            else {
                // Ảnh là URL tệp cục bộ, vì vậy tải lên Firebase Storage
                localImageUrls.add(imageUrl);
                Uri imageUri = Uri.parse(imageUrl); // Thay đổi ở đây
                StorageReference imageRef = storageRef.child(imageUri.getLastPathSegment());
                UploadTask uploadTask = imageRef.putFile(imageUri);
                Task<Uri> downloadUrlTask = uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                });

                imageUploadTasks.add(downloadUrlTask);
            }
        }

        Tasks.whenAllComplete(imageUploadTasks)
                .addOnSuccessListener(taskSnapshots -> {
                    for (Task<Uri> downloadUrlTask : imageUploadTasks) {
                        if (downloadUrlTask.isSuccessful()) {
                            String imageUrlpiu = downloadUrlTask.getResult().toString();
                            updatedImageUrls.add(imageUrlpiu);
                            // Tùy chọn: Bạn cũng có thể loại bỏ URL ảnh cục bộ đã tải lên khỏi danh sách
                            localImageUrls.remove(imageUrlpiu);
                        }
                    }

                    // Gộp hai danh sách (URL Storage Firebase đã tải lên + URL Storage Firebase ban đầu)
                    updatedImageUrls.addAll(storageImageUrls);
                    // Bây giờ bạn có một danh sách hoàn chỉnh của URL ảnh sẵn sàng để thêm vào tài liệu

                    // Gọi hàm để cập nhật thông tin sản phẩm
                    updateProductInfo(updatedImageUrls);

                    Toast.makeText(getApplicationContext(), "Get URL success!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Fail to get URL!", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProductInfo(List<String> updatedImageUrls) {
        // Lấy thông tin sản phẩm từ giao diện người dùng
        String tenSP = name.getText().toString();
        String mota = description.getText().toString();
        long giaSP;
        try {
            giaSP = Long.parseLong(price.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter the price", Toast.LENGTH_SHORT).show();
            return;
        }
        // Kiểm tra các thông tin cần thiết không được để trống
        if (tenSP.isEmpty()) {
            Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mota.isEmpty()) {
            Toast.makeText(this, "Please enter product description", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy danh sách màu sắc mới từ adapter_color
        selectedColors.clear();
        for (MyColor color : allColors) {
            if (color.getIsChecked()) {
                selectedColors.add(color.getColorId());
            }
        }
        selectedSizes.clear();
        for(MySize size : allSize)
        {
            if(size.isChecked())
            {
                selectedSizes.add(size.getSizeCode());
            }
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("PRODUCT");
        productsRef.document(MaSP).update(
                "productName", tenSP,
                "description", mota,
                "productPrice", giaSP,
                "productColor", selectedColors,
                "productSize", selectedSizes,
                "imageUrl", updatedImageUrls
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent_done = new Intent(EditProductWithoutAmount.this, EditProductWithoutAmount.class);
                Toast.makeText(EditProductWithoutAmount.this, "Update successfully", Toast.LENGTH_SHORT).show();
                startActivity(intent_done);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProductWithoutAmount.this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}