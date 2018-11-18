package com.impakter.impakter.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.adapter.ViewPagerAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.UserObj;
import com.impakter.impakter.object.UserRespond;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ImageView ivCover;
    private CircleImageView ivAvatar;
    private TextView tvName, tvCareer, tvAddress, tvCollection, tvFollower, tvFollowing;
    private SmartTabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout layoutFollower, layoutFollowing;
    private LinearLayout btnEdit;

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter filter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews();
        setUpToolbar();
        initData();
        initControl();
        mListener();
        return rootView;
    }

    private void mListener() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.UPDATE_PROFILE)) {
                    getUserInfo(preferenceManager.getUserLogin().getId());
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction(Constants.UPDATE_PROFILE);
        self.registerReceiver(broadcastReceiver, filter);
    }

    private void setUpToolbar() {
//        LinearLayout toolbar = ((MainActivity) self).getToolbar();
        ImageView ivSearch = rootView.findViewById(R.id.iv_search);
        TextView tvTitle = rootView.findViewById(R.id.tv_title);
        btnEdit = rootView.findViewById(R.id.btn_edit);
        btnEdit.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        tvName = rootView.findViewById(R.id.tv_name);
        tvCareer = rootView.findViewById(R.id.tv_career);
        tvAddress = rootView.findViewById(R.id.tv_address);
        tvCollection = rootView.findViewById(R.id.tv_collection);
        tvFollower = rootView.findViewById(R.id.tv_follower);
        tvFollowing = rootView.findViewById(R.id.tv_following);

        ivAvatar = rootView.findViewById(R.id.iv_avatar);
        ivCover = rootView.findViewById(R.id.iv_cover);

        viewPager = rootView.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.setViewPager(viewPager);

        layoutFollower = rootView.findViewById(R.id.layout_follower);
        layoutFollowing = rootView.findViewById(R.id.lay_following);
    }

    private void initData() {
//        UserObj userObj = preferenceManager.getUserLogin();
//        tvName.setText(userObj.getUsername());
//        tvAddress.setText(userObj.getAddress());
//        tvCollection.setText(userObj.getNumberOfCollection() + "");
//        tvFollowing.setText(userObj.getNumberOfFollowings() + "");
//        tvFollower.setText(userObj.getNumberOfFollowers() + "");
//
//        Glide.with(self).load(userObj.getAvatar()).into(ivAvatar);
//        Glide.with(self).load(userObj.getCover()).into(ivCover);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt(Constants.CURRENT_POSITION);
            viewPager.setCurrentItem(position);
        }

        getUserInfo(preferenceManager.getUserLogin().getId());

    }

    private void getUserInfo(int userId) {
        showDialog();
        ConnectServer.getResponseAPI().getUserInfo(userId + "", userId + "").enqueue(new Callback<UserRespond>() {
            @Override
            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        UserObj userObj = response.body().getUserObj();
                        tvName.setText(userObj.getUsername());
                        tvAddress.setText(userObj.getAddress());
                        tvCollection.setText(userObj.getNumberOfCollection() + "");
                        tvFollower.setText(userObj.getNumberOfFollowers() + "");
                        tvFollowing.setText(userObj.getNumberOfFollowings() + "");

                        Glide.with(self).load(userObj.getAvatar()).into(ivAvatar);
                        Glide.with(self).load(userObj.getCover()).into(ivCover);
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<UserRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                closeDialog();
            }
        });
    }

    private void initControl() {
        layoutFollower.setOnClickListener(this);
        layoutFollowing.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new MyFavouriteFragment(), getResources().getString(R.string.favorite));
        adapter.addFrag(new MessageFragment(), getResources().getString(R.string.messages));
        adapter.addFrag(new OrderFragment(), getResources().getString(R.string.orders));
        adapter.addFrag(new ActivityFragment(), getResources().getString(R.string.activity));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.layout_follower:
                FollowerFragment followFragment = new FollowerFragment();
                bundle.putString(Constants.TITLE, getResources().getString(R.string.follower));
                followFragment.setArguments(bundle);
                ((MainActivity) self).showFragmentWithAddMethod(followFragment, true);
                break;
            case R.id.lay_following:
                FollowingFragment followingFragment = new FollowingFragment();
                bundle.putString(Constants.TITLE, getResources().getString(R.string.following));
                bundle.putInt(Constants.SELLER_ID, preferenceManager.getUserLogin().getId());
                followingFragment.setArguments(bundle);
                ((MainActivity) self).showFragmentWithAddMethod(followingFragment, true);
                break;
            case R.id.btn_edit:
                ((MainActivity) self).showFragmentWithAddMethod(new UpdateProfileFragment(), true);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        self.unregisterReceiver(broadcastReceiver);
    }
}
