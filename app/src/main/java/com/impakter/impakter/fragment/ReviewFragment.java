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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.ReviewAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.ReviewRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends BaseFragment {
    private View rootView;
    private RecyclerView rcvReview;
    private List<ReviewRespond.Data> listReviews = new ArrayList<>();
    private ReviewAdapter reviewAdapter;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoData;

    private int sellerId;
    private int page = 1;
    private int totalPage;

    private boolean isFirstLoad = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_review, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);
        progressBar = rootView.findViewById(R.id.progress_bar);
        tvNoData = rootView.findViewById(R.id.tv_no_data);

        rcvReview = rootView.findViewById(R.id.rcv_review);
        rcvReview.setHasFixedSize(true);
        rcvReview.setLayoutManager(new LinearLayoutManager(self));

    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            sellerId = bundle.getInt(Constants.SELLER_ID);
        }
        reviewAdapter = new ReviewAdapter(rcvReview, self, listReviews);
        rcvReview.setAdapter(reviewAdapter);

        reviewAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if (page <= totalPage) {
                    listReviews.add(null);
                    reviewAdapter.notifyItemInserted(listReviews.size() - 1);
                    getMoreListReviews();
                }

            }
        });
        if (getUserVisibleHint())
            getListReviews();
    }

    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                reviewAdapter.setLoaded();
                reviewAdapter.notifyDataSetChanged();
                getListReviews();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        reviewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void getListReviews() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getListReview(sellerId, 1).enqueue(new Callback<ReviewRespond>() {
            @Override
            public void onResponse(Call<ReviewRespond> call, Response<ReviewRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPages();

                        listReviews.clear();
                        listReviews.addAll(response.body().getData());
                        reviewAdapter.notifyDataSetChanged();
                    }
                }
                progressBar.setVisibility(View.GONE);
                checkNoData();
            }

            @Override
            public void onFailure(Call<ReviewRespond> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showToast(t.getMessage());
                checkNoData();
            }
        });
    }

    private void getMoreListReviews() {
        ConnectServer.getResponseAPI().getListReview(sellerId, page).enqueue(new Callback<ReviewRespond>() {
            @Override
            public void onResponse(Call<ReviewRespond> call, Response<ReviewRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listReviews.remove(listReviews.size() - 1);

                        listReviews.addAll(response.body().getData());
                        reviewAdapter.setLoaded();
                        reviewAdapter.notifyDataSetChanged();
                    } else {
                        listReviews.remove(listReviews.size() - 1);
                        reviewAdapter.setLoaded();
                        reviewAdapter.notifyItemRemoved(listReviews.size() - 1);
                        showToast(response.body().getMessage());
                    }
                }
                checkNoData();
            }

            @Override
            public void onFailure(Call<ReviewRespond> call, Throwable t) {
                showToast(t.getMessage());
                checkNoData();
                listReviews.remove(listReviews.size() - 1);
                reviewAdapter.setLoaded();
                reviewAdapter.notifyItemRemoved(listReviews.size() - 1);
            }
        });
    }

    private void checkNoData() {
        if (listReviews.size() == 0) {
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
                getListReviews();
                isFirstLoad = true;
            }
        }
    }
}
