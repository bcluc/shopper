package com.example.shopper.staffview.category.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.category.adapter.DeleteCategoryAdapter;
import com.example.shopper.staffview.category.model.MyCategory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DeleteCategory extends AppCompatActivity {

    private ImageButton back;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private Button btn;
    private RecyclerView rcvCategories;
    private DeleteCategoryAdapter adapter;
    private List<MyCategory> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        back = findViewById(R.id.backIcon);
        rcvCategories = findViewById(R.id.rcvdeleteCategories);
        categoryList = new ArrayList<>();
        adapter = new DeleteCategoryAdapter(categoryList);
        rcvCategories.setLayoutManager(new LinearLayoutManager(this));
        rcvCategories.setAdapter(adapter);
        btn = findViewById(R.id.btn_delete_category);
        firestore = FirebaseFirestore.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteCategory.this, Category.class);
                startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MyCategory> selectedCategories = adapter.getSelectedCategories();
                if (selectedCategories.isEmpty()) {
                    Toast.makeText(DeleteCategory.this, "No categories selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (MyCategory category : selectedCategories) {
                    deleteCategory(category);
                }
            }
        });


        // Lấy danh mục từ Firestore và cập nhật danh sách
        FirebaseFirestore.getInstance().collection("CATEGORY")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("categoryName");
                            String image = document.getString("categoryImage");
                            int quantity = document.getLong("quanity").intValue();
                            MyCategory category = new MyCategory(name, image, quantity);
                            categoryList.add(category);
                        }
                        adapter.notifyDataSetChanged();
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

    private void deleteCategory(MyCategory category) {
        int soLuongSP = category.getQuantity();

        if (soLuongSP == 0) {
            String tenDMToDelete = category.getCategoryName();

            firestore.collection("CATEGORY")
                    .whereEqualTo("categoryName", tenDMToDelete)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                            Toast.makeText(DeleteCategory.this, "My Category deleted successfully", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent(DeleteCategory.this, Category.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DeleteCategory.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(DeleteCategory.this, "Cannot delete category with products, because this category have more than 2 products", Toast.LENGTH_SHORT).show();
        }
    }

}
