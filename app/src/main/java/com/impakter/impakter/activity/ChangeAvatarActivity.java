package com.impakter.impakter.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.fragment.ChangeAvatarFragment;
import com.impakter.impakter.fragment.ChooseImageFragment;

public class ChangeAvatarActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnChooseImage;
    private ImageView ivClose, ivDropDown;
    private TextView tvSave;
    private RelativeLayout toolbar;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter filter;
    private OnRequestCaptureImageListener onRequestCaptureImageListener;

    public RelativeLayout getLayoutToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        showFragment(new ChangeAvatarFragment());
        mListener();
        initViews();
        initData();
        initControl();
    }

    private void mListener() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.FINISH_ACTIVITY)) {
                    ChangeAvatarActivity.this.setResult(RESULT_OK, intent);
                    ChangeAvatarActivity.this.finish();
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction(Constants.FINISH_ACTIVITY);
        registerReceiver(broadcastReceiver, filter);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        btnChooseImage = findViewById(R.id.btn_choose_photo);
        ivClose = findViewById(R.id.iv_close);
        tvSave = findViewById(R.id.tv_save);
        ivDropDown = findViewById(R.id.iv_dropdown);
    }

    private void initData() {

    }

    private void initControl() {
        tvSave.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_content, fragment).commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:

                break;
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_choose_photo:
                if (getSupportFragmentManager().findFragmentById(R.id.fr_content) instanceof ChangeAvatarFragment) {
                    showFragment(new ChooseImageFragment());
                    ivDropDown.setImageResource(R.drawable.ic_arrow_drop_up);
                } else {
                    showFragment(new ChangeAvatarFragment());
                    ivDropDown.setImageResource(R.drawable.ic_arrow_drop_down);
                }
                break;
        }
    }

    public interface OnRequestCaptureImageListener {
        void onSuccess(int requestCode, int resultCode, Intent data);
    }

    public void setOnRequestCaptureImageListener(OnRequestCaptureImageListener onRequestCaptureImageListener) {
        this.onRequestCaptureImageListener = onRequestCaptureImageListener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onRequestCaptureImageListener.onSuccess(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
