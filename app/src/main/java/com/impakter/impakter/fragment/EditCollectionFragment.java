package com.impakter.impakter.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.CollectionDetailAdapter;
import com.impakter.impakter.adapter.CommonAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnDeleteClickListener;
import com.impakter.impakter.events.OnMoveClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CollectionObj;
import com.impakter.impakter.object.CollectionRespond;
import com.impakter.impakter.object.CommonObj;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.ProOfCollectionRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCollectionFragment extends BaseFragment implements View.OnClickListener {
    private final int ACTION_RENAME = 1;
    private final int ACTION_MOVE = 2;
    private View rootView;
    private RecyclerView rcvProduct;
    private List<ProOfCollectionRespond.Data> listCollectionsDetail = new ArrayList<>();
    private CollectionDetailAdapter collectionDetailAdapter;

    private int userId;
    private TextView tvNoData, tvSave;
    private ImageView ivBack;
    private boolean isFirstLoad = false;
    private ProgressBar progressBar;
    private int collectionId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText edtCollectionName;
    private String collectionName;
    private String newCollectionName;

    private List<CommonObj> listCollections = new ArrayList<>();
    private PopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_edit_collection, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
//        ((MainActivity) self).hideToolbar();
        swipeRefreshLayout = rootView.findViewById(R.id.swf_layout);
        ivBack = rootView.findViewById(R.id.iv_back);
        tvNoData = rootView.findViewById(R.id.tv_no_data);
        tvSave = rootView.findViewById(R.id.tv_save);
        progressBar = rootView.findViewById(R.id.progress_bar);
        edtCollectionName = rootView.findViewById(R.id.edt_collection_name);

        rcvProduct = rootView.findViewById(R.id.rcv_product);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new GridLayoutManager(self, 2));

    }

    private void initData() {
        userId = preferenceManager.getUserLogin().getId();

        collectionDetailAdapter = new CollectionDetailAdapter(self, listCollectionsDetail, true);
        rcvProduct.setAdapter(collectionDetailAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            collectionId = bundle.getInt(Constants.COLLECTION_ID);
            collectionName = bundle.getString(Constants.COLLECTION_NAME);
        }
        edtCollectionName.setText(collectionName);

        getProductOfCollection();
        getCollection(userId, collectionId + "");
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProductOfCollection();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        collectionDetailAdapter.setOnMoveClickListener(new OnMoveClickListener() {
            @Override
            public void onMoveClick(View view, int position) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                showMovePopUpWindow(view, listCollectionsDetail.get(position).getId(), position);
            }
        });
        collectionDetailAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position) {
                showConfirmDialog(collectionId, listCollectionsDetail.get(position).getId(), position);
            }
        });
    }

    private void showConfirmDialog(final int collectionId, final int productId, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.alert));
        builder.setMessage(getResources().getString(R.string.confirm_delete_collection));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProductFromCollection(collectionId, productId, position);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void deleteProductFromCollection(int collectionId, int productId, final int position) {
        showDialog();
        ConnectServer.getResponseAPI().deleteProductFromCollection(collectionId, productId, userId).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCollectionsDetail.remove(position);
                        collectionDetailAdapter.notifyItemRemoved(position);
                        showToast(response.body().getMessage());
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                checkNoData();
                closeDialog();
            }

            @Override
            public void onFailure(Call<BaseMessageRespond> call, Throwable t) {
                closeDialog();
                checkNoData();
                showToast(t.getMessage());
            }
        });
    }

    private void renameCollection() {
        showDialog();
        ConnectServer.getResponseAPI().renameCollection(collectionId, newCollectionName).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showToast(response.body().getMessage());
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<BaseMessageRespond> call, Throwable t) {
                showToast(t.getMessage());
                closeDialog();
            }
        });
    }

    private void moveProductToCollection(int fromCollectionId, int toCollectionId, int productId, final int position) {
        showDialog();
        ConnectServer.getResponseAPI().moveProductToCollection(fromCollectionId, toCollectionId, productId).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCollectionsDetail.remove(position);
                        collectionDetailAdapter.notifyItemRemoved(position);
                        showToast(response.body().getMessage());
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<BaseMessageRespond> call, Throwable t) {
                showToast(t.getMessage());
                closeDialog();
            }
        });
    }

    private void getProductOfCollection() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getProductOfCollection(collectionId).enqueue(new Callback<ProOfCollectionRespond>() {
            @Override
            public void onResponse(Call<ProOfCollectionRespond> call, Response<ProOfCollectionRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCollectionsDetail.clear();
                        listCollectionsDetail.addAll(response.body().getData());
                        collectionDetailAdapter.notifyDataSetChanged();
//                        showToast(response.body().getMessage());
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                checkNoData();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProOfCollectionRespond> call, Throwable t) {
                showToast(t.getMessage());
                checkNoData();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void checkNoData() {
        if (listCollectionsDetail.size() == 0) {
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
                isFirstLoad = true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
//                ((MainActivity) self).showToolbar();
                getFragmentManager().popBackStack();
                break;
            case R.id.tv_save:
                showConfirmDialog(ACTION_RENAME, getResources().getString(R.string.confirm_rename_collection));
                break;
        }
    }

    private void showConfirmDialog(final int action, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.alert));
        builder.setMessage(message);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (action == ACTION_RENAME) {
                    if (edtCollectionName.getText().toString().trim().length() == 0) {
                        showToast(getResources().getString(R.string.input_collection_name));
                    } else {
                        newCollectionName = edtCollectionName.getText().toString().trim();
                        renameCollection();
                    }

                } else {
                    //Show popup list of collections
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void getCollection(int userId, String exceptCollectId) {
        ConnectServer.getResponseAPI().getCollection(userId, exceptCollectId).enqueue(new Callback<CollectionRespond>() {
            @Override
            public void onResponse(Call<CollectionRespond> call, Response<CollectionRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCollections.clear();
                        for (CollectionObj collectionObj : response.body().getData()) {
                            listCollections.add(new CommonObj(collectionObj.getId(), collectionObj.getCollectionName()));
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<CollectionRespond> call, Throwable t) {

            }
        });
    }

    private Dialog dialog;

    private void showMoveDialog(final int productId, final int positions) {
        View view = self.getLayoutInflater().inflate(R.layout.popup_move_product_to_collection, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setCancelable(false);
        builder.setView(view);

        ImageView ivClose = view.findViewById(R.id.iv_close);
        ListView lvCollection = view.findViewById(R.id.lv_collection);
        CommonAdapter commonAdapter = new CommonAdapter(self, listCollections);
        lvCollection.setAdapter(commonAdapter);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lvCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int toCollectionId = listCollections.get(position).getId();
                moveProductToCollection(collectionId, toCollectionId, productId, positions);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void showMovePopUpWindow(View v, final int productId, final int positions) {
        View view = self.getLayoutInflater().inflate(R.layout.popup_move_product_to_collection, null);
        int wScreen = self.getResources().getDisplayMetrics().widthPixels;
        popupWindow = new PopupWindow((int) (wScreen * 1 / 2F),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        ListView lvCollection = view.findViewById(R.id.lv_collection);
        CommonAdapter commonAdapter = new CommonAdapter(self, listCollections);
        lvCollection.setAdapter(commonAdapter);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        lvCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int toCollectionId = listCollections.get(position).getId();
                moveProductToCollection(collectionId, toCollectionId, productId, positions);
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(v);
    }
}
