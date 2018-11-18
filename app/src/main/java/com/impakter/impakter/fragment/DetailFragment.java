package com.impakter.impakter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MyBagActivity;
import com.impakter.impakter.adapter.MoreImageAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnAddToBagClickListener;
import com.impakter.impakter.events.OnBuyNowClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.ProductDetailRespond;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private RecyclerView rcvImage;
    private TextView tvDescription;
    private LinearLayout btnAddToBag, btnBuyNow;
    private MoreImageAdapter moreImageAdapter;
    private ArrayList<ProductDetailRespond.MoreFromThisBrand> listImages = new ArrayList<>();
    private String description;
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
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        tvDescription = rootView.findViewById(R.id.tv_description);

        btnAddToBag = rootView.findViewById(R.id.btn_add_to_bag);
        btnBuyNow = rootView.findViewById(R.id.btn_buy_now);

        rcvImage = rootView.findViewById(R.id.rcv_image);
        rcvImage.setHasFixedSize(true);
//        ViewCompat.setNestedScrollingEnabled(rcvImage, true);
        rcvImage.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initData() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            description = bundle.getString(Constants.DESCRIPTION);
            listImages = bundle.getParcelableArrayList(Constants.ARR_IMAGE);
        }
        tvDescription.setText(description);

        moreImageAdapter = new MoreImageAdapter(self, listImages);
        rcvImage.setAdapter(moreImageAdapter);

    }

    private void initControl() {
        btnAddToBag.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_to_bag:
                onAddToBagClickListener.onAddToBagClick();
                break;
            case R.id.btn_buy_now:
                onBuyNowClickListener.onBuyNowClick();
                break;
        }
    }
}
