package com.impakter.impakter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.object.UserObj;

public class PaymentActivity extends BaseActivity {
    private ImageView ivBack;
    private EditText edtCardHolderName, edtCardNumber, edtMonth, edtYear, edtCVV;
    private RadioButton radCreditCard, radPayPal;
    private Button btnContinue;
    private LinearLayout layoutCreditCardInfo;
    private Bundle bundle;

    private int paymentMethod = 1;
    private UserObj userObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initViews();
        initData();
        initControl();
    }

    private void initViews() {
        layoutCreditCardInfo = findViewById(R.id.layout_credit_card_info);

        ivBack = findViewById(R.id.iv_back);
        edtCardHolderName = findViewById(R.id.edt_card_holder_name);
        edtCardNumber = findViewById(R.id.edt_card_number);
        edtMonth = findViewById(R.id.edt_month);
        edtYear = findViewById(R.id.edt_year);
        edtCVV = findViewById(R.id.edt_cvv);

        radCreditCard = findViewById(R.id.rad_credit_card);
        radCreditCard.setChecked(true);
        radPayPal = findViewById(R.id.rad_payPal);

        btnContinue = findViewById(R.id.btn_continue);
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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        edtCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 16) {
                    showToast(getResources().getString(R.string.max_length_16));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        radCreditCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    paymentMethod = Constants.CREDIT_CARD;
                    layoutCreditCardInfo.setVisibility(View.VISIBLE);
                }
            }
        });
        radPayPal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    paymentMethod = Constants.PAYPAL;
                    layoutCreditCardInfo.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentMethod == Constants.CREDIT_CARD) {
                    if (validate()) {
                        Intent intent = getIntent();
                        if (intent != null) {
                            bundle = intent.getExtras();
                            Log.e(TAG, "size: " + bundle.getParcelableArrayList(Constants.ARR_COURIER).size());
                            bundle.putInt(Constants.PAYMENT_METHOD, paymentMethod);
                            bundle.putString(Constants.CARD_HOLDER_NAME, edtCardHolderName.getText().toString().trim());
                            if (userObj.getCardNumber().length() != 0) {
                                bundle.putString(Constants.CARD_NUMBER, userObj.getCardNumber());
                            } else {
                                bundle.putString(Constants.CARD_NUMBER, edtCardNumber.getText().toString().trim());
                            }
                            bundle.putString(Constants.MONTH, edtMonth.getText().toString().trim());
                            bundle.putString(Constants.YEAR, edtYear.getText().toString());
                            bundle.putString(Constants.CVV, edtCVV.getText().toString().trim());

                        }
                        gotoActivity(self, ConfirmActivity.class, bundle);
                    }
                } else {
                    Intent intent = getIntent();
                    if (intent != null) {
                        bundle = intent.getExtras();
                        Log.e(TAG, "size: " + bundle.getParcelableArrayList(Constants.ARR_COURIER).size());
                        bundle.putInt(Constants.PAYMENT_METHOD, paymentMethod);
                        bundle.putString(Constants.CARD_HOLDER_NAME, edtCardHolderName.getText().toString().trim());
                        if (userObj.getCardNumber().length() != 0) {
                            bundle.putString(Constants.CARD_NUMBER, userObj.getCardNumber());
                        } else {
                            bundle.putString(Constants.CARD_NUMBER, edtCardNumber.getText().toString().trim());
                        }
                        bundle.putString(Constants.MONTH, edtMonth.getText().toString().trim());
                        bundle.putString(Constants.YEAR, edtYear.getText().toString());
                        bundle.putString(Constants.CVV, edtCVV.getText().toString().trim());

                    }
                    gotoActivity(self, ConfirmActivity.class, bundle);
                }

            }
        });
    }

    private boolean validate() {
        if (!radPayPal.isChecked() && !radCreditCard.isChecked()) {
            showToast(getResources().getString(R.string.please_choose_payment_method));
            return false;
        } else if (edtCardHolderName.getText().toString().trim().length() == 0) {
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
        } else if (edtCVV.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_cvv));
            edtCVV.requestFocus();
            return false;
        }
        return true;
    }

}
