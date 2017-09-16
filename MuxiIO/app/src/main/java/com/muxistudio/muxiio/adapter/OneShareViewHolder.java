package com.muxistudio.muxiio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.muxistudio.muxiio.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lwxwl on 2017/8/16.
 */

public class OneShareViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title_one_share)
    TextView titleOneShare;
    @BindView(R.id.date_one_share)
    TextView dateOneShare;
    @BindView(R.id.time_one_share)
    TextView timeOneShare;
    @BindView(R.id.text_one_share)
    TextView textOneShare;

    public OneShareViewHolder(View view, Context context){
        super(view);
        ButterKnife.bind(this, view);
    }
}
