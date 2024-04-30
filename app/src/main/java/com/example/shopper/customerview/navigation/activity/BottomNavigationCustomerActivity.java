package com.example.shopper.customerview.navigation.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.shopper.R;
import com.example.shopper.authentication.activity.Login;
import com.example.shopper.customerview.account.changepassword.ChangePassword;
import com.example.shopper.customerview.account.profile.Profile;
import com.example.shopper.customerview.home.chat.activity.Chat;
import com.example.shopper.customerview.home.shoppingcart.ShoppingCart;
import com.example.shopper.customerview.home.trending.Trending;
import com.example.shopper.customerview.navigation.adapter.BottomViewPagerAdapter;
import com.example.shopper.customerview.navigation.fragment.AccountFragment;
import com.example.shopper.customerview.navigation.fragment.HomeFragment;
import com.example.shopper.customerview.home.product.activity.DetailProduct;
import com.example.shopper.customerview.home.product.model.Product;
import com.example.shopper.customerview.home.product.model.ProductCard;
import com.example.shopper.customerview.notification.activity.MyNotification;
import com.example.shopper.customerview.notification.activity.Order;
import com.example.shopper.customerview.util.search.Searching;
import com.example.shopper.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BottomNavigationCustomerActivity extends AppCompatActivity {
    private ViewPager view_pager;
    private BottomNavigationView BottomNavigationView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_bottom_navigation_customer);
        //setContentView(binding.getRoot());

//        view_pager = binding.getRoot().findViewById(R.id.view_pager);
//        BottomNavigationView = binding.getRoot().findViewById(R.id.bottom_navigation_view);

        view_pager = findViewById(R.id.view_pager);
        BottomNavigationView = findViewById(R.id.bottom_navigation_view);

        BottomViewPagerAdapter adapter = new BottomViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        view_pager.setAdapter(adapter);

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        BottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        BottomNavigationView.getMenu().findItem(R.id.menu_notification).setChecked(true);
                        break;
                    case 2:
                        BottomNavigationView.getMenu().findItem(R.id.menu_follow).setChecked(true);
                        break;
                    case 3:
                        BottomNavigationView.getMenu().findItem(R.id.menu_account).setChecked(true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        BottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_account) {
                    view_pager.setCurrentItem(3, true);
                } else if (itemId == R.id.menu_follow) {
                    view_pager.setCurrentItem(2, true);
                } else if (itemId == R.id.menu_notification) {
                    view_pager.setCurrentItem(1, true);
                } else if (itemId == R.id.menu_home) {
                    view_pager.setCurrentItem(0, true);
                }
                return true;
            }
        });
    }

    public void gotoDetailProduct(ProductCard product) {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, DetailProduct.class);
        intent.putExtra("ProductId", product.getProductId());
        startActivity(intent);

    }

    public void gotoDetailProduct(Product product) {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, DetailProduct.class);
        intent.putExtra("ProductId", product.getProductId());
        startActivity(intent);

    }

    public void gotoHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_home_fragment, new HomeFragment());
        fragmentTransaction.commit();
    }

    public void gotoProfileFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_home_fragment, new AccountFragment());
        fragmentTransaction.commit();
    }

    public void gotoSearchingActivity() {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, Searching.class);
        startActivity(intent);
    }

    public void gotoMessageActivity() {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, Chat.class);
        startActivity(intent);
    }

    public void gotoDetail() {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, ShoppingCart.class);
        startActivity(intent);
    }

    public void gotoTrendingActivity() {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, Trending.class);
        startActivity(intent);
    }

    public void gotoNotificationActivity() {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, MyNotification.class);
        startActivity(intent);
    }

    public void gotoOrderActivity() {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, Order.class);
        //  intent.putExtra("MaSP", product.getMasp());
        startActivity(intent);
    }

    public void gotoProfile() {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, Profile.class);
        startActivity(intent);
    }

    public void gotoCLickHelp() {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, Chat.class);
        startActivity(intent);
    }

    public void gotoChangePass() {
        Intent intent = new Intent(BottomNavigationCustomerActivity.this, ChangePassword.class);
        startActivity(intent);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm exit");
        builder.setMessage("Are you sure you want to sign out?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                performLogout();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void performLogout() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(BottomNavigationCustomerActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    public void gotoLogOut() {
        showLogoutConfirmationDialog();
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
}