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
import android.widget.ImageView;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.adapter.ListPeopleAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.ActionFollowRespond;
import com.impakter.impakter.object.ContactRespond;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ImageView ivBack, ivSearch;
    private TextViewHeeboRegular tvTitle;
    private RecyclerView rcvPeople;
    private List<ContactRespond.Data> listPepople = new ArrayList<>();
    private ListPeopleAdapter listPeopleAdapter;
    private int userId;//Id of seller
    private int yourId;//Id of buyer
    private boolean followStatus;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_follow, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        ((MainActivity) self).hideToolbar();
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);

        ivBack = rootView.findViewById(R.id.iv_back);
        ivSearch = rootView.findViewById(R.id.iv_search);

        tvTitle = rootView.findViewById(R.id.tv_title);

        rcvPeople = rootView.findViewById(R.id.rcv_people);
        rcvPeople.setHasFixedSize(true);
        rcvPeople.setLayoutManager(new LinearLayoutManager(self));

    }

    private void initData() {
        yourId = preferenceManager.getUserLogin().getId();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString(Constants.TITLE);
            tvTitle.setText(title);
            userId = bundle.getInt(Constants.SELLER_ID);
        }
        listPeopleAdapter = new ListPeopleAdapter(self, listPepople);
        rcvPeople.setAdapter(listPeopleAdapter);

        getListFollowing();
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);

        listPeopleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (userId == yourId) {
                    follow((TextView) view, Constants.UN_FOLLOW, listPepople.get(position).getId(), position);
                } else {
                    if (listPepople.get(position).getFollowStatus()) {
                        follow((TextView) view, Constants.UN_FOLLOW, listPepople.get(position).getId(), position);
                    } else {
                        follow((TextView) view, Constants.FOLLOW, listPepople.get(position).getId(), position);
                    }
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListFollowing();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void follow(final TextView btnFollow, final int action, int sellerId, final int position) {
        showDialog();
        ConnectServer.getResponseAPI().follow(yourId, sellerId, action).enqueue(new Callback<ActionFollowRespond>() {
            @Override
            public void onResponse(Call<ActionFollowRespond> call, Response<ActionFollowRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showToast(response.body().getMessage());
                        if (action == Constants.FOLLOW) {
                            btnFollow.setText(getResources().getString(R.string.following));
                            listPepople.get(position).setFollowStatus(true);
                        } else {
                            btnFollow.setText(getResources().getString(R.string.follow));
                            listPepople.get(position).setFollowStatus(false);
                        }
                        listPeopleAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<ActionFollowRespond> call, Throwable t) {
                showToast(t.getMessage());
                closeDialog();
            }
        });
    }

    private void getListFollowing() {
        showDialog();
        ConnectServer.getResponseAPI().getListFollowing(userId, yourId).enqueue(new Callback<ContactRespond>() {
            @Override
            public void onResponse(Call<ContactRespond> call, Response<ContactRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listPepople.clear();
                        listPepople.addAll(response.body().getData());
                        listPeopleAdapter.notifyDataSetChanged();
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<ContactRespond> call, Throwable t) {
                closeDialog();
                showToast(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getFragmentManager().popBackStack();
//                ((MainActivity) self).showToolbar();
                break;

            case R.id.iv_search:

                break;
        }
    }
}
