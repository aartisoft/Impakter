package com.impakter.impakter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.RecentSearchAdapter;
import com.impakter.impakter.adapter.ResultSearchAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.dblocal.Convertobject.ConvertObject;
import com.impakter.impakter.dblocal.configrealm.DbContext;
import com.impakter.impakter.dblocal.realmobject.RecentSearchObj;
import com.impakter.impakter.events.OnDeleteClickListener;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.events.OnLoadMoreListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.ResultSearchRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack, ivSearch;
    private EditText edtKeyword;
    private RecyclerView rcvProduct, rcvRecentSearch;
    private List<ResultSearchRespond.Data> listProducts = new ArrayList<>();
    private ResultSearchAdapter resultSearchAdapter;
    private ArrayList<String> listKeywords = new ArrayList<>();
    private RecentSearchAdapter recentSearchAdapter;

    private int page = 1;
    private int totalPage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoData;
    private DbContext dbContext;
    private String keyword = "";
    private LinearLayout layoutSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        DbContext.init(self);
        dbContext = DbContext.getInstance();
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivSearch = findViewById(R.id.iv_search);

        edtKeyword = findViewById(R.id.edt_keyword);
        layoutSearch = findViewById(R.id.layout_search);

        rcvProduct = findViewById(R.id.rcv_product);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new LinearLayoutManager(self));

        rcvRecentSearch = findViewById(R.id.rcv_recent_search);
        rcvRecentSearch.setHasFixedSize(true);
        rcvRecentSearch.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        resultSearchAdapter = new ResultSearchAdapter(rcvProduct, self, listProducts);
        rcvProduct.setAdapter(resultSearchAdapter);

        resultSearchAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                page++;
                if (page <= totalPage) {
                    listProducts.add(null);
                    resultSearchAdapter.notifyItemInserted(listProducts.size() - 1);
                    searchMore(keyword);
                } else {
                    page = 1;
                    resultSearchAdapter.setLoaded();
                    resultSearchAdapter.notifyDataSetChanged();
                }

            }
        });

        listKeywords.clear();
        listKeywords.addAll(ConvertObject.convertRealmStringToString(dbContext.getRecentSearch()));
        Log.e(TAG, "size: " + listKeywords.size());
        recentSearchAdapter = new RecentSearchAdapter(self, listKeywords);
        rcvRecentSearch.setAdapter(recentSearchAdapter);

    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        edtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (layoutSearch.getVisibility() == View.GONE)
                    layoutSearch.setVisibility(View.VISIBLE);
                recentSearchAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = edtKeyword.getText().toString().trim();
                    search(keyword);
                    if (layoutSearch.getVisibility() == View.VISIBLE)
                        layoutSearch.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });
        recentSearchAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String keyword = listKeywords.get(position);
                edtKeyword.setText(keyword);
                search(keyword);
                layoutSearch.setVisibility(View.GONE);
            }
        });
        recentSearchAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position) {
                String primaryKey = listKeywords.get(position);
                if (dbContext.deleteRecentSearcdItem(primaryKey)) {
                    listKeywords.remove(position);
                    recentSearchAdapter.notifyItemRemoved(position);
                }

            }
        });
    }

    private void search(String keyword) {
        if (edtKeyword.getText().toString().trim().length() != 0) {
            RecentSearchObj recentSearchObj = new RecentSearchObj();
            recentSearchObj.setKeyWord(edtKeyword.getText().toString().trim());
            dbContext.addToRecentSearch(recentSearchObj);
//                    listKeywords.add(edtKeyword.getText().toString().trim());
        }

        showDialog();
        ConnectServer.getResponseAPI().search(keyword, 1).enqueue(new Callback<ResultSearchRespond>() {
            @Override
            public void onResponse(Call<ResultSearchRespond> call, Response<ResultSearchRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        totalPage = response.body().getAllPages();

                        listProducts.clear();
                        listProducts.addAll(response.body().getData());
                        resultSearchAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<ResultSearchRespond> call, Throwable t) {
                closeDialog();
                showToast(t.getMessage());
            }
        });
    }

    private void searchMore(String keyword) {
        ConnectServer.getResponseAPI().search(keyword, page).enqueue(new Callback<ResultSearchRespond>() {
            @Override
            public void onResponse(Call<ResultSearchRespond> call, Response<ResultSearchRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listProducts.remove(listProducts.size() - 1);
                        listProducts.addAll(response.body().getData());
                        resultSearchAdapter.setLoaded();
                        resultSearchAdapter.notifyDataSetChanged();
                    } else {
                        listProducts.remove(listProducts.size() - 1);
                        resultSearchAdapter.notifyItemRemoved(listProducts.size() - 1);
                        resultSearchAdapter.setLoaded();
                        showToast(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultSearchRespond> call, Throwable t) {
                showToast(t.getMessage());
                listProducts.remove(listProducts.size() - 1);
                resultSearchAdapter.notifyItemRemoved(listProducts.size() - 1);
                resultSearchAdapter.setLoaded();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_search:
//                if (edtKeyword.getText().toString().trim().length() != 0) {
//                    RecentSearchObj recentSearchObj = new RecentSearchObj();
//                    recentSearchObj.setKeyWord(edtKeyword.getText().toString().trim());
//                    dbContext.addToRecentSearch(recentSearchObj);
////                    listKeywords.add(edtKeyword.getText().toString().trim());
//                }
                keyword = edtKeyword.getText().toString().trim();
                search(keyword);
                if (layoutSearch.getVisibility() == View.VISIBLE)
                    layoutSearch.setVisibility(View.GONE);
                break;
        }
    }
}
