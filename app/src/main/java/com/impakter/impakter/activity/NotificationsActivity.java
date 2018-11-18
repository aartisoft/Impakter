package com.impakter.impakter.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.ActivityAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.fragment.OtherProfileFragment;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CreateRoomRespond;
import com.impakter.impakter.object.NotificationRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private RecyclerView rcvNotification;
    private ActivityAdapter activityAdapter;
    private List<NotificationRespond.Data> listNotifications = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView tvNoData;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int userId;
    private int page = 1;
    private int totalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvNoData = findViewById(R.id.tv_no_data);
        progressBar = findViewById(R.id.progress_bar);
        swipeRefreshLayout = findViewById(R.id.swf_layout);

        rcvNotification = findViewById(R.id.rcv_notification);
        rcvNotification.setHasFixedSize(true);
        rcvNotification.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        userId = preferencesManager.getUserLogin().getId();

        activityAdapter = new ActivityAdapter(rcvNotification, self, listNotifications);
        rcvNotification.setAdapter(activityAdapter);

        activityAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if (page <= totalPage) {
                    listNotifications.add(null);
                    activityAdapter.notifyItemInserted(listNotifications.size() - 1);
                    getMoreNotifications();
                }

            }
        });

        getNotifications();
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
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
                NotificationRespond.Data data = listNotifications.get(position);
                int type = listNotifications.get(position).getType();
                switchScreen(data);
            }
        });
    }

    private void switchScreen(NotificationRespond.Data data) {
        switch (data.getType()) {
            case Constants.TYPE_LIKE_PRODUCT:
            case Constants.TYPE_FOLLOW:
                int userId = data.getParams().getId();
                Intent intent = new Intent();
                intent.putExtra(Constants.USER_ID_COMMENT, userId + "");
                setResult(Activity.RESULT_OK, intent);
                finish();
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
                startActivity(intentProductDetail);
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
        ConnectServer.getResponseAPI().getNotification(12, page).enqueue(new Callback<NotificationRespond>() {
            @Override
            public void onResponse(Call<NotificationRespond> call, Response<NotificationRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPages();

                        listNotifications.clear();
                        listNotifications.addAll(response.body().getData());

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
        ConnectServer.getResponseAPI().getNotification(12, page).enqueue(new Callback<NotificationRespond>() {
            @Override
            public void onResponse(Call<NotificationRespond> call, Response<NotificationRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listNotifications.remove(listNotifications.size() - 1);
                        listNotifications.addAll(response.body().getData());
                        activityAdapter.setLoaded();
                        activityAdapter.notifyDataSetChanged();
                    } else {
                        listNotifications.remove(listNotifications.size() - 1);
                        activityAdapter.setLoaded();
                        activityAdapter.notifyItemRemoved(listNotifications.size() - 1);
                    }
                }
                checkNoData();

            }

            @Override
            public void onFailure(Call<NotificationRespond> call, Throwable t) {
                showToast(t.getMessage());
                checkNoData();
                listNotifications.remove(listNotifications.size() - 1);
                activityAdapter.setLoaded();
                activityAdapter.notifyItemRemoved(listNotifications.size() - 1);
            }
        });
    }

    private void checkNoData() {
        if (listNotifications.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
