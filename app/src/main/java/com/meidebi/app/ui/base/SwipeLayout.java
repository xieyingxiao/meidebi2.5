package com.meidebi.app.ui.base;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by mdb-ii on 15-3-11.
 */
public class SwipeLayout extends FrameLayout {

    private static final int SWIPE_THRESHOLD = 100;
    private float _downX;

    public SwipeLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {

        switch(evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _downX = evt.getX();
            case MotionEvent.ACTION_UP:
                float deltaX = evt.getX() - _downX;

                if(Math.abs(deltaX) > SWIPE_THRESHOLD && deltaX < 0){
                    swipeBackListener.swipeRight();
                }
         }

        return true;
    }


    private SwipeListener swipeBackListener;


    public void setOnSwipeListener(SwipeListener listener) {
        swipeBackListener = listener;
    }



    public interface SwipeListener {
        void swipeRight();
        void swipeLeft();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        onTouchEvent(ev);
        return false;
    }


}
