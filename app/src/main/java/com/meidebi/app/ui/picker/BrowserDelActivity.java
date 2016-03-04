package com.meidebi.app.ui.picker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.browser.HackyViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import info.hoang8f.widget.FButton;

/**
 * Created by Administrator on 2015/3/22.
 */
public class BrowserDelActivity extends BasePullToRefreshActivity {
    public static final String FRAGMENT_INDEX = "FRAGMENT_INDEX";
    public static final String IMAGE_POSITION = "IMAGE_POSITION";
    public static final int INDEX = 2;
    protected ArrayList<AlbumUtil.PhotoEntry> imageUrls;

    DisplayImageOptions options;
    private static final String ISLOCKED_ARG = "isLocked";
    @InjectView(R.id.common_vp)
    ViewPager mViewPager;
    @InjectView(R.id.tv_browser_postion)
    TextView tv_position;
    @InjectView(R.id.tv_browser_total)
    TextView tv_total;
    @InjectView(R.id.btn_pick_select_yes)
    FButton btn_sure;
    ImageAdapter adapter;
    private ImageButton ib_del;


    @OnClick(R.id.btn_pick_select_yes)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_pick_select_yes:
                if(XApplication.getInstance().getSeletedPhoto().size()==0){
                    Toast.makeText(BrowserDelActivity.this, "请选择至少一张图片", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent2 = getIntent();
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable(PickerActivity.ALBUM_KEY, PickerActivity.seleted_photos);
                    intent2.putExtras(bundle);
                    setResult(RESULT_FIRST_USER, intent2);
                    super.finish();
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setUseSwipe(false);
        if(XApplication.getInstance().getSeletedPhoto()==null){
            finish();
        }else {
            super.onCreate(savedInstanceState);
            initData();
            initView(savedInstanceState);
            setTitle("预览图片");
        }
     }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_browser_image;
    }

    protected void initData() {
        imageUrls = (ArrayList<AlbumUtil.PhotoEntry>) getIntent().getSerializableExtra(PickerActivity.ALBUM_KEY);

    }


    protected void initView(Bundle savedInstanceState) {
        ButterKnife.inject(this);
        final String totalFormat = getResources().getString(R.string.image_total);
        tv_total.setText(String.format(totalFormat, imageUrls.size()));
        tv_position.setText(getIntent().getIntExtra(IMAGE_POSITION, 0) + 1 + "");
         btn_sure.setVisibility(View.GONE);

        View title = getLayoutInflater()
                .inflate(R.layout.pick_del_title, null);
        ib_del = ((ImageButton) title.findViewById(R.id.cb_browser));

        getSupportActionBar().setCustomView(title, new ActionBar.LayoutParams(Gravity.RIGHT));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        adapter =  new ImageAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra(IMAGE_POSITION, 0));
        options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.ic_empty)
//                .showImageOnFail(R.drawable.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_position.setText(position + 1 + "");
             }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ib_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumUtil.PhotoEntry current_photo = imageUrls.get(mViewPager.getCurrentItem());
                XApplication.getInstance().getSeletedPhoto().remove(current_photo.imageId);
                imageUrls.remove(mViewPager.getCurrentItem());
                adapter.notifyDataSetChanged();
                if (imageUrls.size() == 0) {
                    XApplication.getInstance().getSeletedPhoto().clear();
                    onBackPressed();
                 }
                tv_total.setText((String.format(totalFormat, imageUrls.size())));
                tv_position.setText((mViewPager.getCurrentItem() + 1) + "");
            }
        });

    }

    private class ImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ImageAdapter() {
            inflater = LayoutInflater.from(BrowserDelActivity.this);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_browser_image, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.iv_browser_image);
            final CircularProgressBar spinner = (CircularProgressBar) imageLayout.findViewById(R.id.pb_load_progressBar);
            ImageLoader.getInstance().displayImage("file://" + imageUrls.get(position).path, imageView, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                }



                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(BrowserDelActivity.this, message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                }
            });

            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }


    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // case R.id.action_refresh:
            // // mContentFragment.loadFirstPageAndScrollToTop();
            // return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent2 = getIntent();
        final Bundle bundle = new Bundle();
        bundle.putSerializable(PickerActivity.ALBUM_KEY, imageUrls);
        intent2.putExtras(bundle);
         setResult(RESULT_OK,intent2);
        super.onBackPressed();
    }
}
