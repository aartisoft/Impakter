package com.impakter.impakter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.MessageAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.ConversationRespond;
import com.impakter.impakter.object.RoomRespond;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private RecyclerView rcvMessage;
    private MessageAdapter messageAdapter;
    private List<ConversationRespond.Data> listChatRooms = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoData;
    private ProgressBar progressBar;

    private int userId;
    private int page = 1;
    private int totalPage;

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initViews();
        initData();
        initControl();
//        mListener();
    }

    private void mListener() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.REFRESH)) {
                    int position = intent.getIntExtra(Constants.CURRENT_POSITION, -1);
                    String lastMessage = intent.getStringExtra(Constants.MESSAGE);
                    listChatRooms.get(position).setLastMessage(lastMessage);
                    messageAdapter.notifyItemChanged(position);
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction(Constants.REFRESH);
        registerReceiver(broadcastReceiver, filter);
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);

        tvNoData = findViewById(R.id.tv_no_data);
        progressBar = findViewById(R.id.progress_bar);
        swipeRefreshLayout = findViewById(R.id.swf_layout);

        rcvMessage = findViewById(R.id.rcv_message);
        rcvMessage.setHasFixedSize(true);
        rcvMessage.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        if (isLoggedIn())
            userId = preferencesManager.getUserLogin().getId();

        messageAdapter = new MessageAdapter(rcvMessage, self, listChatRooms);
        rcvMessage.setAdapter(messageAdapter);


        messageAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if (page <= totalPage) {
                    listChatRooms.add(null);
                    messageAdapter.notifyItemInserted(listChatRooms.size() - 1);
                    getMoreListChatRooms();
                } else {
                    page = 1;
                    messageAdapter.setLoaded();
                    messageAdapter.notifyDataSetChanged();
                }

            }
        });

        getListChatRooms();
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                messageAdapter.setLoaded();
                messageAdapter.notifyDataSetChanged();
                getListChatRooms();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        messageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int receiverId;
                if (listChatRooms.get(position).getSenderId() == userId) {
                    receiverId = listChatRooms.get(position).getReceiverId();
                } else {
                    receiverId = listChatRooms.get(position).getSenderId();
                }
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.CONVERSATION_ID, listChatRooms.get(position).getConversationId());
                bundle.putInt(Constants.RECEIVER_ID, receiverId);
                bundle.putInt(Constants.CURRENT_POSITION, position);
                bundle.putString(Constants.USER_NAME, listChatRooms.get(position).getName());
                gotoActivity(self, MessageDetailActivity.class, bundle);
            }
        });
    }

    private void getListChatRooms() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getListConversation(userId, 1).enqueue(new Callback<ConversationRespond>() {
            @Override
            public void onResponse(Call<ConversationRespond> call, Response<ConversationRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPages();

                        listChatRooms.clear();
                        listChatRooms.addAll(response.body().getData());
                        Collections.sort(listChatRooms, new Comparator<ConversationRespond.Data>() {
                            @Override
                            public int compare(ConversationRespond.Data o1, ConversationRespond.Data o2) {
                                return (int) (o2.getLastTime() - o1.getLastTime());
                            }
                        });
                        messageAdapter.notifyDataSetChanged();
                    }
                }
                progressBar.setVisibility(View.GONE);
                checkNoData();
            }

            @Override
            public void onFailure(Call<ConversationRespond> call, Throwable t) {
                showToast(t.getMessage());
                progressBar.setVisibility(View.GONE);
                checkNoData();
            }
        });
    }

    private void getMoreListChatRooms() {
        ConnectServer.getResponseAPI().getListConversation(userId, page).enqueue(new Callback<ConversationRespond>() {
            @Override
            public void onResponse(Call<ConversationRespond> call, Response<ConversationRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listChatRooms.remove(listChatRooms.size() - 1);

                        listChatRooms.addAll(response.body().getData());
                        messageAdapter.setLoaded();
                        Collections.sort(listChatRooms, new Comparator<ConversationRespond.Data>() {
                            @Override
                            public int compare(ConversationRespond.Data o1, ConversationRespond.Data o2) {
                                return (int) (o2.getLastTime() - o1.getLastTime());
                            }
                        });
                        messageAdapter.notifyDataSetChanged();
                    }
                }
                checkNoData();
            }

            @Override
            public void onFailure(Call<ConversationRespond> call, Throwable t) {
                showToast(t.getMessage());
                checkNoData();
                listChatRooms.remove(listChatRooms.size() - 1);
                messageAdapter.notifyItemRemoved(listChatRooms.size() - 1);
            }
        });
    }

    private void checkNoData() {
        if (listChatRooms.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
    }
}
