package com.muxistudio.muxiio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muxistudio.muxiio.data.SharesBean;
import com.muxistudio.muxiio.utils.MyTextUtils;
import com.muxistudio.muxiio.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class OneShareAdapter extends RecyclerView.Adapter {

    private List<SharesBean> mOneShareList = new ArrayList<>();
    private Context mContext;
    private int mNumber;

    public OneShareAdapter(Context context, int number, List<SharesBean> list){
        this.mContext = context;
        this.mNumber = number;
        this.mOneShareList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mNumber, parent, false);
        OneShareViewHolder oneShareViewHolder = new OneShareViewHolder(view, mContext);
        return oneShareViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (mOneShareList.size() != 0) {
            String title = mOneShareList.get(position).getTitle();
            if(MyTextUtils.length(title)>=18){
                title = MyTextUtils.cutTitle(title,MyTextUtils.length(title)/2);
            }
            ((OneShareViewHolder) holder).mTitleOneShare.setText(title);
            //eg: 03 Aug 2017
            String parseTime1 = MyTextUtils.parseDate(mOneShareList.get(position).getDate());
            String parseTime2 = MyTextUtils.formatDateUsingSlash(parseTime1);
            ((OneShareViewHolder) holder).mDateOneShare.setText(parseTime2);
            ((OneShareViewHolder) holder).mTextOneShare.setText(mOneShareList.get(position).getShare());

        }else{
            ToastUtils.showShort("NOTHING HERE");
        }
    }

    @Override
    public int getItemCount() {
        return mOneShareList.size();
    }

    public void addItem(int position, SharesBean bean){
        mOneShareList.add(position,bean);
        notifyItemInserted(position);

    }

    public void removeItem(int position){
        mOneShareList.remove(position);
        notifyItemRemoved(position);
    }


}
