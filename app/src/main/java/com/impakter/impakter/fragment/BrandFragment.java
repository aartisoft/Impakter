package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.BrandAdapter;
import com.impakter.impakter.adapter.CategorySpinnerAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BrandObj;
import com.impakter.impakter.object.BrandRespond;
import com.impakter.impakter.object.CategoryObj;
import com.impakter.impakter.object.MenuCategoryRespond;
import com.impakter.impakter.object.TimeObj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandFragment extends BaseFragment {
    private View rootView;
    private RecyclerView rcvBrand;
    private ArrayList<BrandObj> listBrands = new ArrayList<>();
    private BrandAdapter brandAdapter;

    private List<CategoryObj> listCategories = new ArrayList<>();
    private CategorySpinnerAdapter categorySpinnerAdapter;

    private Spinner spType;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView tvNoData;

    private boolean isFirst = true;
    private boolean isFirstLoad = false;

    private int userId;
    private String filter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_brand, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        spType = rootView.findViewById(R.id.sp_type);

        tvNoData = rootView.findViewById(R.id.tv_no_data);
        progressBar = rootView.findViewById(R.id.progress_bar);
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);

        rcvBrand = rootView.findViewById(R.id.rcv_brand);
        rcvBrand.setHasFixedSize(true);
        rcvBrand.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        if (isLoggedIn())
            userId = preferenceManager.getUserLogin().getId();

        brandAdapter = new BrandAdapter(self, listBrands);
        rcvBrand.setAdapter(brandAdapter);

        categorySpinnerAdapter = new CategorySpinnerAdapter(self, listCategories);
        spType.setAdapter(categorySpinnerAdapter);

        if (getUserVisibleHint()) {
            getMenuCategory();
            getListBrand();
        }

    }

    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListBrand();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter = listCategories.get(position).getCatName();
                int catId = listCategories.get(position).getCatId();
                if (catId == -1) {
                    filter = 1 + "";
                } else if (catId == -2) {
                    filter = 3 + "";
                }

                if (!isFirst)
                    getListBrand();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMenuCategory() {
//        showDialog();
        ConnectServer.getResponseAPI().getMenuCategory().enqueue(new Callback<MenuCategoryRespond>() {
            @Override
            public void onResponse(Call<MenuCategoryRespond> call, Response<MenuCategoryRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCategories.clear();
                        listCategories.add(new CategoryObj("A To Z", -1));
                        listCategories.addAll(response.body().getData());
                        listCategories.add(new CategoryObj("Only Followed", -2));
                        categorySpinnerAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<MenuCategoryRespond> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                showToast(t.getMessage());
//                closeDialog();
            }
        });
    }

    private void getListBrand() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getHomeBrand(userId, filter).enqueue(new Callback<BrandRespond>() {
            @Override
            public void onResponse(Call<BrandRespond> call, Response<BrandRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listBrands.clear();
                        listBrands.addAll(response.body().getData());
                        brandAdapter.notifyDataSetChanged();
                        brandAdapter.fillSections();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                progressBar.setVisibility(View.GONE);
                checkNoData();
                isFirst = false;
            }

            @Override
            public void onFailure(Call<BrandRespond> call, Throwable t) {
                showToast(t.getMessage());
                progressBar.setVisibility(View.GONE);
                checkNoData();
                isFirst = false;
            }
        });
    }

    private void checkNoData() {
        if (listBrands.size() == 0) {
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
                getMenuCategory();
                getListBrand();
                isFirstLoad = true;
            }
        }
    }
}
