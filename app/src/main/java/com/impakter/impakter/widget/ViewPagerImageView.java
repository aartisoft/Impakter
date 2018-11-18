package com.impakter.impakter.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by TOMMY on 10/21/2017.
 */

public class ViewPagerImageView extends android.support.v7.widget.AppCompatImageView {
    public ViewPagerImageView(Context context) {
        super(context);
    }

    public ViewPagerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPagerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width * 16 / 9);
    }
}
