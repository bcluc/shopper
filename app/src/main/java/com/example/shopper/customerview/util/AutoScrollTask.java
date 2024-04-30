package com.example.shopper.customerview.util;

import androidx.viewpager2.widget.ViewPager2;

import java.util.TimerTask;

public class AutoScrollTask extends TimerTask {
    private ViewPager2 viewPager2;

    public AutoScrollTask(ViewPager2 viewPager2) {
        this.viewPager2 = viewPager2;
    }

    @Override
    public void run() {
        viewPager2.post(new Runnable() {
            @Override
            public void run() {
                viewPager2.setCurrentItem((viewPager2.getCurrentItem() + 1) % viewPager2.getAdapter().getItemCount());
            }
        });
    }
}