package com.muxistudio.muxiio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muxistudio.muxiio.model.ShareList;
import com.muxistudio.muxiio.utils.MyTextUtils;
import com.muxistudio.muxiio.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class OneShareAdapter extends RecyclerView.Adapter {

    private List<ShareList.SharesBean> oneShareList = new ArrayList<>();
    private Context context;
    private int number;

    public OneShareAdapter(Context context, int number, List<ShareList.SharesBean> list){
        this.context = context;
        this.number = number;
        this.oneShareList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(number, parent, false);
        OneShareViewHolder oneShareViewHolder = new OneShareViewHolder(view, context);
        return oneShareViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (oneShareList.size() != 0) {
            String title = oneShareList.get(position).getTitle();
            if(MyTextUtils.length(title)>=18){
                title = MyTextUtils.cutTitle(title,MyTextUtils.length(title)/2);
            }
            ((OneShareViewHolder) holder).titleOneShare.setText(title);
            //eg: 03 Aug 2017
            String parseTime1 = MyTextUtils.parseDate(oneShareList.get(position).getDate());
            String parseTime2 = MyTextUtils.formatDateUsingSlash(parseTime1);
            ((OneShareViewHolder) holder).dateOneShare.setText(parseTime2);
            ((OneShareViewHolder) holder).textOneShare.setText(oneShareList.get(position).getShare());

        }else{
            ToastUtils.showShort("NOTHING HERE");
        }
    }

    @Override
    public int getItemCount() {
        return oneShareList.size();
    }

    public void addItem(int position, ShareList.SharesBean bean){
        oneShareList.add(position,bean);
        notifyItemInserted(position);

    }

    public void removeItem(int position){
        oneShareList.remove(position);
        notifyItemRemoved(position);
    }


}
