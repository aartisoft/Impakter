package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.BuildConfig;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.adapter.OrderReviewAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.object.OrderDetailRespond;

import java.util.ArrayList;
import java.util.List;

public class ListOrderReviewFragment extends BaseFragment {
    private View rootView;

    private TextView tvOrderNo;

    private RecyclerView rcvProduct;
    private List<OrderDetailRespond.ListItems> listProducts = new ArrayList<>();
    private OrderReviewAdapter orderReviewAdapter;
    private String brandName;
    private int orderId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_order_review, container, false);
        initViews();
        initData();
        initControl();
        return rootView;

    }

    private void initViews() {
        tvOrderNo = rootView.findViewById(R.id.tv_order_no);

        rcvProduct = rootView.findViewById(R.id.rcv_product);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new LinearLayoutManager(self));

    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            listProducts = bundle.getParcelableArrayList(Constants.LIST_ORDER);
            brandName = bundle.getString(Constants.BRAND_NAME);
            orderId = bundle.getInt(Constants.ORDER_ID);
        }
        orderReviewAdapter = new OrderReviewAdapter(self, listProducts);
        rcvProduct.setAdapter(orderReviewAdapter);
    }

    private void initControl() {
        orderReviewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderReviewFragment orderReviewFragment = new OrderReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.PRODUCT, listProducts.get(position));
                bundle.putString(Constants.BRAND_NAME, brandName);
                bundle.putInt(Constants.ORDER_ID, orderId);
                orderReviewFragment.setArguments(bundle);
                ((MainActivity) self).showFragmentWithAddMethod(orderReviewFragment, true);
            }
        });
    }
}
