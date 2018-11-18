package com.impakter.impakter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.CountryAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.AddressRespond;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.CountryRespond;
import com.impakter.impakter.object.UserObj;
import com.impakter.impakter.object.UserRespond;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddressActivity extends BaseActivity implements View.OnClickListener {
    private final int SET_PRIMARY_ADDRESS = 1;
    private final int SET_OTHER_ADDRESS = 2;

    private ImageView ivBack;
    private TextView tvSave;
    private SwitchCompat switchCompat;
    private EditText edtFirstName, edtLastName, edtAddress, edtState;
    private EditText edtCity;
    private Spinner spCountry;
    private EditText edtZipCode, edtPhoneNumber;
    private List<CountryRespond.Data> listCountry = new ArrayList<>();
    private CountryAdapter countryAdapter;
    private int userId;
    private String address, cityTown, state, zipCode;
    private String phoneNumber;
    private int countryId;
    private int typeAddress;

    private AddressRespond.PrimaryAddress primaryAddress;
    private AddressRespond.OtherAddress otherAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvSave = findViewById(R.id.tv_save);
        switchCompat = findViewById(R.id.switch_button);

        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtFirstName.setEnabled(false);
        edtLastName.setEnabled(false);

        edtAddress = findViewById(R.id.edt_address);
        edtState = findViewById(R.id.edt_state);
        edtCity = findViewById(R.id.edt_city);
        edtZipCode = findViewById(R.id.edt_zip_code);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);

        spCountry = findViewById(R.id.sp_country);

    }

    private void initData() {
        if (isLoggedIn())
            userId = preferencesManager.getUserLogin().getId();

        Intent intent = getIntent();
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
            if (bundle != null) {
                typeAddress = bundle.getInt(Constants.TYPE_ADDRESS);
                if (typeAddress == Constants.PRIMARY_ADDRESS) {
                    primaryAddress = bundle.getParcelable(Constants.KEY_PRIMARY_ADDRESS);
                } else {
                    otherAddress = bundle.getParcelable(Constants.KEY_OTHER_ADDRESS);
                }
            }
        }
        countryAdapter = new CountryAdapter(self, listCountry);
        spCountry.setAdapter(countryAdapter);

        switchCompat.setChecked(typeAddress == 1 ? true : false);

        if (bundle != null) {
            setDataFromIntent();
        } else {
            getUserInfo(userId);
        }
        getListCountry();

    }

    private void setDataFromIntent() {
        edtFirstName.setText(preferencesManager.getUserLogin().getFirstName());
        edtLastName.setText(preferencesManager.getUserLogin().getLastName());
        if (typeAddress == Constants.PRIMARY_ADDRESS) {
            edtAddress.setText(primaryAddress.getAddress());
            edtState.setText(primaryAddress.getState());
            edtCity.setText(primaryAddress.getCityTown());
            edtZipCode.setText(primaryAddress.getZipcode());
            edtPhoneNumber.setText(primaryAddress.getPhoneNumber());
            countryId = primaryAddress.getCountryId();
        } else {
            edtAddress.setText(otherAddress.getAddress());
            edtState.setText(otherAddress.getState());
            edtCity.setText(otherAddress.getCityTown());
            edtZipCode.setText(otherAddress.getZipcode());
            edtPhoneNumber.setText(otherAddress.getPhoneNumber());
            countryId = otherAddress.getCountryId();
        }


    }

    private void getUserInfo(final int userId) {
        showDialog();
        ConnectServer.getResponseAPI().getUserInfo(userId + "", userId + "").enqueue(new Callback<UserRespond>() {
            @Override
            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        UserObj userObj = response.body().getUserObj();
                        edtFirstName.setText(userObj.getFirstName());
                        edtLastName.setText(userObj.getLastName());
                        edtAddress.setText(userObj.getAddress());
                        edtCity.setText(userObj.getCityTown());
                        edtZipCode.setText(userObj.getPostalCode());
                        edtPhoneNumber.setText(userObj.getPhone());
//                        edtState.setText(userObj.get);
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<UserRespond> call, Throwable t) {
                Toast.makeText(self, t.getMessage(), Toast.LENGTH_SHORT).show();
                closeDialog();
            }
        });
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
        tvSave.setOnClickListener(this);

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = listCountry.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateAddress(final int action) {
        showDialog();
        ConnectServer.getResponseAPI().updateAddress(userId, address, cityTown, state, countryId, zipCode, phoneNumber, action).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showToast(response.body().getMessage());

                        UserObj userObj = preferencesManager.getUserLogin();
                        userObj.setAddress(address);
                        userObj.setCityTown(cityTown);
                        userObj.setStateProvince(state);
                        userObj.setCountryId(String.valueOf(countryId));
                        userObj.setPostalCode(zipCode);
                        userObj.setPhone(phoneNumber);
                        preferencesManager.setUserLogin(userObj);

                        finish();
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<BaseMessageRespond> call, Throwable t) {
                closeDialog();
                showToast(t.getMessage());
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
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                address = edtAddress.getText().toString().trim();
                cityTown = edtCity.getText().toString().trim();
                zipCode = edtZipCode.getText().toString().trim();
                phoneNumber = edtPhoneNumber.getText().toString().trim();
                state = edtState.getText().toString().trim();

                if (validate()) {
                    if (switchCompat.isChecked()) {
                        updateAddress(SET_PRIMARY_ADDRESS);
                    } else {
                        updateAddress(SET_OTHER_ADDRESS);
                    }
                }
                break;
        }
    }
}
