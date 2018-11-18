package com.impakter.impakter.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.BuildConfig;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.UserObj;
import com.impakter.impakter.object.UserRespond;
import com.impakter.impakter.social.facebook.FaceBookManager;
import com.impakter.impakter.social.facebook.FbUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends BaseActivity implements View.OnClickListener, FaceBookManager.ICallbackLoginFacebook {

    private EditText edtFirstName, edtLastName, edtEmail, edtPassword;
    private TextView tvSignIn;
    private LinearLayout btnLoginFacebook, btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        FaceBookManager.initSdk(SignUpActivity.this);
        setContentView(R.layout.activity_sign_up);
        onPrintHashFB();
        initViews();
        initControl();
    }

    private void initViews() {
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        tvSignIn = findViewById(R.id.tv_sign_in);

        btnLoginFacebook = findViewById(R.id.btn_login_facebook);
        btnCreateAccount = findViewById(R.id.btn_create_account);
    }

    private void initControl() {
        btnLoginFacebook.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);

        tvSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_facebook:
                loginFacebook();
                break;
            case R.id.btn_create_account:
                String firstName = edtFirstName.getText().toString().trim();
                String lastName = edtLastName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (validate()) {
                    registerAccount(firstName, lastName, email, password);
                }
                break;
            case R.id.tv_sign_in:
                gotoActivity(self, LoginActivity.class);
                finish();
                break;
        }
    }

    private void loginFacebook() {
        FaceBookManager.login(SignUpActivity.this, this);
    }

    private void loginBySocial(FbUser user) {
        showDialog();
        ConnectServer.getResponseAPI().loginFacebook(user.getId(), user.getFirst_name(), user.getLast_name(), user.getEmail()).enqueue(new Callback<UserRespond>() {
            @Override
            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        handleAfterLogin(response.body().getUserObj());
                        showToast(response.body().getMessage());
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<UserRespond> call, Throwable t) {
                closeDialog();
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void handleAfterLogin(UserObj user) {
//        if (preferencesManager.getUserLogin() != null) {
//            UserObj oldUser = PreferencesManager.getInstance(SignUpActivity.this).getUserLogin();
//            oldUser = user;
//            oldUser.setPassword(edtPassword.getText().toString().trim());
//            preferencesManager.setUserLogin(oldUser);
//        } else {
//            user.setPassword(edtPassword.getText().toString());
        preferencesManager.setUserLogin(user);
//        }

        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void registerAccount(final String firstName, String lastName, final String email, final String password) {
        showDialog();
        ConnectServer.getResponseAPI().register(firstName, lastName, email, password).enqueue(new Callback<UserRespond>() {
            @Override
            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                Log.e(TAG, "url: " + call.request().toString());
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        preferencesManager.setUserLogin(response.body().getUserObj());

                        Intent intent = new Intent(self, LoginActivity.class);
                        intent.putExtra(Constants.KEY_EMAIL, email);
                        intent.putExtra(Constants.KEY_PASSWORD, password);
                        intent.setAction(Constants.SET_ACCOUNT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                        showToast(response.body().getMessage());
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<UserRespond> call, Throwable t) {
                closeDialog();
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private boolean validate() {
        if (edtFirstName.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_input_first_name));
            edtFirstName.requestFocus();
            return false;
        } else if (edtLastName.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_input_your_last_name));
            edtLastName.requestFocus();
            return false;
        } else if (edtEmail.getText().toString().trim().length() == 0) {
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

    private void onPrintHashFB() {
        try {
            PackageInfo info = SignUpActivity.this.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("key", sign);
//                Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FaceBookManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginFbSuccess(FbUser user) {
        // TODO: 16/08/2018 Get data from facebook account then call loginBySocial API to log in into the app
        loginBySocial(user);
    }

    @Override
    public void onLoginFbError() {
        Toast.makeText(this, getResources().getString(R.string.login_faceboo_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFBnoEmailPublic() {
        showToast(R.string.lbl_login_fail_facebook_hidden_email);
    }

    @Override
    public void onLoginFBLoginOrtherUser() {
        showToast(R.string.lbl_login_orther_user);
    }
}
