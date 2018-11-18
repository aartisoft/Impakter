package com.impakter.impakter.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.config.PreferencesManager;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CartIdRespond;
import com.impakter.impakter.object.UserObj;
import com.impakter.impakter.object.UserRespond;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private CheckBox chkRememberMe;
    private TextView tvForgotPassword, tvSignUp;
    private TextViewHeeboRegular btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initControl();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(Constants.SET_ACCOUNT)) {
                String email = intent.getStringExtra(Constants.KEY_EMAIL);
                String password = intent.getStringExtra(Constants.KEY_PASSWORD);

                edtEmail.setText(email);
                edtPassword.setText(password);
            }

        }
    }

    private void initViews() {
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvSignUp = findViewById(R.id.tv_sign_up);

        chkRememberMe = findViewById(R.id.chk_remember_me);
        btnSignIn = findViewById(R.id.btn_sign_in);

        checkRememberAccount();

    }

    private void checkRememberAccount() {
        boolean isRememberAccount = preferencesManager.isSaveAccount();
        if (isRememberAccount) {
            chkRememberMe.setChecked(true);
            edtEmail.setText(preferencesManager.getUserLogin().getEmail());
            edtPassword.setText(preferencesManager.getUserLogin().getPassword());
        } else {
            chkRememberMe.setChecked(false);
        }
    }


    private void initControl() {
        btnSignIn.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        chkRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferencesManager.setSaveAccount(true);
                } else {
                    preferencesManager.setSaveAccount(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                if (validate())
                    login();
                break;
            case R.id.tv_forgot_password:
                gotoActivity(self, ForgotPasswordActivity.class);
                break;
            case R.id.tv_sign_up:
                gotoActivity(self, SignUpActivity.class);
                finish();
                break;
        }
    }

    private void login() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") final String ime = telephonyManager.getDeviceId();

        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString();
        showDialog();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                ConnectServer.getResponseAPI().login(email, password, deviceToken, Constants.TYPE_ANDROID).enqueue(new Callback<UserRespond>() {
                    @Override
                    public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                        Log.e(TAG, "Url: " + call.request().toString());
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals(Constants.SUCCESS)) {
//                                handleAfterLogin(response.body().getUserObj());
                                getMyCartInfo(response.body().getUserObj());
//                                showToast(response.body().getMessage());
                            } else {
                                showToast(response.body().getMessage());
                            }
                        }

                        closeDialog();
                    }

                    @Override
                    public void onFailure(Call<UserRespond> call, Throwable t) {
                        showToast(t.toString());
                        Log.e(TAG, "onFailure: " + t.toString());
                        closeDialog();
                    }
                });
            }
        });
    }

    private void getMyCartInfo(final UserObj userObj) {
        showDialog();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String ime = telephonyManager.getDeviceId();
        ConnectServer.getResponseAPI().getCartId(userObj.getId() + "", ime).enqueue(new Callback<CartIdRespond>() {
            @Override
            public void onResponse(Call<CartIdRespond> call, Response<CartIdRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        GlobalValue.cartId = response.body().getData().getId();
                        handleAfterLogin(userObj);
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

    private boolean validate() {
        if (edtEmail.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_input_email));
            edtEmail.requestFocus();
            return false;
        } else if (edtPassword.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_input_password));
            edtPassword.requestFocus();
            return false;
        } else if (!isValidEmailAddress(edtEmail.getText().toString().trim())) {
            edtEmail.requestFocus();
            showToast(getResources().getString(R.string.wrong_email_type));
            return false;
        }
        return true;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void handleAfterLogin(UserObj user) {
//        if (preferencesManager.getUserLogin() != null) {
//            UserObj oldUser = PreferencesManager.getInstance(LoginActivity.this).getUserLogin();
//            oldUser = user;
//            oldUser.setPassword(edtPassword.getText().toString().trim());
//            preferencesManager.setUserLogin(oldUser);
//        } else {
        user.setPassword(edtPassword.getText().toString());
        preferencesManager.setUserLogin(user);
//        }

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
