package com.example.shopper.staffview.product.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.product.adapter.ColorAdapter;
import com.example.shopper.staffview.product.adapter.SizeAdapter;
import com.example.shopper.staffview.product.model.Color;
import com.example.shopper.staffview.product.model.Size;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;


import java.util.List;

public class AddNewProduct extends AppCompatActivity {
    private List<Size> sizeList;
    //private List<CategoryItem> categoryItemList;
    private List<Color> colorList;
    private SizeAdapter sizeAdapter;
    //private adapter_category_list categoryAdapter;
    private RecyclerView recyclerView_size;
    private RecyclerView recyclerView_color;
    private RecyclerView recyclerView_category;
    private ColorAdapter colorAdapter;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}