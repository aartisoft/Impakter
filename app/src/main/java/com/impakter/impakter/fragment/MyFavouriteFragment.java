package com.impakter.impakter.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.activity.MainActivity;
import com.impakter.impakter.adapter.CollectionAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnDeleteClickListener;
import com.impakter.impakter.events.OnEditClickListener;
import com.impakter.impakter.events.OnFavoriteClickListener;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.events.OnShareClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CollectionObj;
import com.impakter.impakter.object.CollectionRespond;
import com.impakter.impakter.object.CreateCollectionRespond;
import com.impakter.impakter.object.BaseMessageRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFavouriteFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private RecyclerView rcvCollection;
    private List<CollectionObj> listCollections = new ArrayList<>();
    private CollectionAdapter collectionAdapter;

    private int userId;
    private TextView tvNoData;
    private boolean isFirstLoad = false;
    private ProgressBar progressBar;
    private LinearLayout btnAddNewCollection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_favorite, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        btnAddNewCollection = rootView.findViewById(R.id.btn_add_new_collection);
        tvNoData = rootView.findViewById(R.id.tv_no_data);
        progressBar = rootView.findViewById(R.id.progress_bar);

        rcvCollection = rootView.findViewById(R.id.rcv_collection);
        rcvCollection.setHasFixedSize(true);
        rcvCollection.setLayoutManager(new LinearLayoutManager(self));
        ViewCompat.setNestedScrollingEnabled(rcvCollection, false);

        collectionAdapter = new CollectionAdapter(self, listCollections);
        rcvCollection.setAdapter(collectionAdapter);
    }

    private void initData() {
        userId = preferenceManager.getUserLogin().getId();
        if (getUserVisibleHint())
            getCollection();

    }

    private void initControl() {
        btnAddNewCollection.setOnClickListener(this);

        collectionAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int collectionId = listCollections.get(position).getId();
                MyFavouriteDetailFragment myFavouriteDetailFragment = new MyFavouriteDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.COLLECTION_ID, collectionId);
                bundle.putString(Constants.COLLECTION_NAME, listCollections.get(position).getCollectionName());
                myFavouriteDetailFragment.setArguments(bundle);

                ((MainActivity) self).hideToolbar();
                ((MainActivity) self).showFragment(myFavouriteDetailFragment, true);
            }
        });
        collectionAdapter.setOnEditClickListener(new OnEditClickListener() {
            @Override
            public void onEditClick(View view, int position) {
                EditCollectionFragment editCollectionFragment = new EditCollectionFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.COLLECTION_ID, listCollections.get(position).getId());
                bundle.putString(Constants.COLLECTION_NAME, listCollections.get(position).getCollectionName());
                editCollectionFragment.setArguments(bundle);
                ((MainActivity) self).showFragment(editCollectionFragment, true);
            }
        });
        collectionAdapter.setOnFavoriteClickListener(new OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(View view, int position) {
//                FollowerFragment followFragment = new FollowerFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.TITLE, getResources().getString(R.string.like));
//                followFragment.setArguments(bundle);
//                ((MainActivity) self).showFragmentWithAddMethod(followFragment, true);
            }
        });
        collectionAdapter.setOnShareClickListener(new OnShareClickListener() {
            @Override
            public void onShareClick(View view, int position) {

            }
        });
        collectionAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position) {
                showConfirmDialog(listCollections.get(position).getId(), position);
            }
        });
    }

    private void getCollection() {
//        showDialog();
//        progressBar.setVisibility(View.VISIBLE);
        ConnectServer.getResponseAPI().getCollection(userId, "").enqueue(new Callback<CollectionRespond>() {
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
//                progressBar.setVisibility(View.GONE);
//                closeDialog();
            }

            @Override
            public void onFailure(Call<CollectionRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
//                closeDialog();
//                progressBar.setVisibility(View.GONE);
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

    private void showConfirmDialog(final int collectionId, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.alert));
        builder.setMessage(getResources().getString(R.string.confirm_delete_collection));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCollection(collectionId, position);
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

    private void deleteCollection(int collectionId, final int position) {
        showDialog();
        ConnectServer.getResponseAPI().deleteCollection(collectionId, userId).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        listCollections.remove(position);
                        collectionAdapter.notifyItemRemoved(position);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible()) {
            if (isVisibleToUser && !isFirstLoad) {
                getCollection();
                isFirstLoad = true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_new_collection:
                showCreateCollectionDialog();
                break;
        }
    }

    private Dialog dialog;

    private void showCreateCollectionDialog() {
        View view = self.getLayoutInflater().inflate(R.layout.dialog_create_new_collection, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setCancelable(false);
        builder.setView(view);

        final EditText edtCollectionName = view.findViewById(R.id.edt_collection_name);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnOK = view.findViewById(R.id.btn_ok);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCollectionName.getText().toString().trim().length() > 0) {
                    createNewCollection(edtCollectionName.getText().toString().trim(), userId);
                    dialog.dismiss();
                } else {
                    showToast(getResources().getString(R.string.input_collection_name));
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void createNewCollection(String collectionName, int userId) {
        showDialog();
        ConnectServer.getResponseAPI().createCollection(collectionName, userId).enqueue(new Callback<CreateCollectionRespond>() {
            @Override
            public void onResponse(Call<CreateCollectionRespond> call, Response<CreateCollectionRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCollections.add(response.body().getData());
                        collectionAdapter.notifyDataSetChanged();
                        Toast.makeText(self, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                checkNoData();
                closeDialog();
            }

            @Override
            public void onFailure(Call<CreateCollectionRespond> call, Throwable t) {
                closeDialog();
                checkNoData();
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
