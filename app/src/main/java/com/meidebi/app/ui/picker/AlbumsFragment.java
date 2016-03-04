package com.meidebi.app.ui.picker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meidebi.app.R;
import com.meidebi.app.ui.widget.DividerItemDecoration;

import butterknife.ButterKnife;
import butterknife.InjectView;



public class AlbumsFragment extends Fragment {
    public OnClickAlbum listener;
    @InjectView(R.id.common_recy)
    RecyclerView albumsGrid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.common_recyclerview, container, false);
        ButterKnife.inject(this,view);
        albumsGrid.setLayoutManager(new LinearLayoutManager(getActivity()));
        albumsGrid.setItemAnimator(new DefaultItemAnimator());
        albumsGrid.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        setupAdapter();
        return view;
    }

    public void setupAdapter() {
        AlbumUtil.setupAdapter(AlbumUtil.getAlbums(getActivity(),false),albumsGrid,this);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnClickAlbum) {
            listener = (OnClickAlbum) activity;
        } else {
            throw new ClassCastException(activity.toString() + "  Dosen't implement AlbumsFragment.OnClickAlbum !!");
        }
    }


    public static interface OnClickAlbum {

        void onClickAlbum(AlbumUtil.AlbumEntry album);
    }
}
