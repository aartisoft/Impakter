package com.impakter.impakter.fragment;

import android.content.Intent;
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
import com.impakter.impakter.activity.DetailActivity;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.activity.MessageDetailActivity;
import com.impakter.impakter.adapter.ActivityAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CreateRoomRespond;
import com.impakter.impakter.object.NotificationRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFragment extends BaseFragment {
    private View rootView;
    private RecyclerView rcvActivity;
    private ActivityAdapter activityAdapter;
    private List<NotificationRespond.Data> listActivities = new ArrayList<>();
    private TextView tvNoData;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int userId;
    private int page = 1;
    private int totalPage;
    private boolean isFirstLoad = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_activity, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        progressBar = rootView.findViewById(R.id.progress_bar);
        tvNoData = rootView.findViewById(R.id.tv_no_data);
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);

        rcvActivity = rootView.findViewById(R.id.rcv_activity);
        rcvActivity.setHasFixedSize(true);
        rcvActivity.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        userId = preferenceManager.getUserLogin().getId();

        activityAdapter = new ActivityAdapter(rcvActivity, self, listActivities);
        rcvActivity.setAdapter(activityAdapter);

        activityAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if (page <= totalPage) {
                    listActivities.add(null);
                    activityAdapter.notifyItemInserted(listActivities.size() - 1);
                    getMoreNotifications();
                }

            }
        });
        if (getUserVisibleHint()) {
            getNotifications();
        }
    }

    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                activityAdapter.setLoaded();
                activityAdapter.notifyDataSetChanged();
                getNotifications();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        activityAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NotificationRespond.Data data = listActivities.get(position);
                int type = listActivities.get(position).getType();
                switchScreen(data);
            }
        });
    }

    private void switchScreen(NotificationRespond.Data data) {
        switch (data.getType()) {
            case Constants.TYPE_LIKE_PRODUCT:
            case Constants.TYPE_FOLLOW:
                int userId = data.getParams().getId();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.USER_ID_COMMENT, userId + "");
                OtherProfileFragment otherProfileFragment = new OtherProfileFragment();
                otherProfileFragment.setArguments(bundle);
                ((MainActivity) self).showFragment(otherProfileFragment, true);
                break;
            case Constants.TYPE_MESSAGE:
                int receiverId = data.getParams().getId();
                String userName = data.getParams().getName();
                createConversation(receiverId, userName);
                break;
            case Constants.TYPE_UP_VOTE:
            case Constants.TYPE_RATE_PRODUCT:
            case Constants.TYPE_COMMENT_PRODUCT:
                int productId = data.getParams().getId();
                Intent intentProductDetail = new Intent(self, DetailActivity.class);
                intentProductDetail.putExtra(Constants.PRODUCT_ID, productId);
                gotoActivity(intentProductDetail);
                break;
        }
    }

    private void createConversation(final int otherUserId, final String userName) {
        showDialog();
        ConnectServer.getResponseAPI().createConversation(userId, otherUserId).enqueue(new Callback<CreateRoomRespond>() {
            @Override
            public void onResponse(Call<CreateRoomRespond> call, Response<CreateRoomRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.CONVERSATION_ID, response.body().getConversationId());
                        bundle.putInt(Constants.RECEIVER_ID, otherUserId);
                        bundle.putString(Constants.USER_NAME, userName);
                        gotoActivity(self, MessageDetailActivity.class, bundle);
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<CreateRoomRespond> call, Throwable t) {
                closeDialog();
                showToast(t.getMessage());
            }
        });
    }

    private void getNotifications() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getNotification(userId, page).enqueue(new Callback<NotificationRespond>() {
            @Override
            public void onResponse(Call<NotificationRespond> call, Response<NotificationRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPages();

                        listActivities.clear();
                        listActivities.addAll(response.body().getData());

                        activityAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                checkNoData();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NotificationRespond> call, Throwable t) {
                showToast(t.getMessage());
                checkNoData();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getMoreNotifications() {
        ConnectServer.getResponseAPI().getNotification(userId, page).enqueue(new Callback<NotificationRespond>() {
            @Override
            public void onResponse(Call<NotificationRespond> call, Response<NotificationRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listActivities.remove(listActivities.size() - 1);
                        listActivities.addAll(response.body().getData());
                        activityAdapter.setLoaded();
                        activityAdapter.notifyDataSetChanged();
                    } else {
                        listActivities.remove(listActivities.size() - 1);
                        activityAdapter.setLoaded();
                        activityAdapter.notifyItemRemoved(listActivities.size() - 1);
                    }
                }
                checkNoData();

            }

            @Override
            public void onFailure(Call<NotificationRespond> call, Throwable t) {
                showToast(t.getMessage());
                checkNoData();
                listActivities.remove(listActivities.size() - 1);
                activityAdapter.setLoaded();
                activityAdapter.notifyItemRemoved(listActivities.size() - 1);
            }
        });
    }

    private void checkNoData() {
        if (listActivities.size() == 0) {
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
                getNotifications();
                isFirstLoad = true;
            }
        }
    }
}
