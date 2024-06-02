package com.example.shopper.staffview.chat.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.shopper.staffview.chat.fragment.ChatStaffFragment;
import com.example.shopper.staffview.chat.fragment.StatusStaffFragment;

public class ChatBoardStaffAdapter extends FragmentPagerAdapter {

    int tabcount;

    Activity activity;


    public ChatBoardStaffAdapter(@NonNull FragmentManager fm, int behavior, Activity activity) {
        super(fm, behavior);
        tabcount=behavior;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new ChatStaffFragment();

            case 1:
                return new StatusStaffFragment();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabcount;
    }
}

