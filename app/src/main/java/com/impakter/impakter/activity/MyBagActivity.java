package com.impakter.impakter.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.MyBagAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.dblocal.Convertobject.ConvertObject;
import com.impakter.impakter.dblocal.configrealm.DbContext;
import com.impakter.impakter.dblocal.realmobject.CartItemRealmObj;
import com.impakter.impakter.events.OnDeleteClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.CartIdRespond;
import com.impakter.impakter.object.CartItemObj;
import com.impakter.impakter.object.CartItemRespond;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBagActivity extends BaseActivity implements View.OnClickListener {
    private final int ACTION_DELETE = 0;
    private final int ACTION_DELETE_ALL = 1;
    ;
    private ImageView ivBack;
    private RecyclerView rcvProduct;
    private MyBagAdapter myBagAdapter;
    private List<CartItemObj> listProducts = new ArrayList<>();
    private LinearLayout btnBuyNow;
    private String userId;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoData;
    private int cartId;
    private int cartItemId;
    private TextView tvSave;
    private boolean isChangeQuantity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bag);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        btnBuyNow = findViewById(R.id.btn_buy_now);

        swipeRefreshLayout = findViewById(R.id.swf_layout);
        tvNoData = findViewById(R.id.tv_no_data);
        tvSave = findViewById(R.id.tv_save);

        rcvProduct = findViewById(R.id.rcv_product);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(new LinearLayoutManager(self));
    }

    private void initData() {
        cartId = GlobalValue.cartId;
        if (isLoggedIn())
            userId = preferencesManager.getUserLogin().getId() + "";

        myBagAdapter = new MyBagAdapter(self, listProducts);
        rcvProduct.setAdapter(myBagAdapter);

//        getCartItems(cartId);
        if (isLoggedIn()) {
//        getMyCartInfo();
            getCartItems(cartId);
        } else {
            DbContext.init(self);
            listProducts.clear();
            listProducts.addAll(ConvertObject.convertCartItemRealObjToCartItemObj(DbContext.getInstance().getCartItems()));
            for (CartItemObj cartItemObj : listProducts) {
                Log.e(TAG, "image: " + cartItemObj.getImage());
            }
            myBagAdapter.notifyDataSetChanged();
            checkNoData();
        }

    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        tvSave.setOnClickListener(this);

        if (isLoggedIn()) {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getCartItems(cartId);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            swipeRefreshLayout.setEnabled(false);
        }

        myBagAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position) {
                CartItemObj data = listProducts.get(position);
                int productId = data.getId();
                int cartItemId = data.getId();
                String option = "";
                option = data.getOption();
                String size = "", color = "";
                if (option.length() != 0) {
                    size = option.substring(0, option.indexOf("/"));
                    color = option.substring(option.indexOf("/") + 1);
                }
                showConfirmDeleteDialog(cartItemId, cartId, ACTION_DELETE, position, productId, size, color);
            }
        });

        myBagAdapter.setOnIncreaseClickListener(new MyBagAdapter.OnInCreaseClickListener() {
            @Override
            public void onIncreaseClick(View view, int position) {
                if (tvSave.getVisibility() == View.GONE) {
                    tvSave.setVisibility(View.VISIBLE);
                }
//                if (!isChangeQuantity) {
//                    isChangeQuantity = true;
//                }
                CartItemObj data = listProducts.get(position);
                int quantity = data.getQuantity();
                quantity++;
                data.setQuantity(quantity);
                data.setTotalPrice(quantity * data.getPrice());
                myBagAdapter.notifyItemChanged(position);

            }
        });
        myBagAdapter.setOnDecreaseClickListener(new MyBagAdapter.OnDecreaseClickListener() {
            @Override
            public void onDecreaseClick(View view, int position) {
                if (tvSave.getVisibility() == View.GONE) {
                    tvSave.setVisibility(View.VISIBLE);
                }
//                if (!isChangeQuantity) {
//                    isChangeQuantity = true;
//                }
                CartItemObj data = listProducts.get(position);
                int quantity = data.getQuantity();
                if (quantity > 0)
                    quantity--;
                data.setQuantity(quantity);
                data.setTotalPrice(quantity * data.getPrice());
                myBagAdapter.notifyItemChanged(position);
            }
        });
    }

    private void showConfirmDeleteDialog(final int cartItemId, final int cartId, final int action, final int position, final int productId, final String size, final String color) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(self);
        builder.setTitle(getResources().getString(R.string.alert));
        builder.setMessage(getResources().getString(R.string.confirm_delete_product));
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isLoggedIn()) {
                    deleteCartItem(cartItemId, cartId, action, position);
                } else {
                    DbContext.init(self);
                    boolean result = DbContext.getInstance().deleteCartItem(productId + size + "/" + color);
                    if (result) {
                        listProducts.remove(position);
                        myBagAdapter.notifyItemRemoved(position);
                    } else {
                        showToast(getResources().getString(R.string.have_some_error));
                    }
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void deleteCartItem(int cartItemId, int cartId, int action, final int position) {
        showDialog();
        ConnectServer.getResponseAPI().deleteCartItem(cartItemId, cartId, action).enqueue(new Callback<CartItemRespond>() {
            @Override
            public void onResponse(Call<CartItemRespond> call, Response<CartItemRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listProducts.remove(position);
                        myBagAdapter.notifyItemRemoved(position);
                        showToast(response.body().getMessage());
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
                checkNoData();
            }

            @Override
            public void onFailure(Call<CartItemRespond> call, Throwable t) {
                closeDialog();
                checkNoData();
                showToast(t.getMessage());
            }
        });
    }

    private void getMyCartInfo() {
        showDialog();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String ime = telephonyManager.getDeviceId();
        ConnectServer.getResponseAPI().getCartId(userId, ime).enqueue(new Callback<CartIdRespond>() {
            @Override
            public void onResponse(Call<CartIdRespond> call, Response<CartIdRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        cartId = response.body().getData().getId();
                        preferencesManager.setCartId(cartId);
                        getCartItems(cartId);
                    } else {
                        closeDialog();
                    }
                }
//                closeDialog();
            }

            @Override
            public void onFailure(Call<CartIdRespond> call, Throwable t) {
                closeDialog();
            }
        });
    }

    private void getCartItems(int cartId) {
        showDialog();
        ConnectServer.getResponseAPI().getCartItem(cartId).enqueue(new Callback<CartItemRespond>() {
            @Override
            public void onResponse(Call<CartItemRespond> call, Response<CartItemRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listProducts.clear();
                        listProducts.addAll(response.body().getData());
                        myBagAdapter.notifyDataSetChanged();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
                checkNoData();
            }

            @Override
            public void onFailure(Call<CartItemRespond> call, Throwable t) {
                closeDialog();
                checkNoData();
                showToast(t.getMessage());
            }
        });
    }

    private String createJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listProducts.size(); i++) {
            CartItemObj cartItemObj = listProducts.get(i);
            JSONObject object = new JSONObject();
            try {
                object.put("id", cartItemObj.getId() + "");
                object.put("quantity", cartItemObj.getQuantity() + "");

                jsonArray.put(object);

                jsonObject.put("cartId", cartId + "");
                jsonObject.put("listCartItem", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.e(TAG, "createJson: " + jsonObject.toString());
        return jsonObject.toString();
    }

    private void checkNoData() {
        if (listProducts.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
    }

    private void updateCart(String data) {
        showDialog();
        ConnectServer.getResponseAPI().updateCart(data).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        tvSave.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.btn_buy_now:
                if (isLoggedIn()) {
//                    if (isChangeQuantity) {
//                        updateCart(createJson());
//                    } else {
                    gotoActivity(self, ShippingActivity.class);
//                    }
                } else {
                    gotoActivity(self, ContinueAsGuestActivity.class);
                }

                break;
            case R.id.tv_save:
                if (isLoggedIn()) {
                    updateCart(createJson());
                } else {
                    updateCartLocal();
                }
                break;
        }
    }

    private void updateCartLocal() {
        boolean isSuccess;
        for (CartItemObj item : listProducts) {
            DbContext.init(self);
            DbContext dbContext = DbContext.getInstance();
            String primaryKey = item.getId() + item.getOption();
            CartItemRealmObj cartItemObj = new CartItemRealmObj();
            cartItemObj.setPrimaryKeyItem(primaryKey);
            cartItemObj.setId(item.getId());
            cartItemObj.setName(item.getName());
            cartItemObj.setImage(item.getImage());
            cartItemObj.setBrand(item.getBrand());
            cartItemObj.setBrandId(item.getBrandId());
            cartItemObj.setCode(item.getCode());
            cartItemObj.setOption(item.getOption());
            cartItemObj.setQuantity(item.getQuantity());
            cartItemObj.setPrice(item.getPrice());
            cartItemObj.setTotalPrice(item.getTotalPrice());
            dbContext.addItemToCart(cartItemObj);
        }
        isSuccess = true;
        if (isSuccess) {
            tvSave.setVisibility(View.GONE);
            showToast(getResources().getString(R.string.update_success));
        } else {
            showToast(getResources().getString(R.string.have_some_error));
        }
    }
}
