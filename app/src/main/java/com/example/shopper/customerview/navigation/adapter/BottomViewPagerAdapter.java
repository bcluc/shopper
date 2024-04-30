package com.example.shopper.customerview.navigation.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.shopper.customerview.navigation.fragment.AccountFragment;
import com.example.shopper.customerview.navigation.fragment.FollowFragment;
import com.example.shopper.customerview.navigation.fragment.HomeFragment;
import com.example.shopper.customerview.navigation.fragment.NotificationFragment;

public class BottomViewPagerAdapter extends FragmentStatePagerAdapter {
    public BottomViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();

            case 1:
                return new NotificationFragment();

            case 2:
                return new FollowFragment();

            case 3:
                return new AccountFragment();

            default:
                return new HomeFragment();

        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
