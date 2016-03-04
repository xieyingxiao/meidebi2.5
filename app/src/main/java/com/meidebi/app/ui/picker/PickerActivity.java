package com.meidebi.app.ui.picker;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;


public class PickerActivity extends BasePullToRefreshActivity implements AlbumsFragment.OnClickAlbum, ImagesFragment.OnPickImage {


    public static final String ALBUM_KEY = "albumKey";

    public static final String PICKED_IMAGES_KEY = "pickedImagesKey";
    public static final String LIMIT_KEY = "limitKey";

    public static final int PICK_REQUEST = 144;
    public static final int NO_LIMIT = -1;

    private int mLimit = NO_LIMIT;


    private ImagesFragment mImagesFragment;
    private AlbumsFragment mAlbumsFragment;
    private Fragment last_fragment;
    private Uri mCapturedPhotoUri;

    @InjectView(R.id.btn_pick_select_yes)
    FButton btn_selected;
    @InjectView(R.id.tv_pick_choose_album)
    TextView tv_album;

    public static final int CAMERA_RESULT = 0;

    public static ArrayList<AlbumUtil.PhotoEntry> seleted_photos =new ArrayList<AlbumUtil.PhotoEntry>(); //记录点击顺序





    //TODO Add animation

    public AlbumsFragment getmAlbumsFragment() {
        if (mAlbumsFragment == null) {
            mAlbumsFragment = new AlbumsFragment();
        }
        return mAlbumsFragment;
    }

    public ImagesFragment getmImagesFragment() {
        if (mImagesFragment == null) {
            mImagesFragment = new ImagesFragment();
        }
        return mImagesFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        initOptions();
        updateTextAndBadge();
        setupAlbums(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.pick_activity_pick;
    }


    @OnClick({R.id.tv_pick_choose_album, R.id.tv_pick_image_priview, R.id.btn_pick_select_yes})
    protected void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pick_choose_album:
                switchContent(last_fragment, getmAlbumsFragment(), R.id.common_fragment);
                last_fragment = getmAlbumsFragment();
                getSupportActionBar().setTitle("选择相册");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case R.id.tv_pick_image_priview:
                if(XApplication.getInstance().getSeletedPhoto().size()==0){
                    Toast.makeText(PickerActivity.this,"请选择至少一张图片",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(PickerActivity.this, BrowserPickImageActivity.class);
                    intent.putExtra(BrowserPickImageActivity.IMAGE_POSITION, 0);
                    final Bundle albumBundle = new Bundle();
                    albumBundle.putSerializable(PickerActivity.ALBUM_KEY, seleted_photos);
                    intent.putExtras(albumBundle);
                    startActivityForResult(intent, PickerActivity.PICK_REQUEST);
                }
                break;
            case R.id.btn_pick_select_yes:
                if(XApplication.getInstance().getSeletedPhoto().size()==0){
                    Toast.makeText(PickerActivity.this,"请选择至少一张图片",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent2 = getIntent();
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable(PickerActivity.ALBUM_KEY, seleted_photos);
                    intent2.putExtras(bundle);
                    setResult(RESULT_OK, intent2);
                    super.finish();
                }
                break;
        }
    }


    public void initOptions() {
        final Intent options = getIntent();
        if (options != null) {

            try {
                mLimit = options.getExtras().getInt(LIMIT_KEY);
            } catch (NullPointerException ex) {
                mLimit = NO_LIMIT;
            }

            try {
                seleted_photos = (ArrayList<AlbumUtil.PhotoEntry>) options.getSerializableExtra(ALBUM_KEY);//同步上一次选择顺序
            } catch (NullPointerException ex) {
                seleted_photos.clear();
            }
        }
        AlbumUtil.initLimit(mLimit);

    }


    public void setupAlbums(Bundle savedInstanceState) {
        if (findViewById(R.id.common_fragment) != null) {
            if (savedInstanceState == null) {

                 getmImagesFragment().setAlbum(AlbumUtil.getAlbums(this,false).get(0));
                getSupportActionBar().setTitle("所有图片");
                tv_album.setText("所有图片");
                 addFragment(getmImagesFragment());
                 last_fragment = getmImagesFragment();
            }
        }
    }


    public void updateTextAndBadge() {
        int size = XApplication.getInstance().getSeletedPhoto().size();
        if (size == 0) {
//            mDoneBadge.setVisibility(View.GONE);
//            mDoneLayout.setClickable(false);
//            btn_selected.setTextColor(getResources().getColor(R.color.no_checked_photos_text));

        } else if (size == mLimit) {
            btn_selected.setText("完成(" + size + ")");
//            mDoneBadge.getBackground().setColorFilter(getResources().getColor(R.color.reached_limit_text), PorterDuff.Mode.SRC);
//            Toast.makeText(this, R.string.reach_limit, Toast.LENGTH_SHORT).show();

        } else {
//            mDoneText.setTextColor(Color.parseColor("#ffffff"));
//            mDoneLayout.setClickable(true);
//            mDoneBadge.getBackground().setColorFilter(getResources().getColor(R.color.checked_photo), PorterDuff.Mode.SRC);
//            mDoneBadge.setVisibility(View.VISIBLE);
            btn_selected.setText("完成(" + size + ")");
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // case R.id.action_refresh:
            // // mContentFragment.loadFirstPageAndScrollToTop();
            // return true;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                super.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }




    public void capturePhoto() {
        ContentValues values = new ContentValues(8);
        String newname = DateFormat.format("yyyy-MM-dd kk.mm.ss", System.currentTimeMillis()).toString();
        values.put(MediaStore.Images.Media.TITLE, newname);//名称，随便
        values.put(MediaStore.Images.Media.DISPLAY_NAME, newname);
        values.put(MediaStore.Images.Media.DESCRIPTION, "show");//描述，随便
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());//图像的拍摄时间，显示时根据这个排序
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");//默认为jpg格式
        values.put(MediaStore.Images.Media.ORIENTATION, 0);//

         final String CAMERA_IMAGE_BUCKET_ID = String.valueOf(AlbumUtil.cameraFolder.hashCode());
        File parentFile = new File(AlbumUtil.cameraFolder);
        String name = parentFile.getName().toLowerCase();

        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, CAMERA_IMAGE_BUCKET_ID);//id
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, name);

        // 先得到新的URI
        mCapturedPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT,mCapturedPhotoUri);
        try {
            startActivityForResult(i,CAMERA_RESULT);
        }
        catch (  ActivityNotFoundException e) {
         }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT && data == null) {
            AppLogger.e(""+mCapturedPhotoUri.getPath());
               AlbumUtil.PhotoEntry photoEntry= new AlbumUtil.PhotoEntry.Builder(AlbumUtil.getPath(this,mCapturedPhotoUri)).build();
               XApplication.getInstance().getSeletedPhoto().append((int) System.currentTimeMillis(), photoEntry);
                seleted_photos.add(photoEntry);
            Intent intent = new Intent(PickerActivity.this, BrowserPickImageActivity.class);
            intent.putExtra(BrowserPickImageActivity.IMAGE_POSITION, 0);
            final Bundle albumBundle = new Bundle();
            albumBundle.putSerializable(PickerActivity.ALBUM_KEY, seleted_photos);
            intent.putExtras(albumBundle);
            startActivityForResult(intent, PickerActivity.PICK_REQUEST);


            getmImagesFragment().setAlbum(AlbumUtil.getAlbums(this,true).get(0));
            getSupportActionBar().setTitle("所有图片");
            updateTextAndBadge();

        } else if(resultCode==RESULT_OK&& requestCode == PICK_REQUEST){
             getmImagesFragment().refresh();
            btn_selected.setText("完成(" +XApplication.getInstance().getSeletedPhoto().size()+")");
         }else if(resultCode == RESULT_FIRST_USER){
            Intent intent2 = getIntent();
            final Bundle bundle = new Bundle();
            bundle.putSerializable(PickerActivity.ALBUM_KEY, seleted_photos);
            intent2.putExtras(bundle);
            setResult(RESULT_OK, intent2);
            super.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_take_photo, menu);
        return true;
    }


    @Override
    public void finish() {

//        if (mImagesFragment != null && mImagesFragment.isVisible()) {
//            getSupportFragmentManager().popBackStack();
////            getSupportActionBar().setTitle(R.string.albums_title);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        } else {
             super.finish();
//        }
    }


    @Override
    public void onClickAlbum(AlbumUtil.AlbumEntry album) {
        getmImagesFragment().setAlbum(album);
        getmImagesFragment().setupAdapter();
        switchContent(last_fragment, getmImagesFragment(), R.id.common_fragment);
        last_fragment = getmImagesFragment();
        tv_album.setText(album.name);
        getSupportActionBar().setTitle(album.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationIcon(R.drawable.btn_toolbar_back_sel);
    }

    @Override
    public void onTakePhoto() {
        capturePhoto();
    }

    @Override
    public void onPickImage(AlbumUtil.PhotoEntry photoEntry) {
        if (mLimit == NO_LIMIT || XApplication.getInstance().getSeletedPhoto().size() < mLimit) {
            if(XApplication.getInstance().getSeletedPhoto().get(photoEntry.imageId)==null){//同步加
                seleted_photos.add(photoEntry);
            }
            XApplication.getInstance().getSeletedPhoto().put(photoEntry.imageId, photoEntry);

        } else {
            Log.i("onPickImage", "You can't check more images");
        }
        updateTextAndBadge();
    }

    @Override
    public void onUnpickImage(AlbumUtil.PhotoEntry photo) {
        if(
                XApplication.getInstance().getSeletedPhoto().get(photo.imageId)!=null){//同步减
            AppLogger.e("remove");
            seleted_photos.remove(photo);
        }
        XApplication.getInstance().getSeletedPhoto().remove(photo.imageId);
        updateTextAndBadge();
    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//
//        return true;
//    }


}
