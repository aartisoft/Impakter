package com.impakter.impakter.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.google.gson.Gson;
import com.impakter.impakter.object.UserObj;
import com.impakter.impakter.object.UserRespond;

import java.util.Calendar;

import retrofit2.http.PUT;


public class PreferencesManager {
    private String TAG = getClass().getSimpleName();
    private static final String USER_LOGIN = "USER_LOGIN";
    private static final String SAVE_ACCOUNT = "SAVE_ACCOUNT";
    private static final String CHECK_OUT_AS_A_GUEST = "CHECK_OUT_AS_A_GUEST";
    private final String REAL_ESTATE_PREFERENCES = "com.apichat.realestate.preference";

    private static PreferencesManager instance;

    private Context context;

    // ==============================================================

    private final String USER_ID = "USER_ID";
    private final String USER_NAME = "USER_NAME";
    private final String PASSWORD = "PASSWORD";
    private final String EMAIL = "EMAIL";
    private final String NAME = "NAME";
    private final String ADDRESS = "ADDRESS";
    private final String PHONE = "PHONE";
    private final String SKYPE = "SKYPE";
    private final String SEX = "SEX";
    private final String HOST = "HOST";
    private final String IMAGE = "IMAGE";
    private final String TYPE = "TYPE";
    private final String WEBSITE = "WEBSITE";
    private final String CITY_ID = "CITY_ID";
    private final String COUNTRY_ID = "COUNTRY_ID";
    private final String STATE_ID = "STATE_ID";
    private final String COUNTRY_CODE = "COUNTRY_CODE";

    private final String FACEBOOK_TOKEN = "FACEBOOK_TOKEN";

    private final String FACEBOOK_NAME = "FACEBOOK_NAME";
    private final String FACEBOOK_EMAIL = "FACEBOOK_NAME";
    private final String FACEBOOK_ID = "FACEBOOK_ID";
    private final String FACEBOOK_FIRST_NAME = "FACEBOOK_FIRST_NAME";
    private final String FACEBOOK_LAST_NAME = "FACEBOOK_LAST_NAME";
    private final String FACEBOOK_IMAGE = "FACEBOOK_IMAGE";

    private final String CART_ID = "CART_ID";
    private final String CART_ITEMS = "CART_ITEMS";

    private PreferencesManager() {
    }

    /**
     * Constructor
     *
     * @param context
     * @return
     */
    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager();
            instance.context = context;
        }
        return instance;
    }

    public PreferencesManager(Context context) {
        this.context = context;
    }

    public void setUserLogin(UserObj user) {
        Gson gson = new Gson();
        Log.e("SHARED", "user " + gson.toJson(user));
        putStringValue(USER_LOGIN, gson.toJson(user));

    }

    public UserObj getUserLogin() {
        Gson gson = new Gson();
        UserObj user = gson.fromJson(getStringValue(USER_LOGIN), UserObj.class);
        if (user == null)
            return null;
        return user;
    }

    public void clearUser() {

    }

    public void setSaveAccount(boolean isCheck) {
        putBooleanValue(SAVE_ACCOUNT, isCheck);
    }

    public boolean isSaveAccount() {
        return getBooleanValue(SAVE_ACCOUNT);
    }

    public void setAccount(String email, String password) {
        putStringValue(EMAIL, email);
        putStringValue(PASSWORD, password);
    }

    public String getEmail() {
        return getStringValue(EMAIL);
    }

    public void setCartId(int cartId) {
        putIntValue(CART_ID, cartId);
    }

    public int getCartId() {
        return getIntValue(CART_ID);
    }

    public void setCheckOutAsAGuest(boolean checkOutAsAGuest) {
        putBooleanValue(CHECK_OUT_AS_A_GUEST, checkOutAsAGuest);
    }

    public boolean getCheckOutAsAGuest() {
        return getBooleanValue(CHECK_OUT_AS_A_GUEST);
    }

    public String getPassword() {
        return getStringValue(PASSWORD);
    }

    public void setFacebookToken(String fbToken) {
        putStringValue(FACEBOOK_TOKEN, fbToken);
    }

    public void setFbAccount(String name, String firstName, String lastName,
                             String email, String id, String avatar) {
        putStringValue(FACEBOOK_ID, id);
        putStringValue(FACEBOOK_NAME, name);
        putStringValue(FACEBOOK_FIRST_NAME, firstName);
        putStringValue(FACEBOOK_LAST_NAME, lastName);
        putStringValue(FACEBOOK_EMAIL, email);
        putStringValue(FACEBOOK_IMAGE, avatar);
    }

    // ======================== UTILITY FUNCTIONS ========================

    /**
     * Save a long integer to SharedPreferences
     *
     * @param key
     * @param n
     */
    public void putLongValue(String key, long n) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, n);
        editor.commit();
    }

    /**
     * Read a long integer to SharedPreferences
     *
     * @param key
     * @return
     */
    public long getLongValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getLong(key, 0);
    }

    /**
     * Save an integer to SharedPreferences
     *
     * @param key
     * @param n
     */
    public void putIntValue(String key, int n) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, n);
        editor.commit();
    }

    /**
     * Read an integer to SharedPreferences
     *
     * @param key
     * @return
     */
    public int getIntValue(String key) {
        // SmartLog.log(TAG, "Get integer value");
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getInt(key, 0);
    }

    /**
     * Save an string to SharedPreferences
     *
     * @param key
     * @param s
     */
    public void putStringValue(String key, String s) {
        // SmartLog.log(TAG, "Set string value");
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s);
        editor.commit();
    }

    /**
     * Read an string to SharedPreferences
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getString(key, "");
    }

    /**
     * Read an string to SharedPreferences
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringValue(String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getString(key, defaultValue);
    }

    /**
     * Save an boolean to SharedPreferences
     *
     * @param key
     */
    public void putBooleanValue(String key, Boolean b) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    /**
     * Read an boolean to SharedPreferences
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getBoolean(key, false);
    }

    /**
     * Save an float to SharedPreferences
     *
     * @param key
     * @param
     */
    public void putFloatValue(String key, float f) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, f);
        editor.commit();
    }

    /**
     * Read an float to SharedPreferences
     *
     * @param key
     * @return
     */
    public float getFloatValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(
                REAL_ESTATE_PREFERENCES, 0);
        return pref.getFloat(key, 0.0f);
    }

}
