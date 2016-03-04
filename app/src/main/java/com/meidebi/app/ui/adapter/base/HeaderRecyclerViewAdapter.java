package com.meidebi.app.ui.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mdb-ii on 14-12-4.
 */
public abstract class HeaderRecyclerViewAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = Integer.MIN_VALUE;
    public static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;
    public static final int TYPE_ADAPTEE_OFFSET = 2;
    protected InterRecyclerOnItemClick OnItemClickListener;

    public void setOnItemClickListener(InterRecyclerOnItemClick onItemClickListener) {
        OnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent, viewType);
        } else if (viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        }
        return onCreateBasicItemViewHolder(parent, viewType - TYPE_ADAPTEE_OFFSET);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 && holder.getItemViewType() == TYPE_HEADER) {
            onBindHeaderView(holder, position);
        } else if (position ==  (getBasicItemCount()+(useHeader() ? 1 : 0)) && holder.getItemViewType() == TYPE_FOOTER) {
              onBindFooterView(holder, position);
        } else {
            onBindBasicItemView(holder, position - (useHeader() ? 1 : 0));
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = getBasicItemCount();
        if (useHeader()) {
            itemCount += 1;
        }
        if (useFooter()) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && useHeader()) {
            return TYPE_HEADER;
        }
        if (position == (getBasicItemCount()+(useHeader() ? 1 : 0)) && useFooter()) {
            return TYPE_FOOTER;
        }
        if (getBasicItemType(position) >= Integer.MAX_VALUE - TYPE_ADAPTEE_OFFSET) {
            new IllegalStateException("HeaderRecyclerViewAdapter offsets your BasicItemType by " + TYPE_ADAPTEE_OFFSET + ".");
        }
        return getBasicItemType(position) + TYPE_ADAPTEE_OFFSET;
    }

    public abstract boolean useHeader();

    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderView(RecyclerView.ViewHolder holder, int position);

    public abstract boolean useFooter();

    public abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindFooterView(RecyclerView.ViewHolder holder, int position);

    public abstract BasicItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBasicItemView(RecyclerView.ViewHolder holder, int position);

    public abstract int getBasicItemCount();

    /**
     * make sure you don't use [Integer.MAX_VALUE-1, Integer.MAX_VALUE] as BasicItemViewType
     *
     * @param position
     * @return
     */
    public abstract int getBasicItemType(int position);

    protected class BasicItemViewHolder extends RecyclerView.ViewHolder{

        public BasicItemViewHolder(View itemView){
            super(itemView);
            if(OnItemClickListener!=null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setItemViewOnClick(getItemPosition());

                    }
                });
            }
        }

        public int getItemPosition(){
            int position = getPosition();
            if (useHeader()) {
                position -= 1;
            }
            return position;
        }


        protected void setItemViewOnClick(int position){

            OnItemClickListener.OnItemClick(position);

        }
    }



}