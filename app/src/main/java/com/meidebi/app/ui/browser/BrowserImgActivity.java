package com.meidebi.app.ui.browser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meidebi.app.R;
import com.meidebi.app.ui.base.BasePullToRefreshActivity;
import com.meidebi.app.ui.picker.PickerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class BrowserImgActivity extends BasePullToRefreshActivity {
    public static final String FRAGMENT_INDEX = "FRAGMENT_INDEX";
    public static final String IMAGE_POSITION = "IMAGE_POSITION";
    public static final int INDEX = 2;
    protected List<String> imageUrls ;

    DisplayImageOptions options;
    private static final String ISLOCKED_ARG = "isLocked";
     @InjectView(R.id.common_vp)
    ViewPager mViewPager;
      @InjectView(R.id.tv_browser_postion)
    TextView tv_position;
       @InjectView(R.id.tv_browser_total)
    TextView tv_total;

     @Override
    public void onCreate(Bundle savedInstanceState) {
         setUseSwipe(false);
         super.onCreate(savedInstanceState);
         initData();
         initView(savedInstanceState);
         setTitle("浏览图片");

     }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_browser_image;
    }

    protected void initData(){
        imageUrls  =   getIntent().getStringArrayListExtra(PickerActivity.ALBUM_KEY);

    }



    protected void initView(Bundle savedInstanceState){
        ButterKnife.inject(this);
        String totalFormat = getResources().getString(R.string.image_total);
        tv_total.setText(String.format(totalFormat, imageUrls.size()));
        tv_position.setText(getIntent().getIntExtra(IMAGE_POSITION, 0)+1+"");
         mViewPager.setAdapter(new ImageAdapter());
        mViewPager.setCurrentItem(getIntent().getIntExtra(IMAGE_POSITION, 0));

        options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.ic_empty)r)
//                .showImageOnFail(R.drawable.ic_erro
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
                tv_position.setText(position+1+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ImageAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ImageAdapter() {
            inflater = LayoutInflater.from(BrowserImgActivity.this);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.item_browser_image, view, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.iv_browser_image);
            final CircularProgressBar spinner = (CircularProgressBar) imageLayout.findViewById(R.id.pb_load_progressBar);

            ImageLoader.getInstance().displayImage(imageUrls.get(position), imageView, options, new SimpleImageLoadingListener() {
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
                    Toast.makeText(BrowserImgActivity.this, message, Toast.LENGTH_SHORT).show();

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



}
