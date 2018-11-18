package com.impakter.impakter.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;


import com.impakter.impakter.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Minh Toan
 */
public class PasswordEditText extends android.support.v7.widget.AppCompatEditText {

    Drawable eye, eyeStrike;
    Boolean visible = false;
    Boolean useStrike = false;
    Boolean useValidate = false;
    Drawable drawable;
    int ALPHA = (int) (255 * .70f);
    String MATCHER_PATTERN = "((?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).{6,20})"; // (?=.*\d)
    Pattern pattern;
    Matcher matcher;

    public PasswordEditText(Context context) {
        super(context);
        init(null);

    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void initTypeface() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Heebo-Regular.ttf");
            this.setTypeface(tf);
        }
    }

    private void init(AttributeSet attrs) {
        initTypeface();
        this.pattern = Pattern.compile(MATCHER_PATTERN);
        if (attrs != null) {
            TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordEditText, 0, 0);
            this.useStrike = array.getBoolean(R.styleable.PasswordEditText_useStrike, false);
            this.useValidate = array.getBoolean(R.styleable.PasswordEditText_useValidate, false);
        }
        eye = ContextCompat.getDrawable(getContext(), R.drawable.ic_eye_active).mutate();
        eyeStrike = ContextCompat.getDrawable(getContext(), R.drawable.ic_eye_inactive).mutate();

//        if (this.useValidate) {
//            setOnFocusChangeListener(new OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean b) {
//                    if (!b) {
//                        String chuoi = getText().toString();
//                        TextInputLayout textInputLayout = (TextInputLayout) view.getParent();
//                        matcher = pattern.matcher(chuoi);
//                        if (!matcher.matches()) {
//                            textInputLayout.setErrorEnabled(true);
//                            textInputLayout.setError("Mật khẩu phải bao gồm 6 ký tự và một chữ hoa");
//                        } else {
//                            textInputLayout.setErrorEnabled(false);
//                            textInputLayout.setError("");
//                        }
//
//
//                    }
//                }
//            });
//        }
        setUp();
    }

    private void setUp() {
        setInputType(InputType.TYPE_CLASS_TEXT | (visible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
        Drawable[] drawables = getCompoundDrawables();
        drawable = useStrike && !visible ? eyeStrike : eye;
        drawable.setAlpha(ALPHA);
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && event.getX() >= (getRight() - drawable.getBounds().width())) {
            visible = !visible;
            setUp();
            invalidate();
        }
        return super.onTouchEvent(event);
    }

}
