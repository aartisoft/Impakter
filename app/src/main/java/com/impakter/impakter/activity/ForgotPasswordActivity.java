package com.impakter.impakter.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    private EditText edtEmail;
    private TextViewHeeboRegular btnSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        initControl();
    }

    private void initViews() {
        edtEmail = findViewById(R.id.edt_email);
        btnSendEmail = findViewById(R.id.btn_send_email);
    }

    private void initControl() {
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (validate()) {
                    forgotPassword(email);
                }
            }
        });
    }

    private void forgotPassword(String email) {
        showDialog();
        ConnectServer.getResponseAPI().forgotPassword(email).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showMessageDialog(response.body().getMessage());
                    } else {
                        showMessageDialog(response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<BaseMessageRespond> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                showToast(t.getMessage());
                closeDialog();
            }
        });
    }

    private boolean validate() {
        if (edtEmail.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.please_input_email));
            edtEmail.requestFocus();
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

    private void showMessageDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setTitle(self.getResources().getString(R.string.email_has_been_sent));
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }
}
