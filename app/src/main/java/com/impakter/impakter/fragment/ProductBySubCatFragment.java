package com.impakter.impakter.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.SwitchPreference;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.BuyProductDialog;
import com.impakter.impakter.activity.Demo;
import com.impakter.impakter.activity.ShareDialog;
import com.impakter.impakter.adapter.LatestAdapter;
import com.impakter.impakter.adapter.ProductAdapter;
import com.impakter.impakter.adapter.ProductBySubCatAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnBuyClickListener;
import com.impakter.impakter.events.OnFavoriteClickListener;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.events.OnShareClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.HomeLatestRespond;
import com.impakter.impakter.object.ProductByCategoryRespond;
import com.impakter.impakter.object.ProductDetailRespond;
import com.impakter.impakter.object.ProductObj;
import com.impakter.impakter.utils.AppUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductBySubCatFragment extends BaseFragment implements OnBuyClickListener, OnFavoriteClickListener, OnShareClickListener {
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rcvProductByType;
    private ProductBySubCatAdapter productBySubCatAdapter;
    private List<ProductByCategoryRespond.Data> listProducts = new ArrayList<>();

    private int categoryId, subCategoryId, typeTime;
    private int page = 1;
    private int totalPage;
    private ProgressBar progressBar;
    private boolean isFirstLoad = false;
    private TextView tvNoData;

    private View viewOverLay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_by_type, container, false);
        initViews();
        getDataFromIntent();
        initData();
        initControl();
        return rootView;
    }

    private void getDataFromIntent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryId = bundle.getInt(Constants.CATEGORY_ID);
            subCategoryId = bundle.getInt(Constants.SUBCATEGORY_ID);
        }
    }

    private void initViews() {
        viewOverLay = rootView.findViewById(R.id.view_over_lay);

        tvNoData = rootView.findViewById(R.id.tv_no_data);
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);
        progressBar = rootView.findViewById(R.id.progress_bar);

        rcvProductByType = rootView.findViewById(R.id.rcv_product_by_type);
        rcvProductByType.setHasFixedSize(true);
        rcvProductByType.setLayoutManager(new GridLayoutManager(self, 2));
    }

    private void initData() {
        productBySubCatAdapter = new ProductBySubCatAdapter(rcvProductByType, self, listProducts);
        rcvProductByType.setAdapter(productBySubCatAdapter);
        productBySubCatAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if (page <= totalPage) {
                    listProducts.add(null);
                    productBySubCatAdapter.notifyItemInserted(listProducts.size() - 1);
                    getMoreProductByCat();
                } else {
                    page = 1;
//                    showToast("No More Data");
                }

            }
        });

        if (getUserVisibleHint())
            getProductByCat();

    }

    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                productBySubCatAdapter.setLoaded();
                productBySubCatAdapter.notifyDataSetChanged();
                getProductByCat();
            }
        });

        productBySubCatAdapter.setOnBuyClickListener(this);
        productBySubCatAdapter.setOnFavoriteClickListener(this);
        productBySubCatAdapter.setOnShareClickListener(this);
    }

    private void getProductByCat() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getProductByCategory(categoryId, subCategoryId, 1).enqueue(new Callback<ProductByCategoryRespond>() {
            @Override
            public void onResponse(Call<ProductByCategoryRespond> call, Response<ProductByCategoryRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPages();
                        listProducts.clear();
                        listProducts.addAll(response.body().getData());
                        productBySubCatAdapter.notifyDataSetChanged();
//                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                checkNoData();
            }

            @Override
            public void onFailure(Call<ProductByCategoryRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                checkNoData();
            }
        });
    }

    private void getMoreProductByCat() {
        ConnectServer.getResponseAPI().getProductByCategory(categoryId, subCategoryId, page).enqueue(new Callback<ProductByCategoryRespond>() {
            @Override
            public void onResponse(Call<ProductByCategoryRespond> call, Response<ProductByCategoryRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listProducts.remove(listProducts.size() - 1);
                        listProducts.addAll(response.body().getData());
                        productBySubCatAdapter.setLoaded();
                        productBySubCatAdapter.notifyDataSetChanged();
//                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductByCategoryRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                listProducts.remove(listProducts.size() - 1);
                productBySubCatAdapter.notifyItemRemoved(listProducts.size() - 1);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible()) {
            if (isVisibleToUser && !isFirstLoad) {
                getProductByCat();
                isFirstLoad = true;
            }
        }
    }

    private void checkNoData() {
        if (listProducts.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
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
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(@NonNull Call<ProductDetailRespond> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
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
