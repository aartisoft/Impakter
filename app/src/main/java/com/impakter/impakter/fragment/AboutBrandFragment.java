package com.impakter.impakter.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.BrandDetailActivity;
import com.impakter.impakter.adapter.MoreImageAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnAddToBagClickListener;
import com.impakter.impakter.events.OnBuyNowClickListener;
import com.impakter.impakter.object.ProductDetailRespond;

import java.util.ArrayList;

public class AboutBrandFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private TextView tvAbout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about_brand, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        tvAbout = rootView.findViewById(R.id.tv_about);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String introduction = bundle.getString(Constants.INTRODUCTION);
            tvAbout.setText(introduction);
        }
    }

    private void initControl() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
