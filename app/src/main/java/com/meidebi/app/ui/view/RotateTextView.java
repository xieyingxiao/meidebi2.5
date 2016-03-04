package com.meidebi.app.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class RotateTextView extends TextView implements AnimationListener {
	private float mFromDegrees;
	private float mToDegrees;
	private float mCenterX;
	private float mCenterY;
	private float mDepthZ;
	private boolean mReverse;
	private int mDuration;

	public RotateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setRotate3dAnimation(float fromDegrees, float toDegrees,
			float centerX, float centerY, float depthZ, boolean reverse,int duration) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
		mDepthZ = depthZ;
		mReverse = reverse;
		mDuration = duration;
	}
	
	public void startAnim(Boolean isVisble){
		
	}
	
	

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

}
