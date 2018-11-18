package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.CategorySpinnerAdapter;
import com.impakter.impakter.adapter.ProductSellerAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CartSellerRespond;
import com.impakter.impakter.object.CategoryObj;
import com.impakter.impakter.object.SellerProfileRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends BaseFragment {
    private View rootView;
    private Spinner spCategory;
    private RecyclerView rcvTrending;
    private ArrayList<SellerProfileRespond.ArrProduct> listProducts = new ArrayList<>();
    private ProductSellerAdapter productSellerAdapter;
    private List<CategoryObj> listCategories = new ArrayList<>();
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String categoryId = "";
    private int page = 1;
    private int sellerId;
    private int totalPage;

    private TextView tvNoData;
    private boolean isFirst = true;
    private boolean isFirstLoad;
    private ProgressBar progressBar;

    private String userId = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);
        spCategory = rootView.findViewById(R.id.sp_category);
        progressBar = rootView.findViewById(R.id.progress_bar);

        tvNoData = rootView.findViewById(R.id.tv_no_data);

        rcvTrending = rootView.findViewById(R.id.rcv_trending);
        rcvTrending.setHasFixedSize(true);
        rcvTrending.setLayoutManager(new GridLayoutManager(self, 3));
    }

    private void initData() {
        if (isLoggedIn()) {
            userId = preferenceManager.getUserLogin().getId() + "";
        } else {
            userId = "";
        }
        Bundle bundle = getArguments();
        if (bundle != null)
            sellerId = bundle.getInt(Constants.SELLER_ID);

        productSellerAdapter = new ProductSellerAdapter(rcvTrending, self, listProducts);
        rcvTrending.setAdapter(productSellerAdapter);

        productSellerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if (page <= totalPage) {
                    listProducts.add(null);
                    productSellerAdapter.notifyItemInserted(listProducts.size() - 1);
                    getMoreProductSeller();
                }

            }
        });

        categorySpinnerAdapter = new CategorySpinnerAdapter(self, listCategories);
        spCategory.setAdapter(categorySpinnerAdapter);

        if (getUserVisibleHint()) {
            getListCartOfSeller();
            getProductSeller();
        }
    }


    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                productSellerAdapter.setLoaded();
                getProductSeller();
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
                productSellerAdapter.setLoaded();
                if (!isFirst)
                    getProductSeller();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getListCartOfSeller() {
//        showDialog();
//        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getListCartOfSeller(sellerId).enqueue(new Callback<CartSellerRespond>() {
            @Override
            public void onResponse(Call<CartSellerRespond> call, Response<CartSellerRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCategories.clear();
                        listCategories.add(new CategoryObj("ALL", -1));
                        listCategories.addAll(response.body().getListCategory());
                        categorySpinnerAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                checkNoData();
//                progressBar.setVisibility(View.GONE);
//                closeDialog();
            }

            @Override
            public void onFailure(Call<CartSellerRespond> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                checkNoData();
//                closeDialog();
            }
        });
    }

    private void getProductSeller() {
//        showDialog();
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getSellerProfile(sellerId, userId, categoryId, 1).enqueue(new Callback<SellerProfileRespond>() {
            @Override
            public void onResponse(Call<SellerProfileRespond> call, Response<SellerProfileRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPage();
                        listProducts.clear();
                        listProducts.addAll(response.body().getArrProduct());
                        productSellerAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                isFirst = false;
//                checkNoData();
                progressBar.setVisibility(View.GONE);
                closeDialog();
            }

            @Override
            public void onFailure(Call<SellerProfileRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                isFirst = false;
                progressBar.setVisibility(View.GONE);
//                closeDialog();
                checkNoData();
            }
        });
    }

    private void getMoreProductSeller() {
        ConnectServer.getResponseAPI().getSellerProfile(sellerId, userId, categoryId, page).enqueue(new Callback<SellerProfileRespond>() {
            @Override
            public void onResponse(Call<SellerProfileRespond> call, Response<SellerProfileRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listProducts.remove(listProducts.size() - 1);
                        listProducts.addAll(response.body().getArrProduct());
                        productSellerAdapter.setLoaded();
                        productSellerAdapter.notifyDataSetChanged();
                    } else {
                        listProducts.remove(listProducts.size() - 1);
                        productSellerAdapter.setLoaded();
                        productSellerAdapter.notifyItemRemoved(listCategories.size() - 1);
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                checkNoData();
            }

            @Override
            public void onFailure(Call<SellerProfileRespond> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                checkNoData();
                listProducts.remove(listProducts.size() - 1);
                productSellerAdapter.setLoaded();
                productSellerAdapter.notifyItemRemoved(listCategories.size() - 1);
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
                getListCartOfSeller();
                getProductSeller();
                isFirstLoad = true;
            }
        }
    }
}
