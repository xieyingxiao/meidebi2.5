package com.meidebi.app.ui.base;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mdb-ii on 15-3-11.
 */
public abstract class SwipeBackActivity extends BaseFragmentActivity {

    OnSwipeTouchListener swipeListener;
    private boolean useSwipe = true;

    public void setUseSwipe(boolean useSwipe) {
        this.useSwipe = useSwipe;
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        if (useSwipe) {
            swipeListener = new OnSwipeTouchListener(SwipeBackActivity.this) {
                @Override
                public void onSwipeLeft() {
                    SwipeLeft();
                    super.onSwipeLeft();
                }

                @Override
                public void onSwipeRight() {
                    onBackPressed();
                    super.onSwipeRight();
                }

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return super.onTouch(v, event);
                }
            };
            view.setOnTouchListener(swipeListener);
        }
        super.setContentView(view);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (useSwipe) {
            Boolean touchble = swipeListener.getGestureDetector().onTouchEvent(ev);

            if (!touchble && swipeListener.isG_swipe()) {
                return false;
            }
        }


        return super.dispatchTouchEvent(ev);
    }

    public void SwipeLeft() {

    }
}