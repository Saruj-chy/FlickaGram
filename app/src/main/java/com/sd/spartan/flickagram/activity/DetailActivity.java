package com.sd.spartan.flickagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import com.sd.spartan.flickagram.R;
import com.sd.spartan.flickagram.adapter.ViewPagerHandler;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ViewPager viewPager = findViewById(R.id.view_pager);
        String posi = getIntent().getStringExtra("pos");

        ViewPagerHandler viewPagerHandler = new ViewPagerHandler(getSupportFragmentManager(), MainActivity.flickerModelList.size());
        viewPager.setAdapter(viewPagerHandler);
        viewPager.setCurrentItem(Integer.parseInt(posi));

    }
}