package com.muxistudio.muxiio.listener;

import android.view.View;

/**
 * Created by kolibreath on 17-7-30.
 */

public interface MyItemClickListener {
  //  public void onDeleteBtnClick(int position,int shareId);
    public void onItemClick(View view,View anotherView,int position);
    public void OnItemLongClick(View view,View anotherView ,int position);
}
