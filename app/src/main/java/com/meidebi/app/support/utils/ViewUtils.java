package com.meidebi.app.support.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

import butterknife.ButterKnife;

/**
 * Created by mdb-ii on 15-2-3.
 */
public  class ViewUtils {

    public static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setEnabled(false);
        }
    };
    public static final ButterKnife.Action<View> ENABLED = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setEnabled(true);
        }
    };

    public static final ButterKnife.Action<View> VISIBLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };

    public static final ButterKnife.Action<View> GONE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };

    public static final ButterKnife.Action<View> ALPHA_FADE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setFillBefore(true);
            alphaAnimation.setDuration(500);
            alphaAnimation.setStartOffset(index * 100);
            view.startAnimation(alphaAnimation);
        }
    };

  }
