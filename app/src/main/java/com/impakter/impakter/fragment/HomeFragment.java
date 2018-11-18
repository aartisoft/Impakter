package com.impakter.impakter.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.SearchActivity;
import com.impakter.impakter.adapter.ViewPagerAdapter;
import com.impakter.impakter.widget.textview.TextViewHeeboMedium;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class HomeFragment extends BaseFragment {
    private View rootView;
    private ImageView ivSearch;
    //    private TabLayout tabLayout;
    private SmartTabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        iniViews();
        initData();
        initControl();
        return rootView;
    }

    private void iniViews() {
        ivSearch = rootView.findViewById(R.id.iv_search);

        viewPager = rootView.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout = rootView.findViewById(R.id.tab_main);
        tabLayout.setViewPager(viewPager);
    }

    private void initData() {

    }

    private void initControl() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchActivity.class);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new CategoryFragment(), getResources().getString(R.string.categories));
        adapter.addFrag(new LatestFragment(), getResources().getString(R.string.lastest));
        adapter.addFrag(new TrendingFragment(), getResources().getString(R.string.trending));
        adapter.addFrag(new BrandFragment(), getResources().getString(R.string.brand));
        viewPager.setAdapter(adapter);
    }
}
