package com.meidebi.app.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class DrawbleEditText extends EditText {
    private Drawable mRightDrawable;
    private boolean isHasFocus;
    private onTouchListener touchListener;

    public DrawbleEditText(Context context) {
        super(context);
    }

    public DrawbleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawbleEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public onTouchListener getTouchListener() {
        return touchListener;
    }

    public void setTouchListener(onTouchListener touchListener) {
        this.touchListener = touchListener;
    }

    /**
     * 当手指抬起的位置在clean的图标的区域
     * 我们将此视为进行清除操作
     * getWidth():得到控件的宽度
     * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的)
     * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
     * getPaddingRight():clean的图标右边缘至控件右边缘的距离
     * 于是:
     * getWidth() - getTotalPaddingRight()表示:
     * 控件左边到clean的图标左边缘的区域
     * getWidth() - getPaddingRight()表示:
     * 控件左边到clean的图标右边缘的区域
     * 所以这两者之间的区域刚好是clean的图标的区域
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (touchListener != null) {
                    boolean isTouch = (event.getX() > (getWidth() - getTotalPaddingRight())) &&
                            (event.getX() < (getWidth() - getPaddingRight()));
                    if (isTouch) {
                        touchListener.onTouch(isTouch);
                    }
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    public interface onTouchListener {
        public void onTouch(boolean isTouch);
    }


}