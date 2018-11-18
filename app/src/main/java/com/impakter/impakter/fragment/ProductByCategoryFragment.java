package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.adapter.ViewPagerAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.object.HomeCategoryRespond;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProductByCategoryFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private SmartTabLayout tabLayout;
    private ViewPager viewPager;

    private ArrayList<HomeCategoryRespond.SubCatArray> listSubCategory = new ArrayList<>();
    private int categoryId, subCategoryId;
    private int page = 1;
    private int currentPosition;
    private ImageView ivBack, ivSearch, ivFilter;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_by_category, container, false);
        initViews();
        initData();
        initControl();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ivFilter.setVisibility(View.GONE);
                        getFragmentManager().popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    private void initViews() {
        ivBack = rootView.findViewById(R.id.iv_back);
        ivSearch = rootView.findViewById(R.id.iv_search);
        ivFilter = rootView.findViewById(R.id.iv_filter);

        viewPager = rootView.findViewById(R.id.viewpager);

        tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.setViewPager(viewPager);
    }

    private void tabsLayoutBuilder(List<HomeCategoryRespond.SubCatArray> listSubCategory) {

        int number = 0;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (HomeCategoryRespond.SubCatArray subCatList : listSubCategory) {
            Bundle data = new Bundle();
            data.putInt(Constants.CATEGORY_ID, categoryId);
            data.putInt(Constants.SUBCATEGORY_ID, subCatList.getId());
            ProductBySubCatFragment productBySubCatFragment = new ProductBySubCatFragment();
            productBySubCatFragment.setArguments(data);
            adapter.addFrag(productBySubCatFragment, subCatList.getName());
//            tabLayout.addTab(tabLayout.newTab().setText(subCatList.getName()), number);
//            number = number + 1;
        }
        if (listSubCategory.size() > 4) {
            tabLayout.setCustomTabView(R.layout.custom_tab_text_more_4_tab, R.id.custom_text);
        } else {
            tabLayout.setCustomTabView(R.layout.custom_tab_text_les_4_tab, R.id.custom_text);
        }
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(currentPosition);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//        });
////        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.gray));
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new CategoryFragment(), getResources().getString(R.string.categories));
        adapter.addFrag(new LatestFragment(), getResources().getString(R.string.lastest));
        adapter.addFrag(new TrendingFragment(), getResources().getString(R.string.trending));
        adapter.addFrag(new BrandFragment(), getResources().getString(R.string.brand));
        viewPager.setAdapter(adapter);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentPosition = bundle.getInt(Constants.CURRENT_POSITION);
            categoryId = bundle.getInt(Constants.CATEGORY_ID);
            listSubCategory = bundle.getParcelableArrayList(Constants.LIST_SUB);
            tabsLayoutBuilder(listSubCategory);

        }
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.iv_search:

                break;
            case R.id.iv_filter:

                break;
        }
    }
}
