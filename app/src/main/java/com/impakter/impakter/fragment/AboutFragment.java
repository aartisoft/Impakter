package com.impakter.impakter.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.BrandDetailActivity;
import com.impakter.impakter.activity.MyBagActivity;
import com.impakter.impakter.adapter.MoreImageAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnAddToBagClickListener;
import com.impakter.impakter.events.OnBuyNowClickListener;
import com.impakter.impakter.object.ProductDetailRespond;

import java.util.ArrayList;

public class AboutFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private TextView tvAbout, tvGoToBrandShop, tvBrandName;

    private RecyclerView rcvImage;
    private LinearLayout btnAddToBag, btnBuyNow;
    private MoreImageAdapter moreImageAdapter;
    private ArrayList<ProductDetailRespond.MoreFromThisBrand> listImages = new ArrayList<>();
    private int sellerId;

    private OnAddToBagClickListener onAddToBagClickListener;
    private OnBuyNowClickListener onBuyNowClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAddToBagClickListener = (OnAddToBagClickListener) context;
        onBuyNowClickListener = (OnBuyNowClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        tvAbout = rootView.findViewById(R.id.tv_about);
        tvGoToBrandShop = rootView.findViewById(R.id.tv_go_to_brand_shop);
        tvBrandName = rootView.findViewById(R.id.tv_brand_name);

        btnAddToBag = rootView.findViewById(R.id.btn_add_to_bag);
        btnBuyNow = rootView.findViewById(R.id.btn_buy_now);

        rcvImage = rootView.findViewById(R.id.rcv_image);
        rcvImage.setHasFixedSize(true);
        rcvImage.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            sellerId = bundle.getInt(Constants.SELLER_ID);

            String introduction = bundle.getString(Constants.INTRODUCTION);
            tvAbout.setText(introduction);

            String brandName = bundle.getString(Constants.BRAND_NAME);
            tvBrandName.setText(brandName);

            listImages = bundle.getParcelableArrayList(Constants.ARR_IMAGE);
        }
        moreImageAdapter = new MoreImageAdapter(self, listImages);
        rcvImage.setAdapter(moreImageAdapter);
    }

    private void initControl() {
        tvGoToBrandShop.setOnClickListener(this);
        btnAddToBag.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_go_to_brand_shop:
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.SELLER_ID, sellerId);
                Intent intent = new Intent(self, BrandDetailActivity.class);
                intent.putExtras(bundle);
                self.startActivity(intent);
                self.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
                break;
            case R.id.btn_add_to_bag:
                onAddToBagClickListener.onAddToBagClick();
                break;
            case R.id.btn_buy_now:
                onBuyNowClickListener.onBuyNowClick();
                break;
        }
    }
}
