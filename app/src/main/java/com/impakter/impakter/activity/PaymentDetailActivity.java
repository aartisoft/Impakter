package com.impakter.impakter.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.CreditCardAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.UserObj;
import com.impakter.impakter.utils.AESCrypt;

import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvEdit;
    private EditText edtCardHolderName, edtCardNumber, edtMonth, edtYear, edtCVV;
    private Button btnSave;
    private UserObj userObj;
    private String secretKey;

    private String holderName, cardNumber, expirationDate, CVV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_payment);
        secretKey = "0123456789abcdef";

        initViews();
        initData();
        initControl();
//        if (BuildConfig.DEBUG) {
//            AESCrypt.DEBUG_LOG_ENABLED = true;
//        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvEdit = findViewById(R.id.tv_edit);

        edtCardHolderName = findViewById(R.id.edt_holder_name);
        edtCardNumber = findViewById(R.id.edt_card_number);
        edtMonth = findViewById(R.id.edt_month);
        edtYear = findViewById(R.id.edt_year);
        edtCVV = findViewById(R.id.edt_cvv);

        btnSave = findViewById(R.id.btn_save);

        edtCardHolderName.setEnabled(false);
        edtCardNumber.setEnabled(false);
        edtMonth.setEnabled(false);
        edtYear.setEnabled(false);
        edtCVV.setEnabled(false);

//        Log.e(TAG, "initViews: " + decrypt("oyiKCazms2lpch5eLEW8nQ=="));
    }

    private void initData() {
        if (isLoggedIn())
            userObj = preferencesManager.getUserLogin();

        edtCardHolderName.setText(userObj.getCardHolderName());
        edtCardNumber.setText(userObj.getCardNumberToDisplay());
        String expirationDate = userObj.getCardExpirationDate();
        if (expirationDate.trim().length() != 0) {
            String month = expirationDate.substring(0, expirationDate.indexOf("/"));
            String year = expirationDate.substring(expirationDate.indexOf("/") + 1);
            edtMonth.setText(month);
            edtYear.setText(year);
        }
        edtCVV.setText(userObj.getCardNumberCvv());
    }

    private void initControl() {
        ivBack.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        btnSave.setOnClickListener(this);

//        edtCardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());

    }

    private String encrypt(String message) {
        try {
            String encryptedMsg = AESCrypt.encrypt(secretKey, message);
            return encryptedMsg;
        } catch (GeneralSecurityException e) {
            //handle error
            Log.e(TAG, "err: " + e.getMessage());
        }
        return message;
    }

    private String decrypt(String encryptedMsg) {
        try {
            String messageAfterDecrypt = AESCrypt.decrypt(secretKey, encryptedMsg);
            return messageAfterDecrypt;
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "decrypt err: " + e.getMessage());
            //handle error - could be due to incorrect password or tampered encryptedMsg
        }
        return encryptedMsg;
    }

    private boolean validate() {
        if (edtCardHolderName.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_card_holder_name));
            edtCardHolderName.requestFocus();
            return false;
        } else if (edtCardNumber.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_card_number));
            edtCardNumber.requestFocus();
            return false;
        } else if (edtMonth.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_month));
            edtMonth.requestFocus();
            return false;
        } else if (edtYear.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_year));
            edtYear.requestFocus();
            return false;
        } else if (edtCVV.getText().toString().length() == 0) {
            showToast(getResources().getString(R.string.input_cvv));
            edtCVV.requestFocus();
            return false;
        }
        return true;
    }

    private void editCreditCard(final String holderName, final String cardNumber, final String expirationDate, final String cvv) {
        showDialog();
        ConnectServer.getResponseAPI().editCreditCard(userObj.getId(), holderName, cardNumber, expirationDate, cvv).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        userObj.setCardHolderName(holderName);
                        userObj.setCardNumber(cardNumber);
                        userObj.setCardExpirationDate(expirationDate);
                        userObj.setCardNumberCvv(cvv);
                        preferencesManager.setUserLogin(userObj);

                        btnSave.setVisibility(View.GONE);
                        edtCardHolderName.setEnabled(false);
                        edtCardNumber.setEnabled(false);
                        edtMonth.setEnabled(false);
                        edtYear.setEnabled(false);
                        edtCVV.setEnabled(false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_edit:
                if (btnSave.getVisibility() == View.GONE)
                    btnSave.setVisibility(View.VISIBLE);

                edtCardHolderName.setEnabled(true);
                edtCardNumber.setEnabled(true);
                edtMonth.setEnabled(true);
                edtYear.setEnabled(true);
                edtCVV.setEnabled(true);
                break;
            case R.id.btn_save:
                if (validate()) {
                    holderName = edtCardHolderName.getText().toString().trim();
                    cardNumber = edtCardNumber.getText().toString().trim();
                    expirationDate = edtMonth.getText().toString().trim() + "/" + edtYear.getText().toString().trim();
                    CVV = edtCVV.getText().toString().trim();

                    editCreditCard(holderName, cardNumber, expirationDate, CVV);
                }

                break;
        }
    }
}
