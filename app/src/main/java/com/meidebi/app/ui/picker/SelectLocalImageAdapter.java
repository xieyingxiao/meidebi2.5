//package com.meidebi.app.ui.picker;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//
//import com.meidebi.app.R;
//import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
//import com.meidebi.app.ui.browser.BucketEntry;
//
//import java.util.List;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
//public class SelectLocalImageAdapter extends BaseRecyclerAdapter<BucketEntry> {
//  	private List<BucketEntry> list_selected = null;
//
//	public SelectLocalImageAdapter(Context context, List<BucketEntry> mList) {
//		super(context, mList);
//  	}
//
//
//
//    @Override
//    public boolean useHeader() {
//        return true;
//    }
//
//    @Override
//    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
//        View v =getLayoutInflater().inflate(R.layout.adapter_grid_upload,parent,false);
//        return new ViewHodler(v);
//    }
//
//    @Override
//    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
//        setDataView((ViewHodler)holder,position);
//
//    }
//
//
//
//	private void setDataView(final ViewHodler holder, int position) {// 显示数据
//		final BucketEntry item = mData.get(position);
//
//		for (int i = 0; i < list_selected.size(); i++) {
//			if (list_selected.get(i).bucketUrl.equals(item.bucketUrl)) {
//				holder._cb.setChecked(true);
//			}
//		}
// 			holder._cb.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
////					if (holder._cb.isChecked()) {
////						if (list_selected.size() != 10) {
////							list_selected.add(item);
////							((SelectImgActivity) context)
////									.refreshGridSelected();
////						} else {
////							Toast.makeText(context, "上传照片不能超过十张",
////                                    Toast.LENGTH_SHORT).show();
////							holder._cb.setChecked(false);
////						}
////					} else if (!holder._cb.isChecked()) {
////						list_selected.remove(item);
////						((SelectImgActivity) context)
////								.refreshGridSelected();
////					}
////					((SelectImgActivity) context).refreshSelectNum();
//				}
//			});
//	}
//
//
//
//
//    class ViewHodler extends BasicItemViewHolder {
//
//        @InjectView(R.id.iv_adapter_upload)
//        ImageView _img;
//
//
//         @InjectView(R.id.cb_adapter_selected)
//         CheckBox _cb;
//
//
//
//
//        public ViewHodler(View itemView) {
//            super(itemView);
//            ButterKnife.inject(this, itemView);
//        }
//
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
//        View v =getLayoutInflater().inflate(R.layout.adapter_add_photo_footer,parent,false);
//        return new ViewHeader(v);
//    }
//
//    @Override
//    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {
//        super.onBindHeaderView(holder, position);
//    }
//
//    class ViewHeader extends RecyclerView.ViewHolder {
//
//
//        public ViewHeader(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    OnItemClickListener.OnFoooterClick(getPosition());
//                }
//            });
//
//        }
//    }
//
//
//}
