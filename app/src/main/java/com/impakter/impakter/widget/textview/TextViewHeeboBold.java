package com.impakter.impakter.widget.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewHeeboBold extends TextView {

    public TextViewHeeboBold(Context context) {
        super(context);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Heebo-Bold.ttf");
        this.setTypeface(face);
    }

    public TextViewHeeboBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Heebo-Bold.ttf");
        this.setTypeface(face);
    }

    public TextViewHeeboBold(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Heebo-Bold.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
