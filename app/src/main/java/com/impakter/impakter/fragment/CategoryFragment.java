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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.adapter.CategoryAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.HomeCategoryRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends BaseFragment {
    private View rootView;
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private List<HomeCategoryRespond.Data> listHomeCategories = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private boolean isFirstLoad = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        rcvCategory = rootView.findViewById(R.id.rcv_category);
        rcvCategory.setHasFixedSize(true);
        rcvCategory.setLayoutManager(new LinearLayoutManager(self));

        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);
        progressBar = rootView.findViewById(R.id.progress_bar);

    }

    private void initData() {
        categoryAdapter = new CategoryAdapter(self, listHomeCategories);
        rcvCategory.setAdapter(categoryAdapter);

        if (getUserVisibleHint() && !isFirstLoad)
            getHomeCategory();
    }

    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomeCategory();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getHomeCategory() {
//        showDialog();
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getHomeCategory().enqueue(new Callback<HomeCategoryRespond>() {
            @Override
            public void onResponse(Call<HomeCategoryRespond> call, Response<HomeCategoryRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listHomeCategories.clear();
                        listHomeCategories.addAll(response.body().getData());
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
//                closeDialog();
            }

            @Override
            public void onFailure(Call<HomeCategoryRespond> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
//                closeDialog();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible()) {
            if (isVisibleToUser && !isFirstLoad) {
                getHomeCategory();
                isFirstLoad = true;
            }
        }
    }
}
