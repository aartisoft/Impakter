package com.impakter.impakter.utils;

import android.content.Context;

import com.impakter.impakter.widget.dialog.ProgressDialog;

public class DialogUtility {
    private Context context = null;

    private static DialogUtility instance = null;
    private ProgressDialog pDialog;

    /**
     * Constructor
     *
     * @param context
     */
    private DialogUtility(Context context) {
        this.context = context;
    }

    /**
     * Get class instance
     *
     * @param context
     * @return
     */
    public static DialogUtility getInstance(Context context) {
        if (instance == null) {
            instance = new DialogUtility(context);
        }
        return instance;
    }

    public void showDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(context);
        }
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void closeDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
        }
    }
}
