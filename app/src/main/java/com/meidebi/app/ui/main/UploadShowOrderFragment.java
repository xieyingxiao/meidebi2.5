package com.meidebi.app.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.service.bean.show.Draft;
import com.meidebi.app.service.dao.ShowOrderDao;
import com.meidebi.app.support.utils.component.LoginUtil;
import com.meidebi.app.ui.adapter.UploadGridAdapter;
import com.meidebi.app.ui.adapter.base.InterRecyclerOnItemClick;
import com.meidebi.app.ui.base.BaseFragment;
import com.meidebi.app.ui.base.BaseFragmentActivity;
import com.meidebi.app.ui.commonactivity.CommonFragmentActivity;
import com.meidebi.app.ui.commonactivity.OnBackPressedListener;
import com.meidebi.app.ui.picker.AlbumUtil;
import com.meidebi.app.ui.picker.BrowserDelActivity;
import com.meidebi.app.ui.picker.BrowserPickImageActivity;
import com.meidebi.app.ui.picker.PickerActivity;
import com.orm.SugarRecord;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

public class UploadShowOrderFragment extends BaseFragment implements
		OnClickListener,InterRecyclerOnItemClick{
	@InjectView(R.id.btn_show_send)
    FButton btn_send;
	private Uri imageFileUri = null;
	private static final int CAMERA_RESULT = 0;
	private static final int PIC_RESULT = 1;
	public static final int BROWSER_PIC = 2;
	private SparseArray<AlbumUtil.PhotoEntry> final_photos = new SparseArray<AlbumUtil.PhotoEntry>();
    private ArrayList<AlbumUtil.PhotoEntry> select_photos = new ArrayList<AlbumUtil.PhotoEntry>();

    private ShowOrderDao dao;
	@InjectView(R.id.common_recyclerview)
    RecyclerView up_grid;
    @InjectView(R.id.et_show_order)
    EditText et_show;
	private UploadGridAdapter adapter;

    public static final int Drafts_save = 0;

    public static final int Drafts_ready = 1;
    public static final int Drafts_sending = 2;
    public static final int Drafts_sendsucess = 3;






    public ShowOrderDao getDao() {
		if (dao == null) {
			dao = new ShowOrderDao(getActivity());
		}
		return dao;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
        CommonFragmentActivity activity = (CommonFragmentActivity)getActivity();
        activity.setOnBackPressedListener(new BaseBackPressedListener());
		View v = inflater.inflate(R.layout.fragment_show_order_upload, null);
        ButterKnife.inject(this,v);
        setTitle(getString(R.string.order_show_edit_title));
        initView();

//		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
//				newBroadcastReceiver, new IntentFilter(AppAction.PHOTORESULT));
		return v;
	}


    public void setTitle(String title){
        ((BaseFragmentActivity)getActivity()).getSupportActionBar().setTitle(title);
    }
	private void initView() {
		// TODO Auto-generated method stub

        up_grid.setLayoutManager(new GridLayoutManager(getActivity(),4));
        up_grid.setItemAnimator(new DefaultItemAnimator());

		adapter = new UploadGridAdapter(this,
                select_photos);
        adapter.setData(select_photos);
        adapter.setOnItemClickListener(this);
		up_grid.setAdapter(adapter);

	}










	@OnClick({R.id.btn_show_send})
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_show_send:
            if(TextUtils.isEmpty(et_show.getText().toString())){
                et_show.setError("内容不能为空");
                return;
            }
            if( final_photos.size()==0){
                Toast.makeText(getActivity(),"请选择至少一张图片",Toast.LENGTH_SHORT).show();
                 return;
            }
            btn_send.setClickable(false);
            btn_send.setSelected(false);
 		    getDao().startUploadService(getActivity(),saveDrafts(Drafts_ready));
            XApplication.getInstance().getSeletedPhoto().clear();
            getActivity().finish();
            Toast.makeText(getActivity(),"已添加到-我的晒单-上传中队列里",Toast.LENGTH_SHORT).show();
            break;
//        case R.id.tv_show_save:
//
//                break;
		}

	}

    private Draft saveDrafts(int status){
        Draft drafts = new Draft();
        Gson gson = new Gson();
        drafts.setContent(et_show.getText().toString());
        drafts.setPicJson(gson.toJson(select_photos));
        drafts.setMstatus(status);
        drafts.setUid(LoginUtil.getId());
        drafts.setCover(select_photos.get(0).path);
        drafts.setTitle("来自比友["+ XApplication.getInstance().getAccountBean().getUsername()+"]的手机晒单");
        drafts.setId(Long.toString(SugarRecord.save(drafts)));
        return  drafts;
    }




	//
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == getActivity().RESULT_OK) {
			switch (requestCode) {
                case PickerActivity.CAMERA_RESULT:

                    // }
                    break;
                case PickerActivity.PICK_REQUEST:
                    final_photos = XApplication.getInstance().getSeletedPhoto();
                    select_photos = (ArrayList<AlbumUtil.PhotoEntry>)intent.getSerializableExtra(PickerActivity.ALBUM_KEY);
                    adapter.setData(select_photos);
                    adapter.notifyDataSetChanged();
                    break;
            }
 			}else if (resultCode == getActivity().RESULT_CANCELED){

              XApplication.getInstance().setSeletedPhoto(final_photos);

        }

		}


//	private BroadcastReceiver newBroadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String resultCode = intent.getAction();
//			if (resultCode == AppAction.PHOTORESULT) {
//				// up_grid.setAdapter(adapter);
//				picPath_list.clear();
//				picPath_list.addAll(XApplication.getInstance().getCcb()
//						.getCloudPhotoList());
//				// for(int i =
//				// 0;i<XApplication.getInstance().getCcb().getCloudPhotoList().size();i++){
//				// picPath_list.add(XApplication.getInstance().getCcb().getCloudPhotoList().get(i));
//				// }
//				BucketEntry bucketEntry = new BucketEntry(0, "", "");
//				picPath_list.add(bucketEntry);
//				adapter.notifyDataSetChanged();
//			}
//		}
//	};

//	private final BroadcastReceiver uploadReceiver = new AbstractUploadServiceReceiver() {
//
//		@Override
//		public void onProgress(int progress) {
//			Log.i("AndroidUploadService", "The progress is: " + progress);
//		}
//
//		@Override
//		public void onError(Exception exception) {
//			Log.e("AndroidUploadService", exception.getLocalizedMessage(),
//					exception);
//		}
//
//		@Override
//		public void onCompleted(int serverResponseCode,
//				String serverResponseMessage) {
//			Log.i("AndroidUploadService", "Upload completed: "
//					+ serverResponseCode + ", " + serverResponseMessage);
//			final_photos.clear();
//			adapter.notifyDataSetChanged();
//		}
//	};

	public void setUploadButtonView(Boolean b) {
		if (b) {
			btn_send.setVisibility(View.VISIBLE);
		} else {
			btn_send.setVisibility(View.INVISIBLE);
		}
	}



    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getActivity(), BrowserDelActivity.class);
        intent.putExtra(BrowserDelActivity.IMAGE_POSITION, 0);
        final Bundle albumBundle = new Bundle();
        albumBundle.putSerializable(PickerActivity.ALBUM_KEY,select_photos);
        intent.putExtras(albumBundle);
        startActivityForResult(intent, PickerActivity.PICK_REQUEST);


     }

    @Override
    public void OnFoooterClick(int position) {
        final Intent pickIntent = new Intent(getActivity(), PickerActivity.class);
         pickIntent.putExtra(PickerActivity.LIMIT_KEY, 30); // Set a limit
        final Bundle albumBundle = new Bundle();
        albumBundle.putSerializable(PickerActivity.ALBUM_KEY,select_photos);
        pickIntent.putExtras(albumBundle);
        startActivityForResult(pickIntent, PickerActivity.PICK_REQUEST);
     }

    public class BaseBackPressedListener implements OnBackPressedListener {


        @Override
        public void doBack() {
           XApplication.getInstance().getSeletedPhoto().clear();
           select_photos.clear();
        }
    }

}
