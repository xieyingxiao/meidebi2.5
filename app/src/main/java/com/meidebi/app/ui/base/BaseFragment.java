package com.meidebi.app.ui.base;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.ui.widget.ViewLoading;
import com.meidebi.app.ui.widget.action.ILoadingAction;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi")
public class BaseFragment extends Fragment implements ILoadingAction{

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public final int NETERR = 404;
	public final int DATAERR = 400;

	private Fragment mContent = null;
	private Fragment mParentFragment = null;
	private String fragmentName  = null;

	public String getFragmentName() {
		return fragmentName;
	}

	public void setFragmentName(String fragmentName) {
		this.fragmentName = fragmentName;
	}

	public BaseFragment() {
		this.setFragmentName(getClass().getName());
	}

	public Fragment getmParentFragment() {
		return mParentFragment;
	}

	public void setmParentFragment(Fragment mParentFragment) {
		this.mParentFragment = mParentFragment;
	}

	public void switchContentWithBackInThird(Fragment from, Fragment to) {
		if (mContent != to) {
			mContent = to;
			FragmentTransaction transaction = this.getChildFragmentManager()
					.beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(from).add(R.id.common_fragment, to); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to); // 隐藏当前的fragment，显示下一个
			}
			transaction.commitAllowingStateLoss();
		}
	}

	/**
	 * 替换通用的fragment
	 * 
	 * @param fragment
	 *            传人需要的frgament
	 * @param resId
	 *            替换的id
	 */
	protected void addFragmentInThird(Fragment fragment) {
		if (mContent != fragment) {
			mContent = fragment;
			FragmentManager fragmentManager = this.getChildFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.add(R.id.common_fragment, fragment);
			fragmentTransaction.commitAllowingStateLoss();
		}
	}





	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
 	}
	
	
	public void regBroadCast(String appAction){
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				newBroadcastReceiver, new IntentFilter(appAction));
	}
	
    private BroadcastReceiver newBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	doBroadCastAction(intent);
        }
    };
    
    protected void doBroadCastAction(Intent intent){
    	
    }

    
	private ViewLoading view_load;
	
	protected ViewLoading getView_load(View v) {
		if(view_load ==null){
			view_load = new ViewLoading(v);
			view_load.setAction(this);
		}
		return view_load;
	}

	@Override
	public void onReload() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setRetainInstance(true);


    }



}
