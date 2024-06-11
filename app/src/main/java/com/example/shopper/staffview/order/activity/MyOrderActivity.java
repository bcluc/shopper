package com.example.shopper.staffview.order.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.shopper.R;
import com.example.shopper.staffview.StaffHomePage;
import com.example.shopper.staffview.order.fragment.CancelFragment;
import com.example.shopper.staffview.order.fragment.ConfirmFragment;
import com.example.shopper.staffview.order.fragment.DeliveredFragment;
import com.example.shopper.staffview.order.fragment.DeliveringFragment;
import com.example.shopper.staffview.order.fragment.WaitFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyOrderActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ImageButton back_to_Home;
    private Button detail;
    private int selectedTabPosition = -1; // Default value is -1 to indicate no specific tab is selected

    private int [] tabLayoutSize = new int[5];
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        AdminOrderPagerAdapter adapter = new AdminOrderPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayoutSize[0] = 0;
        back_to_Home = findViewById(R.id.imgbtn_back);
        selectedTabPosition = getIntent().getIntExtra("selected_tab", -1);

        if (selectedTabPosition >= 0 && selectedTabPosition < AdminOrderPagerAdapter.NUM_PAGES) {
            viewPager.setCurrentItem(selectedTabPosition);
        }
        adapter.setupBadges(tabLayout);
        back_to_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyOrderActivity.this, StaffHomePage.class);
                startActivity(intent);
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference trangthaiRef = db.collection("ORDER");
        trangthaiRef.whereEqualTo("state", "Confirm")
                .addSnapshotListener((queryDocumentSnapshots, e) ->{
                    if (e != null) {
                        // Handle the error here if necessary.
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        int confirmCount = queryDocumentSnapshots.size();
                        tabLayoutSize[0] = confirmCount;
                        adapter.updateBadgeValue(0,confirmCount);
                    }

                });
        FirebaseFirestore db_onwait = FirebaseFirestore.getInstance();
        CollectionReference onwaitRef = db_onwait.collection("ORDER");
        onwaitRef.whereEqualTo("state", "Wait")
                .addSnapshotListener((queryDocumentSnapshots, e) ->{
                    if (e != null) {
                        // Handle the error here if necessary.
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        int confirmCount = queryDocumentSnapshots.size();
                        tabLayoutSize[1] = confirmCount;
                        adapter.updateBadgeValue(1,confirmCount);
                    }

                });
        FirebaseFirestore db_delivering = FirebaseFirestore.getInstance();
        CollectionReference deliveringRef = db_delivering.collection("ORDER");
        deliveringRef.whereEqualTo("state", "Delivering")
                .addSnapshotListener((queryDocumentSnapshots, e) ->{
                    if (e != null) {
                        // Handle the error here if necessary.
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        int confirmCount = queryDocumentSnapshots.size();
                        tabLayoutSize[2] = confirmCount;
                        adapter.updateBadgeValue(2,confirmCount);
                    }
                });
        FirebaseFirestore db_delivered = FirebaseFirestore.getInstance();
        CollectionReference deliveredRef = db_delivered.collection("ORDER");
        deliveredRef.whereEqualTo("state", "Delivered")
                .addSnapshotListener((queryDocumentSnapshots, e) ->{
                    if (e != null) {
                        // Handle the error here if necessary.
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        int confirmCount = queryDocumentSnapshots.size();
                        tabLayoutSize[3] = confirmCount;
                        adapter.updateBadgeValue(3,confirmCount);
                    }

                });
        FirebaseFirestore db_cancel = FirebaseFirestore.getInstance();
        CollectionReference cancelRef = db_cancel.collection("ORDER");
        cancelRef.whereEqualTo("state", "Cancel")
                .addSnapshotListener((queryDocumentSnapshots, e) ->{
                    if (e != null) {
                        // Handle the error here if necessary.
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        int confirmCount = queryDocumentSnapshots.size();
                        tabLayoutSize[4] = confirmCount;
                        adapter.updateBadgeValue(4,confirmCount);
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
    private class AdminOrderPagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 5;
        private BadgeDrawable[] badgeDrawables = new BadgeDrawable[NUM_PAGES];
        private final String[] TAB_TITLES = {"Confirm", "Wait", "Delivering", "Delivered", "Cancel"};

        private int confirmProductCount = 0;
        public void setSelectedTabPosition(int position) {
            selectedTabPosition = position;
        }
        public AdminOrderPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        private BadgeDrawable getBadge(int position) {
            TabLayout.Tab tab = tabLayout.getTabAt(position);
            if (tab != null) {
                BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
                badgeDrawable.setMaxCharacterCount(3);
                return badgeDrawable;
            }
            return null;
        }
        private void setupBadges(TabLayout tabLayout) {
            for (int i = 0; i < NUM_PAGES; i++) {
                badgeDrawables[i] = getBadge(i);
                updateBadgeValue(i, tabLayoutSize[i]);
            }
        }
        public void updateBadgeValue(int position, int value) {
            BadgeDrawable badgeDrawable = badgeDrawables[position];
            if (badgeDrawable != null) {
                badgeDrawable.setNumber(value);
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ConfirmFragment();
                case 1:
                    return new WaitFragment();
                case 2:
                    return new DeliveringFragment();
                case 3:
                    return new DeliveredFragment();
                case 4:
                    return new CancelFragment();
                default:
                    throw new IllegalStateException("Invalid position: " + position);
            }
        }


        // Phương thức để cập nhật số lượng sản phẩm trong trạng thái "Confirm"
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }


    }


}