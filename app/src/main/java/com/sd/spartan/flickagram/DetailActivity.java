package com.sd.spartan.flickagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class DetailActivity extends AppCompatActivity {
    ViewPager viewPager ;
    private TabLayout mTabLayout;
    CircularViewPagerHandler circularViewPagerHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        viewPager = findViewById(R.id.view_pager) ;
        mTabLayout = findViewById(R.id.tab_layout);

        String posi = getIntent().getStringExtra("pos");

        circularViewPagerHandler = new CircularViewPagerHandler(getSupportFragmentManager(), MainActivity.flickerModelList.size()) ;
        viewPager.setAdapter(circularViewPagerHandler);
        viewPager.setCurrentItem(Integer.parseInt(posi));

//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e("toast", "Pos:"+ position ) ;
//                viewPager.setCurrentItem(position);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });



        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }
}