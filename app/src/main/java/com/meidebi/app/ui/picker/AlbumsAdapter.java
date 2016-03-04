package com.meidebi.app.ui.picker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meidebi.app.R;
import com.meidebi.app.ui.adapter.base.BaseRecyclerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class AlbumsAdapter extends BaseRecyclerAdapter<AlbumUtil.AlbumEntry> {

    public final ArrayList<AlbumUtil.AlbumEntry> albums;
    public final AlbumsFragment fragment;

    public AlbumsAdapter(final ArrayList<AlbumUtil.AlbumEntry> albums, final AlbumsFragment fragment) {
        super(fragment.getActivity(),albums);
        this.albums = albums;
        this.fragment = fragment;
        setupItemListener();
    }

    @Override
    public boolean useFooter() {
        return false;
    }




    @Override
    public BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v =getLayoutInflater().inflate(R.layout.pick_adapter_album,parent,false);
        return new ViewHodler(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        setupAlbum((ViewHodler) holder, position);
    }




    public void setupAlbum(final ViewHodler holder, int position) {
        AlbumUtil.AlbumEntry album=albums.get(position);
        holder.name.setText(album.name);
        holder.count.setText("("+album.photos.size() + ")");

        ImageLoader.getInstance().displayImage("file://" + album.coverPhoto.path, holder.thumbnail,options,
                animateFirstListener);
    }



    public void setupItemListener() {
//        fragment.albumsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
//                final AlbumUtil.AlbumEntry album = albums.get(position);
//                fragment.listener.onClickAlbum(album);
//            }
//        });
    }



    class ViewHodler extends BasicItemViewHolder {

        @InjectView(R.id.album_thumbnail)
        ImageView thumbnail;
        @InjectView(R.id.album_count)
        TextView count;
        @InjectView(R.id.album_name)
        TextView name;


        public ViewHodler(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 final AlbumUtil.AlbumEntry album = albums.get(getPosition());
                  fragment.listener.onClickAlbum(album);
                }
            });
        }


    }
}
