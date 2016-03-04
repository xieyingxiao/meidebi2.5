package com.meidebi.app.ui.picker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.ui.adapter.base.InterRecyclerOnItemClick;
import com.meidebi.app.ui.base.RecyclerViewPauseOnScrollListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by yazeed44 on 11/23/14.
 */
public class ImagesFragment extends Fragment implements InterRecyclerOnItemClick{

    @InjectView(R.id.common_recy)
    RecyclerView imgGrid;
    public OnPickImage pickListener;
    AlbumUtil.AlbumEntry album;
    ImagesAdapter adapter;
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.pick_fragment_image_browse, container, false);
        ButterKnife.inject(this,view);
          imgGrid.setLayoutManager(new GridLayoutManager(getActivity(),3));
        imgGrid.setItemAnimator(new DefaultItemAnimator());
        imgGrid.setOnScrollListener(new RecyclerViewPauseOnScrollListener(ImageLoader.getInstance(),
                false, false));
        setupAdapter();
//         imgGrid.addItemDecoration(new SpacesItemDecoration(10,3));
         return view;
    }

    public void setAlbum(AlbumUtil.AlbumEntry album) {
        this.album = album;
    }

    public void setupAdapter() {
        adapter = new ImagesAdapter(album,this);
        adapter.setOnItemClickListener(this);
        imgGrid.setAdapter(adapter);
    }

    public void refresh(){
        adapter.setData(album.photos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnPickImage) {
            pickListener = (OnPickImage) activity;


        } else {
            throw new ClassCastException(activity.toString() + "  Dosen't implement ImagesFragment.OnPickImage !!");
        }
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getActivity(), BrowserPickImageActivity.class);
        intent.putExtra(BrowserPickImageActivity.IMAGE_POSITION, position);
        final Bundle albumBundle = new Bundle();
        albumBundle.putSerializable(PickerActivity.ALBUM_KEY, album.photos);
        intent.putExtras(albumBundle);
        getActivity().startActivityForResult(intent,PickerActivity.PICK_REQUEST);
     }

    @Override
    public void OnFoooterClick(int position) {
        pickListener.onTakePhoto();
    }


    public static interface OnPickImage {
        public void onTakePhoto();


        public void onPickImage(AlbumUtil.PhotoEntry photoEntry);

        public void onUnpickImage(AlbumUtil.PhotoEntry photo);
    }


}
