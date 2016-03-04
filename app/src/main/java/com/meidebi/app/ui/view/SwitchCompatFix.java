package com.meidebi.app.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.text.TextPaint;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * Created by mdb-ii on 15-3-4.
 */
public class SwitchCompatFix extends SwitchCompat {

    public SwitchCompatFix(Context context) {
        super(context);
    }

    public SwitchCompatFix(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchCompatFix(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSwitchTextAppearance(Context context, int resid) {
        TypedArray appearance = context.obtainStyledAttributes(resid, new int[]{android.R.attr.textSize});
        TypedArray appearance_tx = context.obtainStyledAttributes(resid, new int[]{android.R.attr.textColor});
       int color =  appearance_tx.getColor(0,0);
        int ts = appearance.getDimensionPixelSize(0, 0);

        try {
            Field field = SwitchCompat.class.getDeclaredField("mTextPaint");
            field.setAccessible(true);

            TextPaint textPaint = (TextPaint) field.get(this);
            textPaint.setTextSize(ts);
            textPaint.setColor(color);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        appearance.recycle();

        super.setSwitchTextAppearance(context, resid);
    }
}