package com.impakter.impakter.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.LinearLayout;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CartIdRespond;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderConfirmationActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnGoToMyOrder, btnGoToHome, btnGoToMyProfile;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        btnGoToHome = findViewById(R.id.btn_go_to_home);
        btnGoToMyOrder = findViewById(R.id.btn_go_to_my_order);
        btnGoToMyProfile = findViewById(R.id.btn_go_to_my_profile);
    }

    private void initData() {
        if (isLoggedIn())
            userId = preferencesManager.getUserLogin().getId() + "";
        getMyCartInfo();
    }

    private void initControl() {
        btnGoToHome.setOnClickListener(this);
        btnGoToMyProfile.setOnClickListener(this);
        btnGoToMyOrder.setOnClickListener(this);
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
                        GlobalValue.cartId = response.body().getData().getId();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<CartIdRespond> call, Throwable t) {
                closeDialog();
                showToast(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(self, MainActivity.class);
        switch (v.getId()) {
            case R.id.btn_go_to_home:
                gotoActivityWithClearTask(self, MainActivity.class);
                break;
            case R.id.btn_go_to_my_order:
                intent.setAction(Constants.SHOW_MY_ORDER);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_go_to_my_profile:
                intent.setAction(Constants.SHOW_PROFILE);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
