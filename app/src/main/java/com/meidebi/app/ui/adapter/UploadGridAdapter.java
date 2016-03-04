package com.meidebi.app.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meidebi.app.R;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
import com.meidebi.app.ui.picker.AlbumUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UploadGridAdapter extends BaseRecyclerAdapter<AlbumUtil.PhotoEntry> {
	private Fragment fragment;
	private List<Map<String, Object>> dailoglist = null;



	
	
	public UploadGridAdapter(Fragment fragment, ArrayList<AlbumUtil.PhotoEntry> mList) {
		super(fragment.getActivity(), mList);
		this.fragment = fragment;
 	}



	private void setDataView(final ViewHodler holder, final int position) {// 显示数据
        AlbumUtil.PhotoEntry item = mData.get(position);
//		if (position != getCount() - 1) {
//			imageLoader.displayImage(item.bucketUrl, holder._img, options,
//					animateFirstListener);
//			holder._ib.setVisibility(View.GONE);
//			holder._img.setVisibility(View.VISIBLE);
//		} else {
//			((UploadShowOrderFragment) fragment).setUploadButtonView(true);// 刷新上传按钮
//			if (getCount() > 10) {
//				mData.remove(getCount() - 1);
//			} else if (getCount() == 1) {
//				((UploadShowOrderFragment) fragment).setUploadButtonView(false);
//				holder._ib.setVisibility(View.VISIBLE);
//				holder._img.setVisibility(View.GONE);
//				holder._ib.setImageResource(R.drawable.btn_blue_sel);
//			} else if (getCount() == 10 && !TextUtils.isEmpty(item.bucketName)) {
//				holder._ib.setVisibility(View.GONE);
//				holder._img.setVisibility(View.VISIBLE);
				imageLoader.displayImage("file://"+item.path, holder._img, options,
						animateFirstListener);
 //			} else {
//				holder._ib.setVisibility(View.VISIBLE);
//				holder._img.setVisibility(View.GONE);
//				holder._ib.setImageResource(R.drawable.btn_blue_sel);
//				// holder._img.setBackgroundResource(R.color.white);
//			}
//		}
//		holder._img.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				Intent it = new Intent();
////				it.putExtra("num", position);
//////				it.setClass(context, BrowserImageVpActivity.class);
////				context.startActivity(it);
//			}
//		});
//		holder._ib.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (LoginUtil.isAccountLogin(fragment.getActivity())) {
//					if (dailoglist == null) {
//						dailoglist = new ArrayList<Map<String, Object>>();
//						Map<String, Object> map4 = new HashMap<String, Object>();
//						map4.put(AppConfigs.LIST_TEXT,
//								fragment.getString(R.string.upload_dialog_take_photo));
//						dailoglist.add(map4);
//						Map<String, Object> map3 = new HashMap<String, Object>();
//						map3.put(AppConfigs.LIST_TEXT,
//								fragment.getString(R.string.upload_dialog_photolib));
//						dailoglist.add(map3);
//					}
////					if (dialog_select_img == null) {
////						dialog_select_img = new DialogListFragment("头像上传",
////								dailoglist);
////						dialog_select_img.setCLICK_LISTENER(LIST_CLICK_LISTENER);
////					}
////					dialog_select_img.show(fragment.getFragmentManager(),"");
//				}
//			}
//		});
	}

    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_grid_upload,parent,false);
        return new ViewHodler(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setDataView((ViewHodler)holder,position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.adapter_add_photo_footer,parent,false);
        return new ViewFooter(v);
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {
//        setFooterView((ViewFooter)holder);
    }




    class ViewHodler extends BasicItemViewHolder  {

        @InjectView(R.id.iv_adapter_upload)
          ImageView _img;

        public ViewHodler(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);

         }



    }


    class ViewFooter extends RecyclerView.ViewHolder {


        public ViewFooter(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnItemClickListener.OnFoooterClick(getPosition());
                }
            });

         }
    }
}


