package com.impakter.impakter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.impakter.impakter.BaseActivity;
import com.impakter.impakter.R;
import com.impakter.impakter.adapter.MenuAdapter;
import com.impakter.impakter.adapter.MessageAdapter;
import com.impakter.impakter.config.Constants;
import com.impakter.impakter.config.GlobalValue;
import com.impakter.impakter.dblocal.Convertobject.ConvertObject;
import com.impakter.impakter.dblocal.configrealm.DbContext;
import com.impakter.impakter.fragment.FavouriteFragment;
import com.impakter.impakter.fragment.HomeFragment;
import com.impakter.impakter.fragment.MyFavouriteFragment;
import com.impakter.impakter.fragment.OtherProfileFragment;
import com.impakter.impakter.fragment.ProfileFragment;
import com.impakter.impakter.fragment.UpdateProfileFragment;
import com.impakter.impakter.network.ConnectServer;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.CartItemObj;
import com.impakter.impakter.object.UserObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int NOTIFICATIONS = 0;
    private static final int MESSAGES = 1;
    private static final int PAYMENT_DETAILS = 2;
    private static final int SHIPPING_DETAILS = 3;
    private static final int SETTINGS = 4;
    private static final int CUSTOMER_CARE = 5;
    private static final int LOG_OUT = 6;

    private static final int TAB_MY_ORDER = 2;

    private ImageView tabHome, tabFavourite, tabAccount, tabCart, tabMenu;
    private ImageView ivClose;
    private MenuAdapter menuAdapter;
    private DrawerLayout drawerLayout;
    private RecyclerView rcvMenu;
    private ArrayList<String> listMenu = new ArrayList<>();

    private ImageView ivSearch;
    private LinearLayout btnEdit;
    private LinearLayout toolbar;
    private OnRequestChangeAvatarListener onActivityResultListener;
    private boolean doubleBackToExitPressedOnce = false;

    private List<CartItemObj> listProducts = new ArrayList<>();

    public LinearLayout getToolbar() {
        return toolbar;
    }

    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    private void setUpToolbar() {
        ImageView ivSearch = toolbar.findViewById(R.id.iv_search);
        ImageView ivFilter = toolbar.findViewById(R.id.iv_filter);
        LinearLayout btnEdit = toolbar.findViewById(R.id.btn_edit);
        TextView tvTitle = toolbar.findViewById(R.id.tv_title);
        btnEdit.setVisibility(View.GONE);
        ivSearch.setVisibility(View.VISIBLE);
        ivFilter.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        initControl();
        processShowScreen();

        if (isLoggedIn())
            checkHasCartItemLocal();

//        showFragment(new HomeFragment(), false);
    }

    private void checkHasCartItemLocal() {
        DbContext.init(self);
        listProducts.clear();
        listProducts.addAll(ConvertObject.convertCartItemRealObjToCartItemObj(DbContext.getInstance().getCartItems()));

        if (listProducts.size() != 0) {
            String cartData = createJsonCartItem();
            saveProductToCart(cartData);
        }
    }

    private void saveProductToCart(String cartData) {
        showDialog();
        ConnectServer.getResponseAPI().saveProductToCart(cartData).enqueue(new Callback<BaseMessageRespond>() {
            @Override
            public void onResponse(Call<BaseMessageRespond> call, Response<BaseMessageRespond> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        DbContext.getInstance().deleteAllCartItem();
                        Log.e(TAG, "save product to cart successfully");
                    } else {
                        Log.e(TAG, "err: " + response.body().getMessage());
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

    private String createJsonCartItem() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listProducts.size(); i++) {
            CartItemObj cartItemObj = listProducts.get(i);
            JSONObject object = new JSONObject();
            try {
                object.put("proId", cartItemObj.getId() + "");
                object.put("quantity", cartItemObj.getQuantity() + "");
                String option = "";
                option = cartItemObj.getOption();
                String size = "", color = "";
                if (option.length() != 0) {
                    size = option.substring(0, option.indexOf("/"));
                    color = option.substring(option.indexOf("/") + 1);
                }
                object.put("size", size);
                object.put("color", color);

                jsonArray.put(object);

                jsonObject.put("cartId", GlobalValue.cartId + "");
                jsonObject.put("listCartItem", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.e(TAG, "createJson: " + jsonObject.toString());
        return jsonObject.toString();
    }

    private void processShowScreen() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case Constants.SHOW_PROFILE:
                        if (isLoggedIn()) {
                            formatBottomMenu();
                            tabAccount.setImageResource(R.drawable.ic_account_active);
                            if (getSupportFragmentManager().findFragmentById(R.id.fr_content) instanceof ProfileFragment) {
                                return;
                            } else {
                                showFragment(new ProfileFragment(), false);
                            }
                        } else {
                            showConfirmDialog();
                        }
                        break;
                    case Constants.SHOW_MY_ORDER:
                        if (isLoggedIn()) {
                            formatBottomMenu();
                            tabAccount.setImageResource(R.drawable.ic_account_active);
                            if (getSupportFragmentManager().findFragmentById(R.id.fr_content) instanceof ProfileFragment) {
                                return;
                            } else {
                                ProfileFragment profileFragment = new ProfileFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt(Constants.CURRENT_POSITION, TAB_MY_ORDER);
                                profileFragment.setArguments(bundle);
                                showFragment(profileFragment, false);
                            }
                        } else {
                            showConfirmDialog();
                        }
                        break;
                    default:
                        showFragment(new HomeFragment(), false);
                        break;
                }
            } else {
                showFragment(new HomeFragment(), false);
            }
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);

        tabHome = findViewById(R.id.tab_home);
        tabFavourite = findViewById(R.id.tab_favourite);
        tabAccount = findViewById(R.id.tab_account);
        tabCart = findViewById(R.id.tab_cart);
        tabMenu = findViewById(R.id.tab_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        rcvMenu = findViewById(R.id.rcv_menu);
        rcvMenu.setHasFixedSize(true);
        rcvMenu.setLayoutManager(new LinearLayoutManager(self));

        ivClose = findViewById(R.id.iv_close);
        ivSearch = findViewById(R.id.iv_search);
        btnEdit = findViewById(R.id.btn_edit);
    }

    private void initData() {
        menuAdapter = new MenuAdapter(self, listMenu);
        rcvMenu.setAdapter(menuAdapter);

        menuAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TextView textView, int position) {
                switch (position) {
                    case NOTIFICATIONS:
                        if (isLoggedIn()) {
                            setItemBackgroundMenu(position);
                            Intent intent = new Intent(self, NotificationsActivity.class);
                            startActivityForResult(intent, Constants.REQUEST_CODE_GO_TO_NOTIFICATION_ACTIVITY);
                            overridePendingTransition(R.anim.slide_in_left,
                                    R.anim.slide_out_left);
                        } else {
                            showConfirmDialog();
                        }

                        break;
                    case MESSAGES:
                        if (isLoggedIn()) {
                            setItemBackgroundMenu(position);
                            gotoActivity(self, MessageActivity.class);
                        } else {
                            showConfirmDialog();
                        }

                        break;
                    case PAYMENT_DETAILS:
                        if (isLoggedIn()) {
                            setItemBackgroundMenu(position);
                            gotoActivity(self, PaymentDetailActivity.class);
                        } else {
                            showConfirmDialog();
                        }

                        break;
                    case SHIPPING_DETAILS:
                        if (isLoggedIn()) {
                            setItemBackgroundMenu(position);
                            gotoActivity(self, AddressDetailActivity.class);
                        } else {
                            showConfirmDialog();
                        }
                        break;
                    case SETTINGS:
                        setItemBackgroundMenu(position);
                        break;
                    case CUSTOMER_CARE:
                        if (isLoggedIn()) {
                            setItemBackgroundMenu(position);
                            gotoActivity(self, CustomerCareActivity.class);
                        } else {
                            showConfirmDialog();
                        }

                        break;
                    case LOG_OUT:
                        logout();
                        setItemBackgroundMenu(position);
                        break;
                }
            }
        });
        initMenu();
    }

    private void setItemBackgroundMenu(int position) {
        menuAdapter.setPosition(position);
        menuAdapter.notifyDataSetChanged();
        drawerLayout.closeDrawers();
    }

    private void initMenu() {
        listMenu.add(getResources().getString(R.string.notifications));
        listMenu.add(getResources().getString(R.string.messages));
        listMenu.add(getResources().getString(R.string.payment_details));
        listMenu.add(getResources().getString(R.string.shipping_details));
        listMenu.add(getResources().getString(R.string.setting));
        listMenu.add(getResources().getString(R.string.customer_care));
        if (isLoggedIn()) {
            listMenu.add(getResources().getString(R.string.logout));
        }
        menuAdapter.notifyDataSetChanged();
    }

    private void initControl() {
        tabHome.setOnClickListener(this);
        tabFavourite.setOnClickListener(this);
        tabAccount.setOnClickListener(this);
        tabCart.setOnClickListener(this);
        tabMenu.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
    }

    private void showHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fr_content, new HomeFragment()).addToBackStack(null).commit();
    }

    public void showFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
                    .replace(R.id.fr_content, fragment).addToBackStack(fragment.getClass().getSimpleName()).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
//                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
                    .replace(R.id.fr_content, fragment).commit();
        }
    }

    public void showFragmentWithAddMethod(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
                    .add(R.id.fr_content, fragment).addToBackStack(fragment.getClass().getSimpleName()).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
                    .add(R.id.fr_content, fragment).commit();
        }
    }

    private void formatBottomMenu() {
        setUpToolbar();

        tabHome.setImageResource(R.drawable.ic_home_inactive);
        tabFavourite.setImageResource(R.drawable.ic_favourite_inactive);
        tabAccount.setImageResource(R.drawable.ic_account_inactive);
        tabCart.setImageResource(R.drawable.ic_shopping_inactive);
        tabMenu.setImageResource(R.drawable.ic_menu_inactive);
    }

    private void logout() {
        clearUser();
        gotoActivityWithClearTop(self, LoginActivity.class);
        finish();
    }

    private void clearUser() {
        preferencesManager.setSaveAccount(false);
        UserObj userObj = new UserObj();
        preferencesManager.setUserLogin(userObj);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_home:
                formatBottomMenu();
                tabHome.setImageResource(R.drawable.ic_home_selected);
                if (getSupportFragmentManager().findFragmentById(R.id.fr_content) instanceof HomeFragment) {
                    return;
                } else {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("Home");
                    if (fragment != null) {
                        getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    } else {
                        showFragment(new HomeFragment(), true);
                    }

                }
                break;
            case R.id.tab_favourite:
                if (isLoggedIn()) {
                    formatBottomMenu();
                    tabFavourite.setImageResource(R.drawable.ic_favourite_active);
                    if (getSupportFragmentManager().findFragmentById(R.id.fr_content) instanceof FavouriteFragment) {
                        return;
                    } else {
                        showFragment(new FavouriteFragment(), false);
                    }
                } else {
                    showConfirmDialog();
                }

                break;
            case R.id.tab_account:
                if (isLoggedIn()) {
                    formatBottomMenu();
                    tabAccount.setImageResource(R.drawable.ic_account_active);
                    if (getSupportFragmentManager().findFragmentById(R.id.fr_content) instanceof ProfileFragment) {
                        return;
                    } else {
                        showFragment(new ProfileFragment(), false);
                    }
                } else {
                    showConfirmLoginDialog();
                }

                break;
            case R.id.tab_cart:
//                if (isLoggedIn()) {
                formatBottomMenu();
//                    tabCart.setImageResource(R.drawable.ic_shopping_active);
//                    showToolbar();
                gotoActivity(self, MyBagActivity.class);

//                } else {
//                    showConfirmDialog();
//                }

                break;
            case R.id.tab_menu:
                formatBottomMenu();
                tabMenu.setImageResource(R.drawable.ic_menu_active);
                drawerLayout.openDrawer(Gravity.END);
                break;
            case R.id.iv_close:
                formatBottomMenu();
                drawerLayout.closeDrawers();
                break;
            case R.id.iv_search:

                break;

            case R.id.btn_edit:
                showFragment(new UpdateProfileFragment(), true);
                break;
        }
    }

    public interface OnRequestChangeAvatarListener {
        void onSuccess(int requestCode, int resultCode, Intent data);
    }

    public void setOnActivityResult(OnRequestChangeAvatarListener OnRequestChangeAvatarListener) {
        this.onActivityResultListener = OnRequestChangeAvatarListener;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_GO_TO_PRODUCT_DETAIL_ACTIVITY || requestCode == Constants.REQUEST_CODE_GO_TO_NOTIFICATION_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String userId = data.getStringExtra(Constants.USER_ID_COMMENT);
                OtherProfileFragment otherProfileFragment = new OtherProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.USER_ID_COMMENT, userId);
                otherProfileFragment.setArguments(bundle);
                formatBottomMenu();
                tabAccount.setImageResource(R.drawable.ic_account_active);
//                showToolbar();
                showFragment(otherProfileFragment, false);
            }
        } else {
            onActivityResultListener.onSuccess(requestCode, resultCode, data);
        }
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

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setTitle(getResources().getString(R.string.alert));
        builder.setMessage(getResources().getString(R.string.please_login));
        builder.setCancelable(false);
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoActivity(self, LoginActivity.class);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawers();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.click_back_again_to_exit), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
