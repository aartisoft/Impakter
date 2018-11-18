package com.impakter.impakter.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.config.PreferencesManager;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.CartIdRespond;
import com.impakter.impakter.object.UserObj;
import com.impakter.impakter.object.UserRespond;
import com.impakter.impakter.utils.NetworkUtility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    private Handler mHandler;
    private Context self;

    List<String> listPermission = new ArrayList<>();
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private static final String TYPE_FACEBOOK = "1";
    private static final String TYPE_GOOGLEPLUS = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions()) {
                gotoMainActivity();
            }
        } else {
            gotoMainActivity();
        }

    }

    private void getMyCartInfo(final UserObj userObj) {
//        showDialog();
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

    private void saveCartToOrder(final UserObj userObj) {
        showDialog();
        ConnectServer.getResponseAPI().saveCartToOrder(GlobalValue.cartId, userObj.getId()).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        handleAfterLogin(userObj);
                    } else {
                        Log.e(TAG, "error: " + response.body().getMessage());
                    }
                }
                closeDialog();
            }

            @Override
            public void onFailure(Call<BaseMessageRespond> call, Throwable t) {
                closeDialog();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void gotoMainActivity() {

        // Push notification.
        this.mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (isLoggedIn()) {
                    //check expired
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    @SuppressLint("MissingPermission") final String ime = telephonyManager.getDeviceId();
                    final UserObj user = preferencesManager.getUserLogin();
                    if (user != null) {
                        if (user.getSocialNetworkId().trim().length() == 0) {
                            showDialog();
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String deviceToken = instanceIdResult.getToken();
                                    ConnectServer.getResponseAPI().login(user.getEmail(), user.getPassword(), deviceToken, Constants.TYPE_ANDROID).enqueue(new Callback<UserRespond>() {
                                        @Override
                                        public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                                            if (response.isSuccessful()) {
                                                if (response.body().getStatus().equals(Constants.SUCCESS)) {
                                                    UserObj userObj = response.body().getUserObj();
                                                    getMyCartInfo(userObj);
//                                                    if (GlobalValue.isCheckOutAsAGuest) {
//                                                        saveCartToOrder(userObj);
//                                                    } else {
//                                                        handleAfterLogin(userObj);
//                                                    }
                                                } else {
                                                    closeDialog();
                                                    showToast(response.body().getMessage());
                                                }
                                            }
//                                            closeDialog();
                                        }

                                        @Override
                                        public void onFailure(Call<UserRespond> call, Throwable t) {
                                            showToast(t.toString());
                                            closeDialog();
                                        }
                                    });
                                }
                            });

                        } else {
                            String socialId = user.getSocialNetworkId();
                            String firstName = user.getFirstName();
                            String lastName = user.getLastName();
                            String email = user.getEmail();
                            showDialog();
                            ConnectServer.getResponseAPI().loginFacebook(socialId, firstName, lastName, email).enqueue(new Callback<UserRespond>() {
                                @Override
                                public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus().equals(Constants.SUCCESS)) {
                                            UserObj userObj = response.body().getUserObj();
                                            handleAfterLogin(userObj);
                                        } else {
                                            showToast(response.body().getMessage());
                                        }
                                    }
                                    closeDialog();
                                }

                                @Override
                                public void onFailure(Call<UserRespond> call, Throwable t) {
                                    showToast(t.toString());
                                    closeDialog();
                                }
                            });
                        }

                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this,
                            OnboardingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_left);
                    finish();
                }
            }
        }, 500);
    }

    private void handleAfterLogin(UserObj user) {
        if (preferencesManager.getUserLogin() != null) {
            UserObj oldUser = preferencesManager.getUserLogin();
            user = oldUser;
            preferencesManager.setUserLogin(user);
        } else {
            preferencesManager.setUserLogin(user);
        }

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    public boolean isLoggedIn() {
        if (preferencesManager.getUserLogin() == null) {
            return false;
        } else {
            if (preferencesManager.getUserLogin().getId() != 0) {
                return true;
            } else
                return false;
        }
    }

    private boolean checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermission.add(permission);
            }
        }

        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermission.toArray
                    (new String[listPermission.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                Boolean allPermissionsGranted = true;
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            allPermissionsGranted = false;
                            break;
                        }
                    }
                    if (!allPermissionsGranted) {
                        boolean somePermissionsForeverDenied = false;
                        boolean checkAllPermissionAllowed = true;
                        for (String permission : permissions) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                                //denied
                                checkAndRequestPermissions();
                                checkAllPermissionAllowed = false;
                                Log.e("denied", permission);
                            } else {
                                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                                    //allowed
                                    Log.e("allowed", permission);
                                } else {
                                    //set to never ask again
                                    Log.e("set to never ask again", permission);
                                    somePermissionsForeverDenied = true;
                                    checkAllPermissionAllowed = false;
                                    break;
                                }
                            }
                        }

                        if (somePermissionsForeverDenied) {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Permissions Required")
                                    .setMessage("You have forcefully denied some of the required permissions " +
                                            "for this action. Please open settings, go to permissions and allow them.")
                                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.exit(1);
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        }
                        if (checkAllPermissionAllowed) {
                            gotoMainActivity();
                        }
                    } else {
                        gotoMainActivity();
                    }
                }
        }
    }
}
