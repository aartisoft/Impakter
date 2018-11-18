package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.adapter.OrderAdapter;
import com.impakter.impakter.adapter.StatusSpinnerAdapter;
import com.impakter.impakter.adapter.TimeSpinnerAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.OrderObject;
import com.impakter.impakter.object.OrderRespond;
import com.impakter.impakter.object.TimeObj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends BaseFragment {
    private View rootView;
    private RecyclerView rcvOrder;
    private OrderAdapter orderAdapter;
    private List<OrderObject> listOrders = new ArrayList<>();

    private Spinner spStatus, spTime;
    private TimeSpinnerAdapter timeSpinnerAdapter;
    private StatusSpinnerAdapter statusSpinnerAdapter;

    private ArrayList<TimeObj> listTypeTime = new ArrayList<>();

    private TextView tvNoData;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;


    private int userId;
    private String status = "";
    private String sortTime = "";
    private boolean isFirstLoad;
    private boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        tvNoData = rootView.findViewById(R.id.tv_no_data);
        progressBar = rootView.findViewById(R.id.progress_bar);
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);

        spStatus = rootView.findViewById(R.id.sp_status);
        spTime = rootView.findViewById(R.id.sp_time);

        rcvOrder = rootView.findViewById(R.id.rcv_order);
        rcvOrder.setHasFixedSize(true);
        rcvOrder.setLayoutManager(new LinearLayoutManager(self));

    }

    private void initData() {
        initTypeTimeData();
        userId = preferenceManager.getUserLogin().getId();
        statusSpinnerAdapter = new StatusSpinnerAdapter(self);
        spStatus.setAdapter(statusSpinnerAdapter);

        timeSpinnerAdapter = new TimeSpinnerAdapter(self, listTypeTime);
        spTime.setAdapter(timeSpinnerAdapter);

        orderAdapter = new OrderAdapter(self, listOrders);
        rcvOrder.setAdapter(orderAdapter);

        if (getUserVisibleHint()) {
            getOrders();
        }
    }

    private void initTypeTimeData() {
        listTypeTime.clear();
        listTypeTime.add(new TimeObj(-1, getResources().getString(R.string.all)));
        listTypeTime.add(new TimeObj(1, getResources().getString(R.string.last_24h)));
        listTypeTime.add(new TimeObj(2, getResources().getString(R.string.last_2_days)));
        listTypeTime.add(new TimeObj(3, getResources().getString(R.string.last_week)));
        listTypeTime.add(new TimeObj(4, getResources().getString(R.string.last_month)));
    }

    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrders();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TimeObj timeObj = (TimeObj) parent.getItemAtPosition(position);
                status = timeObj.getId() + "";
                if (status.equals("-1")) {
                    status = "";
                }
                if (!isFirst)
                    getOrders();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TimeObj timeObj = (TimeObj) parent.getItemAtPosition(position);
                sortTime = timeObj.getId() + "";
                if (sortTime.equals("-1")) {
                    sortTime = "";
                }
                if (!isFirst)
                    getOrders();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        orderAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.ORDER_ID, listOrders.get(position).getOrderId());
                OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
                orderDetailFragment.setArguments(bundle);
                ((MainActivity) self).showFragmentWithAddMethod(orderDetailFragment, true);
            }
        });
    }

    private void getOrders() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getOrders(userId, status, sortTime).enqueue(new Callback<OrderRespond>() {
            @Override
            public void onResponse(Call<OrderRespond> call, Response<OrderRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listOrders.clear();
                        if (response.body().getData() != null && response.body().getData().size() != 0)
                            for (OrderRespond.Data data : response.body().getData()) {
                                int orderNumber = data.getOrderNumber();
                                for (OrderRespond.ArrOrder order : data.getArrOrder()) {
                                    OrderObject orderObject = new OrderObject();
                                    orderObject.setOrderNumber(orderNumber);
                                    orderObject.setOrderId(order.getId());
                                    orderObject.setStatus(order.getStatus());
                                    orderObject.setTotalPrice(order.getTotal());
                                    orderObject.setProductName(order.getName());
                                    orderObject.setBrandId(order.getBrandId());
                                    orderObject.setBrandName(order.getBrandName());
                                    orderObject.setImage(order.getImage());
                                    listOrders.add(orderObject);
                                }
                            }
                        orderAdapter.fillSections();
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                    isFirst = false;
                }
                checkNoData();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<OrderRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                checkNoData();
                isFirst = false;
            }
        });
    }

    private void checkNoData() {
        if (listOrders.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible()) {
            if (isVisibleToUser && !isFirstLoad) {
                getOrders();
                isFirstLoad = true;
            }
        }
    }
}
