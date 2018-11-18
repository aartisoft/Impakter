package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.object.ReviewOrderRespond;
import com.impakter.impakter.object.ReviewRespond;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class OrderReviewConfirmationFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ImageView ivProduct, ivBack;
    private TextView tvProductName, tvBrand, tvOption, tvReview;
    private MaterialRatingBar ratingBar;

    private Button btnBackToProfile, btnAddMoreReview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_review_confirmation, container, false);
        initViews();
        initData();
        initControl();
        return rootView;

    }

    private void initViews() {
        ivBack = rootView.findViewById(R.id.iv_back);
        ivProduct = rootView.findViewById(R.id.iv_product);
        ratingBar = rootView.findViewById(R.id.rating_bar);

        tvProductName = rootView.findViewById(R.id.tv_product_name);
        tvBrand = rootView.findViewById(R.id.tv_brand_name);
        tvOption = rootView.findViewById(R.id.tv_option);
        tvReview = rootView.findViewById(R.id.tv_review);

        btnAddMoreReview = rootView.findViewById(R.id.btn_add_more_review);
        btnBackToProfile = rootView.findViewById(R.id.btn_back_to_profile);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String review = bundle.getString(Constants.DESCRIPTION);
            ReviewOrderRespond.Data data = bundle.getParcelable(Constants.PRODUCT);
            tvProductName.setText(data.getName());
            tvBrand.setText(data.getBrand());
            tvOption.setText(data.getOptions());
            tvReview.setText(review);
            ratingBar.setRating(data.getRate());
        }
    }

    private void initControl() {
        btnBackToProfile.setOnClickListener(this);
        btnAddMoreReview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_to_profile:
                getFragmentManager().popBackStack(OrderDetailFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;

            case R.id.btn_add_more_review:
                getFragmentManager().popBackStack(OrderReviewFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.iv_back:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
