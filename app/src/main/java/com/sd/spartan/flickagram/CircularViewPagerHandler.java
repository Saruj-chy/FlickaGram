package com.sd.spartan.flickagram;

import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class CircularViewPagerHandler extends FragmentStatePagerAdapter {
    private final int mNumOfTabs;

    public CircularViewPagerHandler(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public @NotNull
    Fragment getItem(int position) {
        return MyFragment.newInstance(position);

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}