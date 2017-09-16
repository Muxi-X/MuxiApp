package com.muxistudio.muxiio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.muxiio.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kolibreath on 17-8-7.
 */

public class MessageViewHolder {
    @BindView(R.id.messenger_avatar)
    ImageView mngAvatar;
    @BindView(R.id.messenger_action)
    TextView mngAction;
    @BindView(R.id.messenger_title)
    TextView mngTitle;
    @BindView(R.id.messenger_date)
    TextView mngDate;
    @BindView(R.id.messenger_content)
    TextView mngContent;
    @BindView(R.id.messenger_username)
    TextView mngUsername;


    public MessageViewHolder(Context context, int resid){
        View view = LayoutInflater.from(context).inflate(resid,null);
        
        ButterKnife.bind(this,view);
    }
}
