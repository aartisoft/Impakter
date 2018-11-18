package com.impakter.impakter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.adapter.OrderDetailAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.OrderDetailRespond;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private TextView tvBrandName, tvOrderNo, tvStatus, tvTotalItem, tvShippingPrice, tvTaxes, tvTotal, tvOrderDate, tvShippingInformation, tvShippingAddress;
    private TextView tvContactBuyer;
    private Button btnAddReview;
    private CircleImageView ivBrand;
    private ImageView ivBack;
    private TextView btnContact;

    private RecyclerView rcvProduct;
    private OrderDetailAdapter orderDetailAdapter;
    private List<OrderDetailRespond.ListItems> listProducts = new ArrayList<>();
    private int orderId;
    private String brandName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        initViews();
        initData();
        initControl();
        return rootView;

    }

    private void initViews() {
        ivBrand = rootView.findViewById(R.id.iv_brand);
        ivBack = rootView.findViewById(R.id.iv_back);

        tvBrandName = rootView.findViewById(R.id.tv_brand_name);
        tvOrderNo = rootView.findViewById(R.id.tv_order_no);
        tvStatus = rootView.findViewById(R.id.tv_status);
        tvTotalItem = rootView.findViewById(R.id.tv_total_item);
        tvShippingPrice = rootView.findViewById(R.id.tv_shipping_price);
        tvTaxes = rootView.findViewById(R.id.tv_taxes);
        tvTotal = rootView.findViewById(R.id.tv_total_price);
        tvOrderDate = rootView.findViewById(R.id.tv_order_date);
        tvShippingInformation = rootView.findViewById(R.id.tv_information);
        tvShippingAddress = rootView.findViewById(R.id.tv_shipping_address);
        tvContactBuyer = rootView.findViewById(R.id.tv_contact_buyer);

        btnContact = rootView.findViewById(R.id.btn_contact);
        btnAddReview = rootView.findViewById(R.id.btn_add_review);

        rcvProduct = rootView.findViewById(R.id.rcv_product);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new LinearLayoutManager(self));
        ViewCompat.setNestedScrollingEnabled(rcvProduct, false);

    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderId = bundle.getInt(Constants.ORDER_ID);
        }
        orderDetailAdapter = new OrderDetailAdapter(self, listProducts);
        rcvProduct.setAdapter(orderDetailAdapter);

        getDetailOrder();
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnAddReview.setOnClickListener(this);
        orderDetailAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void setData(OrderDetailRespond.Data data) {
        Glide.with(self).load(data.getShipmentImage()).into(ivBrand);

        brandName = data.getBrandName();
        tvBrandName.setText(data.getBrandName());
        tvOrderNo.setText(getResources().getString(R.string.order_no) + ". " + data.getOrderId());
        tvStatus.setText(data.getStatus());
        tvTotalItem.setText(data.getListItems().size() + " " + getResources().getString(R.string.item));
        tvShippingPrice.setText(getResources().getString(R.string.lbl_currency) + data.getShippingPrice());
        tvTaxes.setText(getResources().getString(R.string.lbl_currency) + data.getTax());
        tvTotal.setText(getResources().getString(R.string.lbl_currency) + data.getTotalOrderPrice());
        tvOrderDate.setText(data.getOrderDate());
        tvShippingInformation.setText(data.getShipmentInformation());
        tvShippingAddress.setText(data.getShippingAddress());

    }

    private void getDetailOrder() {
        showDialog();
        ConnectServer.getResponseAPI().getOrderDetail(orderId).enqueue(new Callback<OrderDetailRespond>() {
            @Override
            public void onResponse(Call<OrderDetailRespond> call, Response<OrderDetailRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        if (response.body().getData().getStatus().equalsIgnoreCase("delivered")) {
                            btnAddReview.setVisibility(View.VISIBLE);
                        } else {
                            btnAddReview.setVisibility(View.GONE);
                        }
                        setData(response.body().getData());

                        listProducts.clear();
                        listProducts.addAll(response.body().getData().getListItems());
                        orderDetailAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<OrderDetailRespond> call, Throwable t) {
                showToast(t.getMessage());
                closeDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_contact:

                break;

            case R.id.btn_add_review:
                ListOrderReviewFragment listOrderReviewFragment = new ListOrderReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.LIST_ORDER, (ArrayList<? extends Parcelable>) listProducts);
                bundle.putString(Constants.BRAND_NAME, brandName);
                bundle.putInt(Constants.ORDER_ID, orderId);
                listOrderReviewFragment.setArguments(bundle);
                ((MainActivity) self).showFragmentWithAddMethod(listOrderReviewFragment, true);
                break;
        }
    }
}
