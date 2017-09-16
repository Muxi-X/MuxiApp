package com.muxistudio.muxiio.itemDecorator;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by kolibreath on 17-8-19.
 */

public class ItemDecorator extends RecyclerView.ItemDecoration{

    public static int HORIZEONTAL = LinearLayout.HORIZONTAL;
    private int orientation;
    private int decoration;

    public ItemDecorator(int orientation,int decoration){
        this.orientation = orientation;
        this.decoration = decoration;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int lastPostion = state.getItemCount() - 1;
        int current = parent.getChildLayoutPosition(view);

        if(orientation== LinearLayoutManager.VERTICAL) {
            // if it's the last item
            if (current == lastPostion) {
                //if so, create a rect which width and height are both zero
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0,0,decoration);
            }
        }
    }
}
