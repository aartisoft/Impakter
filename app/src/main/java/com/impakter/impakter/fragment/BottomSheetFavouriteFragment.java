package com.impakter.impakter.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.R;
import com.impakter.impakter.adapter.ListMyCollectionAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.PreferencesManager;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.AddProToCollectionRespond;
import com.impakter.impakter.object.CollectionObj;
import com.impakter.impakter.object.CollectionRespond;
import com.impakter.impakter.widget.dialog.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetFavouriteFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private View rootView;
    private RecyclerView rcvCollection;
    private ListMyCollectionAdapter myCollectionAdapter;
    private List<CollectionObj> listCollections = new ArrayList<>();
    private ImageView ivClose;
    private LinearLayout btnAddNewCollection;
    private EditText edtSearch;
    private BottomSheetBehavior sheetBehavior;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoData;
    private ProgressBar progressBar;

    private int userId;
    private int productId;
    private ProgressDialog progressDialog;

    public BottomSheetFavouriteFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);
                sheetBehavior = BottomSheetBehavior.from(bottomSheet);
//                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_sheet_favorite, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        ivClose = rootView.findViewById(R.id.iv_close);
        edtSearch = rootView.findViewById(R.id.edt_search);
        btnAddNewCollection = rootView.findViewById(R.id.btn_add_new_collection);

        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);
        tvNoData = rootView.findViewById(R.id.tv_no_data);
        progressBar = rootView.findViewById(R.id.progress_bar);

        rcvCollection = rootView.findViewById(R.id.rcv_collection);
        rcvCollection.setHasFixedSize(true);
        rcvCollection.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void initData() {
        userId = PreferencesManager.getInstance(getActivity()).getUserLogin().getId();
        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = bundle.getInt(Constants.PRODUCT_ID);
        }
        myCollectionAdapter = new ListMyCollectionAdapter(getActivity(), listCollections);
        rcvCollection.setAdapter(myCollectionAdapter);

        getCollection();
    }

    private void initControl() {
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        myCollectionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int collectionId = listCollections.get(position).getId();
                addProductToCollection(collectionId, productId);
            }
        });
        ivClose.setOnClickListener(this);
        btnAddNewCollection.setOnClickListener(this);
    }

    private void addProductToCollection(int collectionId, int productId) {
        showDialog();
        ConnectServer.getResponseAPI().addProductToCollection(collectionId, productId).enqueue(new Callback<AddProToCollectionRespond>() {
            @Override
            public void onResponse(Call<AddProToCollectionRespond> call, Response<AddProToCollectionRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<AddProToCollectionRespond> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                closeDialog();
            }
        });
    }

    protected void showDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private void getCollection() {
//        showDialog();
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getCollection(userId, "").enqueue(new Callback<CollectionRespond>() {
            @Override
            public void onResponse(Call<CollectionRespond> call, Response<CollectionRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCollections.clear();
                        listCollections.addAll(response.body().getData());
                        myCollectionAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                checkNoData();
                progressBar.setVisibility(View.GONE);
//                closeDialog();
            }

            @Override
            public void onFailure(Call<CollectionRespond> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                closeDialog();
                progressBar.setVisibility(View.GONE);
                checkNoData();
            }
        });
    }

    public static void hideKeyboardInAndroidFragment(View view) {
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void checkNoData() {
        if (listCollections.size() == 0) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                this.dismiss();
                break;
        }
    }
}
