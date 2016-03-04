package com.meidebi.app.support.utils.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.meidebi.app.XApplication;

public class MyAnimListener {
	private int aminres;
	private IMyAnimListenner listenner;
	public MyAnimListener(int aminres){
		this.aminres = aminres;
 	}
	
	public void setListener(IMyAnimListenner listenner){
		this.listenner = listenner;
	}
	
	
	
	public void start(View v){
	 Animation anim = AnimationUtils.loadAnimation(XApplication.getInstance(), aminres);
	 v.startAnimation(anim);
	 anim.setAnimationListener(new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			listenner.onAnimDo(AnimStatus.start);
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			listenner.onAnimDo(AnimStatus.repeat);
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			listenner.onAnimDo(AnimStatus.end);
  		}
	});
	}
	
}
