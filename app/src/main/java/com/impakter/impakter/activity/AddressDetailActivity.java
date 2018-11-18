package com.impakter.impakter.activity;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.OtherAddressAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.events.OnItemClickListener;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.AddressRespond;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvUserName, tvUserName1, tvAddress, tvCity, tvPhoneNumber;
    private TextView tvOtherAddress, tvOtherCity, tvOtherPhoneNumber;
    private Button btnAddNewAddress;
    private LinearLayout layoutPrimaryAddress, layoutOtherAddress;

    private int userId;
    private AddressRespond.PrimaryAddress primaryAddress;
    private AddressRespond.OtherAddress otherAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);
        initViews();
        initControl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        btnAddNewAddress = findViewById(R.id.btn_add_new_address);

        layoutPrimaryAddress = findViewById(R.id.layout_primary_address);
        layoutOtherAddress = findViewById(R.id.layout_other_address);

        tvUserName = findViewById(R.id.tv_user_name);
        tvUserName1 = findViewById(R.id.tv_user_name_1);
        tvAddress = findViewById(R.id.tv_address);
        tvCity = findViewById(R.id.tv_city_country);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);

        tvOtherAddress = findViewById(R.id.tv_other_address);
        tvOtherCity = findViewById(R.id.tv_other_city_country);
        tvOtherPhoneNumber = findViewById(R.id.tv_other_phone_number);

    }

    private void initData() {
        if (isLoggedIn())
            userId = preferencesManager.getUserLogin().getId();

        getAddress();
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        btnAddNewAddress.setOnClickListener(this);
        layoutPrimaryAddress.setOnClickListener(this);
        layoutOtherAddress.setOnClickListener(this);

    }

    private void getAddress() {
        showDialog();
        ConnectServer.getResponseAPI().getListAddress(userId).enqueue(new Callback<AddressRespond>() {
            @Override
            public void onResponse(Call<AddressRespond> call, Response<AddressRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        primaryAddress = response.body().getData().getPrimaryAddress();
                        otherAddress = response.body().getData().getOtherAddress();

                        if (primaryAddress != null) {
                            setPrimaryAddressData(primaryAddress);
                            layoutPrimaryAddress.setVisibility(View.VISIBLE);
                        } else {
                            layoutPrimaryAddress.setVisibility(View.INVISIBLE);
                        }

                        if (otherAddress != null) {
                            setOtherAddressData(otherAddress);
                            layoutOtherAddress.setVisibility(View.VISIBLE);
                        } else {
                            layoutOtherAddress.setVisibility(View.INVISIBLE);
                        }

                        if (response.body().getCheckAddNew()) {
                            btnAddNewAddress.setVisibility(View.VISIBLE);
                        } else {
                            btnAddNewAddress.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<AddressRespond> call, Throwable t) {
                closeDialog();
                showToast(t.getMessage());
            }
        });
    }

    private void setPrimaryAddressData(AddressRespond.PrimaryAddress primaryAddressData) {
        tvUserName.setText(preferencesManager.getUserLogin().getUsername());
        tvAddress.setText(primaryAddressData.getAddress());

        String city = (primaryAddressData.getCityTown().length() == 0 ? primaryAddressData.getCityTown() : primaryAddressData.getCityTown() + ", ")
                + (primaryAddressData.getState().length() == 0 ? primaryAddressData.getState() : primaryAddressData.getState() + ", ")
                + (primaryAddressData.getCountry().length() == 0 ? primaryAddressData.getCountry() : primaryAddressData.getCountry() + ", ")
                + primaryAddressData.getZipcode();

        tvCity.setText(city);
        tvPhoneNumber.setText(primaryAddressData.getPhoneNumber());
    }

    private void setOtherAddressData(AddressRespond.OtherAddress otherAddress) {
        tvUserName1.setText(preferencesManager.getUserLogin().getUsername());
        tvOtherAddress.setText(otherAddress.getAddress());

        String city = (otherAddress.getCityTown().length() == 0 ? otherAddress.getCityTown() : otherAddress.getCityTown() + ", ")
                + (otherAddress.getState().length() == 0 ? otherAddress.getState() : otherAddress.getState() + ", ")
                + (otherAddress.getCountry().length() == 0 ? otherAddress.getCountry() : otherAddress.getCountry() + ", ")
                + otherAddress.getZipcode();

        tvOtherCity.setText(city);
        tvPhoneNumber.setText(otherAddress.getPhoneNumber());
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_add_new_address:
                gotoActivity(self, AddNewAddressActivity.class);
                break;
            case R.id.layout_primary_address:
                bundle.putInt(Constants.TYPE_ADDRESS, Constants.PRIMARY_ADDRESS);
                bundle.putParcelable(Constants.KEY_PRIMARY_ADDRESS, primaryAddress);
                gotoActivity(self, EditAddressActivity.class, bundle);
                break;
            case R.id.layout_other_address:
                bundle.putInt(Constants.TYPE_ADDRESS, Constants.OTHER_ADDRESS);
                bundle.putParcelable(Constants.KEY_OTHER_ADDRESS, otherAddress);
                gotoActivity(self, EditAddressActivity.class, bundle);
                break;
        }
    }
}
