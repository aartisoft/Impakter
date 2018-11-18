package com.impakter.impakter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.impakter.impakter.BaseFragment;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.UserObj;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private TextView tvSave;
    private ImageView ivBack;
    private UserObj userObj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        initViews();
        initData();
        initControl();
        return rootView;
    }

    private void initViews() {
        tvSave = rootView.findViewById(R.id.tv_save);
        ivBack = rootView.findViewById(R.id.iv_back);

        edtCurrentPassword = rootView.findViewById(R.id.edt_current_password);
        edtNewPassword = rootView.findViewById(R.id.edt_new_password);
        edtConfirmPassword = rootView.findViewById(R.id.edt_confirm_password);
    }

    private void initData() {
        userObj = preferenceManager.getUserLogin();
    }

    private void initControl() {
        tvSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                String currentPassword = edtCurrentPassword.getText().toString().trim();
                String newPassword = edtNewPassword.getText().toString().trim();
                if (validate()) {
                    changePassword(currentPassword, newPassword);
                }
                break;
            case R.id.iv_back:
                getFragmentManager().popBackStack();
                break;

        }
    }

    private boolean validate() {
        if (edtCurrentPassword.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_current_password));
            edtCurrentPassword.requestFocus();
            return false;
        } else if (edtNewPassword.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_new_password));
            edtNewPassword.requestFocus();
            return false;
        } else if (edtConfirmPassword.getText().toString().trim().length() == 0) {
            showToast(getResources().getString(R.string.input_confirm_password));
            edtConfirmPassword.requestFocus();
            return false;
        } else if (!edtNewPassword.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim())) {
            showToast(getResources().getString(R.string.confirm_password_mismatch));
            edtConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void changePassword(String currentPassword, String newPassword) {
        showDialog();
        ConnectServer.getResponseAPI().changePassword(userObj.getId(), currentPassword, newPassword).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        showToast(response.body().getMessage());

                        UserObj userObj = preferenceManager.getUserLogin();
                        userObj.setPassword(edtNewPassword.getText().toString().trim());
                        preferenceManager.setUserLogin(userObj);

                        getFragmentManager().popBackStack();
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

}
