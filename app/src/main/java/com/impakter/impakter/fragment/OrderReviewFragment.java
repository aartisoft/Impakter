package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.activity.OrderConfirmationActivity;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.OrderDetailRespond;
import com.impakter.impakter.object.ReviewOrderRespond;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderReviewFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ImageView ivBack, ivProduct;
    private TextView tvProductName, tvBrand, tvDescription, tvOption;
    private MaterialRatingBar ratingBar;
    private EditText edtContent;
    private Button btnCancel, btnSubmit;
    private OrderDetailRespond.ListItems product;
    private int orderId;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_review, container, false);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initViews();
        initData();
        initControl();
        return rootView;

    }

    private void initViews() {
        ivBack = rootView.findViewById(R.id.iv_back);
        ivProduct = rootView.findViewById(R.id.iv_product);

        tvProductName = rootView.findViewById(R.id.tv_product_name);
        tvBrand = rootView.findViewById(R.id.tv_brand);
        tvDescription = rootView.findViewById(R.id.tv_description);
        tvOption = rootView.findViewById(R.id.tv_option);

        ratingBar = rootView.findViewById(R.id.rating_bar);
        edtContent = rootView.findViewById(R.id.edt_content);
        edtContent.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtContent.setRawInputType(InputType.TYPE_CLASS_TEXT);

        btnSubmit = rootView.findViewById(R.id.btn_submit);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
    }

    private void initData() {
        userId = preferenceManager.getUserLogin().getId();
        Bundle bundle = getArguments();
        String brandName = "";
        if (bundle != null) {
            product = bundle.getParcelable(Constants.PRODUCT);
            brandName = bundle.getString(Constants.BRAND_NAME);
            orderId = bundle.getInt(Constants.ORDER_ID);
        }
        tvProductName.setText(product.getName());
        tvBrand.setText(brandName);
        tvDescription.setText(product.getCode());
        tvOption.setText(product.getOptions());
        ratingBar.setRating(product.getRate());
        Glide.with(self).load(product.getImage()).into(ivProduct);

    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void reviewOrder(final String content, float rate) {
        showDialog();
        ConnectServer.getResponseAPI().reviewOrder(orderId, product.getOrderItemId(), userId, content, rate).enqueue(new Callback<ReviewOrderRespond>() {
            @Override
            public void onResponse(Call<ReviewOrderRespond> call, Response<ReviewOrderRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showToast(response.body().getMessage());
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.DESCRIPTION, content);
                        bundle.putParcelable(Constants.PRODUCT, response.body().getData());
                        OrderReviewConfirmationFragment orderReviewConfirmationFragment = new OrderReviewConfirmationFragment();
                        orderReviewConfirmationFragment.setArguments(bundle);
                        ((MainActivity) self).showFragmentWithAddMethod(orderReviewConfirmationFragment, true);
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<ReviewOrderRespond> call, Throwable t) {
                showToast(t.getMessage());
                closeDialog();
            }
        });
    }

    private boolean validate() {
        if (edtContent.getText().toString().trim().length() == 0) {
            edtContent.requestFocus();
            showToast(getResources().getString(R.string.input_content));
            return false;
        }
        if (ratingBar.getRating() == 0) {
            showToast(getResources().getString(R.string.please_rating));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_cancel:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_submit:
                if (validate())
                    reviewOrder(edtContent.getText().toString().trim(), ratingBar.getRating());
                break;
        }
    }
}
