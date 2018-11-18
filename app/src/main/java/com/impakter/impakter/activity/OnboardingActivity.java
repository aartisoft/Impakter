package com.impakter.impakter.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.CartIdRespond;
import com.impakter.impakter.widget.textview.TextViewHeeboRegular;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnboardingActivity extends BaseActivity implements View.OnClickListener {

    private TextViewHeeboRegular btnSignIn, btnSignUp;
    private TextView tvContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        initViews();
        initControl();
    }

    private void initViews() {
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);
        tvContinue = findViewById(R.id.tv_continue);
    }

    private void initControl() {
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                gotoActivity(self, LoginActivity.class);
                break;
            case R.id.btn_sign_up:
                gotoActivity(self, SignUpActivity.class);
                break;
            case R.id.tv_continue:
                gotoActivity(self, MainActivity.class);
                finish();
                break;
        }
    }
}
