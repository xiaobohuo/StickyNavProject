package com.dom.a10174987.stickynavproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dom.a10174987.stickynavproject.com.dom.a10174987.stickynavproject.view.SimpleFragment;
import com.dom.a10174987.stickynavproject.com.dom.a10174987.stickynavproject.view.SimpleIndicatorView;

public class MainActivity extends AppCompatActivity {

    private static final String[] strs = new String[]{"语文", "数学", "英语", "理综"};
    private ViewPager viewPager;
    private SimpleIndicatorView indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        indicator = (SimpleIndicatorView) findViewById(R.id.id_indicator_view);
        viewPager = (ViewPager) findViewById(R.id.id_viewpager);

        indicator.setIndicatorTitles(strs);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return SimpleFragment.newInstance(strs[position]);
            }

            @Override
            public int getCount() {
                return strs.length;
            }

        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
//                indicator.scroll(position, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //TODO
            }
        });
    }
}
