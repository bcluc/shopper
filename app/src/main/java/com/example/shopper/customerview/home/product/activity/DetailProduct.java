package com.example.shopper.customerview.home.product.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopper.R;

public class DetailProduct extends AppCompatActivity {
    private String dataColor, dataSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onColorClick(String colorName) {
        // Xử lý sự kiện khi màu được chọn
        // Dữ liệu màu tên và mã màu được truyền từ adapter qua activity
        dataColor = colorName;
        Log.d("MyColor", dataColor);
    }

    public void onSizeClick(String size) {
        // Xử lý sự kiện khi màu được chọn
        // Dữ liệu màu tên và mã màu được truyền từ adapter qua activity
        dataSize = size;
        Log.d("Size", dataSize);
    }
}