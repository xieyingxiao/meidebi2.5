package com.meidebi.app.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mdb-ii on 15-2-9.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int count;

    public SpacesItemDecoration(int space,int count) {
        this.space = space;
        this.count =count;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int top = space;
        int right = 0;
        int left = space;
//        int bottom = space;

//        if((parent.getChildPosition(view)+1)<=count){
//            top = space;
//        }

         if((parent.getChildPosition(view)+1)%count==0){
            right = space;


          }

        outRect.top = top;
        outRect.left = left;
        outRect.right = right;
//        outRect.bottom = bottom;
    }
}