package com.impakter.impakter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.CountryAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CountryRespond;
import com.impakter.impakter.object.UserObj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private Button btnContinue;

    private EditText edtFirstName, edtLastName, edtAddress, edtState, edtCity;
    private EditText edtZipCode, edtPhoneNumber;

    private List<CountryRespond.Data> listCountry = new ArrayList<>();
    private CountryAdapter countryAdapter;
    private TextView tvNext;

    private Spinner spCountry;
    private int countryId;
    private UserObj userObj;
    private Bundle bundle;
    private String selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvNext = findViewById(R.id.tv_next);

        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtAddress = findViewById(R.id.edt_address);
        edtState = findViewById(R.id.edt_state);
        edtCity = findViewById(R.id.edt_city);
        edtZipCode = findViewById(R.id.edt_zip_code);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);

        spCountry = findViewById(R.id.sp_country);
        btnContinue = findViewById(R.id.btn_continue);
    }

    private void initData() {
        if (isLoggedIn())
            userObj = preferencesManager.getUserLogin();

        countryAdapter = new CountryAdapter(self, listCountry);
        spCountry.setAdapter(countryAdapter);

        if (userObj != null) {
            edtFirstName.setText(userObj.getFirstName());
            edtLastName.setText(userObj.getLastName());
            edtAddress.setText(userObj.getAddress());
            edtState.setText(userObj.getStateProvince());
            edtCity.setText(userObj.getCityTown());
            edtZipCode.setText(userObj.getPostalCode());
            edtPhoneNumber.setText(userObj.getPhone());

            countryId = Integer.parseInt(userObj.getCountryId());
        }
        getListCountry();
    }

    private void getListCountry() {
        ConnectServer.getResponseAPI().getListCountry().enqueue(new Callback<CountryRespond>() {
            @Override
            public void onResponse(Call<CountryRespond> call, Response<CountryRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        listCountry.clear();
                        listCountry.addAll(response.body().getData());
                        countryAdapter.notifyDataSetChanged();

                        for (int i = 0; i < listCountry.size(); i++) {
                            if (countryId == listCountry.get(i).getId()) {
                                spCountry.setSelection(i);
                            }
                        }
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryRespond> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = listCountry.get(position).getTitle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validate() {
        if (edtAddress.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_your_address));
            edtAddress.requestFocus();
            return false;
        } else if (edtZipCode.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_zip_code));
            edtZipCode.requestFocus();
            return false;
        } else if (edtCity.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_city));
            edtCity.requestFocus();
        } else if (edtPhoneNumber.getText().toString().trim().length() == 0) {
            edtPhoneNumber.requestFocus();
            showToast(getResources().getString(R.string.input_your_phone_number));
            edtPhoneNumber.requestFocus();
        } else if (bundle == null) {
            showToast(getResources().getString(R.string.pleae_choose_shipment_information));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                if (validate()) {
                    Log.e(TAG, "size: " + bundle.getParcelableArrayList(Constants.ARR_COURIER).size());
                    String address = edtAddress.getText().toString().trim();
                    String state = edtState.getText().toString().trim();
                    String city = edtCity.getText().toString().trim();
                    String country = selectedCountry;
                    String zipCode = edtZipCode.getText().toString().trim();

                    String phoneNumber = edtPhoneNumber.getText().toString().trim();
                    String shippingAddress = address + "' " + city + ", " + state + ", " + country + ", " + zipCode;

                    bundle.putString(Constants.SHIPPING_ADDRESS, shippingAddress);
                    bundle.putString(Constants.PHONE_NUMBER, phoneNumber);
                    gotoActivity(self, PaymentActivity.class, bundle);
                }

                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_next:
                Intent intent = new Intent(self, ShipmentTypeActivity.class);
                intent.putExtra(Constants.ZIP_CODE, edtZipCode.getText().toString().trim());
                startActivityForResult(intent, Constants.REQUEST_CODE_GO_TO_CHOOSE_SHIPMENT_SERVICE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_GO_TO_CHOOSE_SHIPMENT_SERVICE && resultCode == RESULT_OK) {
            bundle = data.getExtras();
            Log.e(TAG, "onActivityResult: " + data.getExtras().getParcelableArrayList(Constants.ARR_COURIER).size());
        }
    }
}
