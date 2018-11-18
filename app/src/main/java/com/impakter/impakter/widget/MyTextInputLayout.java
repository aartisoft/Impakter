package com.impakter.impakter.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

public class MyTextInputLayout extends TextInputLayout {
    public MyTextInputLayout(Context context) {
        super(context);
        initTypeface();
    }

    public MyTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeface();
    }

    public MyTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeface();
    }
    private void initTypeface() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Heebo-Regular.ttf");
            this.setTypeface(tf);
        }
    }
}
