package com.meidebi.app.ui.picker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.support.utils.AppLogger;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ImagesAdapter extends BaseRecyclerAdapter<AlbumUtil.PhotoEntry> {


    //    public final AlbumUtil.AlbumEntry album;
    public final ImagesFragment fragment;

    public ImagesAdapter(final AlbumUtil.AlbumEntry album, final ImagesFragment fragment) {
        super(fragment.getActivity(), album.photos);
//        this.album = album;
        this.fragment = fragment;
    }


    public void setupImage(final ViewHolder holder, int position) {
        final AlbumUtil.PhotoEntry photo = mData.get(position);
        imageLoader.displayImage("file://" + photo.path, holder.thumbnail, options,
                animateFirstListener);
        holder.check.setChecked(AlbumUtil.isPicked(photo));
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.check.isChecked()) {
                    //Unpick
                    fragment.pickListener.onUnpickImage(photo);
                } else if (AlbumUtil.sLimit == PickerActivity.NO_LIMIT || AlbumUtil.sLimit > XApplication.getInstance().getSeletedPhoto().size()) {
                    //pick
                    AppLogger.e(AlbumUtil.sLimit + "");
                    fragment.pickListener.onPickImage(photo);
                }
            }
        });

    }


    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v = getLayoutInflater().inflate(R.layout.pick_adapter_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setupImage((ViewHolder) holder, position);
    }


    class ViewHolder extends BasicItemViewHolder {


        @InjectView(R.id.image_thumbnail)
        ImageView thumbnail;
        @InjectView(R.id.image_check)
        CheckBox check;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View v = getLayoutInflater().inflate(R.layout.pick_photo_header_take, parent, false);
        return new ViewHeader(v);
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {
        super.onBindHeaderView(holder, position);
    }

    class ViewHeader extends RecyclerView.ViewHolder {


        public ViewHeader(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            if (OnItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OnItemClickListener.OnFoooterClick(getPosition());
                    }
                });
            }
        }
    }


    @Override
    public boolean useHeader() {
        return true;
    }

    @Override
    public boolean useFooter() {
        return false;
    }
}
