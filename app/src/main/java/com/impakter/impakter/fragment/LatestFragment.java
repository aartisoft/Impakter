package com.impakter.impakter.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.BuyProductDialog;
import com.impakter.impakter.activity.ShareDialog;
import com.impakter.impakter.adapter.CategorySpinnerAdapter;
import com.impakter.impakter.adapter.NewLatestAdapter;
import com.impakter.impakter.adapter.TimeSpinnerAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnBuyClickListener;
import com.impakter.impakter.events.OnFavoriteClickListener;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.events.OnShareClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CategoryObj;
import com.impakter.impakter.object.HomeLatestRespond;
import com.impakter.impakter.object.MenuCategoryRespond;
import com.impakter.impakter.object.ProductDetailRespond;
import com.impakter.impakter.object.TimeObj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestFragment extends BaseFragment implements OnBuyClickListener, OnFavoriteClickListener, OnShareClickListener {
    private View rootView;
    private Spinner spCategory, spTime;
    private RecyclerView rcvLatest;
    //    private LatestAdapter latestAdapter;
    private NewLatestAdapter latestAdapter;
    private List<HomeLatestRespond.Data> listProducts = new ArrayList<>();
    private List<CategoryObj> listCategories = new ArrayList<>();
    private ArrayList<TimeObj> listTypeTime = new ArrayList<>();
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private TimeSpinnerAdapter timeSpinnerAdapter;
    private String categoryId;
    private int typeTime;
    private int page = 1;
    private int totalPage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoData;
    private boolean isFirst = true;
    private ProgressBar progressBar;
    private boolean isFirstLoad = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_latest, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        tvNoData = rootView.findViewById(R.id.tv_no_data);
        spCategory = rootView.findViewById(R.id.sp_category);
        spTime = rootView.findViewById(R.id.sp_time);

        rcvLatest = rootView.findViewById(R.id.rcv_latest);
        rcvLatest.setHasFixedSize(true);
        rcvLatest.setLayoutManager(new GridLayoutManager(self, 2));

        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);
        progressBar = rootView.findViewById(R.id.progress_bar);
    }

    private void initData() {
        initTypeTimeData();

        latestAdapter = new NewLatestAdapter(rcvLatest, self, listProducts);
        rcvLatest.setAdapter(latestAdapter);

        latestAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if (page <= totalPage) {
                    listProducts.add(null);
                    latestAdapter.notifyItemInserted(listProducts.size() - 1);
                    getMoreHomeLatest();
                }

            }
        });

        categorySpinnerAdapter = new CategorySpinnerAdapter(self, listCategories);
        spCategory.setAdapter(categorySpinnerAdapter);

        timeSpinnerAdapter = new TimeSpinnerAdapter(self, listTypeTime);
        spTime.setAdapter(timeSpinnerAdapter);

        if (getUserVisibleHint() && !isFirstLoad) {
            getMenuCategory();
            getHomeLatest();
        }
    }

    private void initTypeTimeData() {
        listTypeTime.clear();
        listTypeTime.add(new TimeObj(0, getResources().getString(R.string.last_24h)));
        listTypeTime.add(new TimeObj(1, getResources().getString(R.string.last_7_days)));
        listTypeTime.add(new TimeObj(2, getResources().getString(R.string.last_30_days)));
    }

    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                latestAdapter.setLoaded();
                latestAdapter.notifyDataSetChanged();
                getHomeLatest();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = listCategories.get(position).getCatId() + "";
                if (categoryId.equals("-1"))
                    categoryId = "";
                page = 1;
                latestAdapter.setLoaded();
                latestAdapter.notifyDataSetChanged();
                if (!isFirst)
                    getHomeLatest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TimeObj timeObj = (TimeObj) parent.getItemAtPosition(position);
                typeTime = timeObj.getId();
                page = 1;
                latestAdapter.setLoaded();
                latestAdapter.notifyDataSetChanged();
                if (!isFirst)
                    getHomeLatest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        latestAdapter.setOnBuyClickListener(this);
        latestAdapter.setOnFavoriteClickListener(this);
        latestAdapter.setOnShareClickListener(this);
    }

    private void getMenuCategory() {
//        showDialog();
        ConnectServer.getResponseAPI().getMenuCategory().enqueue(new Callback<MenuCategoryRespond>() {
            @Override
            public void onResponse(Call<MenuCategoryRespond> call, Response<MenuCategoryRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCategories.clear();
                        listCategories.add(new CategoryObj("ALL", -1));
                        listCategories.addAll(response.body().getData());
                        categorySpinnerAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                } else {
                    showToast(response.body().getMessage());
                }
//                closeDialog();
            }

            @Override
            public void onFailure(Call<MenuCategoryRespond> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                showToast(t.getMessage());
//                closeDialog();
            }
        });
    }

    private void getHomeLatest() {
//        showDialog();
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getHomeLatest(categoryId, typeTime, 1).enqueue(new Callback<HomeLatestRespond>() {
            @Override
            public void onResponse(Call<HomeLatestRespond> call, Response<HomeLatestRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPage();

                        listProducts.clear();
                        listProducts.addAll(response.body().getData());
                        latestAdapter.notifyDataSetChanged();
                    }
                } else {
                    showToast(response.body().getMessage());
                }
                isFirst = false;
                checkNoData();
                progressBar.setVisibility(View.GONE);
//                closeDialog();
            }

            @Override
            public void onFailure(Call<HomeLatestRespond> call, Throwable t) {
                isFirst = false;
                checkNoData();
                progressBar.setVisibility(View.GONE);
//                closeDialog();
                showToast(t.getMessage());
            }
        });
    }

    private void getMoreHomeLatest() {
        ConnectServer.getResponseAPI().getHomeLatest(categoryId, typeTime, page).enqueue(new Callback<HomeLatestRespond>() {
            @Override
            public void onResponse(Call<HomeLatestRespond> call, Response<HomeLatestRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        //   remove progress item
                        listProducts.remove(listProducts.size() - 1);

                        listProducts.addAll(response.body().getData());
                        latestAdapter.setLoaded();
                        latestAdapter.notifyDataSetChanged();
                    } else {
                        listProducts.remove(listProducts.size() - 1);
                        latestAdapter.setLoaded();
                        latestAdapter.notifyItemRemoved(listProducts.size() - 1);
                        showToast(response.body().getMessage());
                    }
                }
                checkNoData();
            }

            @Override
            public void onFailure(Call<HomeLatestRespond> call, Throwable t) {
                showToast(t.getMessage());
                checkNoData();
                listProducts.remove(listProducts.size() - 1);
                latestAdapter.setLoaded();
                latestAdapter.notifyItemRemoved(listProducts.size() - 1);
            }
        });
    }

    private void checkNoData() {
        if (listProducts.size() == 0) {
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
                getMenuCategory();
                getHomeLatest();
                isFirstLoad = true;
            }
        }
    }

    public void showBottomSheetDialog(int productId) {
        BottomSheetFavouriteFragment bottomSheetFragment = new BottomSheetFavouriteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.PRODUCT_ID, productId);
        bottomSheetFragment.setArguments(bundle);
        bottomSheetFragment.show(getFragmentManager(), null);
    }

    private void getProductDetail(int id) {
        showDialog();
        ConnectServer.getResponseAPI().getProductDetail(id).enqueue(new Callback<ProductDetailRespond>() {
            @Override
            public void onResponse(@NonNull Call<ProductDetailRespond> call, @NonNull Response<ProductDetailRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showBuyProductDialog(response.body().getData());
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(@NonNull Call<ProductDetailRespond> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                closeDialog();
            }
        });
    }

    private void showBuyProductDialog(ProductDetailRespond.Data product) {
        BuyProductDialog buyProductDialog = new BuyProductDialog(self, product);
        buyProductDialog.show();
    }

    @Override
    public void onBuyClick(View view, int position) {
        getProductDetail(listProducts.get(position).getId());
    }

    @Override
    public void onFavoriteClick(View view, int position) {
        if (isLoggedIn()) {
            showBottomSheetDialog(listProducts.get(position).getId());
        } else {
            showConfirmLoginDialog();
        }
    }

    @Override
    public void onShareClick(View view, int position) {
        showShareDialog(position);
    }

    private void showShareDialog(int position) {
        ShareDialog shareDialog = new ShareDialog(self, listProducts.get(position).getId());
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(shareDialog.getWindow().getAttributes());
        lp.dimAmount = 0.7f;
        lp.gravity = Gravity.BOTTOM;
        lp.width = self.getResources().getDisplayMetrics().widthPixels;
        lp.height = (int) (self.getResources().getDisplayMetrics().heightPixels * 9 / 11F);
        shareDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        shareDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        shareDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        shareDialog.show();
        shareDialog.getWindow().setAttributes(lp);
        shareDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
