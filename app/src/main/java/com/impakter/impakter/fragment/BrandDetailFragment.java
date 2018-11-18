package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.ViewPagerAdapter;

public class BrandDetailFragment extends BaseFragment {
    private View rootView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_brand_detail, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initData() {

    }

    private void initControl() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new ProductFragment(), getResources().getString(R.string.product));
        adapter.addFrag(new ReviewFragment(), getResources().getString(R.string.review));
        adapter.addFrag(new AboutFragment(), getResources().getString(R.string.about));
        viewPager.setAdapter(adapter);
    }
}
