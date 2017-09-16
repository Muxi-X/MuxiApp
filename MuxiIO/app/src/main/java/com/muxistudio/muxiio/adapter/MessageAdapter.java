package com.muxistudio.muxiio.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.muxistudio.muxiio.model.Message;
import com.muxistudio.muxiio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kolibreath on 17-8-11.
 */

public class  MessageAdapter extends ArrayAdapter<Message> {

    private int resid;
    private Context context;
    private List<Message> messageList = new ArrayList<>();

    public MessageAdapter(Context context,int resid,List<Message> messageList){
        super(context,resid,messageList);
        this.resid = resid;
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resid,null);

        TextView mngAction = (TextView) convertView.findViewById(R.id.messenger_action);
        CircleImageView mngAvatar = (CircleImageView) convertView.findViewById(R.id.messenger_avatar);
        TextView mngContent = (TextView) convertView.findViewById(R.id.messenger_content);
        TextView mngDate   = (TextView) convertView.findViewById(R.id.messenger_date);
        TextView mngTitle = (TextView) convertView.findViewById(R.id.messenger_title);
        TextView mngUsername = (TextView) convertView.findViewById(R.id.messenger_username);

        mngTitle.setText(messageList.get(position).messengerTitle);
        mngContent.setText(messageList.get(position).messengerContent);
        mngUsername.setText(messageList.get(position).messengerUsername);
        mngDate.setText(messageList.get(position).messengerDate);

        Picasso.with(context).load(messageList.get(position).messengerAvatar)
                .into(mngAvatar);

        //listeners

        return convertView;
    }

    private void removeItem(int position,List<Message> messageList){
        messageList.remove(position);
        this.remove(messageList.get(position));
        this.notifyDataSetChanged();
    }

}

