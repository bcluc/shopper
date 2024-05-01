package com.example.shopper.staffview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shopper.R;
import com.example.shopper.authentication.activity.Login;
import com.example.shopper.authentication.model.User;
import com.example.shopper.staffview.product.activity.MyProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class StaffHomePage extends AppCompatActivity {
    private Button btnMyProduct, btnMyOrder, btnSetting,
            btnChat, btnFinancialReport, btnManageUsers, btnCategories, btnViewShop, btnLogOut;
    FirebaseAuth firebaseAuth;

    private ImageButton btnPromotions;
    ProgressDialog progressDialog;
    User user;
    private AppCompatButton btnConfirm, btnWait, btnDelivering, btnDelivered, btnCancel;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        setContentView(R.layout.activity_staff_home_page);
        // initialising all views through id defined above
        btnMyProduct = findViewById(R.id.btn_my_product);
        btnCategories = findViewById(R.id.btn_categories);
        btnSetting = findViewById(R.id.btn_setting);
        btnChat = findViewById(R.id.btn_chat);
        btnMyOrder = findViewById(R.id.btn_my_order);
        btnPromotions = findViewById(R.id.btn_promotions);
        btnFinancialReport = findViewById(R.id.btn_financial_report);
        btnManageUsers = findViewById(R.id.btn_manage_user);
        btnViewShop = findViewById(R.id.btn_view_shop);
        btnPromotions = findViewById(R.id.btn_promotions);
        btnLogOut = findViewById(R.id.btn_logout);
        btnFinancialReport = findViewById(R.id.btn_financial_report);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnWait =findViewById(R.id.btn_wait);
        btnDelivering = findViewById(R.id.btn_delivering);
        btnDelivered = findViewById(R.id.btn_delivered);
        btnCancel = findViewById(R.id.btn_cancel);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi nút "Confirm" được nhấp vào, chuyển đến "activity_MyOrder" và hiển thị fragment Confirm

//                Intent intent = new Intent(StaffHomePage.this, activity_MyOrder.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("selected_tab", 0); // 0 là vị trí tab Confirm
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

        btnWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi nút "Wait" được nhấp vào, chuyển đến "activity_MyOrder" và hiển thị fragment Wait

//                Intent intent = new Intent(StaffHomePage.this, activity_MyOrder.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("selected_tab", 1); // 1 là vị trí tab Wait
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

        btnDelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi nút "Delivering" được nhấp vào, chuyển đến "activity_MyOrder" và hiển thị fragment Delivering

//                Intent intent = new Intent(StaffHomePage.this, activity_MyOrder.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("selected_tab", 2); // 2 là vị trí tab Delivering
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

        btnDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi nút "Delivered" được nhấp vào, chuyển đến "activity_MyOrder" và hiển thị fragment Delivered

//                Intent intent = new Intent(home_page.this, activity_MyOrder.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("selected_tab", 3); // 3 là vị trí tab Delivered
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StaffHomePage.this, activity_MyOrder.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("selected_tab", 4); // 0 là vị trí tab Confirm
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });


        btnFinancialReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(home_page.this, activity_financial.class);
//                startActivity(intent);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffHomePage.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Kiểm tra và xử lý trường hợp khi firebaseAuth.getUid() bị null
        if (firebaseAuth.getCurrentUser() == null) {
            // Xử lý khi firebaseAuth.getUid() bị null, ví dụ chuyển đến màn hình đăng nhập
            Intent intent = new Intent(StaffHomePage.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        progressDialog.setTitle("Fetching data...");
        progressDialog.show();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        DocumentReference documentReference=firebaseFirestore.collection("USERS")
                .document(firebaseAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        ((TextView) findViewById(R.id.userName)).setText(user.getFullName());
                        ((TextView) findViewById(R.id.userID)).setText(user.getUserId());
                        String uri=user.getAvatar();
                        try{
                            if(uri.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(),"null is recieved",Toast.LENGTH_SHORT).show();
                            }
                            else Picasso.get().load(uri).into((ImageView) findViewById(R.id.img_avt));
                        }
                        catch (Exception e)
                        {

                        }
                        if(user.getUserType().equals("Staff")) {
                            btnManageUsers.setVisibility(View.GONE);
                            findViewById(R.id.txt_manage_user).setVisibility(View.GONE);
                        }
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Fetching Failed",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
        btnMyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình "MyProductActivity"
                Intent intent = new Intent(StaffHomePage.this, MyProduct.class);
                startActivity(intent);
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(StaffHomePage.this, activity_setting.class);
//                startActivity(intent);
            }
        });
        btnMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình "MyOrderActivity"

//                Intent intent = new Intent(StaffHomePage.this, activity_MyOrder.class);
//                startActivity(intent);
            }
        });
        btnViewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StaffHomePage.this, activity_viewshop.class);
//                startActivity(intent);
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(StaffHomePage.this, activity_chat_board.class);
//                startActivity(intent);
            }
        });
        btnCategories.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Intent intent = new Intent (StaffHomePage.this, activity_categories.class);
//                startActivity(intent);
            }
        });
        btnPromotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StaffHomePage.this, activity_promotions.class);
//                startActivity(intent);
            }
        });
        btnManageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(StaffHomePage.this, activity_admin_control.class);
//                startActivity(intent);
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
}