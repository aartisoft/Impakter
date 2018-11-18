package com.impakter.impakter.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.impakter.impakter.activity.MessageDetailActivity;
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

public class MessageFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
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
    private boolean isFirstLoad;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message, container, false);
        initViews();
        initData();
        initControl();
        mListener();
        return rootView;
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
        self.registerReceiver(broadcastReceiver, filter);
    }

    private void initViews() {
        tvNoData = rootView.findViewById(R.id.tv_no_data);
        progressBar = rootView.findViewById(R.id.progress_bar);
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);

        rcvMessage = rootView.findViewById(R.id.rcv_message);
        rcvMessage.setHasFixedSize(true);
        rcvMessage.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        if (isLoggedIn())
            userId = preferenceManager.getUserLogin().getId();

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
        if (getUserVisibleHint())
            getListChatRooms();
    }

    private void initControl() {
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

                Intent intent = new Intent(self, MessageDetailActivity.class);
                intent.putExtras(bundle);

                self.startActivity(intent);
                self.overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
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

                        Collections.sort(listChatRooms, new Comparator<ConversationRespond.Data>() {
                            @Override
                            public int compare(ConversationRespond.Data o1, ConversationRespond.Data o2) {
                                return (int) (o2.getLastTime() - o1.getLastTime());
                            }
                        });

                        messageAdapter.setLoaded();
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

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible()) {
            if (isVisibleToUser && !isFirstLoad) {
                getListChatRooms();
                isFirstLoad = true;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        self.unregisterReceiver(broadcastReceiver);
    }
}
