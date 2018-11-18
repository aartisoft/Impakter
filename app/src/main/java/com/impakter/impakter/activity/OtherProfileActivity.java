package com.impakter.impakter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.CollectionOtherPeopleAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.fragment.FavouriteOtherPeopleDetailFragment;
import com.impakter.impakter.fragment.FollowerFragment;
import com.impakter.impakter.fragment.FollowingFragment;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.ActionFollowRespond;
import com.impakter.impakter.object.CollectionObj;
import com.impakter.impakter.object.CollectionRespond;
import com.impakter.impakter.object.UserObj;
import com.impakter.impakter.object.UserRespond;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivCover;
    private CircleImageView ivAvatar;
    private TextView tvName, tvCareer, tvAddress, tvCollection, tvFollower, tvFollowing;
    private TextView tvNoData;
    private Button btnFollow, btnMessage;
    private LinearLayout btnFollower, btnFollowing;

    private RecyclerView rcvCollection;
    private List<CollectionObj> listCollections = new ArrayList<>();
    private CollectionOtherPeopleAdapter collectionAdapter;
    private int otherUserId;
    private int userId;
    private boolean followStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_other_profile);
        initViews();
        setUpToolbar();
        initData();
        initControl();
    }


    private void setUpToolbar() {
//        LinearLayout toolbar = ((MainActivity) self).getToolbar();
        ImageView ivSearch = findViewById(R.id.iv_search);
        LinearLayout btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
    }

    private void initViews() {
        tvName = findViewById(R.id.tv_name);
        tvCareer = findViewById(R.id.tv_career);
        tvAddress = findViewById(R.id.tv_address);
        tvCollection = findViewById(R.id.tv_collection);
        tvFollower = findViewById(R.id.tv_follower);
        tvFollowing = findViewById(R.id.tv_following);
        tvNoData = findViewById(R.id.tv_no_data);

        btnFollow = findViewById(R.id.btn_follow);
        btnMessage = findViewById(R.id.btn_message);
        btnFollower = findViewById(R.id.btn_follower);
        btnFollowing = findViewById(R.id.btn_following);

        ivAvatar = findViewById(R.id.iv_avatar);
        ivCover = findViewById(R.id.iv_cover);

        rcvCollection = findViewById(R.id.rcv_collection);
        rcvCollection.setHasFixedSize(true);
        rcvCollection.setLayoutManager(new LinearLayoutManager(self));
        ViewCompat.setNestedScrollingEnabled(rcvCollection, false);

        collectionAdapter = new CollectionOtherPeopleAdapter(self, listCollections);
        rcvCollection.setAdapter(collectionAdapter);
    }

    private void initData() {
        userId = preferencesManager.getUserLogin().getId();
        Intent intent = getIntent();
        if (intent != null) {
            otherUserId = Integer.parseInt(intent.getStringExtra(Constants.USER_ID_COMMENT));
        }
        getUserInfo(otherUserId, userId);
        getCollection();
    }

    private void initControl() {
        btnFollow.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        btnFollower.setOnClickListener(this);
        btnFollowing.setOnClickListener(this);
        collectionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int collectionId = listCollections.get(position).getId();
                FavouriteOtherPeopleDetailFragment favouriteOtherPeopleDetailFragment = new FavouriteOtherPeopleDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.COLLECTION_ID, collectionId);
                favouriteOtherPeopleDetailFragment.setArguments(bundle);

                ((MainActivity) self).hideToolbar();
                ((MainActivity) self).showFragmentWithAddMethod(favouriteOtherPeopleDetailFragment, true);
            }
        });
    }

    private void getUserInfo(final int otherUserId, int userId) {
        ConnectServer.getResponseAPI().getUserInfo(otherUserId + "", userId + "").enqueue(new Callback<UserRespond>() {
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

                        if (userObj.getFollowStatus() == 0) {
                            followStatus = false;
                            btnFollow.setText(getResources().getString(R.string.follow));
                        } else {
                            followStatus = true;
                            btnFollow.setText(getResources().getString(R.string.following));
                        }

                        Glide.with(self).load(userObj.getAvatar()).into(ivAvatar);
                        Glide.with(self).load(userObj.getCover()).into(ivCover);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCollection() {
        showDialog();
        ConnectServer.getResponseAPI().getCollection(otherUserId, "").enqueue(new Callback<CollectionRespond>() {
            @Override
            public void onResponse(Call<CollectionRespond> call, Response<CollectionRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCollections.clear();
                        listCollections.addAll(response.body().getData());
                        collectionAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                checkNoData();
                closeDialog();
            }

            @Override
            public void onFailure(Call<CollectionRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                closeDialog();
                checkNoData();
            }
        });
    }

    private void checkNoData() {
        if (listCollections.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
    }

    private void follow(final int action) {
        showDialog();
        ConnectServer.getResponseAPI().follow(userId, otherUserId, action).enqueue(new Callback<ActionFollowRespond>() {
            @Override
            public void onResponse(Call<ActionFollowRespond> call, Response<ActionFollowRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showToast(response.body().getMessage());
                        followStatus = response.body().getFollowStatus();
                        if (action == Constants.FOLLOW) {
                            btnFollow.setText(getResources().getString(R.string.following));
                        } else {
                            btnFollow.setText(getResources().getString(R.string.follow));
                        }
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

    //    private void createConversation() {
//        showDialog();
//        ConnectServer.getResponseAPI().createConversation(Integer.parseInt(userId), sellerId).enqueue(new Callback<CreateRoomRespond>() {
//            @Override
//            public void onResponse(Call<CreateRoomRespond> call, Response<CreateRoomRespond> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
//                        Bundle bundle = new Bundle();
//                        bundle.putInt(Constants.CONVERSATION_ID, response.body().getConversationId());
//                        bundle.putInt(Constants.RECEIVER_ID, sellerId);
//                        gotoActivity(self, MessageDetailActivity.class, bundle);
//                    }
//                }
//                closeDialog();
//            }
//
//            @Override
//            public void onFailure(Call<CreateRoomRespond> call, Throwable t) {
//                closeDialog();
//                showToast(t.getMessage());
//            }
//        });
//    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.btn_follow:
                if (isLoggedIn()) {
                    if (followStatus) {
                        follow(Constants.UN_FOLLOW);
                    } else {
                        follow(Constants.FOLLOW);
                    }
                } else {
                    showConfirmLoginDialog();
                }
                break;
            case R.id.btn_message:

                break;
            case R.id.btn_follower:
                FollowerFragment followFragment = new FollowerFragment();
                bundle.putString(Constants.TITLE, getResources().getString(R.string.follower));
                followFragment.setArguments(bundle);
                ((MainActivity) self).showFragmentWithAddMethod(followFragment, true);
                break;
            case R.id.btn_following:
                FollowingFragment followingFragment = new FollowingFragment();
                bundle.putString(Constants.TITLE, getResources().getString(R.string.following));
                bundle.putInt(Constants.SELLER_ID, otherUserId);
                followingFragment.setArguments(bundle);
                ((MainActivity) self).showFragmentWithAddMethod(followingFragment, true);
                break;
        }
    }
}
