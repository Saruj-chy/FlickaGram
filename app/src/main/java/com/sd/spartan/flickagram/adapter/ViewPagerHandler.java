package com.sd.spartan.flickagram.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sd.spartan.flickagram.fragment.MyFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerHandler extends FragmentStatePagerAdapter {
    private final int mNumOfTabs;

    public ViewPagerHandler(FragmentManager fm, int NumOfTabs) {
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