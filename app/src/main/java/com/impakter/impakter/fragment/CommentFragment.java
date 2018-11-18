package com.impakter.impakter.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.activity.MyBagActivity;
import com.impakter.impakter.adapter.CommentAdapter;
import com.impakter.impakter.adapter.MoreImageAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnAddToBagClickListener;
import com.impakter.impakter.events.OnBuyNowClickListener;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CommentRespond;
import com.impakter.impakter.object.ProductDetailRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private RecyclerView rcvComment;
    private CommentAdapter commentAdapter;
    private List<CommentRespond.Data> listComments = new ArrayList<>();
    private int userId, productId;
    private int page = 1;
    private String content = "";
    private TextView tvShowMore, tvNoData;
    private int totalPage;

    private RecyclerView rcvImage;
    private LinearLayout btnAddToBag, btnBuyNow;
    private MoreImageAdapter moreImageAdapter;
    private ArrayList<ProductDetailRespond.MoreFromThisBrand> listImages = new ArrayList<>();
    private boolean isFirstLoad = false;

    private OnAddToBagClickListener onAddToBagClickListener;
    private OnBuyNowClickListener onBuyNowClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAddToBagClickListener = (OnAddToBagClickListener) context;
        onBuyNowClickListener = (OnBuyNowClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        tvShowMore = rootView.findViewById(R.id.tv_show_more);
        tvNoData = rootView.findViewById(R.id.tv_no_data);

        rcvComment = rootView.findViewById(R.id.rcv_comment);
        rcvComment.setHasFixedSize(true);
        rcvComment.setLayoutManager(new LinearLayoutManager(self));
        ViewCompat.setNestedScrollingEnabled(rcvComment, false);

        btnAddToBag = rootView.findViewById(R.id.btn_add_to_bag);
        btnBuyNow = rootView.findViewById(R.id.btn_buy_now);

        rcvImage = rootView.findViewById(R.id.rcv_image);
        rcvImage.setHasFixedSize(true);
        rcvImage.setLayoutManager(new LinearLayoutManager(self, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initData() {
        if (isLoggedIn())
            userId = preferenceManager.getUserLogin().getId();
        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = bundle.getInt(Constants.PRODUCT_ID);
            listImages = bundle.getParcelableArrayList(Constants.ARR_IMAGE);
        }
        moreImageAdapter = new MoreImageAdapter(self, listImages);
        rcvImage.setAdapter(moreImageAdapter);

        commentAdapter = new CommentAdapter(self, listComments);
        rcvComment.setAdapter(commentAdapter);

        if (!isFirstLoad)
            comment();

    }

    private void initControl() {
        tvShowMore.setOnClickListener(this);
        btnAddToBag.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        commentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String id = listComments.get(position).getUserId();
                if (!id.equals(userId + "")) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.USER_ID_COMMENT, id);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }

//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.USER_ID_COMMENT, id);
//                ProfileFragment profileFragment = new ProfileFragment();
//                profileFragment.setArguments(bundle);
//                ((MainActivity) self).showFragment(profileFragment, true);
            }
        });
    }

    private void comment() {
        showDialog();
        ConnectServer.getResponseAPI().comment(productId, userId, content, 1).enqueue(new Callback<CommentRespond>() {
            @Override
            public void onResponse(Call<CommentRespond> call, Response<CommentRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        if (TextUtils.isEmpty(content)) {
                            listComments.clear();
                            totalPage = response.body().getAllPages();
                            listComments.addAll(response.body().getData());
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            listComments.add(0, response.body().getData().get(0));
                            commentAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                checkNoData();
                closeDialog();
            }

            @Override
            public void onFailure(Call<CommentRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                checkNoData();
                closeDialog();
            }
        });
    }

    private void moreComment() {
        showDialog();
        ConnectServer.getResponseAPI().comment(productId, userId, content, page).enqueue(new Callback<CommentRespond>() {
            @Override
            public void onResponse(Call<CommentRespond> call, Response<CommentRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        if (TextUtils.isEmpty(content)) {
                            listComments.addAll(response.body().getData());
                            commentAdapter.notifyDataSetChanged();
                        } else {
                            listComments.add(0, response.body().getData().get(0));
                            commentAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                checkNoData();
                closeDialog();
            }

            @Override
            public void onFailure(Call<CommentRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                checkNoData();
                closeDialog();
            }
        });
    }

    private void checkNoData() {
        if (listComments.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
        if (page < totalPage) {
            tvShowMore.setVisibility(View.VISIBLE);
        } else {
            tvShowMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_more:
                page++;
                if (page <= totalPage) {
                    moreComment();
                } else {
                    page = 1;
                    Toast.makeText(self, getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_add_to_bag:
                onAddToBagClickListener.onAddToBagClick();
                break;

            case R.id.btn_buy_now:
                onBuyNowClickListener.onBuyNowClick();
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFirstLoad) {
            comment();
            isFirstLoad = true;
        }
    }
}
