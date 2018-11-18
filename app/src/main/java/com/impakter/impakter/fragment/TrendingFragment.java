package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.impakter.impakter.adapter.CategorySpinnerAdapter;
import com.impakter.impakter.adapter.TrendingAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CategoryObj;
import com.impakter.impakter.object.HomeTrendingRespond;
import com.impakter.impakter.object.MenuCategoryRespond;
import com.impakter.impakter.object.ProductObj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingFragment extends BaseFragment {
    private View rootView;
    private Spinner spCategory;
    private RecyclerView rcvTrending;
    private ArrayList<HomeTrendingRespond.Data> listProducts = new ArrayList<>();
    private TrendingAdapter trendingAdapter;
    private List<CategoryObj> listCategories = new ArrayList<>();
    private CategorySpinnerAdapter categorySpinnerAdapter;

    private String categoryId;
    private int page = 1;
    private int totalPage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoData;
    private boolean isFirst = true;
    private ProgressBar progressBar;
    private boolean isFirstLoad = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trending, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        tvNoData = rootView.findViewById(R.id.tv_no_data);
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);
        spCategory = rootView.findViewById(R.id.sp_category);
        progressBar = rootView.findViewById(R.id.progress_bar);

        rcvTrending = rootView.findViewById(R.id.rcv_trending);
        rcvTrending.setHasFixedSize(true);
        rcvTrending.setLayoutManager(new GridLayoutManager(self, 3));
    }

    private void initData() {
        trendingAdapter = new TrendingAdapter(rcvTrending, self, listProducts);
        rcvTrending.setAdapter(trendingAdapter);


        trendingAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if (page <= totalPage) {
                    listProducts.add(null);
                    trendingAdapter.notifyItemInserted(listProducts.size() - 1);
                    getMoreHomeTrending();
                }

            }
        });

        categorySpinnerAdapter = new CategorySpinnerAdapter(self, listCategories);
        spCategory.setAdapter(categorySpinnerAdapter);

        if (getUserVisibleHint() && !isFirstLoad) {
            getMenuCategory();
            getHomeTrending();
        }

    }


    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                trendingAdapter.setLoaded();
                trendingAdapter.notifyDataSetChanged();
                getHomeTrending();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = listCategories.get(position).getCatId() + "";
                if (categoryId.equals("-1"))
                    categoryId = "";
                page = 1;
                trendingAdapter.setLoaded();
                trendingAdapter.notifyDataSetChanged();
                if (!isFirst)
                    getHomeTrending();
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
                        listCategories.add(new CategoryObj("ALL", -1));
                        listCategories.addAll(response.body().getData());
                        categorySpinnerAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                } else {
                    showToast(response.body().getMessage());
                }
//                closeDialog();
            }

            @Override
            public void onFailure(Call<MenuCategoryRespond> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                progressBar.setVisibility(View.GONE);
//                closeDialog();
            }
        });
    }

    private void getHomeTrending() {
//        showDialog();
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getHomeTrending(categoryId, 1).enqueue(new Callback<HomeTrendingRespond>() {
            @Override
            public void onResponse(Call<HomeTrendingRespond> call, Response<HomeTrendingRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPage();
                        listProducts.clear();
                        listProducts.addAll(response.body().getData());
                        trendingAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                } else {
                    showToast(response.body().getMessage());
                }
                isFirst = false;
                checkNoData();
                progressBar.setVisibility(View.GONE);
//                closeDialog();
            }

            @Override
            public void onFailure(Call<HomeTrendingRespond> call, Throwable t) {
                isFirst = false;
//                closeDialog();
                progressBar.setVisibility(View.GONE);
                showToast(t.getMessage());
                checkNoData();
            }
        });
    }

    private void getMoreHomeTrending() {
        ConnectServer.getResponseAPI().getHomeTrending(categoryId, page).enqueue(new Callback<HomeTrendingRespond>() {
            @Override
            public void onResponse(Call<HomeTrendingRespond> call, Response<HomeTrendingRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listProducts.remove(listProducts.size() - 1);
                        listProducts.addAll(response.body().getData());
                        trendingAdapter.setLoaded();
                        trendingAdapter.notifyDataSetChanged();
                    } else {
                        listProducts.remove(listProducts.size() - 1);
                        trendingAdapter.setLoaded();
                        trendingAdapter.notifyItemRemoved(listProducts.size() - 1);
                        showToast(response.body().getMessage());
                    }
                } else {
                    showToast(response.body().getMessage());
                }
                checkNoData();
            }

            @Override
            public void onFailure(Call<HomeTrendingRespond> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                checkNoData();
                showToast(t.getMessage());
                listProducts.remove(listProducts.size() - 1);
                trendingAdapter.setLoaded();
                trendingAdapter.notifyItemRemoved(listProducts.size() - 1);
            }
        });
    }

    private void checkNoData() {
        if (listProducts.size() == 0) {
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
                getHomeTrending();
                isFirstLoad = true;
            }
        }
    }
}
