package com.meidebi.app.ui.base;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.meidebi.app.R;
import com.nineoldandroids.view.ViewHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mdb-ii on 14-12-15.
 */
public abstract class BaseFadingActivity extends SwipeBackActivity implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final boolean TOOLBAR_IS_STICKY = true;


    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private int mToolbarColor;
    private boolean mFabIsShown;
//    @InjectView(R.id.overlay)
//    View mOverlayView;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

//    @InjectView(R.id.fab)
//    FloatingActionButton mFab;
//    @InjectView(R.id.title)
//    TextView mTitleView;
    @InjectView(R.id.scroll)
    ObservableScrollView mScrollView;

    public  @InjectView(R.id.ll_img)
    FrameLayout ll_img;
     private int mInitialStatusBarColor;
    private int mFinalStatusBarColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.btn_toolbar_back_sel);

//        mFlexibleSpaceImageHeight = AppConfigs.getCoverHeight();
//        mFlexibleSpaceShowFabOffset = AppConfigs.getCoverHeight()/2;
//
//
//        mActionBarSize = getActionBarSize();
//        mToolbarColor = getResources().getColor(R.color.titlebar_bg);


        mScrollView.setScrollViewCallbacks(this);

//        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);

//        ViewHelper.setScaleX(mFab, 0);
//        ViewHelper.setScaleY(mFab, 0);
//        ViewTreeObserver vto = mFab.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                    mScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                } else {
//                    mScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//                onScrollChanged(-1,false,false);
//            }
//        });
//        mStatusBarManager = new SystemBarTintManager(this);
//        mStatusBarManager.setStatusBarTintEnabled(true);
//        mInitialStatusBarColor = getResources().getColor(R.color.background_material_dark);
//        mFinalStatusBarColor = getResources().getColor(R.color.colorPrimaryDark);
//        setBackgroundAlpha(mToolbar, 0, getResources().getColor(R.color.titlebar_bg));



    }



    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {


        // Translate FAB
//        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
//        int fabTranslationY = Math.max(mActionBarSize - mFab.getHeight() / 2,
//                Math.min(maxFabTranslationY, -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2));
//        ViewHelper.setTranslationX(mFab, ll_img.getWidth() - mFabMargin - mFab.getWidth());
//        ViewHelper.setTranslationY(mFab, fabTranslationY);
//
//        // Show/hide FAB
//        if (ViewHelper.getTranslationY(mFab) < mFlexibleSpaceShowFabOffset) {
//            hideFab();
//        } else {
//            showFab();
//        }
//        int baseColor = getResources().getColor(R.color.titlebar_bg);
//        float alpha = 1 - (float) Math.max(0, mFlexibleSpaceImageHeight - scrollY) / mFlexibleSpaceImageHeight;
//        setBackgroundAlpha(mToolbar, alpha, baseColor);
          ViewHelper.setTranslationY(ll_img, scrollY / 2);
//        int headerHeight = ll_img.getHeight() - mToolbar.getHeight();
//        float ratio = 0;
//        if (scrollY > 0 && headerHeight > 0)
//            ratio = (float) Math.min(Math.max(scrollY, 0), headerHeight) / headerHeight;
//        updateStatusBarColor(ratio);

    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

//    private void showFab() {
//        if (!mFabIsShown) {
//            ViewPropertyAnimator.animate(mFab).cancel();
//            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
//            mFabIsShown = true;
//        }
//    }
//
//    private void hideFab() {
//        if (mFabIsShown) {
//            ViewPropertyAnimator.animate(mFab).cancel();
//            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
//            mFabIsShown = false;
//        }
//    }

//    private void updateStatusBarColor(float scrollRatio) {
//        int r = interpolate(Color.red(mInitialStatusBarColor), Color.red(mFinalStatusBarColor), 1 - scrollRatio);
//        int g = interpolate(Color.green(mInitialStatusBarColor), Color.green(mFinalStatusBarColor), 1 - scrollRatio);
//        int b = interpolate(Color.blue(mInitialStatusBarColor), Color.blue(mFinalStatusBarColor), 1 - scrollRatio);
//       mStatusBarManager.setTintColor(Color.rgb(r, g, b));
//    }


    private int interpolate(int from, int to, float param) {
        return (int) (from * param + to * (1 - param));
    }

    private void setBackgroundAlpha(View view, float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        view.setBackgroundColor(a + rgb);
     }
    protected abstract int getLayoutResource();


}
