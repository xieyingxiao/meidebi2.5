package com.meidebi.app.ui.base;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
/**
 * Created by mdb-ii on 15-3-11.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {
    private boolean g_swipe = false;
    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {
    }

    public void onSwipeRight() {
    }

    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            setG_swipe(false);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                setG_swipe(true);
                if (distanceX > 0){
                    onSwipeRight();
                }
                else{
                    onSwipeLeft();
                    return true;

                }
             }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
//控制手指滑动的距离
            if (Math.abs(distanceY)>=Math.abs(distanceX)) {
                return true;
            }
            return false;
        }

    }

    public boolean isG_swipe() {
        return g_swipe;
    }

    public void setG_swipe(boolean g_swipe) {
        this.g_swipe = g_swipe;
    }
}